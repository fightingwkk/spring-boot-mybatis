package com.neo.mapper.provider;

import com.neo.entity.HealthCheckEntity;
import org.apache.ibatis.jdbc.SQL;

public class HealthProvider {
    public String updateHealthInfo(HealthCheckEntity healthCheckEntity) {
        return new SQL() {
            {
                UPDATE("health_info");

                if (healthCheckEntity.getChd() != null) {
                    SET("chd = #{chd}");
                }
                if (healthCheckEntity.getDiabetes() != null) {
                    SET("diabetes = #{diabetes}");
                }
                if (healthCheckEntity.getHeight() != 0) {
                    SET("height = #{height}");
                }
                if (healthCheckEntity.getWeight() != 0) {
                    SET("weight = #{weight}");
                }
                if (healthCheckEntity.getStroke() != null) {
                    SET("stroke = #{stroke}");
                }
                if (healthCheckEntity.getHypertension() != null) {
                    SET("hypertension = #{hypertension}");
                }
                if (healthCheckEntity.getOther_history() != null) {
                    SET("other_history = #{other_history}");
                }
                if (healthCheckEntity.getFamily_history() != null) {
                    SET("family_history = #{family_history}");
                }
                if (healthCheckEntity.getSmoke() != null) {
                    SET("smoke = #{smoke}");
                }
                if (healthCheckEntity.getSmoking() != null) {
                    SET("smoking = #{smoking}");
                }
                if (healthCheckEntity.getDrink() != null) {
                    SET("drink = #{drink}");
                }
                if (healthCheckEntity.getDrinking() != null) {
                    SET("drinking = #{drinking}");
                }

                WHERE("wechat_id = #{wechat_id}");
            }
        }.toString();
    }
}
