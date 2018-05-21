package com.mingsu.res;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BattleRes extends BaseRes{
	
	//对战类型：0-机器人，1-正常用户
	private int battleType = 0;
	//当前用户的初始星星布局
	private String initStarMine = "";
	//对战用户的初始星星布局
	private String initStarOpponent = "";
	//对战id
	private int id = 0;
	
	public int getBattleType() {
		return battleType;
	}

	public void setBattleType(int battleType) {
		this.battleType = battleType;
	}

	public String getInitStarMine() {
		return initStarMine;
	}

	public void setInitStarMine(String initStarMine) {
		this.initStarMine = initStarMine;
	}

	public String getInitStarOpponent() {
		return initStarOpponent;
	}

	public void setInitStarOpponent(String initStarOpponent) {
		this.initStarOpponent = initStarOpponent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
