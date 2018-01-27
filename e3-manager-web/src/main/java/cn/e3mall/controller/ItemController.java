package cn.e3mall.controller;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    //获取路径中的变量{itemId}相当于占位符，下面取值名称要一致(如果不一致，@Pathvariable要设置value)
    @RequestMapping("/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {

        TbItem item = itemService.getItemById(itemId);

        return item;
    }


    //获取携带的参数，itemId必须与提交的参数名称一致
    /*@RequestMapping("/hello")
	@ResponseBody
	public TbItem getItemById(Long itemId){
		TbItem item = itemService.getItemById(itemId);
		
		return  item;
	}*/

    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult findByPage(Integer page, Integer rows) {

        EasyUIDataGridResult easyUIDataGridResult = itemService.findByPage(page, rows);

        return easyUIDataGridResult;
    }

    /**
     * 添加商品/save
     */

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item, String desc) {
        E3Result result = itemService.addItem(item, desc);

        return result;
    }
}
