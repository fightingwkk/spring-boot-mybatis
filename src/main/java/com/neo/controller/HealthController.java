package com.neo.controller;

import com.neo.entity.*;
import com.neo.mapper.HealthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/healthmanage")
public class HealthController {
    @Autowired
    HealthMapper healthMapper;

    /*
    * 保存健康信息表接口
    * */

    @RequestMapping(value = "/savehealthtable")
    public String saveHealthTable(@RequestBody HealthCheckEntity healCheckEntity) {


        try {
            if (healthMapper.selectHealthInfoById(healCheckEntity.getWechat_id()) != null) {
                healthMapper.updateHealthInfo(healCheckEntity);
            } else {
                healthMapper.saveHealthInfo(healCheckEntity);
            }

        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    /*
   * 查看健康信息表
   * */
    @RequestMapping(value = "/gethealthtable")
    public HealthCheckEntity getHealthTable(@RequestParam("wechat_id") String wechat_id) {
        return healthMapper.selectHealthInfoById(wechat_id);
    }

    /*
    * 保存和更新生化检查表
    * */
    @RequestMapping(value = "/savebiologytable")
    public String saveBiologyTable(@RequestBody BiologyCheckEntity biologyCheckEntity) {
        try {
            if (healthMapper.selectBiologyInfoById(biologyCheckEntity.getWechat_id()) != null) {
                healthMapper.updateBiologyInfo(biologyCheckEntity);
            } else {
                healthMapper.saveBiologyInfo(biologyCheckEntity);
            }
        } catch (Exception e) {
            return "error";
        }
        return "success";
    }

    /*
 * 保存血压心率表
 * */
    @Transactional
    @RequestMapping(value = "/savebloodpressuretable")
    public String saveBloodPressureTable(@RequestBody BloodPressureEntity bloodPressureEntity) {
        try {
            if(healthMapper.selectBloodPressure(bloodPressureEntity.getWechat_id(),bloodPressureEntity.getDate(),bloodPressureEntity.getTime())!=null){
                healthMapper.updateBloodPressure(bloodPressureEntity);
            }else {
                healthMapper.saveBloodPressure(bloodPressureEntity);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }


    /*
    * 查找血压心率记录
    * @Param  timearea:时间区间（一周，一个月）
    * @Param  time:具体时间（晨起，睡前）
    * */
    @RequestMapping(value = "/getbloodpressuretable")
    public List<BloodPressureEntity> getBloodPressureTable(@RequestParam String wechat_id, @RequestParam int timearea, @RequestParam String time){
        return healthMapper.bloodPressureList(wechat_id,timearea,time);
    }


    /*
* 查看患者是否有血压心率记录接口
* return 如果有返回evening 或者 morning 否则 返回nothing
* */
    @RequestMapping(value="/bloodpressuretable/find")
    public String Findbloodhistory(@RequestParam String wechat_id){
        int numMorning = healthMapper.getNumberOfBloodPressure(wechat_id,"morning");
        int numEvening = healthMapper.getNumberOfBloodPressure(wechat_id,"evening");
        int allNum = numEvening + numMorning;
        if(allNum == 0){
            return "nothing";
        }else if(numMorning > 0){
            return "morning";
        }else if(numEvening > 0){
            return "evening";
        }
        return "nothing";
    }

    /*
    * 保存心电表
    * */
    @RequestMapping(value="/savecardiogramtable")
    public String saveCardiogramTable(@RequestBody CardiogramEntity cardiogramEntity){
        try{
            healthMapper.saveCardiogram(cardiogramEntity);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }
    /*
    * 生成风险评估报告
    * */
    @RequestMapping(value="/savereport")
    public String saveReport(@RequestBody RiskReportEntity riskReportEntity){
    try{
        healthMapper.saveRiskReport(riskReportEntity);
    }catch(Exception e ){
        System.out.println(e.getMessage());
        return "error";
    }
    return "success";
    }

    /*
    * 查找所有风险评估报告
    * */
    @RequestMapping(value="/getallreport")
    public List<RiskReportEntity> getAllReport(@RequestParam  String wechat_id){
        return healthMapper.selectAllRiskReport(wechat_id);
    }
    /*
     * 查找最新的风险评估报告
     * */
    @RequestMapping(value="/getnewestreport")
    public RiskReportEntity getNewestReport(@RequestParam String wechat_id){
        return healthMapper.selectNewestReport(wechat_id);
    }
    /*
    * 查找风险评估人数
    * */
    @RequestMapping(value="/getreportnumber" )
    public Integer getReportNumber(){
        return healthMapper.getReportNumber();
    }




}
