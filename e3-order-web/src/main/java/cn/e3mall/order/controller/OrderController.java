package cn.e3mall.order.controller;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Dong
 * Date: Created in 2018/2/5
 */
@Controller
public class OrderController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/order/order-cart")
    public String showOrder(HttpServletRequest request){
        //取用户信息
        TbUser user = (TbUser) request.getAttribute("user");

        if (user != null){
            List<TbItem> cartList = cartService.getTbItemList(user.getId());
            request.setAttribute("cartList",cartList);
        }


        return "order-cart";
    }


    @RequestMapping(value = "/order/create",method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo , HttpServletRequest request){
        //取用户信息
        TbUser user = (TbUser) request.getAttribute("user");
        //把用户信息加入orderInfo
        orderInfo.setUserId(user.getId());
        orderInfo.setBuyerNick(user.getUsername());

        //调用服务,生成订单表
        E3Result result = orderService.createOrder(orderInfo);
        //订单生成成功，清空购物车
        if (result.getStatus() == 200){

            cartService.clear(user.getId());
        }

        //将订单号等数据返回
        request.setAttribute("orderId",result.getData());
        request.setAttribute("payment",orderInfo.getPayment());

        return "success";
    }
}
