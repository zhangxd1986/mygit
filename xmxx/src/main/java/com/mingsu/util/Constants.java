package com.mingsu.util;

public class Constants {
	//等待对战用户redis的key
	public static final String REDIS_BATTLE_WAITUSER_KEY = "BAT_WAITUSER_";
	//相同用户发起对战后请求次数redis的key
	public static final String REDIS_BATTLE_REQTIMES_KEY = "BAT_REQTIMES_";
	//对战用户redis的key
	public static final String REDIS_BATTLE_KEY = "BAT_STARTUSER_";
	//对战用户初始星星布局redis的key
	public static final String REDIS_BATTLE_DEFAULT_STAR_KEY = "BAT_DEFSTAR_";
	//对战胜利用户redis的key
	public static final String REDIS_BATTLE_WINNER_KEY = "BAT_WINNER_";
	//deviceid和积分id对应的redis的key
	public static final String REDIS_USER_SCORE_KEY = "USER_SCORE_";
	//deviceid和等级id对应的redis的key
	public static final String REDIS_USER_GRADE_KEY = "USER_GRADE_";
	//对战中用户当前分数对应的redis的key
	public static final String REDIS_BATTLE_GAMESCORE_KEY = "BAT_GAMESCORE_";
	//对战中用户点击坐标对应的redis的key
	public static final String REDIS_BATTLE_POSITION_KEY = "BAT_POSITION_";
	
	
	//对战等待时间，单位秒
	public static final int BALLTE_WAIT_TIME = 5;
	//对战默认时间【5分钟】，超时认为战斗已结束，单位秒
	public static final int BALLTE_DEFAULT_TIME = 300;
	//对战类型：0-机器人，1-正常用户
	public static final int BALLTE_TYPE_MCH = 0;
	public static final int BALLTE_TYPE_HUMAN = 1;
	//对战机器人默认设备号
	public static final String BALLTE_MCH_DEVICEID = "MCH_HUIEUDB8364HS8";
	
	
	//对战胜利所需分数
	public static final int BALLTE_SCORE_WIN_NEED = 3000;
	//对战胜利得分
	public static final int BALLTE_SCORE_WIN = 20;
	//对战失败得分
	public static final int BALLTE_SCORE_LOSE = 5;
	//用户等级标识
	public static final int USER_GRADE_TONG1 = 1;
	public static final int USER_GRADE_TONG2 = 2;
	public static final int USER_GRADE_TONG3 = 3;
	public static final int USER_GRADE_YIN1 = 4;
	public static final int USER_GRADE_YIN2 = 5;
	public static final int USER_GRADE_YIN3 = 6;
	public static final int USER_GRADE_JIN1 = 7;
	public static final int USER_GRADE_JIN2 = 8;
	public static final int USER_GRADE_JIN3 = 9;
	
	//等级所需分数
	public static final int GRADE_SCORE_NEED_TONG1 = 10;
	public static final int GRADE_SCORE_NEED_TONG2 = 30;
	public static final int GRADE_SCORE_NEED_TONG3 = 70;
	public static final int GRADE_SCORE_NEED_YIN1 = 130;
	public static final int GRADE_SCORE_NEED_YIN2 = 210;
	public static final int GRADE_SCORE_NEED_YIN3 = 410;
	public static final int GRADE_SCORE_NEED_JIN1 = 700;
	public static final int GRADE_SCORE_NEED_JIN2 = 1110;
	public static final int GRADE_SCORE_NEED_JIN3 = 2110;
	
}
