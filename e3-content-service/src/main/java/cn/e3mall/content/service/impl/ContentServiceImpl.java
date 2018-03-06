package cn.e3mall.content.service.impl;

import cn.e3mall.common.pojo.EasyUINode;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	
	@Override
	public List<EasyUINode> getTreeResult(Long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		
		//设置查询条件
		example.createCriteria().andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> catList = tbContentCategoryMapper.selectByExample(example);
		
		//创建返回的数据集
		List<EasyUINode> result = new ArrayList<>();
		
		//遍历集合得到节点的集合
		for (TbContentCategory t : catList) {
			EasyUINode node = new EasyUINode();
			node.setId(t.getId());
			node.setText(t.getName());
			node.setState(t.getIsParent()?"closed":"open");
			result.add(node);
		}
		
		
		return result;
	}
	
	/**
	 * 添加节点，并修改父节点的状态
	 */
	@Override
	public TbContentCategory contentAddCategory(Long parentId, String name) {
		//创建要插入的pojo,这里因为需要返回的id值，需要在配置文件里配置
		TbContentCategory category = new TbContentCategory();
		
		category.setParentId(parentId);
		category.setName(name);
		//true为是父节点，false不是,新建的节点显然不是
		category.setIsParent(false);
		//排序用不上默认1
		category.setSortOrder(1);
		//1，是正常，2，下架
		category.setStatus(1);
		category.setCreated(new Date());
		category.setUpdated(new Date());
		tbContentCategoryMapper.insert(category);
		//判断父节点原来的状态，如果原来不是父节点，将状态改为父节点
		TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			//设置为父节点，并更新到数据库
			parentNode.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		
		return category;
	}


}
