package com.mingsu.interceptor;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.mingsu.model.MyLog;

public class LogInterceptor implements Interceptor {
	
	private static Logger logger = Logger.getLogger(LogInterceptor.class);
	
	public void intercept(Invocation inv) {
		
//		IndexController controller= (IndexController) inv.getController();
//		MyLog myLog = new MyLog();
//		myLog.set("biz", inv.getActionKey());
//		myLog.save();
		
//		String postData=HttpKit.readData(controller.getRequest());
		String password = inv.getController().getRequest().getParameter("email");
		String loginname = inv.getController().getRequest().getParameter("loginname");
		logger.info("Before invoking: email = " + loginname);
		logger.info("Before invoking: password = " + password);
		
		inv.invoke();
	}
}
