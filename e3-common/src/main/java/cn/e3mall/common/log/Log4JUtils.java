package cn.e3mall.common.log;

import org.apache.log4j.Logger;
import sun.reflect.Reflection;

public class Log4JUtils {
    private static Logger logger = null;

    public static Logger getLogger(){
        if (null == logger){
            logger = Logger.getLogger(Reflection.getCallerClass().getName());
        }
        return logger;
    }
}
