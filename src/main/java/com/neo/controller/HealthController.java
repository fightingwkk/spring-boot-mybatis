package com.neo.controller;

import com.neo.entity.*;
import com.neo.mapper.HealthMapper;
import com.neo.mapper.PatientMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/healthmanage")
public class HealthController {
    @Autowired
    HealthMapper healthMapper;
    @Autowired
    PatientMapper patientMapper;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
            logger.info("成功保存健康信息");
        } catch (Exception e) {
            logger.error("保存健康信息失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    /*
   * 查看健康信息表
   * */
    @RequestMapping(value = "/gethealthtable")
    public HealthCheckEntity getHealthTable(@RequestParam("wechat_id") String wechat_id) {
        try{
            HealthCheckEntity healthCheckEntity = healthMapper.selectHealthInfoById(wechat_id);
            logger.info("成功查找健康信息");
            return healthCheckEntity;
        }catch (Exception e){
            logger.error("查找健康信息失败：",e.getMessage());
            return null;
        }
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
            logger.info("成功保存和更新生化检查信息");
        } catch (Exception e) {
            logger.error("保存和更新生化检查信息失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    /*
    *获得生化检查表
     */
    @RequestMapping(value = "/getbiologyinfo")
    public BiologyCheckEntity getBiologyInfo(@RequestParam("wechat_id") String wechat_id) {
        try{

        BiologyCheckEntity biologyCheckEntity = healthMapper.selectBiologyInfoById(wechat_id);
        logger.info("成功获取生化检查信息");
        return biologyCheckEntity;
        }catch (Exception e){
            logger.error("获取生化检查信息失败：",e.getMessage());
            return null;
        }
    }

    /*
 * 保存血压心率表
 * */
    @Transactional
    @RequestMapping(value = "/savebloodpressuretable")
    public String saveBloodPressureTable(@RequestBody BloodPressureEntity bloodPressureEntity) {
        try {
            if (healthMapper.selectBloodPressure(bloodPressureEntity.getWechat_id(), bloodPressureEntity.getDate(), bloodPressureEntity.getTime()) != null) {
                healthMapper.updateBloodPressure(bloodPressureEntity);
            } else {
                healthMapper.saveBloodPressure(bloodPressureEntity);
            }
            logger.info("成功保存血压心率信息");
        } catch (Exception e) {
            logger.error("保存血压心率信息失败：",e.getMessage());
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
    public List<BloodPressureEntity> getBloodPressureTable(@RequestParam String wechat_id, @RequestParam int timearea, @RequestParam String time) {
        try{
            List<BloodPressureEntity> bloodPressureEntityList = healthMapper.bloodPressureList(wechat_id, timearea, time);
            logger.info("成功查找血压心率记录");
            return bloodPressureEntityList;
        }catch (Exception e){
            logger.error("查找血压心率失败：",e.getMessage());
            return null;
        }
    }


    /*
* 查看患者是否有血压心率记录接口
* return 如果有返回evening 或者 morning 否则 返回nothing
* */
    @RequestMapping(value = "/bloodpressuretable/find")
    public String Findbloodhistory(@RequestParam String wechat_id) {
        try{
            int numMorning = healthMapper.getNumberOfBloodPressure(wechat_id, "morning");
            int numEvening = healthMapper.getNumberOfBloodPressure(wechat_id, "evening");
            int allNum = numEvening + numMorning;
            logger.info("成功查看患者是否有血压心率记录");
            if (allNum == 0) {
                return "nothing";
            } else if (numMorning > 0) {
                return "morning";
            } else if (numEvening > 0) {
                return "evening";
            }
            return "nothing";
        }catch (Exception e){
            logger.error("查看患者是否有血压心率记录失败：",e.getMessage());
            return null;
        }

    }

    /*
    * 保存心电图
    * */
    @RequestMapping(value = "/savecardiogramtable")
    public String saveCardiogramTable(@RequestBody CardiogramEntity cardiogramEntity) {
        try {
            healthMapper.saveCardiogram(cardiogramEntity);
            logger.info("成功保存心电图");
        } catch (Exception e) {
            logger.error("查看患者是否有血压心率记录失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    /*
    * 根据患者查找心电图
     */
    @RequestMapping(value = "/findcardiogramtable")
    public List<CardiogramEntity> findCardiogramTable(@RequestParam("wechat_id") String wechat_id) {
        try {
            List<CardiogramEntity> list = healthMapper.findCardiogram(wechat_id);
            logger.info("成查找心电图");
            return list;
        } catch (Exception e) {
            logger.error("查找心电图失败：",e.getMessage());
            return null;
        }
    }
    /*
    *删除心电图
     */
    @RequestMapping(value = "/deletecardiogram", method = RequestMethod.POST)
    public String deleteCardiogramById(@RequestParam("id")int id){
        try{
            healthMapper.deleteCardiogramById(id);
            logger.info("成功删除心电图");
            return "success";
        }catch (Exception e){
            logger.error("删除心电图失败：",e.getMessage());
            return "error";
        }
    }
    /*
    * 生成风险评估报告
    * */
    @RequestMapping(value = "/report/generate", method = RequestMethod.POST)
    public String saveReport(@RequestBody String wechat_id) {

        RiskReportEntity riskReportEntity = healthMapper.selectNewestReportExist(wechat_id);
        double day = 0.0;
        if (riskReportEntity != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(riskReportEntity.getTime());
                Date today = new Date();
                day = (today.getTime() - date.getTime()) / (1000 * 60 * 60 * 24);
            } catch (Exception e) {
                logger.error("生成风险评估报告失败：",e.getMessage());
                return "error";
            }
        }

        if (riskReportEntity == null || (day >= 90)) {
            PatientEntity patientEntity = patientMapper.selectById(wechat_id);
            HealthCheckEntity healthCheckEntity = healthMapper.selectHealthInfoById(wechat_id);
            BiologyCheckEntity biologyCheckEntity = healthMapper.selectBiologyCheckById(wechat_id);
            List<BloodPressureEntity> bloodPressureEntityList = healthMapper.bloodPressureListLatest(wechat_id);//查找最近的血压心率记录

            if (bloodPressureEntityList.size() == 0) {
                return "blood";
            }
            if (healthCheckEntity == null) {
                return "health";
            }
            if (biologyCheckEntity == null) {
                return "biology";
            }
            if (patientEntity.getAge() < 35) {
                return "age";
            }
            //如果有冠心病或脑卒中直接判为10%患病
            if (healthCheckEntity.getChd().equals("有") || healthCheckEntity.getStroke().equals("有")) {
//                DateFormat format = DateFormat.getDateInstance();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String dateStr = format.format(date);
                int count = 0;
                if (riskReportEntity == null) {
                    count = 1;
                } else {
                    count = riskReportEntity.getCount() + 1;
                }
                PatientEntity patientEntity1 = new PatientEntity();
                patientEntity1.setWechat_id(wechat_id);
                patientEntity1.setKind(healthMapper.selectRiskLevelById(5).getRisk_level());
                patientEntity1.setProb(10);
                patientMapper.updatePatientInfo(patientEntity1);
                RiskReportEntity newRiskReportEntity = new RiskReportEntity(wechat_id, count, dateStr, (float) 10, 5);
                healthMapper.saveRiskReport(newRiskReportEntity);
                logger.info("成功生成风险评估报告");
                return "success";
            }
            double systolic_pressure = 0;
            int num = 0;
            for (BloodPressureEntity bloodPressureEntity : bloodPressureEntityList) {
                systolic_pressure += Double.parseDouble(bloodPressureEntity.getSystolic_pressure());
                num++;
            }
            systolic_pressure = systolic_pressure / num;

            float height = healthCheckEntity.getHeight() / 100;
            float weight = healthCheckEntity.getWeight();
            float bodyMassIndex = weight / (height * height);//体质指数
            String sex = patientEntity.getSex();
            int age = patientEntity.getAge();
            int score = 0;//得分
            double prob = 0.0;//患病概率

            //年龄加分
            if (age >= 35) {
                score += (age - 35) / 5;
            }

            //体质指数加分
            if (bodyMassIndex >= 24 && bodyMassIndex < 27.95) {
                score++;
            } else if (bodyMassIndex >= 27.95) {
                score += 2;
            }
            //总胆固醇加分
            float tch = Float.parseFloat(biologyCheckEntity.getTch());
            if (tch * 38.67 >= 200) { // 胆固醇换算 mmol/L×38.67→mg/dL
                score++;
            }

            if (sex.equals("男")) {
                //收缩压加分
                if (systolic_pressure < 120) {
                    score -= 2;
                } else if (systolic_pressure >= 130 && systolic_pressure <= 139) {
                    score++;
                } else if (systolic_pressure >= 140 && systolic_pressure <= 159) {
                    score += 2;
                } else if (systolic_pressure >= 160 && systolic_pressure <= 179) {
                    score += 5;
                } else if (systolic_pressure >= 180) {
                    score += 8;
                }
                //吸烟加分
                if (healthCheckEntity.getSmoke().equals("是")) {
                    score += 2;
                }
                //糖尿病加分
                if (healthCheckEntity.getDiabetes().equals("有")) {
                    score++;
                }

                //判定风险率
                if (score <= -1) {
                    prob = 0.3;
                }
                if (score >= 17) {
                    prob = 52.6;
                }
                switch (score) {
                    case 0:
                        prob = 0.5;
                        break;
                    case 1:
                        prob = 0.6;
                        break;
                    case 2:
                        prob = 0.8;
                        break;
                    case 3:
                        prob = 1.1;
                        break;
                    case 4:
                        prob = 1.5;
                        break;
                    case 5:
                        prob = 2.1;
                        break;
                    case 6:
                        prob = 2.9;
                        break;
                    case 7:
                        prob = 3.9;
                        break;
                    case 8:
                        prob = 5.4;
                        break;
                    case 9:
                        prob = 7.3;
                        break;
                    case 10:
                        prob = 9.7;
                        break;
                    case 11:
                        prob = 12.8;
                        break;
                    case 12:
                        prob = 16.8;
                        break;
                    case 13:
                        prob = 21.7;
                        break;
                    case 14:
                        prob = 27.7;
                        break;
                    case 15:
                        prob = 35.3;
                        break;
                    case 16:
                        prob = 44.3;
                        break;
                }
            } else if (sex.equals("女")) {
                //收缩压加分
                if (systolic_pressure < 120) {
                    score -= 2;
                } else if (systolic_pressure >= 130 && systolic_pressure <= 139) {
                    score++;
                } else if (systolic_pressure >= 140 && systolic_pressure <= 159) {
                    score += 2;
                } else if (systolic_pressure >= 160 && systolic_pressure <= 179) {
                    score += 3;
                } else if (systolic_pressure >= 180) {
                    score += 4;
                }
                //吸烟加分
                if (healthCheckEntity.getSmoke().equals("是")) {
                    score++;
                }
                //糖尿病加分
                if (healthCheckEntity.getDiabetes().equals("有")) {
                    score += 2;
                }

                //风险率判定
                if (score >= 13) {
                    prob = 21.7;
                }
                switch (score) {
                    case -2:
                        prob = 0.1;
                        break;
                    case -1:
                        prob = 0.2;
                        break;
                    case 0:
                        prob = 0.2;
                        break;
                    case 1:
                        prob = 0.2;
                        break;
                    case 2:
                        prob = 0.3;
                        break;
                    case 3:
                        prob = 0.5;
                        break;
                    case 4:
                        prob = 1.5;
                        break;
                    case 5:
                        prob = 2.1;
                        break;
                    case 6:
                        prob = 2.9;
                        break;
                    case 7:
                        prob = 3.9;
                        break;
                    case 8:
                        prob = 5.4;
                        break;
                    case 9:
                        prob = 7.3;
                        break;
                    case 10:
                        prob = 9.7;
                        break;
                    case 11:
                        prob = 12.8;
                        break;
                    case 12:
                        prob = 16.8;
                        break;
                }
            }
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            DateFormat format = DateFormat.getDateInstance();
            Date date = new Date();
            String dateStr = format.format(date);
            int count = 0;
            if (riskReportEntity == null) {
                count = 1;
            } else {
                count = riskReportEntity.getCount() + 1;
            }
            int risk_level_id = 0;
            if (prob < 1) {
                risk_level_id = 2;
            } else if (prob >= 1 && prob < 5) {
                risk_level_id = 3;
            } else if (prob >= 5 && prob < 10) {
                risk_level_id = 4;
            } else if (prob >= 10) {
                risk_level_id = 5;
            }
            PatientEntity patientEntity1 = new PatientEntity();
            patientEntity1.setWechat_id(wechat_id);
            patientEntity1.setKind(healthMapper.selectRiskLevelById(risk_level_id).getRisk_level());
            patientEntity1.setProb((float) prob);
            patientMapper.updatePatientInfo(patientEntity1);
            RiskReportEntity newRiskReportEntity = new RiskReportEntity(wechat_id, count, dateStr, (float) prob, risk_level_id);
            healthMapper.saveRiskReport(newRiskReportEntity);
            logger.info("成功生成风险评估报告");
            return "success";
        } else {
            return "time";
        }

    }


    /*
    * 查找所有风险评估报告
    * */
    @RequestMapping(value = "/report/getall")
    public List<RiskReportEntity> getAllReport(@RequestBody String wechat_id) {
        try{

        List<RiskReportEntity> riskReportEntityList = healthMapper.selectAllRiskReport(wechat_id);
        logger.info("成功查找所有风险评估报告");
        return riskReportEntityList;
        }catch (Exception e){
            logger.error("查找所有风险评估报告失败：",e.getMessage());
            return null;
        }
    }

    /*
     * 查找最新的风险评估报告
     * */
    @RequestMapping(value = "/getnewestreport")
    public RiskReportEntity getNewestReport(@RequestBody String wechat_id) {
        try{
            RiskReportEntity riskReportEntity = healthMapper.selectNewestReport(wechat_id);
            logger.info("成功查找最新的风险评估报告");
            return riskReportEntity;
        }catch (Exception e){
            logger.error("查找最新的风险评估报告失败：",e.getMessage());
            return null;
        }
    }

    /*
    * 根据服务次数查找风险评估报告接口
    * */
    @RequestMapping(value = "/report/getbycount")
    public RiskReportEntity getRiskReportByCount(@RequestParam("wechat_id") String wechat_id, @RequestParam("count") int count) {
        try{
        RiskReportEntity riskReportEntity = healthMapper.selectRiskReportByCount(wechat_id, count);
        logger.info("成功根据服务次数查找风险评估报告");
        return riskReportEntity;
        }catch (Exception e){
            logger.error("根据服务次数查找风险评估报告失败：",e.getMessage());
            return null;
        }
    }

    /*
    * 查找风险评估人数
    * */
    @RequestMapping(value = "/getreportnumber")
    public Integer getReportNumber() {
        try{
            Integer num = healthMapper.getReportNumber();
            logger.info("成功查找风险评估人数");
            return num;
        }catch (Exception e){
            logger.error("查找风险评估人数失败：",e.getMessage());
            return null;
        }
    }

    /*
    *医生发送模板消息
     */
    @RequestMapping(value = "/savemessageremind")
    public String saveMessageRemind(@RequestBody MessageRemindEntity messageRemindEntity){
        try{
            healthMapper.saveMessageRemind(messageRemindEntity);
            logger.info("成功发送医生模板消息");
        }catch (Exception e){
            logger.error("发送医生模板消息失败：",e.getMessage());
            return "error";
        }
        return "success";
    }
    /*
    * 查找具体患者未读模板消息接口
    * */
    @RequestMapping(value = "/message/getunread")
    public List<MessageListEntity> getMessageUnread(@RequestParam String wechat_id) {
        try{
            List<MessageListEntity> messageListEntityList = healthMapper.selectNoreadMessage(wechat_id);
            logger.info("成功查找具体患者未读模板消息");
            return messageListEntityList;
        }catch (Exception e){
            logger.error("查找具体患者未读模板消息失败：",e.getMessage());
            return null;
        }
    }

    /*
    * 将具体模板消息设置为已读接口
    * */
    @RequestMapping(value = "/message/setread")
    public String setMessageRead( @RequestParam("id") int id) {
        try {
            healthMapper.updateMessageReaded(id);
            logger.info("成功将具体模板消息设置为已读");
        } catch (Exception e) {
            logger.error("将具体模板消息设置为已读失败：",e.getMessage());
            return "error";
        }
        return "success";
    }
    /*
    * 将具体模板消息设置为已读接口
    * */
    @RequestMapping(value = "/definemessage/setread")
    public String setDefineMessageRead( @RequestParam("id") int id) {
        try {
            healthMapper.updateDefineMessageReaded(id);
            logger.info("成功将具体模板消息设置为已读");
        } catch (Exception e) {
            logger.error("将具体模板消息设置为已读失败：",e.getMessage());
            return "error";
        }
        return "success";
    }


    /*
    * 查找具体患者未读模板消息数量接口
    * */
    @RequestMapping(value = "/message/unread/getnumber")
    public int getMessageUnreadNumber(@RequestParam String wechat_id) {
        try{
            int num = healthMapper.selectUnreadMessageById(wechat_id);
            logger.info("成功查找具体患者未读模板消息数量");
            return num;
        }catch (Exception e){
            logger.error("查找具体患者未读模板消息数量失败：",e.getMessage());
            return -1;
        }
    }
    /*
* 查找具体患者未读自定义消息数量接口
* */
    @RequestMapping(value = "/definemessage/unread/getnumber")
    public int getDefineMsgUnreadNumber(@RequestParam String wechat_id) {
        try{
            int num = healthMapper.selectUnreadDefineMessageById(wechat_id);
            logger.info("成功查找具体患者未读自定义消息数量");
            return num;
        }catch (Exception e){
            logger.error("查找具体患者未读自定义消息数量失败：",e.getMessage());
            return -1;
        }
    }

    /*
    * 根据模板消息id获取消息内容
    * */
    @RequestMapping(value = "/message/getbyid")
    public MessageRemindEntity messageGetById(@RequestParam int id) {
        try{
            MessageRemindEntity messageRemindEntity = healthMapper.selectMessageRemindById(id);
            logger.info("成功根据模板消息id获取消息内容");
            return messageRemindEntity;
        }catch (Exception e){
            logger.error("根据模板消息id获取消息内容失败");
            return null;
        }
    }
    /*
    * 根据自定义消息id获取自定义消息内容
    * */
    @RequestMapping(value = "/definemessage/getbyid")
    public DefinitionMessageEntity getDefineMsgGetById(@RequestParam int id) {
        try{
            DefinitionMessageEntity definitionMessageEntity = healthMapper.selectDefineMsgById(id);
            logger.info("成功根据自定义消息id获取自定义消息内容");
            return definitionMessageEntity;
        }catch (Exception e){
            logger.error("根据自定义消息id获取自定义消息内容失败：",e.getMessage());
            return null;
        }
    }
    /*
* 根据自定义消息id删除消息内容
* */
    @RequestMapping(value = "/definemessage/delete")
    public String deleteDefineMsgById(@RequestParam int id) {
        try{
            healthMapper.deleteDefinitionMsgById(id);
            logger.info("成功根据自定义消息id删除消息内容");
        }catch (Exception e){
            logger.info("根据自定义消息id删除消息内容失败：",e.getMessage());
            return "error";
        }
        return "success";
    }
    //医生发送自定义消息
    @RequestMapping(value = "/definemessage", method = RequestMethod.POST)
    public String defineMessage(@RequestBody DefinitionMessageEntity definitionMessageEntity){
        try{
            healthMapper.insertDefinitionMessage(definitionMessageEntity);
            logger.info("成功发送医生自定义消息");
        }catch (Exception e){
            logger.error("发送医生自定义消息失败：",e.getMessage());
            return "error";
        }
        return "success";
    }
    /*
    * 查找具体患者自定义消息接口
    * */
    @RequestMapping(value = "/definemessage/get")
    public List<DefinitionMessageEntity> getDefineMsg(@RequestParam String wechat_id) {
        try{
            List<DefinitionMessageEntity> definitionMessageEntityList = healthMapper.selectDefineMsg(wechat_id);
            logger.info("成功查找具体患者自定义消息");
            return definitionMessageEntityList;
        }catch (Exception e){
            logger.error("查找具体患者自定义消息失败");
            return null;
        }
    }

}
