package cn.e3mall.sso.controller;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Dong
 * Date: Created in 2018/1/31
 */
@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    /*@RequestMapping(value = "/user/token/{token}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token ,String callback){

        E3Result result = tokenService.getUserByToken(token);
        //响应结果之前，判断是否为jsonp请求(jsonp请求会发送一个callback参数)
        if (StringUtils.isNotBlank(callback)){
            //将结果封装成一个js函数响应
            return callback + "(" + JsonUtils.objectToJson(result) + ");";
        }

        return JsonUtils.objectToJson(result);
    }*/



    @RequestMapping("/user/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token ,String callback){

        E3Result result = tokenService.getUserByToken(token);
        //判断是否是jsonp请求
        if (StringUtils.isNotBlank(callback)){
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);

            return mappingJacksonValue;
        }

        return result;



    }
}
