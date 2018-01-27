package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.TbContentService;
import cn.e3mall.pojo.TbContent;

@Controller
public class ContentController {
	
	@Autowired
	private TbContentService tbContentService;

	/**
	 * 查询分类下的子信息
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult findResult
		(Long categoryId,Integer page, Integer rows){
		
		EasyUIDataGridResult result = tbContentService.findByContentId(categoryId, page, rows);
		
		
		return result;
	}
	
	/**
	 * 添加新的tbcontent
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent tbContent){
		E3Result result = tbContentService.addContent(tbContent);
		return result;
	}
}
