package cn.e3mall.item.listener;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Dong
 * Date: Created in 2018/1/27
 */
@Controller
public class HtmlGenListener implements MessageListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${HTML_LOCATION}")//html存放目录
    private String HTML_LOCATION;

    @Override
    public void onMessage(Message message) {
        try {
            //创建一个模板，参考jsp

            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            Long itemId = new Long(text);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品id查询商品信息与商品描述
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc itemDesc = itemService.getTbItemDescById(itemId);
            //创建一个数据集
            Map map = new HashMap();
            map.put("item",item);
            map.put("itemDesc",itemDesc);
            //加载模板配置对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");

            //创建输出流，指定路径和文件名
            Writer writer = new FileWriter(new File("D:\\freemarker\\"+itemId+".html"));
            //生成静态页面
            template.process(map,writer);
            //关闭流
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
