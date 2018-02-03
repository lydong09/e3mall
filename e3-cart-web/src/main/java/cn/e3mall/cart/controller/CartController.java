package cn.e3mall.cart.controller;

import cn.e3mall.cart.constant.CartConstant;
import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Dong
 * Date: Created in 2018/2/2
 */
@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private CartService cartService;

    @RequestMapping("/cart/add/{itemId}")
    public String addCart(@PathVariable long itemId , @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request, HttpServletResponse response){
        //登录状态
        TbUser user = (TbUser) request.getAttribute(CartConstant.LOGIN_USER);
        if (user != null){
            //存入服务端，先存入缓存
            cartService.addCart(user.getId(),itemId,num);
            return "cartSuccess";
        }


        //未登录状态
        //创建购物车集合
        List<TbItem> cartList = new ArrayList<>();
        //首先获取cookie里的购物车信息
        String json = CookieUtils.getCookieValue(request, CartConstant.CART, true);
        //如果存在购物车信息
        //加一个flag判断购物车中是否有商品
        boolean flag = false;
        if (StringUtils.isNotBlank(json)){

            cartList = JsonUtils.jsonToList(json, TbItem.class);
            //判断购物车中是否有该商品,有就增加数量
            for (TbItem tbItem : cartList) {
                if (tbItem.getId().longValue() == itemId){
                    tbItem.setNum(tbItem.getNum() + num);
                    flag = true;
                    break;
                }
            }
        }

        if (!flag){

            //没有新增一个加入购物车
            TbItem item = itemService.getItemById(itemId);
            //给商品设置图片
            if (StringUtils.isNotBlank(item.getImage())){
                item.setImage(item.getImage().split(",")[0]);
            }
            //设置购买的商品数量
            item.setNum(num);

            cartList.add(item);
        }

        //将购物车写入cookie
        CookieUtils.setCookie(request,response,CartConstant.CART,JsonUtils.objectToJson(cartList),true);

        return "cartSuccess";
    }

    /**
     * 展示购物车里的商品
     */
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request,HttpServletResponse response){
        //判断登录状态
        //获取购物车中的商品信息并展示
        List<TbItem> cartList = getCart(request);


        TbUser user = (TbUser) request.getAttribute(CartConstant.LOGIN_USER);
        if (user != null){
            //将cookie里的商品和缓存里的商品合并merge
            cartService.merge(user.getId(),cartList);
            //取出缓存里的购物车信息
            cartList = cartService.getTbItemList(user.getId());
            //删除cookie的购物车信息
            CookieUtils.setCookie(request,response,CartConstant.CART,"");
        }


        request.setAttribute("cartList",cartList);

        return "cart";
    }

    /**
     * 更新商品的数量
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public E3Result updateCart(@PathVariable long itemId , @PathVariable Integer num ,
                               HttpServletRequest request,HttpServletResponse response){
        TbUser user = (TbUser) request.getAttribute(CartConstant.LOGIN_USER);
        if (user != null){
            //用户已经登录,更新购物车数量

            return cartService.updateCart(user.getId(),itemId,num);

        }


        //未登录状态
        //获取购物车列表
        List<TbItem> cartList = getCart(request);

        //将数量更新
        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId){
                tbItem.setNum(num);
                break;
            }
        }

        //写入cookie
        CookieUtils.setCookie(request,response,CartConstant.CART,JsonUtils.objectToJson(cartList),true);

        return E3Result.ok();
    }

    /**
     * 删除购物车中的商品
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCart(@PathVariable long itemId , HttpServletRequest request,
                             HttpServletResponse response){
        //判断登录状态
        TbUser user = (TbUser) request.getAttribute(CartConstant.LOGIN_USER);
        if (user != null){
            cartService.deleteCartItem(user.getId(),itemId);
            return "redirect:/cart/cart.html";
        }


        //获得购物车列表
        List<TbItem> cartList = getCart(request);

        for (TbItem tbItem : cartList) {
            if (tbItem.getId().longValue() == itemId){
                cartList.remove(tbItem);
                break;
            }
        }

        //将修改写入cookie
        CookieUtils.setCookie(request,response,CartConstant.CART,JsonUtils.objectToJson(cartList),true);

        return "redirect:/cart/cart.html";
    }

    /**
     * 获取购物车列表
     * @return
     */
    private List<TbItem> getCart(HttpServletRequest request){
        String json = CookieUtils.getCookieValue(request, CartConstant.CART, true);

        if (StringUtils.isBlank(json)){
            return new ArrayList<TbItem>();
        }

        List<TbItem> cartList = JsonUtils.jsonToList(json, TbItem.class);
        return cartList;
    }

}
