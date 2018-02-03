package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Dong
 * Date: Created in 2018/1/31
 */
@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * 跳向登录页面
     */
    @RequestMapping("/page/login")
    public String toLogin(){

        return "login";
    }

    /**
     * 执行登录操作，同时将登录信息保存到redis里
     */
    @RequestMapping(value = "/user/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public E3Result login(String username , String password, HttpServletRequest request, HttpServletResponse response){

        E3Result result = loginService.login(username, password);

        if (result.getStatus() == 200){
            String token = (String) result.getData();
            //将token写入cookie
            CookieUtils.setCookie(request,response,TOKEN_KEY,token);
        }

        return result;
    }

}
