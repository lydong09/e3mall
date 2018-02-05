package cn.e3mall.order.interceptor;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Dong
 * Date: Created in 2018/2/5
 */
public class OrderInterceptor implements HandlerInterceptor{

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CartService cartService;

    @Value("${SSO_URL}")
    private String SSO_URL;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //判断用户登录状态，从token中取用户信息
        String token = CookieUtils.getCookieValue(request, "token", true);
        //如果token不存在，跳转到登录页面
        if (StringUtils.isBlank(token)){
            //登录成功后跳转到当前请求的url
            System.out.println(request.getRequestURL());
            response.sendRedirect(SSO_URL+"/page/login?redirect=" + request.getRequestURL());
            return false;
        }

        //调用sso系统的服务，根据token取用户信息
        E3Result result = tokenService.getUserByToken(token);
        //如果取不到，说明用户过期，需要登录
        if (result.getStatus() != 200){
            System.out.println(request.getRequestURL());
            response.sendRedirect(SSO_URL+"/page/login?redirect=" + request.getRequestURL());

            return false;
        }

        //如果取到用户信息，是登录状态，把用户信息写入request
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user",user);
        //合并cookie里的购物车信息
        String json = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNotBlank(json)){
            //合并购物车
            cartService.merge(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
