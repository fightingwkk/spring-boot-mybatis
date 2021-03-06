package com.neo.mapper;

import com.neo.entity.*;
import org.apache.ibatis.annotations.*;

import com.neo.mapper.provider.PatientProvider;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface PatientMapper {
	// 插入一条患者信息
	@Insert("insert into patient_info (wechat_id,name,id_card,sex,age,phone,address,detailed_address,head_pic)values(#{wechat_id},#{name},#{id_card},#{sex},#{age},#{phone},#{address},#{detailed_address},#{head_pic})")
	void savePatient(PatientEntity patient);
	//查找所有患者
	@Select("select wechat_id from patient_info")
	List<String> selectAllPatients();
	//根据id返回患者信息
	@Select("select * from patient_info where wechat_id = #{wechat_id}")
	@Results({
		@Result(id=true,column="wechat_id",property="wechat_id"),
		@Result(column="name",property="name"),
		@Result(column="id_card",property="id_card"),
		@Result(column="sex",property="sex"),
		@Result(column="age",property="age"),
		@Result(column="phone",property="phone"),
		@Result(column="address",property="address"),
		@Result(column="detailed_address",property="detailed_address"),
		@Result(column="head_pic",property="head_pic")
	})
	PatientEntity selectById(@Param("wechat_id")String wechat_id);
	
	//修改患者信息
	@SelectProvider(type=PatientProvider.class, method="update")
	void updatePatientInfo(PatientEntity patient);



	//患者关注医生
	@Insert("insert into attention (wechat_id,phone) values (#{wechat_id},#{phone})")
	void updateAttention(@Param("wechat_id")String wechat_id,@Param("phone") String phone);

	//查找患者关注的医生
	@Select("select * from attention where wechat_id=#{wechat_id} ")
	AttentionEntity selectMyDoctor(String wechat_id);
	// 根据电话返回所有医生数据
	@Select("select * from doctor_info where phone = #{phone} ")
	DoctorEntity selectDoctorByPhone(String phone);

	//查找患者关注的医生
	@Select("select phone from attention where wechat_id = #{wechat_id}")
	List<String> selectDoctorsByPatient(String wechat_id);

	//根据患者ID返回他关注的医生的phone
	@Select("select phone from attention where wechat_id = #{wechat_id}")
	@Results({
		@Result(column="phone", property="phone")
	})
	String selectDoctorByPatient(String wechat_id);
	//根据微信id号查找已购买的服务包列表
	@Select("select * from purchased_service where wechat_id =#{wechat_id}")
	List<PurchasedServiceEntity> selectPurchasedServiceByWechatId(String  wechat_id);
	//根据id号查找服务包
	@Select("select * from service where id =#{id}")
	ServiceEntity selectServiceById(Integer  id);
	//通过患者微信号查找购买的服务id列表
	@Select("select service_id from purchased_service where wechat_id = #{wechat_id}")
	@Results({
			@Result(column="service_id",property="service_id")
	})
	List<Integer> selectPurchasedServiceId(String wechat_id);
	//通过患者微信号和服务id查找购买的实体
	@Select("select * from purchased_service where wechat_id=#{wechat_id} and service_id=3{service_id}")
	PurchasedServiceEntity selectPurchasedService(@Param("wechat_id")String wechat_id,@Param("service_id") int service_id);

	//购买服务包
	@SelectProvider(type = PatientProvider.class,method = "insertPurchasedService")
	void insertPurchasedService(PurchasedServiceEntity purchasedServiceEntity);

	//查找已支付状态服务包订单信息
	@Select("select * from purchased_service where indent_status=1")
    List<PurchasedServiceEntity> selectAllPurchasedService();

	//设置服务包过期
	@Update("update purchased_service set indent_status=99 where id = #{id}")
	void updatePurchasedServiceExpired(@Param("id") int id);

	//患者评价医生
	@Insert("insert into evaluation (wechat_id, phone, datetime, content, profession, attitude, speed, isread, grade, anonymity) values(#{wechat_id},#{phone},#{datetime},#{content},#{profession},#{attitude},#{speed},#{isread},#{grade},#{anonymity})")
	void insertEvaluation(EvaluationEntity evaluationEntity);

	//查找评价
	@Select("select * from evaluation where wechat_id=#{wechat_id} and phone=#{phone}")
	EvaluationEntity selectEvaluation(@Param("wechat_id") String wechat_id, @Param("phone") String phone);

	//插入消息
	@Insert("insert into message_list (wechat_id,message_id) values(#{wechat_id},#{message_id})")
	void insertMessage(@Param("wechat_id") String wechat_id , @Param("message_id") int message_id);

	//查找所有消息
	@Select("select id from message_remind ")
	List<Integer> selectAllMessage();

	//增加患者建议
	@Insert("insert into suggestion (wechat_id,content,phone,name) values (#{wechat_id},#{content},#{phone},#{name})")
	void insertSuggestion(SuggestionEntity suggestionEntity);

	//查找患者最近三天的建议
	@Select("select * from suggestion where wechat_id = #{wechat_id} and datetime >= date_sub(curdate(),interval 1 day)")
	List<SuggestionEntity> selectSuggestionByTime(String wechat_id);



	//设置留言板信息
	@SelectProvider(type = PatientProvider.class,method = "insertMessageBoard")
	void insertMessageBoard(MessageBoardEntity messageBoardEntity);


	//获取患者的留言板列表
	@Select("select * from message_board where wechat_id=#{wechat_id} and reply_id=0 ")
	List<MessageBoardEntity> selectMessageBoardById(@Param("wechat_id") String wechat_id);

	//根据患者获取患者留言板的最新回复
	@Select("select * from ((select * from message_board where reply_id=0 and patient_delete=0 and wechat_id=#{wechat_id} order by datetime)  union (select b.* from (select max(datetime) datetime,reply_id from message_board where reply_id>0 and patient_delete=0 and wechat_id=#{wechat_id} group by reply_id ) a left join message_board b on a.reply_id=b.reply_id where a.datetime=b.datetime)) c order by c.datetime desc;")
	List<MessageBoardEntity> selectPatientNewestMessageBoard(@Param("wechat_id") String wechat_id);

	//根据医生获取患者留言板的最新回复
	@Select("select * from ((select * from message_board where reply_id=0 and doctor_delete=0 and phone=#{phone} order by datetime)  union (select b.* from (select max(datetime) datetime,reply_id from message_board where reply_id>0  and doctor_delete=0  and phone=#{phone} group by reply_id ) a left join message_board b on a.reply_id=b.reply_id where a.datetime=b.datetime)) c order by c.datetime desc;")
	List<MessageBoardEntity> selectDoctorNewestMessageBoard(@Param("phone") String phone);

	//获取患者发起的留言板
	@Select("select * from message_board where id = #{id}")
	MessageBoardEntity selectPatientMessageBoard(@Param("id")int id);

	//获取一个留言板的所有回复
	@Select("select * from message_board where reply_id=#{reply_id} order by datetime")
	List<MessageBoardEntity> selectMessageBoardReply(@Param("reply_id")int reply_id);

	//患者删除留言板
	@Update("update message_board set patient_delete=1 where id=#{id}")
	void deletePatientMessageBoard(@Param("id")int id);

	//医生删除留言板
	@Update("update message_board set doctor_delete=1 where id=#{id}")
	void deleteDoctorMessageBoard(@Param("id")int id);

	//设置回复为已读
	@Update("update message_board set isread=1 where id=#{id}")
	void updateMessageBoardReaded(@Param("id") int id);

	//获取患者未读医生群发消息的数量
	@Select("select count(*) from patient_group_receiving where wechat_id=#{wechat_id} and isread=0 and delete_status=0")
	int getGroupReceivingUnread(@Param("wechat_id")String wechat_id);

	//获取患者的医生群发消息
	@Select("select a.id,a.phone,a.wechat_id,a.content,a.datetime,a.isread,a.delete_status,b.head_pic,b.name from patient_group_receiving a left join doctor_info b  on a.phone=b.phone where a.wechat_id=#{wechat_id} and a.delete_status=0 order by a.datetime desc")
	List<PatientGroupReceivingEntity> selectPatientGroupReceiving(@Param("wechat_id")String wechat_id);

	//删除患者的医生群发信息
	@Update("update patient_group_receiving set delete_status=1 where id =#{id}")
	void deleteGroupReceiving(@Param("id")int id);

	//设置患者的医生群发消息为已读
	@Update("update patient_group_receiving set isread=1 where id=#{id}")
	 void setGroupReceivingRead(@Param("id") int id);

	//获取患者的一个医生群发详细信息
	@Select("select a.id,a.phone,a.wechat_id,a.content,a.datetime,a.isread,a.delete_status,b.head_pic,b.name from patient_group_receiving a left join doctor_info b  on a.phone=b.phone where a.id=#{id}")
	PatientGroupReceivingEntity getGroupReceiving(@Param("id") int id);
}