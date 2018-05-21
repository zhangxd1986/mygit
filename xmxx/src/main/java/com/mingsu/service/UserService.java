package com.mingsu.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.mingsu.model.User;

/**
 * 本 demo 仅表达最为粗浅的 jfinal 用法，更为有价值的实用的企业级用法
 * 详见 JFinal 俱乐部: http://jfinal.com/club
 * 
 * UserService
 * 所有 sql 与业务逻辑写在 Service 中，不要放在 Model 中，更不
 * 要放在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class UserService {
	private static Logger logger = Logger.getLogger(UserService.class);
	
	/**
	 * 所有的 dao 对象也放在 Service 中
	 */
	private static final User dao = new User().dao();
	
//	public Page<User> paginate(int pageNumber, int pageSize) {
//		return dao.paginate(pageNumber, pageSize, "select *", "from blog order by id asc");
//	}
	
	public User findById(int id) {
		return dao.findById(id);
	}
	
	public void deleteById(int id) {
		dao.deleteById(id);
	}
	
	public Integer userExsit(String deviceid){
		String sql = Db.getSql("userExsit");
		logger.info("+++in UserService userExsit sql = " + sql);
		return Db.queryInt(sql,deviceid);
	}
	
	public Integer queryScoreID(String deviceid){
		String sql = Db.getSql("queryScoreID");
		logger.info("+++in UserService queryScoreID sql = " + sql);
		return Db.queryInt(sql,deviceid);
	}
	
	public Integer queryGradeID(String deviceid){
		String sql = Db.getSql("queryGradeID");
		logger.info("+++in UserService queryGradeID sql = " + sql);
		return Db.queryInt(sql,deviceid);
	}
	
	public Integer queryScore(String deviceid){
		String sql = Db.getSql("queryScore");
		logger.info("+++in UserService queryScore sql = " + sql);
		return Db.queryInt(sql,deviceid);
	}
	
	public Integer queryGrade(String deviceid){
		String sql = Db.getSql("queryGrade");
		logger.info("+++in UserService queryGrade sql = " + sql);
		return Db.queryInt(sql,deviceid);
	}
	
	public void addUser(Record record){
		String sql = Db.getSql("addUser");
		logger.info("+++in UserService addUser sql = " + sql);
		Db.save("xmxx_user", record);
	}
	
	public void initUserScore(Record record){
		String sql = Db.getSql("initUserScore");
		logger.info("+++in UserService initUserScore sql = " + sql);
		Db.save("xmxx_user_score", record);
	}
	
	public void initUserGrade(Record record){
		String sql = Db.getSql("initUserGrade");
		logger.info("+++in UserService initUserGrade sql = " + sql);
		Db.save("xmxx_user_grade", record);
	}
	
	public void addBattle(Record record){
		String sql = Db.getSql("addBattle");
		logger.info("+++in UserService addBattle sql = " + sql);
		Db.save("xmxx_user_battle", record);
	}
	
	public Integer queryBattleID(String deviceidA,String deviceidB){
		Kv cond = Kv.by("deviceidA",deviceidA).set("deviceidB", deviceidB);
		SqlPara sqlPara = Db.getSqlPara("queryBattleID",cond);
		logger.info("+++in UserService queryBattleID sql = " + sqlPara.getSql());
		return Db.findFirst(sqlPara).getInt("id");
	}
	
	public void updateBattle(Record record){
		String sql = Db.getSql("updateBattle");
		logger.info("+++in UserService updateBattle sql = " + sql);
		Db.update("xmxx_user_battle", record);
	}
	
	public void updateUserScore(Record record){
		String sql = Db.getSql("updateUserScore");
		logger.info("+++in UserService updateUserScore sql = " + sql);
		Db.update("xmxx_user_score", record);
	}
	
	public void updateUserGarde(Record record){
		String sql = Db.getSql("updateUserGrade");
		logger.info("+++in UserService updateUserGarde sql = " + sql);
		Db.update("xmxx_user_grade", record);
	}
	
	public Integer queryUserRanking(int score){
		String sql = Db.getSql("queryUserRanking");
		logger.info("+++in UserService queryUserRanking sql = " + sql);
		return Db.queryInt(sql,score);
	}
	
	public List<Integer> queryRankingList(){
		String sql = Db.getSql("queryRankingList");
		logger.info("+++in UserService queryRankingList sql = " + sql);
		return Db.query(sql);
	}
	
	public void addMaiDian(Record record){
		String sql = Db.getSql("addMaiDian");
		logger.info("+++in UserService addMaiDian sql = " + sql);
		Db.save("xmxx_maidian", record);
	}
}
