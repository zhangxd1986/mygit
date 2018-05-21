package com.mingsu.job;

import org.apache.log4j.Logger;

public class Task1 implements Runnable{
	
	private static Logger logger = Logger.getLogger(Task1.class);

	public void run() {
		logger.info("111111");
		System.out.println("sys111111");
	}

}
