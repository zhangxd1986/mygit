#sql("addUser")
insert into xmxx_user(
			deviceid
			,imsi
			,imei
			,hsman
			,hstype
			,ip
			,appversion
			,channel
			,create_time
			) 
		values (
			#deviceid#
			,#imsi#
			,#imei#
			,#hsman#
			,#hstype#
			,#ip#
			,#appversion#
			,#channel#
			,#create_time#
				) 
#end

#sql("initUserScore")
insert into xmxx_user_score(
			deviceid
			,score
			) 
		values (
			#deviceid#
			,0
				) 
#end

#sql("initUserGrade")
insert into xmxx_user_grade(
			deviceid
			,grade
			) 
		values (
			#deviceid#
			,1
				) 
#end


#sql("userExsit")
select count(1) from xmxx_user where deviceid = ?
#end

#sql("queryScoreID")
select id from xmxx_user_score where deviceid = ?
#end

#sql("queryGradeID")
select id from xmxx_user_grade where deviceid = ?
#end

#sql("queryScore")
select score from xmxx_user_score where deviceid = ?
#end

#sql("queryGrade")
select grade from xmxx_user_grade where deviceid = ?
#end

#sql("addBattle")
insert into xmxx_user_battle(
			deviceidA
			,deviceidB
			,create_time
			) 
		values (
			#deviceidA#
			,#deviceidB#
			,#create_time#
				) 
#end

#sql("queryBattleID")
select id from xmxx_user_battle 
where winner is null
and ((deviceidA = #para(deviceidA) and deviceidB = #para(deviceidB)) or (deviceidA = #para(deviceidB) and deviceidB = #para(deviceidA) ))
order by create_time desc
#end

#sql("updateBattle")
update xmxx_user_battle 
set winner = #winner#,edit_time = #edit_time#
where id = #id#
#end


#sql("updateUserScore")
update xmxx_user_score 
set score = #score#,edit_time = #edit_time#
where id = #id#
#end


#sql("updateUserGrade")
update xmxx_user_grade
set grade = #grade#,edit_time = #edit_time#
where id = #id#
#end

#sql("queryUserRanking")
select count(1) from xmxx_user_score where score >= ?
#end

#sql("queryRankingList")
select score from xmxx_user_score 
order by score desc
limit 10
#end

#sql("addMaiDian")
insert into xmxx_maidian(
			deviceid
			,mdtype
			,create_time
			) 
		values (
			#deviceid#
			,#mdtype#
			,#create_time#
				) 
#end
