package com.neo.mapper.provider;


import com.neo.entity.DoctorEntity;
import org.apache.ibatis.jdbc.SQL;



public class DoctorProvider {
    public String updateDoctor(DoctorEntity doctorEntity){
        return new SQL(){
            {
                UPDATE("doctor_info");

                if (doctorEntity.getAdept() != null) {
                    SET("adept = #{adept}");
                }
                if (doctorEntity.getDepartment() != null) {
                    SET("department = #{department}");
                }
                if (doctorEntity.getExperience() != null) {
                    SET("experience = #{experience}");
                }
                if (doctorEntity.getHead_pic() != null) {
                    SET("head_pic = #{head_pic}");
                }
                if (doctorEntity.getHospital() != null) {
                    SET("hospital = #{hospital}");
                }
                if (doctorEntity.getLabel() != null) {
                    SET("label = #{label}");
                }
                if (doctorEntity.getName() != null) {
                    SET("name = #{name}");
                }
                if (doctorEntity.getPassword() != null) {
                    SET("password = #{password}");
                }
                if (doctorEntity.getPractice_code() != null) {
                    SET("practice_code = #{practice_code}");
                }
                if (doctorEntity.getPractice_pic() != null) {
                    SET("practice_pic = #{practice_pic}");
                }
                if (doctorEntity.getQRcode_pic() != null) {
                    SET("QRcode_pic = #{QRcode_pic}");
                }
                if (doctorEntity.getSex() != null) {
                    SET("sex = #{sex}");
                }
                if (doctorEntity.getTitle() != null) {
                    SET("title = #{title}");
                }
                if (doctorEntity.getVerify() != null) {
                    SET("verify = #{verify}");
                }
                WHERE("phone = #{phone}");
            }
        }.toString();
    }
}
