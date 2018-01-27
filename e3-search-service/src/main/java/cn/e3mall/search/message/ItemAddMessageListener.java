package cn.e3mall.search.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.SeachItemMapper;


public class ItemAddMessageListener implements MessageListener{

	@Autowired
	private SeachItemMapper mapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public void onMessage(Message message) {
		
		//获得对象取出消息
		TextMessage textMessage = (TextMessage) message;
		
		try {
			String itemId = textMessage.getText();
			//等待数据提交到数据库
			Thread.sleep(2000);
			
			//查询数据库
			SearchItem searchItem = mapper.getItemById(Long.valueOf(itemId));
			//创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			
			//向文档对象中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			
			//将文档对象加入索引库
			solrServer.add(document);
			
			//提交，别忘了
			solrServer.commit();
			
			System.out.println("数据更新到索引库");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
