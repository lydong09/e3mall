package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SolrDao;
import cn.e3mall.search.service.SolrSearchService;

@Service
public class SolrSearchServiceImpl implements SolrSearchService{

	@Autowired
	private SolrDao solrDao;
	
	/**
	 * 分页展示从索引库中查询出来的数据
	 * @throws SolrServerException 
	 */
	@Override
	public SearchResult getSearchResult(String keyword, Integer page, Integer rows) throws Exception {
		//创建查询对象
		SolrQuery query = new SolrQuery();
		
		//设置查询条件  !!!这里和老师不一样，试试
		//query.set("q", keywords);
		query.setQuery(keyword);
		
		//设置默认搜索域,这个必须要有，否则查询不出数据
		query.set("df", "item_title");
		//设置查询分页信息
		if (page <= 0) {
			page = 1;
		}
		query.setStart((page-1)*rows);
		query.setRows(rows);
		
		//开启高亮
		query.setHighlight(true);
		//添加高亮显示的域
		query.addHighlightField("item_title");
		query.addHighlightField("item_category_name");
		//设置高亮的前后缀
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		
		//执行查询
		SearchResult searchResult = solrDao.getSearchResult(query);
		
		Long recourdCount = searchResult.getRecourdCount();
		//计算总计数
		Integer totalPages = (int) (recourdCount / rows);
		
		if (recourdCount % rows != 0) {
			totalPages++;
		}
		searchResult.setTotalPages(totalPages);
		return searchResult;
	}
}
