package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.service.SolrService;


@Controller
public class SolrController {

	@Autowired
	private SolrService solrService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result getItemList(){
		E3Result result = solrService.getAllItem();
		
		return result;
	}
}
