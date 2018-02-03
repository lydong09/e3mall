package cn.e3mall.sso.service;

import cn.e3mall.common.utils.E3Result;

/**
 * @Author: Dong
 * Date: Created in 2018/1/31
 */
public interface LoginService {
    E3Result login(String username , String password);
}
