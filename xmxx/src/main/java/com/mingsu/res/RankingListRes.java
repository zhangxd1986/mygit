package com.mingsu.res;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RankingListRes extends BaseRes{
	
	//用户积分
	private int score = 0;
	//用户名次
	private int ranking = 1;
	//排行榜列表
	private List<Integer> rankingList;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getRanking() {
		return ranking;
	}

	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	public List<Integer> getRankingList() {
		return rankingList;
	}

	public void setRankingList(List<Integer> rankingList) {
		this.rankingList = rankingList;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
