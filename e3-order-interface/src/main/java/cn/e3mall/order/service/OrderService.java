package cn.e3mall.order.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;

/**
 * @Author: Dong
 * Date: Created in 2018/2/5
 */
public interface OrderService {

    E3Result createOrder(OrderInfo orderInfo);
}
