package com.neo.mapper;

import java.util.List;
import java.util.Map;

import com.neo.entity.*;
import com.neo.mapper.provider.DoctorProvider;
import org.apache.ibatis.annotations.*;

import org.springframework.stereotype.Component;

@Component
public interface DoctorMapper {
    // 根据电话和密码查询账号是否存在
    @Select("select * from doctor_info where phone = #{0} and password = #{1}")
    DoctorEntity selectByPhoneAndPassword(String phone, String password);

    // 存入医生二维码
    @Update("update dactor_info set QRcode_pic = #{QRcode_pic}")
    void updateQRcode(String QRcode_pic);

    // 根据电话返回所有医生数据
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
    void updateHead(@Param("head_pic") String head_pic, @Param("phone") String phone);

    //更新擅长和经验
    @Update("update doctor_info set adept = #{adept},experience = #{experience} where phone = #{phone}")
    void updateAdeptExperience(@Param("phone") String phone, @Param("adept") String adept, @Param("experience") String experience);

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

    //根据医生标签查找患者
    @Select("select label_relation.wechat_id,name,age,label_relation.phone,kind,prob,sex,head_pic,address from label_relation ,patient_info where label_relation.wechat_id=patient_info.wechat_id and label_relation.phone=#{phone} and label_relation.wechat_id=#{wechat_id} and label=#{label}")
    PatientEntity selectPatientsByLabel(@Param("phone") String phone, @Param("wechat_id") String wechat_id, @Param("label") String label);

    //根据医生和标签删除所有此标签下的患者
    @Delete("delete  from label_relation where phone=#{phone} and label=#{label}")
    void deleteLabelPatients(@Param("phone") String phone, @Param("label") String label);

    //删除一个标签下的用户
    @Delete("delete  from label_relation where phone =#{phone} and label=#{label} and wechat_id=#{wechat_id}")
    void deleteLabelPatient(@Param("phone") String phone, @Param("label") String label, @Param("wechat_id") String wechat_id);

    //删除一个用户的标签
    @Delete("delete from label_relation where phone=#{phone} and wechat_id={wechat_id} and label=#{label}")
    void deletePatientLabel(@Param("phone") String phone, @Param("label") String label, @Param("wechat_id") String wechat_id);

    //添加一个患者进组
    @Insert("insert into label_relation (phone,wechat_id,label) values(#{phone},#{wechat_id},#{label})")
    void insertPatientLabel(@Param("phone") String phone, @Param("wechat_id") String wechat_id, @Param("label") String label);

    //根据电话查找所有患者的评价
    @Select("select a.wechat_id,a.datetime,a.content,a.profession,a.attitude,a.speed,a.isread,a.anonymity,b.head_pic,b.name,b.sex,b.age from evaluation a left join patient_info b on a.wechat_id=b.wechat_id where  a.phone=#{phone}")
    List<EvaluationEntity> selectEvaluationByPhone(@Param("phone") String phone);

    //根据电话和患者微信号查找评价
    @Select("  select a.wechat_id,a.datetime,a.content,a.profession,a.attitude,a.speed,a.isread,a.anonymity,b.head_pic,b.name,b.sex,b.age from evaluation a left join patient_info b on a.wechat_id=b.wechat_id where   a.phone=#{phone} and  a.wechat_id=#{wechat_id} and a.datetime=#{datetime}")
    EvaluationEntity selectEvaluationByTime(@Param("phone") String phone,@Param("wechat_id")String wechat_id, @Param("datetime") String datetime);

    //根据电话和患者微信号更新评价
    @Update("update evaluation set isread=1 where phone=#{phone} and  wechat_id=#{wechat_id} and datetime=#{datetime}")
    void updateEvaluationByTime(@Param("phone") String phone,@Param("wechat_id")String wechat_id, @Param("datetime") String datetime);

    //根据电话和患者微信号删除评价
    @Update("delete from evaluation  where phone=#{phone} and  wechat_id=#{wechat_id} and datetime=#{datetime}")
    void deleteEvaluationByTime(@Param("phone") String phone,@Param("wechat_id")String wechat_id, @Param("datetime") String datetime);





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
    String updateToken(@Param("newToken") String newToken, @Param("token") String token);


}
