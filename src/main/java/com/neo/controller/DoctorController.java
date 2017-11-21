package com.neo.controller;

import java.util.*;

import com.neo.entity.*;
import com.neo.util.RedisUtil;
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
    private RedisUtil redisUtil;

    // 插入手机和token
    @CachePut(value = "token", key = "#token")
    @RequestMapping(value = "/savephonetoken")
    public String savePhonetoken(@RequestParam("phone") String phone, @RequestParam("token") String token) {
        try {
            doctorMapper.savePhonetoken(phone, token);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return token;
    }

    // 验证token
    @Cacheable(value = "token", key = "#token")
    @RequestMapping(value = "/selecttoken")
    public String selectToken(@RequestParam String token) {
        return doctorMapper.selectToken(token);
    }

    // 根据手机查找token
    @RequestMapping(value = "/selectphonetoken")
    public String selectPhonetoken(@RequestParam("phone") String phone) {
        return doctorMapper.selectPhoneToken(phone);
    }

    // 删除token
    @CacheEvict(value = "token", key = "#token")
    @RequestMapping(value = "/deletetoken")
    public void deleteToken(@RequestParam String token) {
        doctorMapper.deleteToken(token);
    }

    //根据电话更新token
    @RequestMapping(value = "/updatetoken")
    public String updateToken(@RequestParam String newToken, @RequestParam String token) {
        try{
            doctorMapper.updateToken(newToken, token);
            redisUtil.remove(token);
            redisUtil.set(newToken, newToken);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }




    // 根据电话和密码查询账号是否存在
    @RequestMapping("/loginverify")
    public Boolean loginVerify(@RequestParam String phone, @RequestParam String password) {
        DoctorEntity doctor = doctorMapper.selectByPhoneAndPassword(phone, password);
        if (doctor != null) {
            return true;
        } else {
            return false;
        }
    }

    // 根据电话返回医生的信息
    // @Cacheable(key="#phone")
    @RequestMapping("/findbyphone")
    public DoctorEntity selectByPhone(@RequestParam String phone) {
        return doctorMapper.selectByPhone(phone);
    }

    // 插入医生信息
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam String phone, @RequestParam String password) {
        try {
            doctorMapper.insertDoctor(phone, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    //根据电话更新密码
    @RequestMapping(value = "/updatepwd", method = RequestMethod.POST)
    public String updatePassword(@RequestParam String phone, @RequestParam String password) {
        try {
            doctorMapper.updatePassword(password, phone);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }


    //更新医生信息
    @RequestMapping(value = "/updatedoctorinfodetails", method = RequestMethod.POST)
    public String updateDoctorInfoDetails(@RequestBody DoctorEntity doctorEntity) {
        try {
            doctorMapper.updateDoctor(doctorEntity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }


    // 返回所有医生信息
    @RequestMapping("/findall")
    public List<DoctorEntity> findAllDoctor() {
        return doctorMapper.findAllDoctor();
    }

    // 返回所有服务包
    @RequestMapping("/service")
    public List<ServiceEntity> findAllService() {
        return doctorMapper.findAllService();
    }

    //存头像
    @RequestMapping(value = "/updatehead", method = RequestMethod.POST)
    public void updateHead(@RequestParam String head_pic, @RequestParam String phone) {
        doctorMapper.updateHead(head_pic, phone);
    }

    //更新擅长和经验
    @RequestMapping(value = "/updateintroduction", method = RequestMethod.POST)
    public String updateIntroduction(@RequestParam("phone") String phone, @RequestParam("adept") String adept, @RequestParam("experience") String experience) {
        try{
        doctorMapper.updateAdeptExperience(phone, adept, experience);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    //根据医生和类型返回患者的详细数据
    @RequestMapping(value = "/getpatientsbykind", method = RequestMethod.POST)
    public List<PatientEntity> getPatientsByKind(@RequestParam("phone") String phone, @RequestParam("kind") String kind) {
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
        return list;
    }

    //根据医生和标签返回患者的详细数据
    @RequestMapping(value = "/getpatientbylabel", method = RequestMethod.POST)
    public List<PatientEntity> getPatientsByLabel(@RequestParam String phone, @RequestParam String label) {
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

        return list;
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    //获取医生自定义标签
    @RequestMapping(value = "/getlabel", method = RequestMethod.POST)
    public String getLabel(@RequestParam String phone) {
        return doctorMapper.getDoctorLabel(phone);
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    //删除标签下的一个用户
    @RequestMapping(value = "/deletepatientlabel", method = RequestMethod.POST)
    public String deleteLabelPatient(@RequestParam("phone") String phone, @RequestParam("wechat_id") String wechat_id, @RequestParam("label") String label) {
        try {
            doctorMapper.deleteLabelPatient(phone, label, wechat_id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    //获取患者信息
    @RequestMapping(value = "/getpatient", method = RequestMethod.GET)
    public PatientEntity getPatient(@RequestParam("wechat_id") String wechat_id) {
        return doctorMapper.selectPatient(wechat_id);
    }

    //获取医生未读评价的个数
    @RequestMapping(value = "/getevaluationunread")
    public int getEvaluationUnread(@RequestParam("phone") String phone){
        return doctorMapper.getEvaluationUnread(phone);
    }

    //获取医生的评价
    @RequestMapping(value = "/getevaluation", method = RequestMethod.GET)
    public List<EvaluationEntity> getEvaluation(@RequestParam("phone") String phone) {
        return doctorMapper.selectEvaluationByPhone(phone);
    }

    //获取评价的详细信息
    @Transactional
    @RequestMapping(value = "/getevaluationdetail", method = RequestMethod.POST)
    public EvaluationEntity getEvaluationDetail(@RequestParam("id")int id ) {
        EvaluationEntity evaluationEntity = doctorMapper.selectEvaluationById(id);
        if (evaluationEntity!=null && evaluationEntity.getIsread() == 0) {
            doctorMapper.updateEvaluationById(id);
        }
        return evaluationEntity;
    }

    //删除评价
    @RequestMapping(value = "/deleteevaluation", method = RequestMethod.POST)
    public String deleteEvaluation(@RequestParam("id") int id) {
        try {
            doctorMapper.deleteEvaluationById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        return doctorMapper.selectDoctorGroupSending(phone);
    }
    //删除医生群发历史消息
    @RequestMapping(value = "/groupsendingdelete")
    public String groupSendingDelete(@RequestParam("id")int id){
        try{
            doctorMapper.deleteDoctorGroupSending(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    //医生群发消息,患者获得群发消息
    @Transactional
    @RequestMapping(value = "/groupsending",method = RequestMethod.POST)
    public String groupSending(@RequestBody DoctorGroupSendingEntity doctorGroupSendingEntity){
        try{
            doctorMapper.insertDoctorGroupSending(doctorGroupSendingEntity);
            if(doctorGroupSendingEntity.getType()==0){
            List<AttentionEntity> attentionEntityList = doctorMapper.findMyPatients(doctorGroupSendingEntity.getPhone());
                for (Iterator<AttentionEntity> it=attentionEntityList.iterator(); it.hasNext() ; ) {
                    PatientEntity patientEntity = doctorMapper.selectPatient(it.next().getWechat_id());
                    if(patientEntity.getKind().equals(doctorGroupSendingEntity.getGroup_name())){
                        doctorMapper.insertPatientGroupReceiving(doctorGroupSendingEntity.getPhone(),patientEntity.getWechat_id(),doctorGroupSendingEntity.getContent());
                    }
                }
            }else if(doctorGroupSendingEntity.getType()==1){
                List<String> wechat_idList = doctorMapper.selectPatientByPhoneAndLabel(doctorGroupSendingEntity.getPhone(),doctorGroupSendingEntity.getGroup_name());
                for (Iterator<String> it=wechat_idList.iterator();it.hasNext() ; ) {
                    doctorMapper.insertPatientGroupReceiving(doctorGroupSendingEntity.getPhone(),it.next(),doctorGroupSendingEntity.getContent());
                }
                }
            }catch (Exception e){
            System.out.println(e.getMessage());
            return "error";

        }
        return "success";
    }


    //查找医生的事项提醒
    @RequestMapping(value = "/geteventremind")
    public List<RemindersEntity> getEventRemind(@RequestParam("phone")String phone){
        return doctorMapper.selectRemindersByPhone(phone);
    }


    //获取医生事项提醒的详细信息
    @RequestMapping(value = "/geteventreminddetail")
    public RemindersEntity getEventRemindDetail(@RequestParam("id")int id){
        RemindersEntity remindersEntity = doctorMapper.selectRemindersById(id);
        if(remindersEntity!=null && remindersEntity.getIsread()==0){
            doctorMapper.updateRemindersById(id);
        }
        return remindersEntity;
    }

    //删除医生事项提醒
    @RequestMapping(value = "/eventreminddelete",method = RequestMethod.POST)
    public String eventRemindDelete(@RequestParam("id")int id){

        try{
            doctorMapper.deleteRemindersById(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }
    //获取未读事项提醒的个数
    @RequestMapping(value = "/geteventremindunread")
    public int getEvenRemindUnread(@RequestParam("phone") String phone){
        return doctorMapper.getRemindersUnreadByPhone(phone);
    }













}
