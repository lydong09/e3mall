package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUINode;

import java.util.List;


public interface ItemCatService {
    List<EasyUINode> getItemCatList(Long parentId);
}
