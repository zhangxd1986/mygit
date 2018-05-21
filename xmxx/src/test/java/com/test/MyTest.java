package com.test;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.jfinal.kit.HttpKit;

public class MyTest {
	
//	private static String baseUrl = "http://localhost:8088/";
	private static String baseUrl = "http://120.25.100.16:7777/xmxx/";
//	private static String baseUrl = "http://10.211.55.5:8080/jfinal_latte/";

	private static void login(){
		String loginUrl = baseUrl+"login";

		Map<String, String> map = new HashMap<String, String>();

		for(int i=41;i<46;i++){
			map.put("deviceid","deviceid"+i);
			map.put("imsi","4600000");
			map.put("imei","8666666");
			map.put("hsman","三星");
			map.put("hstype","note");
			map.put("ip","126.3.2.1");
			map.put("appversion","1.0");
			map.put("channel","SXDV");
			
			String s = HttpKit.post(loginUrl, map, "");
			System.out.println(s);
		}
		
	}
	
	private static void battle(){
		String loginUrl = baseUrl+"battle";

		Map<String, String> map = new HashMap<String, String>();

//		map.put("deviceid","1235");
		
		//前5个人两两配对，最后一个请求一次
		for(int i=41;i<46;i++){
			map.put("deviceid","deviceid"+i);
			String s = HttpKit.post(loginUrl, map, "");
			System.out.println("第"+i+"个用户"+map.get("deviceid")+"第1次请求返回："+s);
		}
		//最后一个再请求4次，匹配机器人
		for(int i=2;i<6;i++){
			map.put("deviceid","deviceid"+45);
			String s = HttpKit.post(loginUrl, map, "");
			System.out.println("第5个用户"+map.get("deviceid")+"第"+i+"次请求返回："+s);
		}
		
		//第一个用户第2次请求
		map.put("deviceid","deviceid31");
		String s = HttpKit.post(loginUrl, map, "");
		System.out.println("第1个用户"+map.get("deviceid")+"第2次请求返回："+s);
		
		//第三个用户第2次请求
		map.put("deviceid","deviceid33");
		String s3 = HttpKit.post(loginUrl, map, "");
		System.out.println("第3个用户"+map.get("deviceid")+"第2次请求返回："+s3);
	}
	
	private static void battleing(){
		String loginUrl = baseUrl+"battleing";

		Map<String, String> map = new HashMap<String, String>();

		map.put("deviceid","deviceid41");
		map.put("position","44");
		map.put("score","1000");
		map.put("id","113");
		
		String s3 = HttpKit.post(loginUrl, map, "");
		System.out.println("对战结束，返回："+s3);
	}
	
	private static void userInfo(){
		String loginUrl = baseUrl+"userInfo";

		Map<String, String> map = new HashMap<String, String>();

		map.put("deviceid","deviceid14");
		
		String s3 = HttpKit.post(loginUrl, map, "");
		System.out.println("用户信息，返回："+s3);
	}
	
	private static void gameOver(){
		String loginUrl = baseUrl+"gameOver";

		Map<String, String> map = new HashMap<String, String>();

		map.put("deviceid","deviceid14");
		
		String s3 = HttpKit.post(loginUrl, map, "");
		System.out.println("游戏结束，返回："+s3);
	}
	
	private static void rankingList(){
		String loginUrl = baseUrl+"rankingList";

		Map<String, String> map = new HashMap<String, String>();

		map.put("deviceid","deviceid14");
		
		String s3 = HttpKit.post(loginUrl, map, "");
		System.out.println("排行榜，返回："+s3);
	}
	
	private static void maidian(){
		String loginUrl = baseUrl+"maidian";

		Map<String, String> map = new HashMap<String, String>();

		map.put("deviceid","deviceid14");
		map.put("mdtype","1");
		
		String s3 = HttpKit.post(loginUrl, map, "");
		System.out.println("数据埋点，返回："+s3);
	}
	
	public static void main(String[] args) {
//		login(); 
//		battle();
//		battleing();
//		userInfo();
//		gameOver();
//		rankingList();
		maidian();
	}
	
}
