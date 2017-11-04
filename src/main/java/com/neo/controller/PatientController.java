package com.neo.controller;

import com.neo.entity.*;
import com.neo.mapper.DoctorMapper;
import com.neo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.neo.mapper.PatientMapper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CacheConfig(cacheNames = "patientcache")
@RestController
@RequestMapping("/patient")
public class PatientController {
	@Autowired
	private PatientMapper patientMapper;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private DoctorMapper doctorMapper;

	//存入患者信息
	@CachePut(key = "#patient.getWechat_id()")
	@RequestMapping(value = "/savedetail", method = RequestMethod.POST)
	public PatientEntity savePatient(@RequestBody PatientEntity patient) {
		patientMapper.savePatient(patient);
		return patient;
	}

	//根据id返回患者信息
	@Cacheable(key = "#wechat_id")
	//@CacheEvict(allEntries=true, beforeInvocation=true)
	@RequestMapping("/findbywechatid")
	public PatientEntity selectById(@RequestBody String wechat_id) {
		return patientMapper.selectById(wechat_id);
	}

	//更改信息
	@CachePut(key = "#patient.getWechat_id()")
	//@CacheEvict(allEntries=true, beforeInvocation=true)
	@RequestMapping(value = "/updatepatient", method = RequestMethod.POST)
	public PatientEntity update(@RequestBody PatientEntity patient) {
		//PatientEntity p=new PatientEntity("kkw","www","man",17,"18888888888","halg","c12","231","no");
		patientMapper.updatePatientInfo(patient);
		return patient;
	}


//	//存入患者信息
//	@RequestMapping(value="/savedetail",method=RequestMethod.POST)
//	public PatientEntity savePatient(@RequestBody PatientEntity patient ){
//		patientMapper.savePatient(patient);
//		redisUtil.set(patient.getWechat_id(),patient);
//		return patient;
//	}

//	//根据id返回患者信息
//	@RequestMapping("/findbywechatid")
//	public PatientEntity selectById(@RequestBody String wechat_id ){
//		if(redisUtil.get(wechat_id)==null){
//			PatientEntity patient = patientMapper.selectById(wechat_id);
//			redisUtil.set(wechat_id,patient);
//			return patient;
//		}else{
//			return (PatientEntity) redisUtil.get(wechat_id);
//		}
//	}
//
//	//更改患者信息
//	@RequestMapping(value="/updatepatient", method=RequestMethod.POST)
//	public PatientEntity update(@RequestBody PatientEntity patient){
//		patientMapper.update(patient);
//		redisUtil.set(patient.getWechat_id(),patient);
//		return patient;
//	}


	//患者关注医生
	@RequestMapping(value = "/watchdoctor", method = RequestMethod.POST)
	public String watchDoctor(@RequestParam String wechat_id, @RequestParam String phone) {

		List<String> doctorList = patientMapper.selectDoctorsByPatient(wechat_id);
		for (String doctorPhone : doctorList) {
			if (doctorPhone.equals(phone)) {
				return "error";
			}
		}
		patientMapper.updateAttention(wechat_id, phone);
//		redisUtil.set("findmydoctor"+wechat_id,patientMapper.selectDoctorByPhone(phone));
		return "success";
	}

	//根据患者ID返回他关注的医生的ID
	@Cacheable(key = "'findmydoctor'+#wechat_id")
	@RequestMapping("/findmydoctor")
	public String findMyDoctor(@RequestBody String wechat_id) {
		return patientMapper.selectDoctorByPatient(wechat_id);
	}

	//通过患者微信号查找购买的服务实体列表
	@RequestMapping(value = "/findmyservice")
	public List<PurchasedServiceEntity> findMyService(@RequestBody String wechat_id) {
		return patientMapper.selectPurchasedServiceByWechatId(wechat_id);
	}
	//购买服务接口,传入患者的wechat_id,医生的电话和值为service主键id
	@RequestMapping(value = "/buyservice")
	public String buyService(@RequestParam("wechat_id") String wechat_id, @RequestParam("phone") String phone, @RequestParam("servicelist") List<Integer> servicelist) {
		AttentionEntity attentionEntity = patientMapper.selectMyDoctor(wechat_id);
		if (attentionEntity != null) {
			if (attentionEntity.getPhone().equals(phone)) {
				return buyServices(wechat_id, phone, servicelist);

			} else {
				return "watch";
			}
		} else {
			patientMapper.updateAttention(wechat_id, phone);
			return buyServices(wechat_id, phone, servicelist);
		}
	}

	private String buyServices(String wechat_id, String phone, List<Integer> servicelist) {
		PatientEntity patientEntity = patientMapper.selectById(wechat_id);
		DoctorEntity doctorEntity = doctorMapper.selectByPhone(phone);
		//List<Integer> hasPurchasedServiceEntityList = patientMapper.selectPurchasedServiceId(wechat_id);
		try {
			for (Integer id : servicelist) {
				ServiceEntity serviceEntity = patientMapper.selectServiceById(id);
				PurchasedServiceEntity purchasedServiceEntity = new PurchasedServiceEntity();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
				Date date = new Date();
				StringBuilder dateStr = new StringBuilder(sdf.format(date));
				dateStr.append(patientEntity.getPhone().substring(8));
				DecimalFormat df=new DecimalFormat("0000");
				String str2=df.format(new Random().nextInt(10000));
				dateStr.append(str2);
				purchasedServiceEntity.setIndent_number(dateStr.toString());
				purchasedServiceEntity.setIndent_status(1);
				purchasedServiceEntity.setDoctor_phone(doctorEntity.getPhone());
				purchasedServiceEntity.setDoctor_name(doctorEntity.getName());
				purchasedServiceEntity.setWechat_id(patientEntity.getWechat_id());
				purchasedServiceEntity.setPatient_name(patientEntity.getName());
				purchasedServiceEntity.setPatient_phone(patientEntity.getPhone());
				purchasedServiceEntity.setService_id(id);
				purchasedServiceEntity.setName(serviceEntity.getName());
				purchasedServiceEntity.setDuration(serviceEntity.getDuration());
				purchasedServiceEntity.setSum_count(serviceEntity.getCount());
				purchasedServiceEntity.setLeft_count(serviceEntity.getCount());
				purchasedServiceEntity.setPrice(serviceEntity.getPrice());
				purchasedServiceEntity.setContent(serviceEntity.getContent());
				purchasedServiceEntity.setKind(serviceEntity.getKind());
				purchasedServiceEntity.setRisk_level_id(serviceEntity.getRisk_levelid());
				patientMapper.insertPurchasedService(purchasedServiceEntity);
			}
		} catch (Exception e) {
			return "error";
		}
		return "success";
}

   //患者评价医生
	@RequestMapping(value="/evaluate")
	public String evaluateDoctor(@RequestBody EvaluationEntity evaluationEntity){

			if(patientMapper.selectEvaluation(evaluationEntity.getWechat_id(),evaluationEntity.getPhone())==null){
				patientMapper.insertEvaluation(evaluationEntity);
				return "success";
			}else{
				return "error";
			}

		}
	/*
    * 患者建议接口
    * */
	@RequestMapping(value="/contact/suggest",method = RequestMethod.POST)
	public String suggest(@RequestBody SuggestionEntity suggestionEntity){
		List<SuggestionEntity> suggestionEntityList = patientMapper.selectSuggestionByTime(suggestionEntity.getWechat_id());
		if(suggestionEntityList.size() < 3){
			PatientEntity patientEntity = patientMapper.selectById(suggestionEntity.getWechat_id());
			suggestionEntity.setPhone(patientEntity.getPhone());
			suggestionEntity.setName(patientEntity.getName());
			patientMapper.insertSuggestion(suggestionEntity);
			return "success";
		}else{
			return "count";
		}
	}

	/*
	*发起留言和回复
	 */
	@RequestMapping(value = "/messageboard/set",method = RequestMethod.POST)
	public String setMessageBoard(@RequestBody MessageBoardEntity messageBoardEntity){
	try{
		patientMapper.insertMessageBoard(messageBoardEntity);
	}catch (Exception e){
		System.out.println(e.getMessage());
		return "error";
	}
	return "success";
	}

//	/*
//	*回复留言
//	 */
//	@RequestMapping(value = "/messageboard/reply" ,method = RequestMethod.POST)
//	public String setMessageReply(@RequestBody MessageReplyEntity messageReplyEntity){
//		try{
//			patientMapper.insertMessageReply(messageReplyEntity);
//		}catch (Exception e){
//			System.out.println(e.getMessage());
//			return "error";
//		}
//		return "success";
//	}
	//获取患者留言的最新回复
	@RequestMapping(value = "/messageboard/getnewestmessage")
	public List<MessageBoardEntity> getNewestMessageBoards(@RequestParam("wechat_id") String wechat_id) {
		List<MessageBoardEntity> messageBoardEntityList=patientMapper.selectNewestMessageBoard(wechat_id);
		PatientEntity patientEntity ;
		DoctorEntity doctorEntity;
			HashSet<Integer> idHash=new HashSet<>();
		for (Iterator<MessageBoardEntity> it = messageBoardEntityList.iterator(); it.hasNext() ;) {
			MessageBoardEntity messageBoardEntity = it.next();
			if(messageBoardEntity.getReply_id()!=0){
				if(!idHash.contains(messageBoardEntity.getReply_id())) {
					idHash.add(messageBoardEntity.getReply_id());
					if (messageBoardEntity.getSender() == 0) {
						patientEntity = patientMapper.selectById(messageBoardEntity.getWechat_id());
						messageBoardEntity.setHead_pic(patientEntity.getHead_pic());
						messageBoardEntity.setName(patientEntity.getName());
					} else {
						doctorEntity = patientMapper.selectDoctorByPhone(messageBoardEntity.getPhone());
						messageBoardEntity.setHead_pic(doctorEntity.getHead_pic());
						messageBoardEntity.setName(doctorEntity.getName());
					}
				}else{
					it.remove();
				}
			}else{
				if(idHash.contains(messageBoardEntity.getId())){
					it.remove();
				}else{
					if(messageBoardEntity.getSender()==0){
						patientEntity = patientMapper.selectById(messageBoardEntity.getWechat_id());
						messageBoardEntity.setHead_pic(patientEntity.getHead_pic());
						messageBoardEntity.setName(patientEntity.getName());
					}else{
						doctorEntity=patientMapper.selectDoctorByPhone(messageBoardEntity.getPhone());
						messageBoardEntity.setHead_pic(doctorEntity.getHead_pic());
						messageBoardEntity.setName(doctorEntity.getName());
					}
				}
			}
		}
		return messageBoardEntityList;
	}
	//获取患者一个留言板及回复
	@RequestMapping(value = "/messageboard/getonemessage")
	public List<MessageBoardEntity> getOneMessageBoardAndReply(int id){
		List<MessageBoardEntity> messageBoardEntityList = new LinkedList<>();
		MessageBoardEntity messageBoardEntity = patientMapper.selectPatientMessageBoard(id);
		PatientEntity patientEntity=patientMapper.selectById(messageBoardEntity.getWechat_id());
		if(messageBoardEntity.getReply_id()==0){
			messageBoardEntity.setName(patientEntity.getName());
			messageBoardEntity.setAge(patientEntity.getAge());
			messageBoardEntity.setSex(patientEntity.getSex());
			messageBoardEntityList.add(messageBoardEntity);
			return messageBoardEntityList;
		}else {

			MessageBoardEntity first = patientMapper.selectPatientMessageBoard(messageBoardEntity.getReply_id());
			DoctorEntity doctorEntity =patientMapper.selectDoctorByPhone(messageBoardEntity.getPhone());

			first.setName(patientEntity.getName());
			first.setAge(patientEntity.getAge());
			first.setSex(patientEntity.getSex());
			messageBoardEntityList.add(first);

			List<MessageBoardEntity> replyList = patientMapper.selectMessageBoardReply(messageBoardEntity.getReply_id());
			for (Iterator<MessageBoardEntity> it=replyList.iterator();it.hasNext();) {
				MessageBoardEntity oneReply = it.next();
				if(oneReply.getSender()==0){
					oneReply.setName(patientEntity.getName());
					oneReply.setHead_pic(patientEntity.getHead_pic());
					messageBoardEntityList.add(oneReply);
				}else{
					if(oneReply.getIsread()==0){
						patientMapper.updateMessageBoardReaded(oneReply.getId());
					}
					oneReply.setName(doctorEntity.getName());
					oneReply.setHead_pic(doctorEntity.getHead_pic());
					messageBoardEntityList.add(oneReply);
				}
			}
			return messageBoardEntityList;
		}
	}

}
