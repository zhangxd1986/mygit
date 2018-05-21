package com.mingsu.res;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class BattleingRes extends BaseRes{
	
	//对手当前的分数
	private Integer score = 0;
	//用户请求间隔期间，对手点击的坐标列表
	private List<Integer> positionList;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public List<Integer> getPositionList() {
		return positionList;
	}

	public void setPositionList(List<Integer> positionList) {
		this.positionList = positionList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
