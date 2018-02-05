package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUINode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentCategoryController {

    @Autowired
    private ContentService contentService;


    /**
     * 查询数据库，查出对应的父节点数据
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUINode> getTreeResult
    (@RequestParam(value = "id", defaultValue = "0") Long parentId) {

        List<EasyUINode> result = contentService.getTreeResult(parentId);

        return result;
    }

    /**
     * 添加分类节点
     */
    @RequestMapping(value = "/content/category/create", method = RequestMethod.POST)
    @ResponseBody
    public E3Result contentAdd(Long parentId, String name) {

        TbContentCategory category = contentService.contentAddCategory(parentId, name);

        return E3Result.ok(category);
    }


}
