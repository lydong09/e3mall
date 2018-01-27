package cn.e3mall.search.service;


import cn.e3mall.common.pojo.SearchResult;

public interface SolrSearchService {

    SearchResult getSearchResult(String keywords, Integer page, Integer rows) throws Exception;
}
