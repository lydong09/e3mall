package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;

/**
 * @Author: Dong
 * Date: Created in 2018/1/31
 */
public interface RegisterService {
    E3Result check(String param , String type);

    E3Result register(TbUser tbUser);
}
