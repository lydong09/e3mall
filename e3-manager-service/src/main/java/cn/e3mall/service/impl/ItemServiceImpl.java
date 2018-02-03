package cn.e3mall.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;

    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired //消息中间件模板
    private JmsTemplate jmsTemplate;

    @Resource//默认先对应id取，广播模式
    private Destination topicDestination;
    //添加redis客户端对象
    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_ITEM_PRE}") //键的前缀
    private String REDIS_ITEM_PRE;

    @Value("${ITEM_CACHE_EXPIRE}")//过期时间
    private Integer ITEM_CACHE_EXPIRE;

    @Override
    public TbItem getItemById(Long itemId) {
        //第一种简单方式
        //TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        //添加缓存，先从缓冲里提取，缓存里没有在查数据库
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        //第二种条件查询
        TbItemExample example = new TbItemExample();

        Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = tbItemMapper.selectByExample(example);

        if (list != null && list.size() > 0) {
            //向redis里添加缓存
            try {
                String json = JsonUtils.objectToJson(list.get(0));
                jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", json);
                jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
                //添加缓存
                System.out.println("已经添加到缓存");
            } catch (Exception e) {
                // TODO: handle exception
            }

            return list.get(0);
        }

        return null;

    }

    @Override
    public EasyUIDataGridResult findByPage(Integer page, Integer rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);

        TbItemExample example = new TbItemExample();

        //执行查询
        List<TbItem> list = tbItemMapper.selectByExample(example);

        PageInfo<TbItem> pageInfo = new PageInfo<>(list);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        //取出分页结果
        result.setTotal(pageInfo.getTotal());
        result.setRows(list);

        return result;
    }

    /**
     * 添加商品
     */
    @Override
    public E3Result addItem(TbItem item, String desc) {
        //生成的商品id
        final long itemId = IDUtils.genItemId();

        //添加id
        item.setId(itemId);

        //补全商品的信息
        item.setCreated(new Date());
        item.setUpdated(new Date());

        //1 正常 2下架 3删除 这里不定常量了
        item.setStatus((byte) 1);

        //创建TbItemDesc对象
        TbItemDesc itemDesc = new TbItemDesc();

        //补全信息
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());

        //插入数据库
        tbItemMapper.insert(item);
        tbItemDescMapper.insert(itemDesc);

        //将商品同步到索引库，需要消息中间件activeMq,使用模板类,发送添加的商品id
        jmsTemplate.send(topicDestination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(itemId + "");
                return textMessage;
            }
        });


        //返回结果
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getTbItemDescById(Long itemId) {
        //先查缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                System.out.println("从缓存中提取商品详情");
                return tbItemDesc;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


        TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

        //存入缓存
        try {
            String json = JsonUtils.objectToJson(itemDesc);

            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", json);

            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return itemDesc;
    }

}
