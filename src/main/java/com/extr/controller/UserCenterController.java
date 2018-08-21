package com.extr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.extr.controller.domain.QuestionImproveResult;
import com.extr.domain.question.KnowledgePoint;
import com.extr.domain.question.QuestionHistory;
import com.extr.domain.question.UserQuestionHistory;
import com.extr.security.UserInfo;
import com.extr.service.QuestionService;

/**
 * @author Ocelot
 * @date 2014年7月29日 下午4:31:03
 */
@Controller
public class UserCenterController {

	@Autowired
	private QuestionService questionService;

	/**
	 * 用户中心主页
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "student/usercenter", method = RequestMethod.GET)
	public String userCenterPage(Model model, HttpServletRequest request) {

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		model.addAttribute("username", userInfo.getUsername());
		model.addAttribute("email", userInfo.getEmail());
		model.addAttribute("field", userInfo.getFieldName());
		UserQuestionHistory uqh = questionService
				.getUserQuestionHistoryByUserId(userInfo.getUserid());
		model.addAttribute("lastLoginTime", userInfo.getLastLoginTime());
		List<KnowledgePoint> pointList = questionService
				.getKnowledgePointByFieldId(userInfo.getFieldId(),null);
		// 岗位通用id=1
		List<KnowledgePoint> pointList1 = new ArrayList<KnowledgePoint>();
		if (userInfo.getFieldId() != 1)
			pointList1 = questionService.getKnowledgePointByFieldId(1,null);
		List<Integer> pointIdList = new ArrayList<Integer>();
		List<StatisticsResult> srList = new ArrayList<StatisticsResult>();
		Map<Integer, Map<Integer, QuestionHistory>> history;
		pointList.addAll(pointList1);
		// 获取知识点id列表
		for (KnowledgePoint kp : pointList) {

			pointIdList.add(kp.getPointId());
		}
		List<QuestionImproveResult> questionImproveList = questionService
				.getQuestionImproveResultByQuestionPointIdList(pointIdList);
		Map<Integer, Integer> pointStatisticMap = new HashMap<Integer, Integer>();

		// 获取每种知识点对应的试题数量
		for (QuestionImproveResult qir : questionImproveList) {
			int amount = 0;
			if(pointStatisticMap.containsKey(qir.getQuestionPointId()))
				amount = pointStatisticMap.get(qir.getQuestionPointId());
			//主观题不参加统计 2014-08-13
			if(qir.getQuestionTypeId() == 1 || qir.getQuestionTypeId() == 2 || qir.getQuestionTypeId() == 3 || qir.getQuestionTypeId() == 4)
				amount += qir.getAmount();
			//主观题不参加统计 2014-08-13
			//amount += pointStatisticMap.get(qir.getQuestionPointId());
			pointStatisticMap.put(qir.getQuestionPointId(), amount);
		}
		
		Map<Integer, QuestionHistory> rightMap = new HashMap<Integer, QuestionHistory>();
		Map<Integer, QuestionHistory> wrongMap = new HashMap<Integer, QuestionHistory>();
		
		for (KnowledgePoint kp : pointList) {
			StatisticsResult sr = new StatisticsResult();
			sr.setPointId(kp.getPointId());
			sr.setPointName(kp.getPointName());
			if (uqh != null) {
				history = uqh.getHistory();
				if (history.containsKey(0))
					wrongMap = history.get(0);
				if (history.containsKey(1))
					rightMap = history.get(1);
				Iterator<Integer> rightIt = rightMap.keySet().iterator();
				Iterator<Integer> wrongIt = wrongMap.keySet().iterator();
				int rightAmount = 0;
				int wrongAmount = 0;
				while (rightIt.hasNext()) {
					int key = rightIt.next();
					QuestionHistory tmpQh = rightMap.get(key);
					if (tmpQh.getPointId() == kp.getPointId())
						rightAmount++;
				}
				while (wrongIt.hasNext()) {
					int key = wrongIt.next();
					QuestionHistory tmpQh = wrongMap.get(key);
					if (tmpQh.getPointId() == kp.getPointId())
						wrongAmount++;
				}
				sr.setRightTimes(rightAmount);
				sr.setWrongTimes(wrongAmount);
				int amount = 0;
				if(pointStatisticMap.containsKey(sr.getPointId()))
					amount = pointStatisticMap.get(sr.getPointId());
				System.out.println("amount=" + amount);
				float rightRate = rightAmount + wrongAmount != 0 ? (float) Math
						.round((float) rightAmount * 10000f / amount) / 10000f
						: 0f;
				System.out.println("rightRate=" + rightRate);
				float finishRate = amount != 0 ? (float) Math
						.round((float) (rightAmount + wrongAmount) * 10000f
								/ amount) / 10000f : 0f;
				System.out.println("finishRate=" + finishRate);
				sr.setRightRate(rightRate);
				sr.setFinishRate(finishRate);
			}
			sr.setAmount(pointStatisticMap.get(kp.getPointId()) == null ? 0 : pointStatisticMap.get(kp.getPointId()));
			srList.add(sr);
		}

		model.addAttribute("sr", srList);
		model.addAttribute("labels", this.generateLables(srList));
		model.addAttribute("finishrate", this.generateFinishData(srList));
		model.addAttribute("correctrate", this.generateCorrectData(srList));
		return "student/usercenter";
	}

	/**
	 * 分析页面
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "student/analysis", method = RequestMethod.GET)
	public String userAnalysisPage(Model model, HttpServletRequest request) {

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		UserQuestionHistory uqh = questionService
				.getUserQuestionHistoryByUserId(userInfo.getUserid());
		model.addAttribute("lastLoginTime", userInfo.getLastLoginTime());
		List<KnowledgePoint> pointList = questionService
				.getKnowledgePointByFieldId(userInfo.getFieldId(),null);
		// 岗位通用id=1
		List<KnowledgePoint> pointList1 = new ArrayList<KnowledgePoint>();
		if (userInfo.getFieldId() != 1)
			pointList1 = questionService.getKnowledgePointByFieldId(1,null);
		List<Integer> pointIdList = new ArrayList<Integer>();
		// List<StatisticsResult> srList = new ArrayList<StatisticsResult>();
		Map<Integer, Map<Integer, QuestionHistory>> history;
		pointList.addAll(pointList1);
		// 获取知识点id列表
		for (KnowledgePoint kp : pointList) {

			pointIdList.add(kp.getPointId());
		}
		List<QuestionImproveResult> questionImproveList = questionService
				.getQuestionImproveResultByQuestionPointIdList(pointIdList);

		List<KnowledgePointAnalysisResult> kparl = new ArrayList<KnowledgePointAnalysisResult>();
		Map<Integer, QuestionHistory> rightMap = new HashMap<Integer, QuestionHistory>();
		Map<Integer, QuestionHistory> wrongMap = new HashMap<Integer, QuestionHistory>();
		if (uqh != null) {
			history = uqh.getHistory();

			if (history.containsKey(0))
				wrongMap = history.get(0);
			if (history.containsKey(1))
				rightMap = history.get(1);

		}
		for (KnowledgePoint kp : pointList) {

			KnowledgePointAnalysisResult kpar = new KnowledgePointAnalysisResult();
			kpar.setKnowledgePointId(kp.getPointId());
			kpar.setKnowledgePointName(kp.getPointName());
			List<TypeAnalysis> tal = new ArrayList<TypeAnalysis>();
			// 一个知识点内所有的题
			float totalCount = 0;
			// 做过的题（一个知识点内）
			float finishQuestionCount = 0;
			for (QuestionImproveResult qir : questionImproveList) {
				if (qir.getQuestionPointId() == kp.getPointId()) {
					System.out.println("qir.getAmount()=" + qir.getAmount());
					TypeAnalysis t = new TypeAnalysis();
					t.setQuestionTypeId(qir.getQuestionTypeId());
					t.setQuestionTypeName(qir.getQuestionTypeName());
					t.setRestAmount(qir.getAmount());
					totalCount += qir.getAmount();
					tal.add(t);
				}
			}

			for (TypeAnalysis ta : tal) {
				Iterator<Integer> rightIt = rightMap.keySet().iterator();
				Iterator<Integer> wrongIt = wrongMap.keySet().iterator();
				int rightAmount = 0;
				int wrongAmount = 0;
				while (rightIt.hasNext()) {
					int key = rightIt.next();
					QuestionHistory tmpQh = rightMap.get(key);
					if (tmpQh.getPointId() == kp.getPointId()
							&& tmpQh.getQuestionTypeId() == ta
									.getQuestionTypeId()) {
						rightAmount++;
					}

				}
				while (wrongIt.hasNext()) {
					int key = wrongIt.next();
					QuestionHistory tmpQh = wrongMap.get(key);
					if (tmpQh.getPointId() == kp.getPointId()
							&& tmpQh.getQuestionTypeId() == ta
									.getQuestionTypeId()) {
						wrongAmount++;
					}

				}
				ta.setRightAmount(rightAmount);
				ta.setWrongAmount(wrongAmount);
				finishQuestionCount += rightAmount + wrongAmount;
				ta.setRestAmount(ta.getRestAmount() - rightAmount - wrongAmount);
			}
			kpar.setTypeAnalysis(tal);
			kpar.setFinishRate(((float) Math.round(finishQuestionCount * 1000f
					/ totalCount)) / 1000f);
			kparl.add(kpar);
		}
		model.addAttribute("kparl", kparl);
		return "student/analysis";
	}

	private String generateLables(List<StatisticsResult> srList) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < srList.size(); i++) {
			sb.append("\"");
			sb.append(srList.get(i).getPointName());
			sb.append("\"");
			if (i != srList.size() - 1)
				sb.append(",");
		}
		return sb.toString();

	}

	private String generateFinishData(List<StatisticsResult> srList) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < srList.size(); i++) {
			sb.append(srList.get(i).getFinishRate() * 100);
			if (i != srList.size() - 1)
				sb.append(",");
		}
		return sb.toString();
	}

	private String generateCorrectData(List<StatisticsResult> srList) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < srList.size(); i++) {
			sb.append(srList.get(i).getRightRate() * 100);

			if (i != srList.size() - 1)
				sb.append(",");
		}
		return sb.toString();
	}

	public class TypeAnalysis {

		private int questionTypeId;
		private String questionTypeName;
		private int restAmount;
		private int rightAmount;
		private int wrongAmount;

		public int getQuestionTypeId() {
			return questionTypeId;
		}

		public void setQuestionTypeId(int questionTypeId) {
			this.questionTypeId = questionTypeId;
		}

		public String getQuestionTypeName() {
			return questionTypeName;
		}

		public void setQuestionTypeName(String questionTypeName) {
			this.questionTypeName = questionTypeName;
		}

		public int getRestAmount() {
			return restAmount;
		}

		public void setRestAmount(int restAmount) {
			this.restAmount = restAmount;
		}

		public int getRightAmount() {
			return rightAmount;
		}

		public void setRightAmount(int rightAmount) {
			this.rightAmount = rightAmount;
		}

		public int getWrongAmount() {
			return wrongAmount;
		}

		public void setWrongAmount(int wrongAmount) {
			this.wrongAmount = wrongAmount;
		}
	}

	public class KnowledgePointAnalysisResult {
		private int knowledgePointId;
		private String knowledgePointName;
		private List<TypeAnalysis> typeAnalysis;
		private float finishRate;

		public int getKnowledgePointId() {
			return knowledgePointId;
		}

		public void setKnowledgePointId(int knowledgePointId) {
			this.knowledgePointId = knowledgePointId;
		}

		public String getKnowledgePointName() {
			return knowledgePointName;
		}

		public void setKnowledgePointName(String knowledgePointName) {
			this.knowledgePointName = knowledgePointName;
		}

		public List<TypeAnalysis> getTypeAnalysis() {
			return typeAnalysis;
		}

		public void setTypeAnalysis(List<TypeAnalysis> typeAnalysis) {
			this.typeAnalysis = typeAnalysis;
		}

		public float getFinishRate() {
			return finishRate;
		}

		public void setFinishRate(float finishRate) {
			this.finishRate = finishRate;
		}
	}

	public class StatisticsResult {
		public int pointId;
		public String pointName;
		public int amount;
		public int rightTimes;
		public int wrongTimes;
		public float finishRate;
		public float rightRate;

		public float getFinishRate() {
			return finishRate;
		}

		public void setFinishRate(float finishRate) {
			this.finishRate = finishRate;
		}

		public float getRightRate() {
			return rightRate;
		}

		public void setRightRate(float rightRate) {
			this.rightRate = rightRate;
		}

		public int getPointId() {
			return pointId;
		}

		public void setPointId(int pointId) {
			this.pointId = pointId;
		}

		public String getPointName() {
			return pointName;
		}

		public void setPointName(String pointName) {
			this.pointName = pointName;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public int getRightTimes() {
			return rightTimes;
		}

		public void setRightTimes(int rightTimes) {
			this.rightTimes = rightTimes;
		}

		public int getWrongTimes() {
			return wrongTimes;
		}

		public void setWrongTimes(int wrongTimes) {
			this.wrongTimes = wrongTimes;
		}

	}
}
