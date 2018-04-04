package com.neo.controller;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.neo.entity.*;
import com.neo.mapper.HealthMapper;
import com.neo.mapper.ServiceMapper;
import com.neo.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.neo.mapper.DoctorMapper;

@CacheConfig(cacheNames = "doctorcache")
@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private RedisUtil redisUtil;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 插入手机和token
    @CachePut(value = "token", key = "#token")
    @RequestMapping(value = "/savephonetoken")
    public String savePhonetoken(@RequestParam("phone") String phone, @RequestParam("token") String token) {
        try {
            doctorMapper.savePhonetoken(phone, token);
            logger.info("成功插入手机号和token");
        } catch (Exception e) {
            logger.error("插入手机和token失败:",e.getMessage());
            return "error";
        }
        return token;
    }

    // 验证token
    @Cacheable(value = "token", key = "#token")
    @RequestMapping(value = "/selecttoken", method = RequestMethod.GET)
    public String selectToken(@RequestParam("token") String token) {
        try{
            String token1 = doctorMapper.selectToken(token);
            logger.info("成功验证token");
            return token1;
        }catch (Exception e){
            logger.error("验证token失败：", e.getMessage());
            return null;
        }
    }

    // 根据手机查找token
    @RequestMapping(value = "/selectphonetoken")
    public String selectPhonetoken(@RequestParam("phone") String phone) {
        try{

            String token = doctorMapper.selectPhoneToken(phone);
            logger.info("成功根据手机查找到token");
            return token;
        }catch (Exception e){
            logger.error("根据手机查找token失败：",e.getMessage());
            return null;
        }
    }

    // 删除token
    @CacheEvict(value = "token", key = "#token")
    @RequestMapping(value = "/deletetoken")
    public void deleteToken(@RequestParam String token) {
        try{
        doctorMapper.deleteToken(token);
        logger.info("成功删除token");
        }catch (Exception e){
            logger.error("删除token失败：", e.getMessage());
        }
    }

    //根据电话更新token
    @RequestMapping(value = "/updatetoken")
    public String updateToken(@RequestParam String newToken, @RequestParam String token) {
        try {
            doctorMapper.updateToken(newToken, token);
            redisUtil.remove(token);
            redisUtil.set(newToken, newToken);
            logger.info("成功根据电话更新token");
        } catch (Exception e) {
            logger.error("根据电话更新token失败：", e.getMessage());
            return "error";
        }
        return "success";
    }


    // 根据电话和密码查询账号是否存在
    @RequestMapping("/loginverify")
    public Boolean loginVerify(@RequestParam String phone, @RequestParam String password) {
        try {
            DoctorEntity doctor = doctorMapper.selectByPhoneAndPassword(phone, password);
            logger.info("成功根据电话和密码查询账号是否存在");
            if (doctor != null) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            logger.error("根据电话和密码查询账号是否存在失败：", e.getMessage());
            return false;
        }
    }

    // 根据电话返回医生的信息
    // @Cacheable(key="#phone")
    @RequestMapping("/findbyphone")
    public DoctorEntity selectByPhone(@RequestParam String phone) {
        try{

        DoctorEntity doctorEntity = doctorMapper.selectByPhone(phone);
        logger.info("成功根据电话返回医生的信息");
        return doctorEntity;
        }catch (Exception e){
            logger.error("根据电话返回医生的信息失败：",e.getMessage());
            return null;
        }
    }


    // 插入医生信息
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String phone, @RequestParam String password, @RequestParam String QRcode_pic) {
        try {
            doctorMapper.insertDoctor(phone, password, QRcode_pic);
            logger.info("成功插入一条医生信息");
            //给医生添加服务
            List<ServiceEntity> serviceEntityList = serviceMapper.findAllService();
            for (ServiceEntity service:serviceEntityList) {
                DoctorServiceEntity doctorServiceEntity = new DoctorServiceEntity();
                doctorServiceEntity.setDoctor_phone(phone);
                doctorServiceEntity.setService_id(service.getId());
                doctorServiceEntity.setName(service.getName());
                doctorServiceEntity.setPrice(service.getPrice());
                doctorServiceEntity.setCount(service.getCount());
                doctorServiceEntity.setDuration(service.getDuration());
                doctorServiceEntity.setAdded_status(1);
                doctorMapper.insertDoctorService(doctorServiceEntity);
            }
            logger.info("成功给医生添加服务");
        } catch (Exception e) {
            logger.error("插入医生信息失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    //根据电话更新密码
    @RequestMapping(value = "/updatepwd", method = RequestMethod.POST)
    public String updatePassword(@RequestParam String phone, @RequestParam String password) {
        try {
            doctorMapper.updatePassword(password, phone);
            logger.info("成功更新密码");
        } catch (Exception e) {
            logger.error("更新密码失败:",e.getMessage());
            return "error";
        }
        return "success";
    }


    //更新医生信息
    @RequestMapping(value = "/updatedoctorinfodetails", method = RequestMethod.POST)
    public String updateDoctorInfoDetails(@RequestBody DoctorEntity doctorEntity) {
        try {
            doctorMapper.updateDoctor(doctorEntity);
            logger.info("成功更新医生信息");
        } catch (Exception e) {
            logger.error("更新医生医生信息失败:",e.getMessage());
            return "error";
        }
        return "success";
    }


    // 返回所有医生信息
    @RequestMapping("/findall")
    public List<DoctorEntity> findAllDoctor() {
        try{
            List<DoctorEntity> list = doctorMapper.findAllDoctor();
            logger.info("成功查找所有医生");
            return list;
        }catch (Exception e){
            logger.error("查找所有医生失败:",e.getMessage());
            return null;
        }
    }

    // 返回医生所有服务包
    @RequestMapping("/service")
    public List<DoctorServiceEntity> findDoctorService(@RequestParam("phone") String phone) {
        try{
        List<DoctorServiceEntity> list = serviceMapper.findDoctorService(phone);
        logger.info("成功返回医生所有服务包");
        return list;
        }catch (Exception e){
            logger.error("返回医生所有服务包失败：",e.getMessage());
            return null;
        }
    }

    //存头像
    @RequestMapping(value = "/updatehead", method = RequestMethod.POST)
    public void updateHead(@RequestParam String head_pic, @RequestParam String phone) {
        try{
        doctorMapper.updateHead(head_pic, phone);
        logger.info("成功存储医生头像");
        }catch (Exception e){
            logger.error("存储医生头像失败：",e.getMessage());
        }
    }

    //更新擅长和经验
    @RequestMapping(value = "/updateintroduction", method = RequestMethod.POST)
    public String updateIntroduction(@RequestParam("phone") String phone, @RequestParam("adept") String adept, @RequestParam("experience") String experience) {
        try {
            doctorMapper.updateAdeptExperience(phone, adept, experience);
            logger.info("成功更新擅长和经验");
        } catch (Exception e) {
            logger.error("更新擅长和经验失败");
            return "error";
        }
        return "success";
    }

    //根据医生和类型返回患者的详细数据
    @RequestMapping(value = "/getpatientsbykind", method = RequestMethod.POST)
    public List<PatientEntity> getPatientsByKind(@RequestParam("phone") String phone, @RequestParam("kind") String kind) {
        try{
            List<AttentionEntity> patientAttention = doctorMapper.findMyPatients(phone);
            if (patientAttention == null) {
                return null;
            }
            List<PatientEntity> list = new LinkedList<>();
            for (AttentionEntity ae : patientAttention) {
                PatientEntity patientEntity = doctorMapper.selectPatient(ae.getWechat_id());
                if (patientEntity.getKind() != null && patientEntity.getKind().equals(kind)) {
                    list.add(patientEntity);
                }
            }
            logger.info("成功根据医生和类型返回患者的详细数据");
            return list;
        }catch (Exception e){
            logger.error("根据医生和类型返回患者的详细数据失败：",e.getMessage());
            return null;
        }
    }

    //根据医生和标签返回患者的详细数据
    @RequestMapping(value = "/getpatientbylabel", method = RequestMethod.POST)
    public List<PatientEntity> getPatientsByLabel(@RequestParam String phone, @RequestParam String label) {
        try{
            List<AttentionEntity> patientAttention = doctorMapper.findMyPatients(phone);
            if (patientAttention == null) {
                return null;
            }
            List<PatientEntity> list = new LinkedList<>();

            for (AttentionEntity ae : patientAttention) {
                PatientEntity patientEntity = doctorMapper.selectPatientsByLabel(phone, ae.getWechat_id(), label);
                if (patientEntity != null) {

                    list.add(patientEntity);
                }
            }
            logger.info("成功根据医生和标签返回患者的详细数据");
            return list;
        }catch (Exception e){
            logger.error("根据医生和标签返回患者的详细数据失败：", e.getMessage());
            return null;
        }
    }

    //添加患者进入医生分组
    @RequestMapping(value = "/addpatientlabel", method = RequestMethod.POST)
    public String addPatientLabel(@RequestParam("phone") String phone, @RequestParam("wechat_id") String wechat_id, @RequestParam("label") String label) {
        try {
            if (doctorMapper.selectPatientsByLabel(phone, wechat_id, label) == null) {
                doctorMapper.insertPatientLabel(phone, wechat_id, label);
            } else {
                return "success";
            }
            logger.info("成功添加患者进入医生分组");
        } catch (Exception e) {
            logger.info("根据医生和标签返回患者的详细数据失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    //获取医生自定义标签
    @RequestMapping(value = "/getlabel", method = RequestMethod.POST)
    public String getLabel(@RequestParam String phone) {
        try{

        String label = doctorMapper.getDoctorLabel(phone);
        logger.info("成功获取医生自定义标签");
        return label;
        }catch (Exception e){
            logger.error("获取医生自定义标签失败");
            return null;
        }
    }

    //删除医生自定义标签
    @Transactional
    @RequestMapping(value = "/deletelabel", method = RequestMethod.POST)
    public String deleteLabel(@RequestParam("phone") String phone, @RequestParam("labelStr") String labelStr, @RequestParam("label") String label) {
        try {
            doctorMapper.deleteLabelPatients(phone, label);
            DoctorEntity doctorEntity = new DoctorEntity();
            doctorEntity.setPhone(phone);
            doctorEntity.setLabel(labelStr);
            doctorMapper.updateDoctor(doctorEntity);
            logger.info("成功删除医生自定义标签");
        } catch (Exception e) {
            logger.error("删除医生自定义标签失败");
            return "error";
        }
        return "success";
    }

    //删除标签下的一个用户
    @RequestMapping(value = "/deletepatientlabel", method = RequestMethod.POST)
    public String deleteLabelPatient(@RequestParam("phone") String phone, @RequestParam("wechat_id") String wechat_id, @RequestParam("label") String label) {
        try {
            doctorMapper.deleteLabelPatient(phone, label, wechat_id);
            logger.info("成功删除标签下的一个用户");
        } catch (Exception e) {
            logger.error("删除标签下的一个用户失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    //获取患者信息
    @RequestMapping(value = "/getpatient", method = RequestMethod.GET)
    public PatientEntity getPatient(@RequestParam("wechat_id") String wechat_id) {
        try{

        PatientEntity patientEntity = doctorMapper.selectPatient(wechat_id);
        logger.info("成功获取患者信息");
        return patientEntity;
        }catch (Exception e){
            logger.error("获取患者信息失败：",e.getMessage());
            return null;
        }
    }

    //获取医生未读评价的个数
    @RequestMapping(value = "/getevaluationunread")
    public int getEvaluationUnread(@RequestParam("phone") String phone) {
        try{

        int num = doctorMapper.getEvaluationUnread(phone);
        logger.info("成功获取医生未读评价的个数");
        return num;
        }catch (Exception e){
            logger.error("获取医生未读评价的个数失败");
            return -1;
        }
    }

    //获取医生的评价
    @RequestMapping(value = "/getevaluation", method = RequestMethod.GET)
    public List<EvaluationEntity> getEvaluation(@RequestParam("phone") String phone) {
        try{

        List<EvaluationEntity> list = doctorMapper.selectEvaluationByPhone(phone);
        logger.info("成功获取医生的评价");
        return list;
        }catch (Exception e){
            logger.error("获取医生的评价失败");
            return null;
        }
    }

    //获取评价的详细信息
    @Transactional
    @RequestMapping(value = "/getevaluationdetail", method = RequestMethod.POST)
    public EvaluationEntity getEvaluationDetail(@RequestParam("id") int id) {
        try{

            EvaluationEntity evaluationEntity = doctorMapper.selectEvaluationById(id);
            if (evaluationEntity != null && evaluationEntity.getIsread() == 0) {
                doctorMapper.updateEvaluationById(id);
            }
            logger.info("成功获取评价的详细信息");
            return evaluationEntity;
        }catch (Exception e){
            logger.error("获取评价的详细信息失败:",e.getMessage());
            return null;
        }
    }

    //删除评价
    @RequestMapping(value = "/deleteevaluation", method = RequestMethod.POST)
    public String deleteEvaluation(@RequestParam("id") int id) {
        try {
            doctorMapper.deleteEvaluationById(id);
            logger.info("成功删除评价");
        } catch (Exception e) {
            logger.error("删除评价失败");
            return "error";
        }
        return "success";
    }

    //医生群发消息
//    @RequestMapping(value = "/groupsending", method = RequestMethod.POST)
//    public String groupSending(@RequestParam("message") DoctorGroupSendingEntity doctorGroupSendingEntity, @RequestParam("list") List<String> list) {
//        try {
//            doctorMapper.insertDoctorGroupSending(doctorGroupSendingEntity);
//            for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
//                String wechat_id = it.next();
//                doctorMapper.insertDoctorGroupReceiving(doctorGroupSendingEntity.getPhone(), wechat_id, doctorGroupSendingEntity.getContent());
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return "error";
//        }
//        return "success";
//    }

    //获取医生群发消息历史
    @RequestMapping(value = "/groupsendinghistory")
    public List<DoctorGroupSendingEntity> groupSendingHistory(@RequestParam("phone") String phone) {
        try{

        List<DoctorGroupSendingEntity> doctorGroupSendingEntityList = doctorMapper.selectDoctorGroupSending(phone);
        logger.info("成功获取医生群发消息历史");
        return doctorGroupSendingEntityList;
        }catch (Exception e){
            logger.error("获取医生群发消息历史失败");
            return null;
        }
    }

    //删除医生群发历史消息
    @RequestMapping(value = "/groupsendingdelete")
    public String groupSendingDelete(@RequestParam("id") int id) {
        try {
            doctorMapper.deleteDoctorGroupSending(id);
            logger.info("成功删除医生群发历史消息");
        } catch (Exception e) {
            logger.error("删除医生群发历史消息失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    //医生群发消息,患者获得群发消息
    @Transactional
    @RequestMapping(value = "/groupsending", method = RequestMethod.POST)
    public List<PatientGroupReceivingEntity> groupSending(@RequestBody DoctorGroupSendingEntity doctorGroupSendingEntity) {
        try {
            doctorMapper.insertDoctorGroupSending(doctorGroupSendingEntity);
            Set<String> send_patients = new HashSet<>();//用于去重
            List<PatientGroupReceivingEntity> patientGroupReceivingEntityList = new LinkedList<>();//医生群发的消息


            List<HashMap> groups = JSON.parseArray(doctorGroupSendingEntity.getGroup_names(), HashMap.class);
            List<HashMap> kinds = JSON.parseArray(doctorGroupSendingEntity.getKind_names(), HashMap.class);
            List<HashMap> patients = JSON.parseArray(doctorGroupSendingEntity.getPatient_names(), HashMap.class);

            //发送类型级别的患者
            if (doctorGroupSendingEntity.getKind_names() != null && !doctorGroupSendingEntity.getKind_names().equals("")) {
                List<AttentionEntity> attentionEntityList = doctorMapper.findMyPatients(doctorGroupSendingEntity.getPhone());
                for (Iterator<AttentionEntity> it = attentionEntityList.iterator(); it.hasNext(); ) {
                    PatientEntity patientEntity = doctorMapper.selectPatient(it.next().getWechat_id());
//                    String[] kinds = doctorGroupSendingEntity.getKind_names().split(",");
                    for (HashMap<String,String> kind : kinds) {
                        if (patientEntity.getKind().equals(kind.get("kind_name"))) {
                            send_patients.add(patientEntity.getWechat_id());
//                            doctorMapper.insertPatientGroupReceiving(doctorGroupSendingEntity.getPhone(), patientEntity.getWechat_id(), doctorGroupSendingEntity.getContent());
                            break;
                        }
                    }

                }
            }
            //发送自定义群组的患者
            if (doctorGroupSendingEntity.getGroup_names() != null && !doctorGroupSendingEntity.getGroup_names().equals("")) {
//                String[] groups = doctorGroupSendingEntity.getGroup_names().split(",");
                for (HashMap<String,String> group : groups) {
                    List<String> wechat_idList = doctorMapper.selectPatientByPhoneAndLabel(doctorGroupSendingEntity.getPhone(), group.get("group_name"));
                    for (String wechat_id : wechat_idList ) {
                        send_patients.add(wechat_id);
//                        doctorMapper.insertPatientGroupReceiving(doctorGroupSendingEntity.getPhone(), it.next(), doctorGroupSendingEntity.getContent());
                    }
                }
            }
            //发送单独的患者
            if (doctorGroupSendingEntity.getPatient_names() != null && !doctorGroupSendingEntity.getPatient_names().equals("")) {
//                String[] patients = doctorGroupSendingEntity.getPatient_names().split(",");
                for (HashMap<String,String> patient : patients) {
                    send_patients.add(patient.get("patient_name"));
//                    doctorMapper.insertPatientGroupReceiving(doctorGroupSendingEntity.getPhone(), patient, doctorGroupSendingEntity.getContent());
                }
            }
            //发送消息
            DoctorEntity doctorEntity = doctorMapper.selectByPhone(doctorGroupSendingEntity.getPhone());
            for (String patient: send_patients) {
                PatientEntity patientEntity = doctorMapper.selectPatient(patient);
                PatientGroupReceivingEntity patientGroupReceivingEntity = new PatientGroupReceivingEntity();
                patientGroupReceivingEntity.setPhone(doctorGroupSendingEntity.getPhone());
                patientGroupReceivingEntity.setDoctor_name(doctorEntity.getName());
                patientGroupReceivingEntity.setWechat_id(patient);
                patientGroupReceivingEntity.setPatient_name(patientEntity.getName());
                patientGroupReceivingEntity.setContent(doctorGroupSendingEntity.getContent());
                patientGroupReceivingEntityList.add(patientGroupReceivingEntity);
                doctorMapper.insertPatientGroupReceiving(doctorGroupSendingEntity.getPhone(),doctorEntity.getName(), patient, patientEntity.getName(),doctorGroupSendingEntity.getContent());
            }
            logger.info("成功发送医生的群发消息");
            return patientGroupReceivingEntityList;
        } catch (Exception e) {
            logger.error("发送医生群发消息失败：",e.getMessage());
            return null;
        }

    }


    //医生群发消息给所有患者
    @Transactional
    @RequestMapping(value = "/groupsendingall", method = RequestMethod.POST)
    public List<PatientGroupReceivingEntity> groupSendingAll(@RequestBody DoctorGroupSendingEntity doctorGroupSendingEntity) {
        try {
            doctorMapper.insertDoctorGroupSending(doctorGroupSendingEntity);
            List<AttentionEntity> attentionEntityList = doctorMapper.findMyPatients(doctorGroupSendingEntity.getPhone());
            List<PatientGroupReceivingEntity> patientGroupReceivingEntityList = new LinkedList<>();//医生群发的消息

            //发送消息
            DoctorEntity doctorEntity = doctorMapper.selectByPhone(doctorGroupSendingEntity.getPhone());
            for (AttentionEntity attentionEntity: attentionEntityList) {
                PatientEntity patientEntity = doctorMapper.selectPatient(attentionEntity.getWechat_id());
                PatientGroupReceivingEntity patientGroupReceivingEntity = new PatientGroupReceivingEntity();
                patientGroupReceivingEntity.setPhone(doctorGroupSendingEntity.getPhone());
                patientGroupReceivingEntity.setDoctor_name(doctorEntity.getName());
                patientGroupReceivingEntity.setWechat_id(attentionEntity.getWechat_id());
                patientGroupReceivingEntity.setPatient_name(patientEntity.getName());
                patientGroupReceivingEntity.setContent(doctorGroupSendingEntity.getContent());
                patientGroupReceivingEntityList.add(patientGroupReceivingEntity);
                doctorMapper.insertPatientGroupReceiving(doctorGroupSendingEntity.getPhone(),doctorEntity.getName(), attentionEntity.getWechat_id(), patientEntity.getName(),doctorGroupSendingEntity.getContent());
            }
            logger.info("成功发送医生的群发消息给所有患者");
            return patientGroupReceivingEntityList;
        } catch (Exception e) {
            logger.error("发送医生群发消息给所有患者失败：",e.getMessage());
            return null;
        }
    }


    //查找医生的事项提醒
    @RequestMapping(value = "/geteventremind")
    public List<RemindersEntity> getEventRemind(@RequestParam("phone") String phone) {
        try{

        List<RemindersEntity> remindersEntityList = doctorMapper.selectRemindersByPhone(phone);
        logger.info("成功查找医生的事项提醒");
        return remindersEntityList;
        }catch (Exception e){
            logger.error("查找医生的事项提醒失败：",e.getMessage());
            return null;
        }
    }


    //获取医生事项提醒的详细信息
    @RequestMapping(value = "/geteventreminddetail")
    public RemindersEntity getEventRemindDetail(@RequestParam("id") int id) {
        try {
            RemindersEntity remindersEntity = doctorMapper.selectRemindersById(id);
            if (remindersEntity != null && remindersEntity.getIsread() == 0) {
                doctorMapper.updateRemindersById(id);
            }
            logger.info("成功获取医生事项提醒的详细信息");
            return remindersEntity;
        }catch (Exception e){
            logger.error("获取医生事项提醒的详细信息失败：",e.getMessage());
            return null;
        }
    }
    //删除医生事项提醒
    @RequestMapping(value = "/eventreminddelete", method = RequestMethod.POST)
    public String eventRemindDelete(@RequestParam("id") int id) {

        try {
            doctorMapper.deleteRemindersById(id);
            logger.info("成功删除医生事项提醒");
        } catch (Exception e) {
            logger.error("删除医生事项提醒失败：",e.getMessage());
            return "error";
        }
        return "success";
    }

    //获取未读事项提醒的个数
    @RequestMapping(value = "/geteventremindunread")
    public int getEvenRemindUnread(@RequestParam("phone") String phone) {
        try{

        int num = doctorMapper.getRemindersUnreadByPhone(phone);
        logger.info("成功获取未读事项提醒的个数");
        return num;
        }catch (Exception e){
            logger.error("获取未读事项提醒的个数失败：",e.getMessage());
            return -1;
        }
    }

    /*
    * 医生建议接口
    * */
    @RequestMapping(value = "/suggestion/add", method = RequestMethod.POST)
    public String addSuggestion(@RequestBody SuggestionEntity suggestionEntity) {
        try {
            doctorMapper.insertSuggestion(suggestionEntity);
            logger.info("成功插入医生建议");
            return "success";
        } catch (Exception e) {
            logger.error("插入医生建议失败：",e.getMessage());
            return "error";
        }
    }

    /*
    *获取软件版本
     */
    @RequestMapping(value = "/software/get", method = RequestMethod.GET)
    public SoftwareEntity getSoftware() {
        try {

            SoftwareEntity softwareEntity = doctorMapper.getSoftware();
            logger.info("成功获取软件");
            return softwareEntity;
        } catch (Exception e) {
            logger.error("获取软件出错：",e.getMessage());
            return null;
        }
    }
}
