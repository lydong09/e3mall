package cn.e3mall.cart.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbItem;

import java.util.List;

/**
 * @Author: Dong
 * Date: Created in 2018/2/2
 */
public interface CartService {
    E3Result addCart(long userId,long itemId , Integer num);

    E3Result merge(long userId,List<TbItem> cartList);

    List<TbItem> getTbItemList(long userId);

    E3Result updateCart(long userId,long itemId,int num);

    E3Result deleteCartItem(long userId,long itemId);
}
