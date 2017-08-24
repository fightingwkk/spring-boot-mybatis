package com.neo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neo.entity.DoctorEntity;
import com.neo.entity.ServiceEntity;
import com.neo.mapper.DoctorMapper;

@CacheConfig(cacheNames = "doctorcache")
@RestController
@RequestMapping("/doctor")
public class DoctorController {
	@Autowired
	private DoctorMapper doctorMapper;

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
		doctorMapper.insertDoctor(phone, password);
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



	// 插入手机和token
	@CachePut(value = "token", key = "#token")
	@RequestMapping(value = "/savephonetoken")
	public String savePhonetoken(@RequestParam("phone") String phone, @RequestParam("token") String token) {
		doctorMapper.savePhonetoken(phone, token);
		return token;
	}

	// 查找token
	@Cacheable(value = "token", key = "#token")
	@RequestMapping(value = "/selecttoken")
	public String selectToken(@RequestParam String token) {
		return doctorMapper.selectToken(token);
	}

	// 根据手机查找token
	@RequestMapping(value = "/selectphonetoken")
	public String selectPhonetoken(@RequestParam("phone") String phone) {
		return doctorMapper.selectPhonetoken(phone);
	}

	// 删除token
	@CacheEvict(value = "token", key = "#token")
	@RequestMapping(value = "/deletetoken")
	public void deleteToken(@RequestParam String token) {
		doctorMapper.deleteToken(token);
	}

	//根据电话更新token
	@CachePut(value = "token", key = "#token")
	@RequestMapping(value = "/updatetoken")
	public String updateToken(@RequestParam String token,@RequestParam String phone){
		doctorMapper.updateToken(token, phone);
		return token;
	}


}
