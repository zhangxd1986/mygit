package com.mingsu.job;

import org.apache.log4j.Logger;

public class Task2 implements Runnable{
	
	private static Logger logger = Logger.getLogger(Task2.class);

	public void run() {
		logger.info("222222");
		System.out.println("sys222222");
	}

}
