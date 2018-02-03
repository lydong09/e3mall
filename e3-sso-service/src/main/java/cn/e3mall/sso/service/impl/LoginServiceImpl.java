package cn.e3mall.sso.service.impl;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

/**
 * @Author: Dong
 * Date: Created in 2018/1/31
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;
    /**
     * 执行登录操作，将登录信息保存到redis里,这是单点登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public E3Result login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return E3Result.build(400,"用户账号或密码填写不完整");
        }
        //将密码使用md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //设置查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();

        criteria.andUsernameEqualTo(username);
        criteria.andPasswordEqualTo(password);
        //执行查询
        List<TbUser> tbUsers = tbUserMapper.selectByExample(example);

        if (tbUsers == null || tbUsers.size() <= 0){
            return E3Result.build(400,"用户名或者密码错误");
        }

        //将数据加入缓存
        TbUser tbUser = tbUsers.get(0);
        //生成唯一token
        String token = UUID.randomUUID().toString();
        jedisClient.set("session:" +token, JsonUtils.objectToJson(tbUser));
        //设置过期时间
        jedisClient.expire("session:" +token,SESSION_EXPIRE);

        //将信息写入cookie,需要request,response,交给Controller层做

        return E3Result.ok(token);
    }
}
