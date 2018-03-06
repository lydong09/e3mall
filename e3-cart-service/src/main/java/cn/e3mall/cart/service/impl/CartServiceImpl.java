package cn.e3mall.cart.service.impl;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Dong
 * Date: Created in 2018/2/2
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private TbItemMapper tbItemMapper;

    @Value("${CART_PRE}")
    private String CART_PRE;

    /**
     * 将购物车商品信息加入缓存
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public E3Result addCart(long userId, long itemId , Integer num) {
        //向redis中添加购物车信息,先判断缓存里是否有该用户购物车信息
        Boolean flag = jedisClient.hexists(CART_PRE + ":" + userId, itemId + "");

        if (flag){
            //取出对应id的商品
            String json = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");
            //转换为商品集合
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            //数量更新
            tbItem.setNum(tbItem.getNum() + num);
            //写回redis
            jedisClient.hset(CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(tbItem));

            return E3Result.ok();
        }

        //如果不存在查询出商品信息，加入redis
        TbItem item = tbItemMapper.selectByPrimaryKey(itemId);
        item.setNum(num);
        //取图片
        if (StringUtils.isNotBlank(item.getImage())){

            item.setImage(item.getImage().split(",")[0]);
        }

        jedisClient.hset(CART_PRE + ":" + userId, itemId + "",JsonUtils.objectToJson(item));

        return E3Result.ok();
    }

    /**
     * 合并cookie和redis里的购物车信息
     * @param userId
     * @param cartList
     * @return
     */
    @Override
    public E3Result merge(long userId , List<TbItem> cartList) {
        //遍历cookie的购物车集合
        if (cartList != null && cartList.size() > 0){

            for (TbItem tbItem : cartList) {
                addCart(userId,tbItem.getId(),tbItem.getNum());
            }
        }

        return E3Result.ok();
    }

    /**
     * 获取redis里购物车信息
     */
    public List<TbItem> getTbItemList(long userId){
        List<String> hvals = jedisClient.hvals(CART_PRE + ":" + userId);
        //创建商品的结果集
        List<TbItem> tbItemList = new ArrayList<>();
        for (String hval : hvals) {
            //装换为pojo
            TbItem tbItem = JsonUtils.jsonToPojo(hval, TbItem.class);
            tbItemList.add(tbItem);
        }


        return tbItemList;
    }

    /**
     * 更新购物车数量
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    @Override
    public E3Result updateCart(long userId, long itemId, int num) {
        String json = jedisClient.hget(CART_PRE + ":" + userId, itemId + "");

        TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);

        tbItem.setNum(num);
        //写入缓存
        jedisClient.hset(CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        
        return E3Result.ok();
    }

    /**
     * 删除购物车商品
     * @param userId
     * @param itemId
     * @return
     */
    @Override
    public E3Result deleteCartItem(long userId, long itemId) {
        jedisClient.hdel(CART_PRE + ":" + userId, itemId + "");
        return E3Result.ok();
    }

    @Override
    public E3Result clear(long userId) {
        //删除用户缓存的购物车
        jedisClient.del(CART_PRE + ":" + userId);
        return E3Result.ok();
    }


}
