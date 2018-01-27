package cn.e3mall.service.impl;

import cn.e3mall.common.pojo.EasyUINode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImp implements ItemCatService {
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUINode> getItemCatList(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andParentIdEqualTo(parentId);
        //查询出itemCat集
        List<TbItemCat> itemCat = tbItemCatMapper.selectByExample(example);
        //创建封装数据的集合
        List<EasyUINode> result = new ArrayList<>();

        for (TbItemCat tbItemCat : itemCat) {
            EasyUINode node = new EasyUINode();
            //设置父id
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            node.setState(tbItemCat.getIsParent() ? "closed" : "open");

            result.add(node);
        }

        return result;
    }

}
