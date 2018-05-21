package com.mingsu.util;

public class ErrorCode {
	//登录
	public static final int LOGIN_ERROR = 1001;
	public static final String LOGIN_ERROR_MSG = "登录失败";
	
	//对战
	public static final int BALLTE_WAIT = 1002;
	public static final String BALLTE_WAIT_MSG = "等待匹配对手";
	//用户调用对战中接口，但是redis中已没有对战信息，返回游戏已结束
	public static final int BALLTE_END = 1003;
	public static final String BALLTE_END_MSG = "游戏已结束";
	
	public static final int BALLTE_WIN = 1004;
	public static final String BALLTE_WIN_MSG = "胜利";
	public static final int BALLTE_FAIL = 1005;
	public static final String BALLTE_FAIL_MSG = "失败";
}
