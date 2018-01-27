package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器
 * @author 89388
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{
	
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception e) {
		//打印控制台
		e.printStackTrace();
		//打印日志,有三个级别
		logger.debug("测试出现异常");
		logger.info("出现异常");
		logger.error("系统发生异常", e);
		
		//发送邮件，短信,使用第三方的webService
		
		//返回错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		
		return modelAndView;
	}

}
