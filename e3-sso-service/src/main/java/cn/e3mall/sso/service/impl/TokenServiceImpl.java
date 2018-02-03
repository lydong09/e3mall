package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author: Dong
 * Date: Created in 2018/1/31
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    @Override
    public E3Result getUserByToken(String token) {
        //从redis中取出存放的用户信息
        String json = jedisClient.get("session:" + token);

        if (json == null){
            //用户信息已超过三十分钟未访问
            return E3Result.build(201,"用户信息已经过期");
        }

        //重新设置过期时间
        jedisClient.expire("session:" + token,SESSION_EXPIRE);
        //获得存储的用户对象
        TbUser tbUser = JsonUtils.jsonToPojo(json, TbUser.class);

        return E3Result.ok(tbUser);
    }
}
