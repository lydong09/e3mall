package cn.e3mall.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Dong
 * Date: Created in 2018/1/27
 */
@Controller
public class RegisterController {

    @RequestMapping("/page/register")
    public String showRegister(){

        return "register";
    }
}
