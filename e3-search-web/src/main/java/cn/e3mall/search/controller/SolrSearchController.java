package cn.e3mall.search.controller;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SolrSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SolrSearchController {

    @Autowired
    private SolrSearchService solrSearchService;

    @Value("${SEARCH_ROWS}")
    private Integer SEARCH_ROWS;

    @RequestMapping("/search")
    public String getSearchResult(String keyword, @RequestParam(defaultValue = "1") Integer page,
                                  Model model) throws Exception {
        //keyword是通过get方法传入的有乱码
        keyword = new String(keyword.getBytes("iso8859-1"), "utf-8");

        SearchResult searchResult = solrSearchService.getSearchResult(keyword, page, SEARCH_ROWS);

        //将结果传递给页面
        model.addAttribute("query", keyword);
        model.addAttribute("totalPages", searchResult.getTotalPages());
        model.addAttribute("itemList", searchResult.getItemList());
        model.addAttribute("page", page);
        //返回逻辑视图
        return "search";
    }
}
