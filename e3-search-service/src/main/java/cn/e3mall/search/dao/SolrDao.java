package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;
//索引的需求不多，先不写接口了
@Repository
public class SolrDao {

	@Autowired
	private SolrServer solrServer;
	
	public SearchResult getSearchResult(SolrQuery query) throws SolrServerException{
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//得到查询的结果集
		SolrDocumentList results = queryResponse.getResults();
		//创建SearchResult对象封装数据
		SearchResult searchResult = new SearchResult();
		//设置查询出来的总数
		searchResult.setRecourdCount(results.getNumFound());
		//取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		//创建集合封装数据
		List<SearchItem> itemList = new ArrayList<>();
		for (SolrDocument solrDocument : results) {
			SearchItem item = new SearchItem();
			item.setId((String) solrDocument.get("id"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setPrice((long) solrDocument.get("item_price"));
			//处理高亮分类是否高亮
			String category_name = "";
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			List<String> category = map.get("item_category_name");
			if (category != null && category.size() > 0) {
				category_name = category.get(0);
			}else{
				category_name = (String) solrDocument.get("item_category_name");
			}
			item.setCategory_name(category_name);
			//最后处理高亮
			String title = "";
			
			List<String> list = map.get("item_title");
			if (list != null && list.size() > 0) {
				title = list.get(0);
			}else{
				title = (String) solrDocument.get("item_title");
			}
			item.setTitle(title);
			
			//加入集合
			itemList.add(item);
			
		}
		
		//添加入结果中
		searchResult.setItemList(itemList);
		
		return searchResult;
	}
	
}
