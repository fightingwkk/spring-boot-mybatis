package com.neo.controller;

import java.util.*;

import com.neo.entity.AttentionEntity;
import com.neo.entity.PatientEntity;
import com.neo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.neo.entity.DoctorEntity;
import com.neo.entity.ServiceEntity;
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
		try{
			doctorMapper.insertDoctor(phone, password);
		}catch(Exception e) {
			System.out.println(e.getMessage());
			return "error";
		}
		return "success";
	}
	//根据电话更新密码
	@RequestMapping(value = "/updatepwd", method = RequestMethod.POST)
	public String updatePassword(@RequestParam String phone,@RequestParam String password){
		try{
			doctorMapper.updatePassword(password,phone);
		}catch(Exception e){
			System.out.println(e.getMessage());
			return "error";
		}
		return "success";
	}


	//更新医生信息
	@RequestMapping(value = "/updatedoctorinfodetails",method = RequestMethod.POST)
	public String updateDoctorInfoDetails(@RequestBody DoctorEntity doctorEntity){
		try{
			doctorMapper.updateDoctor(doctorEntity);
		}catch(Exception e){
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
	@RequestMapping(value="/updatehead",method=RequestMethod.POST)
	public void updateHead(@RequestParam String head_pic,@RequestParam String phone){
		doctorMapper.updateHead(head_pic, phone);
	}

	//更新擅长和经验
	@RequestMapping(value = "/updateintroduction",method = RequestMethod.POST)
	public void updateIntroduction(@RequestParam("phone")String phone,@RequestParam("adept")String adept,@RequestParam("experience")String experience){
		doctorMapper.updateAdeptExperience(phone,adept,experience);
	}

	//返回患者的详细数据
	@RequestMapping(value = "/getpatient",method = RequestMethod.POST)
	public List<Map> getPatients(@RequestParam String phone){
		List<AttentionEntity> patientAttention = doctorMapper.findMyPatients(phone);

		List<Map> list = new LinkedList<>();
		for(AttentionEntity ae: patientAttention){
			Map<String,Object> patientMap = new HashMap<>();
			patientMap.put("name",doctorMapper.selectPatient(ae.getWechat_id()).getName());
			patientMap.put("age",doctorMapper.selectPatient(ae.getWechat_id()).getAge());
			patientMap.put("phone",doctorMapper.selectPatient(ae.getWechat_id()).getPhone());
			patientMap.put("kind",doctorMapper.selectPatient(ae.getWechat_id()).getKind());
			patientMap.put("prob",doctorMapper.selectPatient(ae.getWechat_id()).getProb());
			patientMap.put("kind",doctorMapper.selectPatient(ae.getWechat_id()).getKind());
			patientMap.put("lable",ae.getLabel());
			list.add(patientMap);
		}
			return list;
	}

	//获取医生自定义标签
	@RequestMapping(value = "/getlabel",method = RequestMethod.POST)
	public String getlabel(@RequestParam String phone){
		return doctorMapper.getDoctorLabel(phone);
	}








	// 插入手机和token
	@CachePut(value = "token", key = "#token")
	@RequestMapping(value = "/savephonetoken")
	public String savePhonetoken(@RequestParam("phone") String phone, @RequestParam("token") String token) {
		try{
				doctorMapper.savePhonetoken(phone, token);

		}catch (Exception e){
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
	public String updateToken(@RequestParam String newToken,@RequestParam String token){
		doctorMapper.updateToken(newToken, token);
		redisUtil.remove(token);
		redisUtil.set(newToken,newToken);
		return newToken;
	}




}
