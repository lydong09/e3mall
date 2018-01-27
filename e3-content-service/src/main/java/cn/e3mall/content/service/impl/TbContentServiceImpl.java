package cn.e3mall.content.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.TbContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TbContentServiceImpl implements TbContentService {

	@Autowired
	private TbContentMapper tbContentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT}")
	private String CONTENT;//big advertisement key
	
	
	/**
	 * 将对应的content查询出来，并分页显示
	 */
	@Override
	public EasyUIDataGridResult findByContentId(Long categoryId, Integer page, Integer rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		
		//根据categoryId查询出结果集
		TbContentExample example = new TbContentExample();
		example.createCriteria().andCategoryIdEqualTo(categoryId);
		List<TbContent> list = tbContentMapper.selectByExample(example);
		
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//取出分页结果
		result.setTotal(pageInfo.getTotal());
		result.setRows(list);
		return result;
	}


	@Override
	public E3Result addContent(TbContent tbContent) {
		
		//补充数据
		tbContent.setUpdated(new Date());
		tbContent.setCreated(new Date());
		
		tbContentMapper.insert(tbContent);
		
		//清楚缓存，保证缓存和数据库同步
		try {
			jedisClient.hdel(CONTENT, tbContent.getCategoryId().toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return E3Result.ok();
	}


	@Override
	public List<TbContent> findIndexImage(Long iMAGE_LUNBO_ID) {
		//执行增删改操作后，就得清空缓存
		//查询数据库之前先查缓存，出现异常捕获，让其能正常查询数据库
		try {
			String json = jedisClient.hget(CONTENT, iMAGE_LUNBO_ID+"");
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				System.out.println("已经从缓存中获取");
				return list;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		TbContentExample example = new TbContentExample();
		
		example.createCriteria().andCategoryIdEqualTo(iMAGE_LUNBO_ID);
		
		List<TbContent> list = tbContentMapper.selectByExample(example);
		
		//查询数据将其放入缓存,同样需要捕获，防止出现异常导致查询出的数据丢失
		try {
			String json = JsonUtils.objectToJson(list);
			jedisClient.hset(CONTENT, iMAGE_LUNBO_ID+"", json);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
	}

}
