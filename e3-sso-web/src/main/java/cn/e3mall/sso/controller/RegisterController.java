package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Dong
 * Date: Created in 2018/1/27
 */
@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @RequestMapping("/page/register")
    public String showRegister(){

        return "register";
    }

    /**
     * 注册效验,响应ajax请求
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public E3Result check(@PathVariable String param ,@PathVariable String type){

        E3Result result = registerService.check(param, type);
        return result;
    }


    /**produces = "application/json;charset=utf-8";spring4.1版本后加入了下面这种方式
     * 这两种方式都可以
     * 执行注册
     * @return
     */
    @RequestMapping(value = "/user/register",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public E3Result register(TbUser tbUser){

        E3Result result = registerService.register(tbUser);

        return result;
    }

}
