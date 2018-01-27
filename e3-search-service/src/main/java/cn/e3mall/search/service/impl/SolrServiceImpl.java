package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.SeachItemMapper;
import cn.e3mall.search.service.SolrService;

@Service
public class SolrServiceImpl implements SolrService{
	
	@Autowired
	private SolrServer solrServer;
	
	@Autowired
	private SeachItemMapper searchItemMapper;
	
	

	@Override
	public E3Result getAllItem(){
			
		try {
			
			//查询出数据库所有上架的商品，这里将上架的信息写死了,应该定义常量
			List<SearchItem> itemList = searchItemMapper.getItemList();
			
			//遍历添加文档对象，并提交到索引库
			for (SearchItem searchItem : itemList) {
				//创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				
				//添加文档信息
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				solrServer.add(document);
			}
			solrServer.commit();
			//成功导入
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			
			return E3Result.build(500, "加入索引库失败");
		}
		
		
	}

	

}
