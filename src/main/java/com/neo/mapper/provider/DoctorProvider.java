package com.neo.mapper.provider;


import com.neo.entity.DoctorEntity;
import org.apache.ibatis.jdbc.SQL;



public class DoctorProvider {
    public String updateDoctor(DoctorEntity doctorEntity){
        return new SQL(){
            {
                UPDATE("doctor_info");

                if (doctorEntity.getAdept() != null && !doctorEntity.getAdept().equals("")) {
                    SET("adept = #{adept}");
                }
                if (doctorEntity.getDepartment() != null && !doctorEntity.getDepartment().equals("")) {
                    SET("department = #{department}");
                }
                if (doctorEntity.getExperience() != null && !doctorEntity.getExperience().equals("")) {
                    SET("experience = #{experience}");
                }
                if (doctorEntity.getHead_pic() != null && !doctorEntity.getHead_pic().equals("")) {
                    SET("head_pic = #{head_pic}");
                }
                if (doctorEntity.getHospital() != null && !doctorEntity.getHospital().equals("")) {
                    SET("hospital = #{hospital}");
                }
                if (doctorEntity.getLabel() != null && !doctorEntity.getLabel().equals("")) {
                    SET("label = #{label}");
                }
                if (doctorEntity.getName() != null && !doctorEntity.getName().equals("")) {
                    SET("name = #{name}");
                }
                if (doctorEntity.getPassword() != null && !doctorEntity.getPassword().equals("")) {
                    SET("password = #{password}");
                }
                if (doctorEntity.getPractice_code() != null && !doctorEntity.getPractice_code().equals("")) {
                    SET("practice_code = #{practice_code}");
                }
                if (doctorEntity.getPractice_pic() != null && !doctorEntity.getPractice_pic().equals("")) {
                    SET("practice_pic = #{practice_pic}");
                }
                if (doctorEntity.getQRcode_pic() != null && !doctorEntity.getQRcode_pic().equals("")) {
                    SET("QRcode_pic = #{QRcode_pic}");
                }
                if (doctorEntity.getSex() != null && doctorEntity.getSex().equals("")) {
                    SET("sex = #{sex}");
                }
                if (doctorEntity.getTitle() != null && !doctorEntity.getTitle().equals("")) {
                    SET("title = #{title}");
                }
                if (doctorEntity.getVerify() != null && doctorEntity.getVerify().equals("")) {
                    SET("verify = #{verify}");
                }
                WHERE("phone = #{phone}");
            }
        }.toString();
    }
}
