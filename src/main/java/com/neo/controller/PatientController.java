package com.neo.controller;

import com.neo.entity.*;
import com.neo.mapper.DoctorMapper;
import com.neo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
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
                DecimalFormat df = new DecimalFormat("0000");
                String str2 = df.format(new Random().nextInt(10000));
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
    @RequestMapping(value = "/evaluate")
    public String evaluateDoctor(@RequestBody EvaluationEntity evaluationEntity) {

        if (patientMapper.selectEvaluation(evaluationEntity.getWechat_id(), evaluationEntity.getPhone()) == null) {
            patientMapper.insertEvaluation(evaluationEntity);
            return "success";
        } else {
            return "error";
        }

    }

    /*
    * 患者建议接口
    * */
    @RequestMapping(value = "/contact/suggest", method = RequestMethod.POST)
    public String suggest(@RequestBody SuggestionEntity suggestionEntity) {
        List<SuggestionEntity> suggestionEntityList = patientMapper.selectSuggestionByTime(suggestionEntity.getWechat_id());
        if (suggestionEntityList.size() < 3) {
            PatientEntity patientEntity = patientMapper.selectById(suggestionEntity.getWechat_id());
            suggestionEntity.setPhone(patientEntity.getPhone());
            suggestionEntity.setName(patientEntity.getName());
            patientMapper.insertSuggestion(suggestionEntity);
            return "success";
        } else {
            return "count";
        }
    }

    /*
    *发起留言和回复
     */
    @RequestMapping(value = "/messageboard/set", method = RequestMethod.POST)
    public String setMessageBoard(@RequestBody MessageBoardEntity messageBoardEntity) {
        try {
            patientMapper.insertMessageBoard(messageBoardEntity);
        } catch (Exception e) {
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
    //根据患者获取留言的最新回复
    @RequestMapping(value = "/messageboard/getpatientnewestmessage")
    public List<MessageBoardEntity> getPatientNewestMessageBoards(@RequestParam("wechat_id") String wechat_id) {
        List<MessageBoardEntity> messageBoardEntityList = patientMapper.selectPatientNewestMessageBoard(wechat_id);
        return selectMessageBoardAndReply(messageBoardEntityList, 0);
    }

    //根据医生获取留言的最新回复
    @RequestMapping(value = "/messageboard/getdoctornewestmessage")
    public List<MessageBoardEntity> getNewestMessageBoards(@RequestParam("phone") String phone) {
        List<MessageBoardEntity> messageBoardEntityList = patientMapper.selectDoctorNewestMessageBoard(phone);
        return selectMessageBoardAndReply(messageBoardEntityList, 1);
    }

    //筛选留言和回复
    private List<MessageBoardEntity> selectMessageBoardAndReply(List<MessageBoardEntity> messageBoardEntityList, int who) {
        PatientEntity patientEntity;
        DoctorEntity doctorEntity;


        HashSet<Integer> idHash = new HashSet<>();
        for (Iterator<MessageBoardEntity> it = messageBoardEntityList.iterator(); it.hasNext(); ) {
            MessageBoardEntity messageBoardEntity = it.next();
            if (messageBoardEntity.getReply_id() != 0) {
                if (!idHash.contains(messageBoardEntity.getReply_id())) {
                    idHash.add(messageBoardEntity.getReply_id());
                    if (who == 1) {
                        patientEntity = patientMapper.selectById(messageBoardEntity.getWechat_id());
                        messageBoardEntity.setSex(patientEntity.getSex());
                        messageBoardEntity.setAge(patientEntity.getAge());
                        messageBoardEntity.setHead_pic(patientEntity.getHead_pic());
                        messageBoardEntity.setName(patientEntity.getName());
                    } else {
                        doctorEntity = patientMapper.selectDoctorByPhone(messageBoardEntity.getPhone());
                        messageBoardEntity.setHead_pic(doctorEntity.getHead_pic());
                        messageBoardEntity.setName(doctorEntity.getName());
                    }
                } else {
                    it.remove();
                }
            } else {
                if (idHash.contains(messageBoardEntity.getId())) {
                    it.remove();
                } else {
                    if (who == 1) {
                        patientEntity = patientMapper.selectById(messageBoardEntity.getWechat_id());
                        messageBoardEntity.setHead_pic(patientEntity.getHead_pic());
                        messageBoardEntity.setAge(patientEntity.getAge());
                        messageBoardEntity.setSex(patientEntity.getSex());
                        messageBoardEntity.setName(patientEntity.getName());
                    } else {
                        doctorEntity = patientMapper.selectDoctorByPhone(messageBoardEntity.getPhone());
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
    public List<MessageBoardEntity> getOneMessageBoardAndReply(@RequestParam("id") int id, @RequestParam("who") int who) {

        List<MessageBoardEntity> messageBoardEntityList = new LinkedList<>();
        MessageBoardEntity messageBoardEntity = patientMapper.selectPatientMessageBoard(id);
        PatientEntity patientEntity = patientMapper.selectById(messageBoardEntity.getWechat_id());
//		if(messageBoardEntity.getReply_id()==0){
//			messageBoardEntity.setName(patientEntity.getName());
//			messageBoardEntity.setAge(patientEntity.getAge());
//			messageBoardEntity.setSex(patientEntity.getSex());
//			messageBoardEntityList.add(messageBoardEntity);
//			return messageBoardEntityList;
//		}else {
        MessageBoardEntity first;
        if (messageBoardEntity.getReply_id() == 0) {
            first = messageBoardEntity;
        } else {
            first = patientMapper.selectPatientMessageBoard(messageBoardEntity.getReply_id());
        }
        DoctorEntity doctorEntity = patientMapper.selectDoctorByPhone(messageBoardEntity.getPhone());

        first.setName(patientEntity.getName());
        first.setAge(patientEntity.getAge());
        first.setSex(patientEntity.getSex());
        messageBoardEntityList.add(first);


        List<MessageBoardEntity> replyList;

        if (messageBoardEntity.getReply_id() != 0) {
            replyList = patientMapper.selectMessageBoardReply(messageBoardEntity.getReply_id());
        } else {
            replyList = patientMapper.selectMessageBoardReply(messageBoardEntity.getId());
        }
        if (replyList != null) {
            for (Iterator<MessageBoardEntity> it = replyList.iterator(); it.hasNext(); ) {
                MessageBoardEntity oneReply = it.next();
                if (oneReply.getSender() == 0 && who == 1 && oneReply.getIsread() == 0) {
                    patientMapper.updateMessageBoardReaded(oneReply.getId());
                }
                if (oneReply.getSender() == 1 && who == 0 && oneReply.getIsread() == 0) {
                    patientMapper.updateMessageBoardReaded(oneReply.getId());
                }
                if (oneReply.getSender() == 0) {
                    oneReply.setName(patientEntity.getName());
                    oneReply.setHead_pic(patientEntity.getHead_pic());
                    messageBoardEntityList.add(oneReply);
                } else {

                    oneReply.setName(doctorEntity.getName());
                    oneReply.setHead_pic(doctorEntity.getHead_pic());
                    messageBoardEntityList.add(oneReply);
                }
            }
        }
        return messageBoardEntityList;
    }


    //删除留言
    @RequestMapping(value = "/messageboard/delete", method = RequestMethod.POST)
    public String deletePatientMessageBoard(@RequestParam("id") int id, @RequestParam("who") int who) {
        try {
            MessageBoardEntity messageBoardEntity = patientMapper.selectPatientMessageBoard(id);
            if (who == 0) {

                patientMapper.deletePatientMessageBoard(messageBoardEntity.getId());
            } else {
                patientMapper.deleteDoctorMessageBoard(messageBoardEntity.getId());
            }
            if (messageBoardEntity.getReply_id() != 0) {
                patientMapper.deletePatientMessageBoard(messageBoardEntity.getReply_id());
                List<MessageBoardEntity> replyList = patientMapper.selectMessageBoardReply(messageBoardEntity.getReply_id());
                for (Iterator<MessageBoardEntity> it = replyList.iterator(); it.hasNext(); ) {
                    if (who == 0) {
                        patientMapper.deletePatientMessageBoard(it.next().getId());
                    } else {
                        patientMapper.deleteDoctorMessageBoard(it.next().getId());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    //获取患者未读群发消息个数
    @RequestMapping(value = "/groupreceiving/unread")
    public int getGroupReceivingUnread(@RequestParam("wechat_id") String wechat_id) {
        return patientMapper.getGroupReceivingUnread(wechat_id);
    }

    //获取患者的医生群发消息
    @RequestMapping(value = "/groupreceiving/list")
    public List<PatientGroupReceivingEntity> getPatientGroupReceiving(@RequestParam("wechat_id") String wechat_id) {
        return patientMapper.selectPatientGroupReceiving(wechat_id);
    }

    /*
    *删除患者的医生群发消息
     */
    @RequestMapping(value = "/groupreceiving/delete", method = RequestMethod.POST)
    public String deleteGroupReceiving(@RequestParam("id") int id) {
        try {
            patientMapper.deleteGroupReceiving(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }
        return "success";
    }

    /*
     *设置患者的医生群发消息为已读
     */
    @RequestMapping(value = "/groupreceiving/setread", method = RequestMethod.POST)
    public String setGroupReceivingRead(@RequestParam("id") int id){
        try{
            patientMapper.setGroupReceivingRead(id);
        }catch (Exception e){
            System.out.println("设置患者的医生群发消息为已读时出错:"+e.getMessage());
            return "error";
        }
        return "success";
    }
    /*
     *获取患者的一个医生群发消息的详细信息
     */
    @RequestMapping(value = "/groupreceiving/get",method = RequestMethod.GET)
   public  PatientGroupReceivingEntity getGroupReceiving(@RequestParam("id") int id){
        return patientMapper.getGroupReceiving(id);
    }
}
