package com.neo.mapper;

import java.util.List;
import java.util.Map;

import com.neo.entity.AttentionEntity;
import com.neo.entity.PatientEntity;
import com.neo.mapper.provider.DoctorProvider;
import org.apache.ibatis.annotations.*;

import com.neo.entity.DoctorEntity;
import com.neo.entity.ServiceEntity;

public interface DoctorMapper {
	// 根据电话和密码查询账号是否存在
	@Select("select * from doctor_info where phone = #{0} and password = #{1}")
	DoctorEntity selectByPhoneAndPassword(String phone, String password);

	// 存入医生二维码
	@Update("update dactor_info set QRcode_pic = #{QRcode_pic}")
	void updateQRcode(String QRcode_pic);

	// 根据电话返回所有数据
	@Select("select * from doctor_info where phone = #{phone} ")
	DoctorEntity selectByPhone(String phone);

	////根据电话更新密码
	@Update("update doctor_info set password = #{password} where phone = #{phone}")
	void updatePassword(@Param("password") String password, @Param("phone") String phone);

	//新增一条医生数据
	@Insert("insert into doctor_info (phone,password) values (#{0},#{1})")
	void insertDoctor(String phone, String password);

	// 返回所有医生数据
	@Select("select * from doctor_info ")
	List<DoctorEntity> findAllDoctor();

	//更新医生数据
	@SelectProvider(type = DoctorProvider.class, method = "updateDoctor")
	void updateDoctor(DoctorEntity doctorEntity);

	// 返回所有的服务包
	@Select("select * from service order by time asc")
	List<ServiceEntity> findAllService();
	//存头像
	@Update("update doctor_info set head_pic = #{head_pic} where phone = #{phone}")
	void updateHead(@Param("head_pic")String head_pic, @Param("phone")String phone);
	//更新擅长和经验
	@Update("update doctor_info set adept = #{adept},experience = #{experience} where phone = #{phone}")
	void updateAdeptExperience(@Param("phone") String phone,@Param("adept") String adept, @Param("experience") String experience);
	//获取医生自定义标签
	@Select("select label from doctor_info where phone=#{phone}")
	@Results({
			@Result(column = "label", property = "label")
	})
	String getDoctorLabel(String phone);
	//查找关注医生的患者
	@Select("select * from attention where phone=#{phone}")
	List<AttentionEntity> findMyPatients(String phone);
	//查找患者
	@Select("select * from patient_info where wechat_id = #{wechat_id}")
	PatientEntity selectPatient(String wechat_id);













	
	// 查找token
	@Select("select token from token where token = #{token}")
	String selectToken(String token);

	// 删除token
	@Delete("delete from token where token = #{token}")
	void deleteToken(String token);

	// 插入phone和token
	@Select("insert into token (phone,token) values (#{phone},#{token})")
	String savePhonetoken(@Param("phone") String phone, @Param("token") String token);

	// 根据phone查找token
	@Select("select token from token where phone = #{phone}")
	String selectPhoneToken(String phone);
	//更新token
	@Select("update token set token = #{newToken} where token =#{token}")
	String updateToken(@Param("newToken")String newToken, @Param("token")String token);


}
