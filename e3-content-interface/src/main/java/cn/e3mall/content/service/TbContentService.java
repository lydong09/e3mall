package cn.e3mall.content.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbContent;

import java.util.List;

public interface TbContentService {

	EasyUIDataGridResult findByContentId(Long categoryId, Integer page, Integer rows);

	E3Result addContent(TbContent tbContent);

	List<TbContent> findIndexImage(Long iMAGE_LUNBO_ID);
}
