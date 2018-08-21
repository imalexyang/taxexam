package com.extr.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.extr.controller.domain.Message;
import com.extr.controller.domain.QuestionQueryResult;
import com.extr.domain.question.QuestionHistory;
import com.extr.domain.question.QuestionType;
import com.extr.domain.question.UserQuestionHistory;
import com.extr.security.UserInfo;
import com.extr.service.ExamService;
import com.extr.service.QuestionService;
import com.extr.util.QuestionAdapter;

/**
 * @author Ocelot
 * @date 2014年7月29日 下午4:30:18
 */
@Controller
public class PracticeController {

	@Autowired
	public QuestionService questionService;
	@Autowired
	public ExamService examService;
	private static Logger log = Logger.getLogger(PracticeController.class);

	/**
	 * 强化练习
	 * 
	 * @param model
	 * @param exam_history_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/student/practice-improve/{knowledgePointId}/{questionTypeId}", method = RequestMethod.GET)
	public String practiceImprove(Model model, HttpServletRequest request,
			@PathVariable("knowledgePointId") int knowledgePointId,
			@PathVariable("questionTypeId") int questionTypeId) {

		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		List<QuestionQueryResult> qqrList = questionService
				.getQuestionAnalysisListByPointIdAndTypeId(questionTypeId,
						knowledgePointId);
		String questionTypeName = "";
		String fieldName = "";
		try{
			fieldName = qqrList.get(0).getPointName().split(">")[1];
		}catch(Exception e){
			log.info(e.getMessage());
		}
		
		List<QuestionType> questionTypeList = questionService.getQuestionTypeList();
		for(QuestionType qt : questionTypeList){
			
			if(qt.getId() == questionTypeId){
				questionTypeName = qt.getName();
				break;
			}	
		}
		int amount = qqrList.size();
		StringBuilder sb = new StringBuilder();
		for(QuestionQueryResult qqr : qqrList){
			QuestionAdapter adapter = new QuestionAdapter(qqr,strUrl);
			sb.append(adapter.getStringFromXML());
		}
		
		model.addAttribute("questionStr", sb.toString());
		model.addAttribute("amount", amount);
		model.addAttribute("fieldName", fieldName);
		model.addAttribute("questionTypeName", questionTypeName);
		model.addAttribute("practiceName", "强化练习");
		model.addAttribute("knowledgePointId", knowledgePointId);
		model.addAttribute("questionTypeId", questionTypeId);
		return "student/practice-improve-qh";
	}
	
	/**
	 * 错题练习
	 * 
	 * @param model
	 * @param exam_history_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/student/practice-incorrect/{knowledgePointId}", method = RequestMethod.GET)
	public String practiceIncorrectQuestions(Model model, HttpServletRequest request,@PathVariable("knowledgePointId") int knowledgePointId) {
		
		
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		UserQuestionHistory uqh = questionService.getUserQuestionHistoryByUserId(userInfo.getUserid());
		
		List<Integer> idList = new ArrayList<Integer>();
		if(uqh != null){
			if(uqh.getHistory().containsKey(0)){
				Iterator<Integer> it = uqh.getHistory().get(0).keySet().iterator();
				while(it.hasNext()){
					idList.add(it.next());
				}
			}
		}
		List<QuestionQueryResult> qqrList = new ArrayList<QuestionQueryResult>();
		if(uqh != null && uqh.getHistory() != null && uqh.getHistory().containsKey(0))
			qqrList = examService.getQuestionDescribeListByIdList(idList);
		String questionTypeName = "";
		String fieldName = "";

		/*List<QuestionType> questionTypeList = questionService.getQuestionTypeList();
		for(QuestionType qt : questionTypeList){
			
			if(qt.getId() == questionTypeId){
				questionTypeName = qt.getName();
				break;
			}	
		}*/
		List<QuestionQueryResult> qqrListWithPointId = new ArrayList<QuestionQueryResult>();
		for(QuestionQueryResult qqr : qqrList){
			if(qqr.getKnowledgePointId() == knowledgePointId)
				qqrListWithPointId.add(qqr);
		}
		
		try{
			fieldName = qqrListWithPointId.get(0).getPointName().split(">")[1];
		}catch(Exception e){
			log.info(e.getMessage());
		}
		
		
		int amount = qqrListWithPointId.size();
		qqrList = null;
		StringBuilder sb = new StringBuilder();
		for(QuestionQueryResult qqr : qqrListWithPointId){
			QuestionAdapter adapter = new QuestionAdapter(qqr,strUrl);
			sb.append(adapter.getStringFromXML());
		}
		
		model.addAttribute("questionStr", sb.toString());
		model.addAttribute("amount", amount);
		model.addAttribute("fieldName", fieldName);
		model.addAttribute("questionTypeName", "错题库");
		model.addAttribute("practiceName", "错题练习");
		return "student/practice-improve";
	}
	
	/**
	 * 练习模式完成一道题
	 * @param sp
	 * @return
	 */
	@RequestMapping(value = "/student/practice-improve", method = RequestMethod.POST)
	public @ResponseBody
	Message submitPractice(@RequestBody QuestionHistory qh) {
		Message m = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		UserQuestionHistory uqh = questionService.getUserQuestionHistoryByUserId(userInfo.getUserid());
		boolean isNew = uqh == null ? true : false;
		if(uqh == null){
			uqh = new UserQuestionHistory();
			uqh.setModifyTime(new Date());
			uqh.setUserId(userInfo.getUserid());
		}
		boolean isRight = qh.getAnswer().equals(qh.getMyAnswer()) ? true : false;
		qh.setTime(new Date());
		qh.setRight(isRight);
		qh.setTime(new Date());
		
		int questionId = qh.getQuestionId();
		int questionTypeId = qh.getQuestionTypeId();
		QuestionHistory questionHistory = qh;
		
		Map<Integer, Map<Integer, QuestionHistory>> map = uqh.getHistory();
		if(map == null || map.size() == 0)
			map = new HashMap<Integer, Map<Integer, QuestionHistory>>();
		if(questionTypeId == 1 || questionTypeId == 2 || questionTypeId == 3 || questionTypeId == 4){
			Map<Integer, QuestionHistory> histMap = new TreeMap<Integer, QuestionHistory>();
			
			if(isRight){
				if(map.containsKey(1))
					histMap = map.get(1);
				if(map.containsKey(0)){
					map.get(0).remove(questionId);
				}
				histMap.put(questionId, questionHistory);
				map.put(1, histMap);
			} else {
				if(map.containsKey(0))
					histMap = map.get(0);
				if(map.containsKey(1)){
					map.get(1).remove(questionId);
				}
				histMap.put(questionId, questionHistory);
				map.put(0, histMap);
			}
		} else {
			
			Map<Integer, QuestionHistory> histMap = new TreeMap<Integer, QuestionHistory>();
			if(map.containsKey(-1))
				histMap = map.get(-1);
			histMap.put(questionId, questionHistory);
			map.put(-1, histMap);
		}
		uqh.setHistory(map);
		try {
			if(isNew)
				questionService.addUserQuestionHistory(uqh);
			else
				questionService.updateUserQuestionHistory(uqh);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			m.setResult(e.getClass().getName());
			e.printStackTrace();
		}
		
		return m;
	}
	
	/**
	 * 获取用户的练习记录（试题ID）
	 * @param userId
	 * @param knowledgePointId
	 * @return
	 */
	@RequestMapping(value = "/student/practice-improve-his/{knowledgePointId}/{questionTypeId}", method = RequestMethod.GET)
	public @ResponseBody List<Integer> getFinishedQuestionId(Model model, HttpServletRequest request,
			@PathVariable("knowledgePointId") int knowledgePointId,@PathVariable("questionTypeId") int questionTypeId){
		//写死的，需要传个同名的参数过来
//		int questionTypeId = 1;
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		UserQuestionHistory uqh = questionService.getUserQuestionHistoryByUserId(userInfo.getUserid());
		Map<Integer,QuestionHistory> rightMap = new TreeMap<Integer,QuestionHistory>();
		Map<Integer,QuestionHistory> wrongMap = new TreeMap<Integer,QuestionHistory>();
		Map<Integer,QuestionHistory> otherMap = new TreeMap<Integer,QuestionHistory>();
		List<Integer> l = new ArrayList<Integer>();
		List<QuestionHistory> questionHistoryList = new ArrayList<QuestionHistory>();
		if(uqh != null){
			if(uqh.getHistory().containsKey(0)){
				wrongMap = uqh.getHistory().get(0);
				Iterator<Integer> wrongIt = wrongMap.keySet().iterator();
				while(wrongIt.hasNext()){
					int key = wrongIt.next();
					if(wrongMap.get(key).getPointId() == knowledgePointId && wrongMap.get(key).getQuestionTypeId() == questionTypeId)
						questionHistoryList.add(wrongMap.get(key));
				}
			}
				
			if(uqh.getHistory().containsKey(1)){
				rightMap = uqh.getHistory().get(1);
				Iterator<Integer> rightIt = rightMap.keySet().iterator();
				while(rightIt.hasNext()){
					int key = rightIt.next();
					if(rightMap.get(key).getPointId() == knowledgePointId && rightMap.get(key).getQuestionTypeId() == questionTypeId)
						questionHistoryList.add(rightMap.get(key));
				}
			}
			
			if(uqh.getHistory().containsKey(-1)){
				otherMap = uqh.getHistory().get(-1);
				Iterator<Integer> otherIt = otherMap.keySet().iterator();
				while(otherIt.hasNext()){
					int key = otherIt.next();
					if(otherMap.get(key).getPointId() == knowledgePointId && otherMap.get(key).getQuestionTypeId() == questionTypeId)
						questionHistoryList.add(otherMap.get(key));
				}
			}
		}
		
		Collections.sort(questionHistoryList);
		
		for(QuestionHistory questionHistory : questionHistoryList){
			l.add(questionHistory.getQuestionId());
		}
		
		return l;
		
		
	}
}
