package cn.e3mall.cart.interceptor;

import cn.e3mall.cart.constant.CartConstant;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Dong
 * Date: Created in 2018/2/2
 * true 放行  false 拦截
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //前处理,handler执行之前
        //从cookie中取出token
        String token = CookieUtils.getCookieValue(request, "token");
        if (StringUtils.isBlank(token)){
            return true;
        }

        //调用远程服务查询用户状态
        E3Result result = tokenService.getUserByToken(token);

        if (result.getStatus() != 200){
            return true;
        }

        //等于200说明用户正常登录状态
        TbUser user = (TbUser) result.getData();
        //把用户信息放入request域
        request.setAttribute(CartConstant.LOGIN_USER,user);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        //handler还行之后，modelAndView执行之后

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {

    }
}
