package com.neo.controller;

import com.neo.entity.PurchasedServiceEntity;
import com.neo.entity.ServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neo.entity.PatientEntity;
import com.neo.mapper.PatientMapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@CacheConfig(cacheNames = "patientcache")
@RestController
@RequestMapping("/patient")
public class PatientController {
	@Autowired
	private PatientMapper patientMapper;
	
	//存入患者信息
	@CachePut(key="#patient.getWechat_id()")
	@RequestMapping(value="/savedetail",method=RequestMethod.POST)
	public PatientEntity savePatient(@RequestBody PatientEntity patient ){
		patientMapper.savePatient(patient);
		return patient;
	}
	
	//根据id返回患者信息
    @Cacheable(key="#wechat_id")
	//@CacheEvict(allEntries=true, beforeInvocation=true)
	@RequestMapping("/findbywechatid")
	public PatientEntity selectById(@RequestBody String wechat_id ){
		return patientMapper.selectById(wechat_id);
	}
	
	//更改信息
	@CachePut(key="#patient.getWechat_id()")
	//@CacheEvict(allEntries=true, beforeInvocation=true)
	@RequestMapping(value="/updatepatient", method=RequestMethod.POST)
	public PatientEntity update(@RequestBody PatientEntity patient){
		//PatientEntity p=new PatientEntity("kkw","www","man",17,"18888888888","halg","c12","231","no");
			patientMapper.update(patient);
		return patient;
	}
	
	//患者关注医生
	@RequestMapping(value="/watchdoctor", method=RequestMethod.POST)
	public String watchDoctor(@RequestParam String wechat_id, @RequestParam String phone){
		patientMapper.updateAttention(wechat_id, phone);
		return "success";
	}
	
	//根据患者ID返回他关注的医生的ID
	@Cacheable(key="'findmydoctor'+#wechat_id")
	@RequestMapping("/findmydoctor")
	public String findmydoctor(@RequestBody String wechat_id){
		return patientMapper.selectDoctorByPatient(wechat_id);
	}

	////通过患者微信号查找购买的服务实体列表
	@RequestMapping(value = "/findmyservice")
	public List<PurchasedServiceEntity> findMyService(@RequestBody String wechat_id){
		return patientMapper.selectPurchasedServiceByWechatId(wechat_id);
	}
	//调用远程服务下的购买服务接口,传入患者的openid和值为service主键id的list
	@RequestMapping(value="/buyservice")
	String buyService(@RequestParam("wechat_id") String wechat_id, @RequestParam("servicelist") List<Integer> servicelist){
		try{
			for (Integer id:servicelist) {
				ServiceEntity se = patientMapper.selectServiceById(id);
				patientMapper.insertPurchasedService(wechat_id,id,se.getCount(),se.getCount(),se.getName(),se.getPrice(),se.getDuration(),se.getContent(),se.getKind());
			}
		}catch (Exception e){
			return "error";
		}
		return "success";

	}

}
