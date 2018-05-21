package com.mingsu.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CommonUtil {
	private static Map<Integer,String> gardeMap;
	static{
		gardeMap = new HashMap<Integer,String>();
		gardeMap.put(1, "青铜1");
		gardeMap.put(2, "青铜2");
		gardeMap.put(3, "青铜3");
		gardeMap.put(4, "白银1");
		gardeMap.put(5, "白银2");
		gardeMap.put(6, "白银3");
		gardeMap.put(7, "黄金1");
		gardeMap.put(8, "黄金2");
		gardeMap.put(9, "黄金3");
	}
	
	public static String dbnow(){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	public static final void second(long seconds){
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static String initStar(){
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<100;i++){
			sb.append(r.nextInt(5)+1);
		}
		return sb.toString();
	}
	
	public static int getGradeByScoreAdd(int score,int addScore){
		Integer grade = Constants.USER_GRADE_JIN3;
		
		if(score + addScore < Constants.GRADE_SCORE_NEED_TONG1){
			grade = Constants.USER_GRADE_TONG1;
		}else if(score < Constants.GRADE_SCORE_NEED_TONG2
				&& score + addScore >= Constants.GRADE_SCORE_NEED_TONG2){
			grade = Constants.USER_GRADE_TONG2;
		}else if(score < Constants.GRADE_SCORE_NEED_TONG3
				&& score + addScore >= Constants.GRADE_SCORE_NEED_TONG3){
			grade = Constants.USER_GRADE_TONG3;
		}else if(score < Constants.GRADE_SCORE_NEED_YIN1
				&& score + addScore >= Constants.GRADE_SCORE_NEED_YIN1){
			grade = Constants.USER_GRADE_YIN1;
		}else if(score < Constants.GRADE_SCORE_NEED_YIN2
				&& score + addScore >= Constants.GRADE_SCORE_NEED_YIN2){
			grade = Constants.USER_GRADE_YIN2;
		}else if(score < Constants.GRADE_SCORE_NEED_YIN3
				&& score + addScore >= Constants.GRADE_SCORE_NEED_YIN3){
			grade = Constants.USER_GRADE_YIN3;
		}else if(score < Constants.GRADE_SCORE_NEED_JIN1
				&& score + addScore >= Constants.GRADE_SCORE_NEED_JIN1){
			grade = Constants.USER_GRADE_JIN1;
		}else if(score < Constants.GRADE_SCORE_NEED_JIN2
				&& score + addScore >= Constants.GRADE_SCORE_NEED_JIN2){
			grade = Constants.USER_GRADE_JIN2;
		}else if(score < Constants.GRADE_SCORE_NEED_JIN3
				&& score + addScore >= Constants.GRADE_SCORE_NEED_JIN3){
			grade = Constants.USER_GRADE_JIN3;
		}
		return grade;
	}
	
	public static int getGradeByScore(int score){
		Integer grade = Constants.USER_GRADE_JIN3;
		
		if(score < Constants.GRADE_SCORE_NEED_TONG1){
			grade = Constants.USER_GRADE_TONG1;
		}else if(score >= Constants.GRADE_SCORE_NEED_TONG2 && score < Constants.GRADE_SCORE_NEED_TONG3){
			grade = Constants.USER_GRADE_TONG2;
		}else if(score >= Constants.GRADE_SCORE_NEED_TONG3 && score < Constants.GRADE_SCORE_NEED_YIN1){
			grade = Constants.USER_GRADE_TONG3;
		}else if(score >= Constants.GRADE_SCORE_NEED_YIN1 && score < Constants.GRADE_SCORE_NEED_YIN2){
			grade = Constants.USER_GRADE_YIN1;
		}else if(score >= Constants.GRADE_SCORE_NEED_YIN2 && score < Constants.GRADE_SCORE_NEED_YIN3){
			grade = Constants.USER_GRADE_YIN2;
		}else if(score >= Constants.GRADE_SCORE_NEED_YIN3 && score < Constants.GRADE_SCORE_NEED_JIN1){
			grade = Constants.USER_GRADE_YIN3;
		}else if(score >= Constants.GRADE_SCORE_NEED_JIN1 && score < Constants.GRADE_SCORE_NEED_JIN2){
			grade = Constants.USER_GRADE_JIN1;
		}else if(score >= Constants.GRADE_SCORE_NEED_JIN2 && score < Constants.GRADE_SCORE_NEED_JIN3){
			grade = Constants.USER_GRADE_JIN2;
		}else if(score >= Constants.GRADE_SCORE_NEED_JIN3){
			grade = Constants.USER_GRADE_JIN3;
		}
		return grade;
	}
	
	public static String getGradeNameByScore(int score){
		Integer grade = getGradeByScore(score);
		return gardeMap.get(grade);
	}
	
	public static void main(String[] args) {
		System.out.println(initStar());
		System.out.println(initStar().length());
	}
}
