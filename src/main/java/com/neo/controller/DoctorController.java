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
    public void updateIntroduction(@RequestParam("phone") String phone, @RequestParam("adept") String adept, @RequestParam("experience") String experience) {
        doctorMapper.updateAdeptExperience(phone, adept, experience);
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
            if ( patientEntity.getKind()!=null && patientEntity.getKind().equals(kind)) {
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
            PatientEntity patientEntity = doctorMapper.selectPatientsByLabel(phone,ae.getWechat_id(), label);
            if(patientEntity!=null){

            list.add(patientEntity);
            }
        }

        return list;
    }

    //添加患者进入医生分组
    @RequestMapping(value = "/addpatientlabel",method = RequestMethod.POST)
   public String addPatientLabel(@RequestParam("phone")String phone,@RequestParam("wechat_id")String wechat_id,@RequestParam("label")String label){
        try{
            if(doctorMapper.selectPatientsByLabel(phone,wechat_id,label)==null){

            doctorMapper.insertPatientLabel(phone,wechat_id,label);
            }else{
                return "success";
            }
        }catch (Exception e){
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
    doctorMapper.deleteLabelPatient(phone,label,wechat_id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }
    //获取患者信息
    @RequestMapping(value = "/getpatient",method = RequestMethod.GET)
    public PatientEntity getPatient(@RequestParam("wechat_id")String wechat_id){
       return doctorMapper.selectPatient(wechat_id);
    }

    //获取医生的评价
    @RequestMapping(value = "/getevaluation",method = RequestMethod.GET)
    public List<EvaluationEntity> getEvaluation(@RequestParam("phone")String phone){
        return doctorMapper.selectEvaluationByPhone(phone);
    }

    //获取评价的详细信息
    @Transactional
    @RequestMapping(value = "/getevaluationdetail",method = RequestMethod.POST)
    public EvaluationEntity getEvaluationDetail(@RequestParam("phone")String phone,@RequestParam("wechat_id")String wechat_id,@RequestParam("datetime")String datetime){
        EvaluationEntity evaluationEntity = doctorMapper.selectEvaluationByTime(phone,wechat_id,datetime);
        if(evaluationEntity.getIsread()==0){

        doctorMapper.updateEvaluationByTime(phone,wechat_id,datetime);
        }
        return evaluationEntity;

    }
    //删除评价
    @RequestMapping(value = "/deleteevaluation",method = RequestMethod.POST)
    public String deleteEvaluation(@RequestParam("phone")String phone,@RequestParam("wechat_id")String wechat_id,@RequestParam("datetime")String datetime){
        try{
            doctorMapper.deleteEvaluationByTime(phone,wechat_id,datetime);
        }catch(Exception e){
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }








    // 插入手机和token
    @CachePut(value = "token", key = "#token")
    @RequestMapping(value = "/savephonetoken")
    public String savePhonetoken(@RequestParam("phone") String phone, @RequestParam("token") String token) {
        try {
            doctorMapper.savePhonetoken(phone, token);

        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        doctorMapper.updateToken(newToken, token);
        redisUtil.remove(token);
        redisUtil.set(newToken, newToken);
        return newToken;
    }


}
