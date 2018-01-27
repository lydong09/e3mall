package cn.e3mall.TestLogger;

import cn.e3mall.common.log.Log4JUtils;

public class testLogger {
    public static void main(String[] args) {
        Log4JUtils.getLogger().info("hello world");
    }
}
