package com.mingsu.controller;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import com.mingsu.res.BaseRes;
import com.mingsu.res.BattleRes;
import com.mingsu.res.BattleingRes;
import com.mingsu.res.LoginRes;
import com.mingsu.res.RankingListRes;
import com.mingsu.res.UserInfoRes;
import com.mingsu.service.UserService;
import com.mingsu.util.CommonUtil;
import com.mingsu.util.Constants;
import com.mingsu.util.ErrorCode;

/**
 * IndexController
 */

public class IndexController extends Controller{
	static UserService service = new UserService();
	
	private static Logger logger = Logger.getLogger(IndexController.class);
	
	public void login() {
		
		LoginRes res = new LoginRes();
		
		String deviceid = getPara("deviceid");
		String imsi = getPara("imsi");
		String imei = getPara("imei");
		String hsman = getPara("hsman");
		String hstype = getPara("hstype");
		String ip = getPara("ip");
		String appversion = getPara("appversion");
		String channel = getPara("channel");

		Record record = new Record().set("deviceid", deviceid)
				.set("imsi", imsi)
				.set("imei", imei)
				.set("hsman", hsman)
				.set("hstype", hstype)
				.set("ip", ip)
				.set("appversion", appversion)
				.set("channel", channel)
				.set("create_time", CommonUtil.dbnow());
		
		if(service.userExsit(deviceid) > 0){
			renderJson(res);
			return;
		}
		service.addUser(record);
		
		Integer scoreID = service.queryScoreID(deviceid);
		
		if(scoreID != null && scoreID > 0){
			renderJson(res);
			return;
		}
		
		Record record2 = new Record().set("deviceid", deviceid);
		service.initUserScore(record2);
		
		Integer gradeID = service.queryGradeID(deviceid);
		if(gradeID != null && gradeID > 0){
			renderJson(res);
			return;
		}
		service.initUserGrade(record2);
		
		Cache myCache = Redis.use();
		//设置用户唯一设备号和积分、等级的对应关系
		myCache.set(Constants.REDIS_USER_SCORE_KEY+deviceid, scoreID);
		myCache.set(Constants.REDIS_USER_GRADE_KEY+deviceid, gradeID);
		
		logger.info("+++req login,res = "+res);
		renderJson(res);
	}
	
	//查询用户积分及等级
	public void userInfo() {
		
		UserInfoRes res = new UserInfoRes();
		
		String deviceid = getPara("deviceid");
		Integer score = service.queryScore(deviceid);
		if(score == null){
			score = 0;
		}
		res.setScore(score);
		res.setGradeName(CommonUtil.getGradeNameByScore(score));
		
		logger.info("+++req userInfo,res = "+res);
		renderJson(res);
	}

	/**
	 * 第一人请求进来，等待，直到第二人来开始对战
	 * 第二人请求进来，查询当前有没有等待对战的玩家，若有，则与之对战【同时响应A，B两个请求】
	 * 若没有，则充当第一人的角色，等待
	 */
	public void battle() {
		BattleRes res = new BattleRes();
		String deviceid = getPara("deviceid");
		
		Cache myCache = Redis.use();
		String battleUsers = myCache.get(Constants.REDIS_BATTLE_KEY+deviceid);

		//说明本次请求的用户已经在对战了【A】，要返回之前【B】的对战信息
		//是A用户前一次请求还没有对手返回等待，这次轮训已经有对手了，返回对战开始的状态
		if(StringUtils.isNotBlank(battleUsers)
				&& battleUsers.contains(deviceid)){
			String[] userArr = battleUsers.split("##");
			String opponentUser = userArr[0];
			String nowUser = userArr[1];
			//args[0]是对手，args[1]是本次请求的用户
			String initStarMine = myCache.get(Constants.REDIS_BATTLE_DEFAULT_STAR_KEY+deviceid);
			
			logger.info("User A Polling start,deviceid = " + deviceid + ", nowUser from redis = " + nowUser + ",opponentUser from redis = " + opponentUser);
			
			String initStarOpponent = myCache.get(Constants.REDIS_BATTLE_DEFAULT_STAR_KEY+opponentUser);
			res.setInitStarMine(initStarMine);
			res.setInitStarOpponent(initStarOpponent);
			
			res.setBattleType(Constants.BALLTE_TYPE_HUMAN);
			//查询当前对战id，下发。对战进行中时客户端上传
			res.setId(service.queryBattleID(nowUser,opponentUser));
			
			logger.info("+++req battle,res = "+res);
			renderJson(res);
			return;
		}
		
		String waitUser = myCache.get(Constants.REDIS_BATTLE_WAITUSER_KEY);
		if(StringUtils.isNotBlank(waitUser) && !waitUser.equals(deviceid)){
			//双人对战
			res.setBattleType(Constants.BALLTE_TYPE_HUMAN);
			myCache.del(Constants.REDIS_BATTLE_WAITUSER_KEY);
			//等待的用户请求次数也清除【按请求顺序，A只和B打，不和C打】
			myCache.del(Constants.REDIS_BATTLE_REQTIMES_KEY+waitUser);
			
			setBattleInfo(deviceid,waitUser,res,myCache);
			
		}else{
			//设置当前用户等待时间【5秒】
			myCache.setex(Constants.REDIS_BATTLE_WAITUSER_KEY, Constants.BALLTE_WAIT_TIME, deviceid);
			
			Long times = myCache.incr(Constants.REDIS_BATTLE_REQTIMES_KEY+deviceid);
			logger.info("+++req battle,times =  "+times +", deviceid = "+ deviceid);
			if(Constants.BALLTE_WAIT_TIME == times){
				//客户端每隔1秒请求一次，第5次还是没有等待用户，下发机器人，并且清除该用户的请求记录
				res.setBattleType(Constants.BALLTE_TYPE_MCH);
				myCache.del(Constants.REDIS_BATTLE_REQTIMES_KEY+deviceid);
				myCache.del(Constants.REDIS_BATTLE_WAITUSER_KEY);
				
				waitUser = Constants.BALLTE_MCH_DEVICEID;
				setBattleInfo(deviceid,waitUser,res,myCache);
				
			}else{
				/**
				 * 这里有一点瑕疵：当A请求一次之后，如果客户端没有轮训请求到5次，那么这个次数就在redis中一直存在，
				 * 当值=4的时候，过几天用户再次请求，则1秒就匹配对手或者机器人了，用户其实也无感知
				 * redis中存的key是BRT_deviceid
				 */
				res.setCode(ErrorCode.BALLTE_WAIT);
				res.setMsg(ErrorCode.BALLTE_WAIT_MSG);
				logger.info("+++req battle,res = "+res);
				renderJson(res);
				return;
			}
		}
			
		//条件满足时，完成工作
		logger.info("+++req battle,res = "+res);
		renderJson(res);
		
	}
	
	/**
	 * 对战进行中接口
	 */
	@SuppressWarnings("unchecked")
	public void battleing() {
		
		BattleingRes res = new BattleingRes();
		String deviceid = getPara("deviceid");
		String position = getPara("position");
		String gameScore = getPara("score");
		String id = getPara("id");
		
		Cache myCache = Redis.use();
		String battleUsers = myCache.get(Constants.REDIS_BATTLE_KEY+deviceid);
		if(StringUtils.isBlank(battleUsers)
				||!battleUsers.contains(deviceid)){
			res.setCode(ErrorCode.BALLTE_END);
			res.setMsg(ErrorCode.BALLTE_END_MSG);
			logger.info("+++req battleing,res = "+res);
			renderJson(res);
			return;
		}
		
		String[] userArr = battleUsers.split("##");
		String opponentUser;
		
		if(deviceid.equals(userArr[0])){
			opponentUser = userArr[1];
		}else{
			opponentUser = userArr[0];
		}
		
		//score=-1表示用户跟机器人玩，并且输了
		if("-1".equals(gameScore)){
			myCache.del(Constants.REDIS_BATTLE_KEY+deviceid);
			myCache.del(Constants.REDIS_BATTLE_KEY+opponentUser);
			//更新当前用户积分，并且判断是否需要升级
			setScore(deviceid,Constants.BALLTE_SCORE_LOSE);
			res.setCode(ErrorCode.BALLTE_FAIL);
			res.setMsg(ErrorCode.BALLTE_FAIL_MSG);
			logger.info("+++req battleing,res = "+res);
			renderJson(res);
			return;
		}
		
		//设置自己的分数和坐标列表
		//设置用户当前游戏得分【供对手请求时下发给对手】，-1表示当前用户并未操作，客户端为了获取对手信息的请求，因此不需要设置坐标
		myCache.setex(Constants.REDIS_BATTLE_GAMESCORE_KEY+deviceid, Constants.BALLTE_DEFAULT_TIME, gameScore);
		if(!"-1".equals(position)){
			myCache.lpush(Constants.REDIS_BATTLE_POSITION_KEY+deviceid, position);
		}
		
		//取到对手的分数和点击的坐标列表
		Object opponentScore = myCache.get(Constants.REDIS_BATTLE_GAMESCORE_KEY+opponentUser);
		Integer opponentScoreInt = 0;
		if(opponentScore != null){
			opponentScoreInt = Integer.parseInt(opponentScore+"");
		}
		res.setScore(opponentScoreInt);
		List<Integer> positionList = myCache.lrange(Constants.REDIS_BATTLE_POSITION_KEY+opponentUser, 0, -1);
		Collections.reverse(positionList);
		res.setPositionList(positionList);
		myCache.del(Constants.REDIS_BATTLE_POSITION_KEY+opponentUser);
//		myCache.del(Constants.REDIS_BATTLE_GAMESCORE_KEY+opponentUser);
		
		//查询对手是否胜利，若对手胜利，则返回游戏结束，并且清除本次游戏缓存
		if("1".equals(myCache.get(Constants.REDIS_BATTLE_WINNER_KEY+opponentUser)+"")){
			myCache.del(Constants.REDIS_BATTLE_KEY+deviceid);
			myCache.del(Constants.REDIS_BATTLE_KEY+opponentUser);
			//更新当前用户积分，并且判断是否需要升级
			setScore(deviceid,Constants.BALLTE_SCORE_LOSE);
			res.setCode(ErrorCode.BALLTE_FAIL);
			res.setMsg(ErrorCode.BALLTE_FAIL_MSG);
			logger.info("+++req battleing,res = "+res);
			renderJson(res);
			return;
		}
		
		//当前用户对战胜利，记录分数，当前用户返回游戏结束，获胜；对手下次请求上来返回游戏结束，失败
		if(Integer.parseInt(gameScore) >= Constants.BALLTE_SCORE_WIN_NEED){
			//设置胜者标识
			myCache.setex(Constants.REDIS_BATTLE_WINNER_KEY+deviceid, Constants.BALLTE_DEFAULT_TIME, "1");
			//更新对战明细表，确定胜者
			Record record = new Record().set("winner", deviceid)
					.set("id", id)
					.set("edit_time", CommonUtil.dbnow());
			service.updateBattle(record);
			//更新当前用户积分，并且判断是否需要升级
			setScore(deviceid,Constants.BALLTE_SCORE_WIN);
			
			res.setCode(ErrorCode.BALLTE_WIN);
			res.setMsg(ErrorCode.BALLTE_WIN_MSG);
			logger.info("+++req battleing,res = "+res);
			renderJson(res);
			return;
		}
		
		logger.info("+++req battleing,res = "+res);
		renderJson(res);
	}
	
	//游戏结束
	public void gameOver() {
		
		BaseRes res = new BaseRes();
		Cache myCache = Redis.use();
		
		String deviceid = getPara("deviceid");
		
		String battleUsers = myCache.get(Constants.REDIS_BATTLE_KEY+deviceid);
		
		myCache.del(Constants.REDIS_BATTLE_KEY+deviceid);

		if(StringUtils.isNotBlank(battleUsers)){
			String[] userArr = battleUsers.split("##");
			myCache.del(Constants.REDIS_BATTLE_KEY+userArr[0]);
			myCache.del(Constants.REDIS_BATTLE_KEY+userArr[1]);
		}
		
		logger.info("+++req gameOver,res = "+res);
		renderJson(res);
	}

	//排行榜
	public void rankingList() {
		
		RankingListRes res = new RankingListRes();
		String deviceid = getPara("deviceid");
		
		Integer score = service.queryScore(deviceid);
		res.setScore(score);
		res.setRanking(service.queryUserRanking(score));
		res.setRankingList(service.queryRankingList());
		
		logger.info("+++req rankingList,res = "+res);
		renderJson(res);
	}
	
	//数据埋点
	public void maidian() {
		
		BaseRes res = new BaseRes();
		String deviceid = getPara("deviceid");
		String mdtype = getPara("mdtype");
		
		Record record = new Record().set("deviceid", deviceid)
				.set("mdtype", mdtype)
				.set("create_time", CommonUtil.dbnow());
		service.addMaiDian(record);
		
		logger.info("+++req maidian,res = "+res);
		renderJson(res);
	}


	//对战开始，设置双方初始星星布局，维护双方信息到redis中【B和A匹配成功后，A第二次请求上来返回AB的信息】
	private void setBattleInfo(String nowUser, String waitUser, BattleRes res, Cache myCache){
		//取到AB的初始星星布局设置在响应中
		String initStarMine = CommonUtil.initStar();
		String initStarOpponent = CommonUtil.initStar();
		res.setInitStarMine(initStarMine);
		res.setInitStarOpponent(initStarOpponent);
		
		//维护AB状态，表示二人正在对战
		myCache.setex(Constants.REDIS_BATTLE_KEY+nowUser, Constants.BALLTE_DEFAULT_TIME, nowUser+"##"+waitUser);
		myCache.setex(Constants.REDIS_BATTLE_KEY+waitUser, Constants.BALLTE_DEFAULT_TIME, nowUser+"##"+waitUser);
		//维护AB的初始战局
		myCache.setex(Constants.REDIS_BATTLE_DEFAULT_STAR_KEY+nowUser, Constants.BALLTE_DEFAULT_TIME, initStarMine);
		myCache.setex(Constants.REDIS_BATTLE_DEFAULT_STAR_KEY+waitUser, Constants.BALLTE_DEFAULT_TIME, initStarOpponent);
	
		//对战信息入库
		Record record = new Record().set("deviceidA", nowUser)
				.set("deviceidB", waitUser)
				.set("create_time", CommonUtil.dbnow());
		service.addBattle(record);
		//查询当前对战id，下发。对战进行中时客户端上传
		res.setId(service.queryBattleID(nowUser,waitUser));
	}
		
	//更新当前用户积分，并且判断是否需要升级
	private void setScore(String deviceid, int addScore){
		//原始分数
		Integer score = service.queryScore(deviceid);
		
		Cache myCache = Redis.use();
		Integer scoreID = myCache.get(Constants.REDIS_USER_SCORE_KEY+deviceid);
		if(scoreID == null){
			scoreID = service.queryScoreID(deviceid);
			myCache.set(Constants.REDIS_USER_SCORE_KEY+deviceid,scoreID);
		}
		Record record = new Record().set("id", scoreID)
				.set("score", addScore+score)
				.set("edit_time", CommonUtil.dbnow());
		service.updateUserScore(record);
		
		//判断是否需要升级
		Integer gradeID = myCache.get(Constants.REDIS_USER_GRADE_KEY+deviceid);
		if(gradeID == null){
			gradeID = service.queryGradeID(deviceid);
			myCache.set(Constants.REDIS_USER_GRADE_KEY+deviceid,gradeID);
		}
		
		Integer grade = CommonUtil.getGradeByScoreAdd(score, addScore);
		
		Record gradeRecord = new Record().set("id", gradeID)
				.set("grade", grade)
				.set("edit_time", CommonUtil.dbnow());
		service.updateUserGarde(gradeRecord);
		
	}
	
}



