package com.neo.mapper.provider;

import com.neo.entity.MessageBoardEntity;
import com.neo.entity.PurchasedServiceEntity;
import org.apache.ibatis.jdbc.SQL;

import com.neo.entity.PatientEntity;

public class PatientProvider {
    public String update(PatientEntity patient) {
        return new SQL() {
            {
                UPDATE("patient_info");
                if (patient.getName() != null) {
                    SET("name = #{name}");
                }
                if (patient.getId_card() != null) {
                    SET("id_card = #{id_card}");
                }
                if (patient.getSex() != null) {
                    SET("sex = #{sex}");
                }
                if (patient.getAge() != 0) {
                    SET("age = #{age}");
                }
                if (patient.getPhone() != null) {
                    SET("phone = #{phone}");
                }
                if (patient.getAddress() != null) {
                    SET("address = #{address}");
                }
                if (patient.getDetailed_address() != null) {
                    SET("detailed_address = #{detailed_address}");
                }
                if (patient.getHead_pic() != null) {
                    SET("head_pic = #{head_pic}");
                }
                if (patient.getKind() != "") {
                    SET("kind = #{kind}");
                }
                if (patient.getProb() != 0) {
                    SET("prob = #{prob}");
                }
                WHERE("wechat_id = #{wechat_id}");
            }
        }.toString();
    }

    public String insertPurchasedService(PurchasedServiceEntity purchasedServiceEntity) {
        return new SQL() {
            {
                INSERT_INTO("purchased_service");
                if (purchasedServiceEntity.getPurchased_time() != null) {
                    VALUES("purchased_time", "#{purchased_time}");
                }
                if (purchasedServiceEntity.getIndent_number() != null) {
                    VALUES("indent_number", "#{indent_number}");
                }
                if (purchasedServiceEntity.getIndent_status() != 0) {
                    VALUES("indent_status", "#{indent_status}");
                }
                if (purchasedServiceEntity.getDoctor_phone() != null) {
                    VALUES("doctor_phone", "#{doctor_phone}");
                }
                if (purchasedServiceEntity.getDoctor_name() != null) {
                    VALUES("doctor_name", "#{doctor_name}");
                }
                if (purchasedServiceEntity.getWechat_id() != null) {
                    VALUES("wechat_id", "#{wechat_id}");
                }
                if (purchasedServiceEntity.getPatient_name() != null) {
                    VALUES("patient_name", "#{patient_name}");
                }
                if (purchasedServiceEntity.getPatient_phone() != null) {
                    VALUES("patient_phone", "#{patient_phone}");
                }
                if (purchasedServiceEntity.getService_id() != 0) {
                    VALUES("service_id", "#{service_id}");
                }
                if (purchasedServiceEntity.getName() != null) {
                    VALUES("name", "#{name}");
                }
                if (purchasedServiceEntity.getDuration() != null) {
                    VALUES("duration", "#{duration}");
                }
                if (purchasedServiceEntity.getSum_count() != 0) {
                    VALUES("sum_count", "#{sum_count}");
                }
                if (purchasedServiceEntity.getLeft_count() != 0) {
                    VALUES("left_count", "#{left_count}");
                }
                if (purchasedServiceEntity.getPrice() != null) {
                    VALUES("price", "#{price}");
                }

                if (purchasedServiceEntity.getKind() != null) {
                    VALUES("kind", "#{kind}");
                }
                if (purchasedServiceEntity.getRisk_level_id() != 0) {
                    VALUES("risk_level_id", "#{risk_level_id}");
                }
            }
        }.toString();
    }

    //插入留言板
    public String insertMessageBoard(MessageBoardEntity messageBoardEntity) {
        return new SQL() {
            {
                INSERT_INTO("message_board");
                if (messageBoardEntity.getPhone() != null && !messageBoardEntity.getPhone().equals("")) {
                    VALUES("phone", "#{phone}");
                }
                if (messageBoardEntity.getWechat_id() != null && !messageBoardEntity.getWechat_id().equals("")) {
                    VALUES("wechat_id", "#{wechat_id}");
                }

                if (messageBoardEntity.getSender() != -1) {
                    VALUES("sender", "#{sender}");
                }
                if (messageBoardEntity.getPicture() != null && !messageBoardEntity.getPicture().equals("")) {
                    VALUES("picture", "#{picture}");
                }
                if (messageBoardEntity.getContent() != null && !messageBoardEntity.getContent().equals("")) {
                    VALUES("content", "#{content}");
                }
                if (messageBoardEntity.getDatetime() != null && !messageBoardEntity.getDatetime().equals("")) {
                    VALUES("datetime", "#{datetime}");
                }
                if (messageBoardEntity.getReply_id() != -1) {
                    VALUES("reply_id", "#{reply_id}");
                }
                if (messageBoardEntity.getIsread() != -1) {
                    VALUES("isread", "#{isread}");
                }

            }
        }.toString();
    }
}
