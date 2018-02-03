package cn.e3mall.portal.controller;

import cn.e3mall.content.service.TbContentService;
import cn.e3mall.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private TbContentService tbContentService;

    @Value("${IMAGE_LUNBO_ID}")
    private Long iMAGE_LUNBO_ID;

    @RequestMapping("index")
    public String goIndex(Model model) {

        List<TbContent> ad1List = tbContentService.findIndexImage(iMAGE_LUNBO_ID);

        model.addAttribute("ad1List", ad1List);

        return "index";
    }


}
