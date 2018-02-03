package cn.e3mall.sso.service.impl;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: Dong
 * Date: Created in 2018/1/31
 */
@Service
public class RegisterServiceImpl implements RegisterService{

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public E3Result check(String param, String type) {
        //判断传入的信息
        if (StringUtils.isBlank(param) || StringUtils.isBlank(type)){
            return E3Result.build(400,"传入的信息不完整");
        }

        //设置查询条件
        TbUserExample example = new TbUserExample();

        final TbUserExample.Criteria criteria = example.createCriteria();
        //判断数据是1 用户名 2 手机号
        if ("1".equals(type)){
            criteria.andUsernameEqualTo(param);
            List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
            if (tbUsers != null && tbUsers.size() > 0){
                return E3Result.build(400,"用户名已经被占用");
            }
        }

        if ("2".equals(type)){
            criteria.andPhoneEqualTo(param);
            List<TbUser> tbUsers = tbUserMapper.selectByExample(example);
            if (tbUsers != null && tbUsers.size() > 0){
                return E3Result.build(400,"手机号已经被占用");
            }
        }


        return E3Result.ok(true);
    }

    /**
     * 执行注册
     * @param tbUser
     * @return
     */
    @Override
    public E3Result register(TbUser tbUser) {
        //判断用户信息是否完整
        if (StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPassword())
                || StringUtils.isBlank(tbUser.getPhone())){
            return E3Result.build(400,"用户数据不完整");
        }
        //判断用户名与手机号是否被占用
        E3Result result = check(tbUser.getUsername(), "1");
        if (!(Boolean) result.getData()){
            return E3Result.build(400,"用户名被占用");
        }
        result = check(tbUser.getPhone(),"2");
        if (!(Boolean) result.getData()){
            return E3Result.build(400,"手机号被占用");
        }


        //补全pojo属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());

        //对密码进行md5加密
        String s = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(s);

        tbUserMapper.insert(tbUser);

        return E3Result.ok();
    }


}
