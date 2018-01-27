package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUINode;
import cn.e3mall.pojo.TbContentCategory;


public interface ContentService {
	
	List<EasyUINode> getTreeResult(Long parentId);
	TbContentCategory contentAddCategory(Long parentId, String name);
	
}
