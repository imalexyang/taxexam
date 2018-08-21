package com.extr.controller;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.extr.controller.domain.AnswerSheetItem;
import com.extr.controller.domain.ExamFinishParam;
import com.extr.controller.domain.Message;
import com.extr.controller.domain.PaperCreatorParam;
import com.extr.controller.domain.QuestionQueryResult;
import com.extr.domain.exam.ExamHistory;
import com.extr.domain.exam.ExamPaper;
import com.extr.domain.exam.PracticePaper;
import com.extr.domain.question.Question;
import com.extr.domain.question.QuestionHistory;
import com.extr.domain.question.QuestionStruts;
import com.extr.domain.question.QuestionType;
import com.extr.domain.question.UserQuestionHistory;
import com.extr.domain.user.User;
import com.extr.security.UserInfo;
import com.extr.service.ExamService;
import com.extr.service.PracticeService;
import com.extr.service.QuestionService;
import com.extr.service.UserService;
import com.extr.util.Page;
import com.extr.util.PagingUtil;
import com.extr.util.QuestionAdapter;
import com.extr.util.xml.Object2Xml;

@Controller
public class ExamController {

	@Autowired
	private ExamService examService;
	@Autowired
	private UserService userService;
	@Autowired
	private PracticeService practiceService;
	@Autowired
	private QuestionService questionService;
	private static Logger log = Logger.getLogger(ExamController.class);
	private static final String SUCCESS_MESSAGE = "success";
	private static final String FAILED_MESSAGE = "failed";

	public class ReportResultAll{
		public User user;
		public int total ;
		public int wrong ;
		public int right ;
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		public int getWrong() {
			return wrong;
		}
		public void setWrong(int wrong) {
			this.wrong = wrong;
		}
		public int getRight() {
			return right;
		}
		public void setRight(int right) {
			this.right = right;
		}		
	}
	
	public class ReportResult {
		public int sum;
		public int rightTimes;
		public int wrongTimes;

		public int getSum() {
			return sum;
		}

		public int getRightTimes() {
			return rightTimes;
		}

		public int getWrongTimes() {
			return wrongTimes;
		}
	};

	/**
	 * 准备模拟考试
	 * 
	 * @param model
	 * @param exam_history_id
	 * @param request
	 * @return
	 */
	/*@RequestMapping(value = "/student/practice-testing", method = RequestMethod.GET)
	public String practiceStart(Model model, HttpServletRequest request,
			@RequestParam(value = "kp", required = false) String knowledgepoint) {

		System.out.println(knowledgepoint);

		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		List<Question> questionList = practiceService
				.getQuestionListByQuestionTypeIdAndReferenceId(-1,
						userInfo.getFieldId(), 20);
		List<String> htmlList = new ArrayList<String>();

		for (Question q : questionList) {
			htmlList.add(new QuestionAdapter(q, null, null, strUrl)
					.getStringFromXML(false, false, false));
		}
		model.addAttribute("htmlList", htmlList);
		return "student/practice-testing";
	}*/
	
	@RequestMapping(value = "/student/practice-test", method = RequestMethod.GET)
	public String practiceStartNew(Model model, HttpServletRequest request,
			@RequestParam(value = "kp", required = false) String knowledgepoint) {

		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		List<Integer> fieldIdList = new ArrayList<Integer>();
		fieldIdList.add(userInfo.getFieldId());
		List<Integer> typeIdList = new ArrayList<Integer>();
		typeIdList.add(1);
		typeIdList.add(2);
		typeIdList.add(3);
		typeIdList.add(4);
		List<QuestionQueryResult> qqrList = questionService.getQuestionQueryResultListByFieldIdList(fieldIdList,typeIdList, 20);
		
		String fieldName = "";
		try{
			fieldName = qqrList.get(0).getPointName().split(">")[1];
		}catch(Exception e){
			log.info(e.getMessage());
		}
		
		int amount = qqrList.size();
		StringBuilder sb = new StringBuilder();
		for(QuestionQueryResult qqr : qqrList){
			QuestionAdapter adapter = new QuestionAdapter(qqr,strUrl);
			sb.append(adapter.getStringFromXML());
		}
		
		model.addAttribute("questionStr", sb.toString());
		model.addAttribute("amount", amount);
		model.addAttribute("fieldName", "随机练习");
		return "student/practice-improve";
	}

	@RequestMapping(value = "student/practice-random", method = RequestMethod.GET)
	public String practice(Model model, HttpServletRequest request) {

		return "student/practice-testing";
	}

	@RequestMapping(value = "student/practice-finished", method = RequestMethod.POST)
	public @ResponseBody
	Message practiceFinished(@RequestBody ExamFinishParam efp,
			HttpServletRequest request) {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		HashMap<Integer, AnswerSheetItem> hm = efp.getAs();
		Iterator<Integer> it = hm.keySet().iterator();
		List<Integer> idList = new ArrayList<Integer>();
		while (it.hasNext()) {
			int key = it.next();
			idList.add(key);
			AnswerSheetItem as = hm.get(key);
			System.out.println(key + "=" + as.getPoint());
		}
		PracticePaper practicePaper = new PracticePaper();
		practicePaper.setAnswer_sheet(Object2Xml.toXml(hm));
		practicePaper.setName("我的练习");
		List<QuestionQueryResult> questionList = examService.getQuestionDescribeListByIdList(idList);

		practicePaper.setContent(Object2Xml.toXml(questionList));
		practicePaper.setUserId(userInfo.getUserid());
		practiceService.insertPracticePaper(practicePaper);
		Message message = new Message();
		return message;
	}

	@RequestMapping(value = "student/practice-finished-page", method = RequestMethod.GET)
	public String practiceFinishedPage(Model model) {
		return "student/practice-finish";
	}

	@RequestMapping(value = "student/exam-report", method = RequestMethod.GET)
	public String examFinishedReportPage(Model model, HttpServletRequest request) {
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		PracticePaper practicePaper = practiceService
				.getPracticePaperByUserID(userInfo.getUserid());

		List<QuestionQueryResult> questionList = Object2Xml.toBean(
				practicePaper.getContent(), ArrayList.class);
		List<Integer> idList = new ArrayList<Integer>();
		HashMap<Integer, AnswerSheetItem> hm = Object2Xml.toBean(
				practicePaper.getAnswer_sheet(), HashMap.class);

		List<String> htmlStr = new ArrayList<String>();
		HashMap<Integer, QuestionQueryResult> questionMap = new HashMap<Integer, QuestionQueryResult>();
		for (QuestionQueryResult qqr : questionList) {
			idList.add(qqr.getQuestionId());
			
			QuestionAdapter adapter = new QuestionAdapter(hm.get(qqr.getQuestionId()),qqr,strUrl);
			htmlStr.add(adapter.getReportStringFromXML());
		}

		model.addAttribute("htmlStr", htmlStr);

		return "student/exam-finish-report";
	}

	@RequestMapping(value = "student/finish-exam", method = RequestMethod.GET)
	public String examFinishedPage(Model model) {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		PracticePaper practicePaper = practiceService
				.getPracticePaperByUserID(userInfo.getUserid());

		List<QuestionQueryResult> questionList = Object2Xml.toBean(
				practicePaper.getContent(), ArrayList.class);
		List<Integer> idList = new ArrayList<Integer>();
		for (QuestionQueryResult q : questionList) {
			idList.add(q.getQuestionId());
		}
		HashMap<Integer, AnswerSheetItem> hm = Object2Xml.toBean(
				practicePaper.getAnswer_sheet(), HashMap.class);

		int total = questionList.size();
		int wrong = 0;
		int right = 0;

		HashMap<String, ReportResult> reportResultList = new HashMap<String, ReportResult>();
		List<QuestionQueryResult> questionQueryList = examService
				.getQuestionDescribeListByIdList(idList);
		HashMap<Integer, Boolean> answer = new HashMap<Integer, Boolean>();
		for (QuestionQueryResult q : questionQueryList) {
			String pointName = q.getPointName().split(">")[1];
			if (q.getQuestionTypeId() != 1 && q.getQuestionTypeId() != 2
					&& q.getQuestionTypeId() != 3)
				continue;
			if (hm.get(q.getQuestionId()) != null) {
				if (q.getAnswer().equals(hm.get(q.getQuestionId()).getAnswer())) {
					answer.put(q.getQuestionId(), true);
					right++;
					if (reportResultList.containsKey(pointName)) {
						ReportResult r = reportResultList.get(pointName);
						r.sum++;
						r.rightTimes++;
						reportResultList.put(pointName, r);
					} else {
						ReportResult r = new ReportResult();
						r.sum = 1;
						r.rightTimes = 1;
						reportResultList.put(pointName, r);
					}
				} else {
					answer.put(q.getQuestionId(), false);
					wrong++;
					if (reportResultList.containsKey(pointName)) {
						ReportResult r = reportResultList.get(pointName);
						r.sum++;
						r.wrongTimes++;
						reportResultList.put(pointName, r);
					} else {
						ReportResult r = new ReportResult();
						r.sum = 1;
						r.wrongTimes = 1;
						reportResultList.put(pointName, r);
					}
				}
				hm.remove(q.getQuestionId());
			}
		}

		model.addAttribute("total", total);
		model.addAttribute("wrong", wrong);
		model.addAttribute("right", right);
		model.addAttribute("reportResultList", reportResultList);
		model.addAttribute("create_time", practicePaper.getCreate_time());
		model.addAttribute("answer", answer);
		model.addAttribute("idList", idList);
		return "student/exam-finished";
	}

	/**
	 * 准备模拟考试
	 * 
	 * @param model
	 * @param exam_history_id
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/student/examing/{examPaperId}", method = RequestMethod.GET)
	public String examing(Model model, HttpServletRequest request,
			@PathVariable("examPaperId") int examPaperId) {
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		User user=userService.getUserById(userInfo.getUserid());
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int duration = 0;		
		ExamHistory examHistory = examService
				.getUserExamHistoryByUserIdAndExamPaperId(userInfo.getUserid(),
						examPaperId);
		ExamPaper examPaper = examService.getExamPaperById(examPaperId);
		String content = "";
		if (examHistory != null) {
			content = examHistory.getContent();
			
			duration = examHistory.getDuration();
			Date now = new Date();
			
			long startT = examHistory.getCreateTime().getTime();
			long endT = 0;
			try {
				endT = df.parse(df.format(now)).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			long minutsPass = (endT - startT) / 1000;
			
			duration = (int) (minutsPass >= (long)duration ? 0 : duration - minutsPass);
			
			/*if(duration == 0){
				model.addAttribute("msg", "您已经提交过试卷，不能再做题。" );
				return "home";
			}*/				
			if (user.getStatus().equals("1")) {
				model.addAttribute("msg", "您已经提交过试卷，不能再做题。" );
				return "home";
			}				
		} else {
			duration = examPaper.getDuration();	
			content = examPaper.getContent();
			examHistory = new ExamHistory();
			examHistory.setContent(content);
			examHistory.setExamPaperId(examPaperId);
			examHistory.setUserId(userInfo.getUserid());
			examHistory.setDuration(examPaper.getDuration());
			examService.addUserExamHistory(examHistory);
			
		}

		@SuppressWarnings("unchecked")
		List<QuestionQueryResult> questionList = Object2Xml.toBean(content,
				List.class);

		StringBuilder sb = new StringBuilder();
		for (QuestionQueryResult question : questionList) {
			QuestionAdapter adapter = new QuestionAdapter(question, strUrl);
			sb.append(adapter.getUserExamPaper());
		}

		model.addAttribute("examHistoryId", examHistory.getHistId());
		model.addAttribute("examPaperId", examPaperId);
		model.addAttribute("duration", duration );
		model.addAttribute("htmlStr", sb.toString());
		return "student/examing";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/student/exam-submit", method = RequestMethod.POST)
	public @ResponseBody
	Message finishExam(@RequestBody ExamFinishParam efp) {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Message message = new Message();
		try {
			ExamHistory examHistory = examService
					.getUserExamHistoryByHistId(efp.getExam_history_id());
			List<QuestionQueryResult> questionList = Object2Xml.toBean(examHistory.getContent(), List.class);
			float pointGet = 0f;
			for(QuestionQueryResult qqr : questionList){
				if(qqr.getAnswer().equals(efp.getAs().get(qqr.getQuestionId()).getAnswer()))
					pointGet += qqr.getQuestionPoint();
			}
			//计算得分
			examHistory.setPointGet(pointGet);
			examHistory.setAnswerSheet(Object2Xml.toXml(efp.getAs()));
			examHistory.setSubmitTime(new Date());
			examHistory.setDuration(efp.getDuration());
			examService.updateExamHistory(examHistory);
			User user =new User();
			user.setId(userInfo.getUserid());
			user.setStatus("1");
			userService.updateStatus(user);
		} catch (Exception e) {
			e.printStackTrace();
			message.setResult(e.getClass().getName());
		}

		return message;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/student/exam-report/{examPaperId}", method = RequestMethod.GET)
	public String examFinishPage(Model model,
			@PathVariable("examPaperId") int examPaperId,
			HttpServletRequest request) {
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		ExamHistory examHistory = examService
				.getUserExamHistoryByUserIdAndExamPaperId(userInfo.getUserid(),
						examPaperId);

		List<QuestionQueryResult> questionList = Object2Xml.toBean(
				examHistory.getContent(), ArrayList.class);
		HashMap<Integer, AnswerSheetItem> hm = Object2Xml.toBean(
				examHistory.getAnswerSheet(), HashMap.class);

		StringBuilder sb = new StringBuilder();
		for (QuestionQueryResult q : questionList) {
			QuestionAdapter adapter = new QuestionAdapter(hm.get(q
					.getQuestionId()), q, strUrl);
			sb.append(adapter.getReportStringFromXML());
		}
		model.addAttribute("htmlStr", sb.toString());
		model.addAttribute("examPaperId", examPaperId);
		return "student/paper-exam-finish-report";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "student/finish-exam/{examPaperId}", method = RequestMethod.GET)
	public String paperExamFinishedPage(Model model,
			@PathVariable("examPaperId") int examPaperId) {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		ExamHistory examHistory = examService
				.getUserExamHistoryByUserIdAndExamPaperId(userInfo.getUserid(),
						examPaperId);
		HashMap<Integer, AnswerSheetItem> hm = Object2Xml.toBean(
				examHistory.getAnswerSheet(), HashMap.class);

		int total = hm.size();
		int wrong = 0;
		int right = 0;

		HashMap<String, ReportResult> reportResultList = new HashMap<String, ReportResult>();
		List<QuestionQueryResult> questionQueryList = Object2Xml.toBean(
				examHistory.getContent(), List.class);
		List<Integer> idList = new ArrayList<Integer>();
		HashMap<Integer, Boolean> answer = new HashMap<Integer, Boolean>();
		for (QuestionQueryResult q : questionQueryList) {
			String pointName = q.getPointName().split(">")[1];
			idList.add(q.getQuestionId());
			if (q.getQuestionTypeId() != 1 && q.getQuestionTypeId() != 2
					&& q.getQuestionTypeId() != 3)
				continue;
			if (hm.get(q.getQuestionId()) != null) {
				if (q.getAnswer().equals(hm.get(q.getQuestionId()).getAnswer())) {
					answer.put(q.getQuestionId(), true);
					right++;
					if (reportResultList.containsKey(pointName)) {
						ReportResult r = reportResultList.get(pointName);
						r.sum++;
						r.rightTimes++;
						reportResultList.put(pointName, r);
					} else {
						ReportResult r = new ReportResult();
						r.sum = 1;
						r.rightTimes = 1;
						reportResultList.put(pointName, r);
					}
				} else {
					answer.put(q.getQuestionId(), false);
					wrong++;
					if (reportResultList.containsKey(pointName)) {
						ReportResult r = reportResultList.get(pointName);
						r.sum++;
						r.wrongTimes++;
						reportResultList.put(pointName, r);
					} else {
						ReportResult r = new ReportResult();
						r.sum = 1;
						r.wrongTimes = 1;
						reportResultList.put(pointName, r);
					}
				}
				hm.remove(q.getQuestionId());
			}
		}

		model.addAttribute("total", total);
		model.addAttribute("wrong", wrong);
		model.addAttribute("right", right);
		model.addAttribute("reportResultList", reportResultList);
		model.addAttribute("create_time", examHistory.getCreateTime());
		model.addAttribute("answer", answer);
		model.addAttribute("idList", idList);
		model.addAttribute("examPaperId", examPaperId);
		return "student/paper-exam-finished";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "admin/exam-paper-report/{examPaperId}", method = RequestMethod.GET)
	public String paperExamFinishedPageAll(Model model,
			@PathVariable("examPaperId") int examPaperId,
			HttpServletRequest request) {
		//UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int index = 1;
		if(request.getParameter("page") != null)
			index = Integer.parseInt(request.getParameter("page"));
		Page<User> page = new Page<User>();
		page.setPageNo(index);
		page.setPageSize(10);
		List<User> userList =userService.getUserListByRoleIdAndStatus(3,1,page);		
		//model.addAttribute("userList", userList);
		
		
		List<ReportResultAll> rraList=new ArrayList<ReportResultAll>();
		for(User user:userList){
			ReportResultAll rra=new ReportResultAll();			
			ExamHistory examHistory = examService.getUserExamHistoryByUserIdAndExamPaperId(user.getId(),examPaperId);
			
			int total = 60;
			int wrong = 0;
			int right = 0;
			
			if(null!=examHistory){
				right = (int) examHistory.getPointGet();
				wrong = total-right;			
				
		/*		HashMap<Integer, AnswerSheetItem> hm = Object2Xml.toBean(
						examHistory.getAnswerSheet(), HashMap.class);

				total = hm.size();				

				HashMap<String, ReportResult> reportResultList = new HashMap<String, ReportResult>();
				List<QuestionQueryResult> questionQueryList = Object2Xml.toBean(
						examHistory.getContent(), List.class);
				List<Integer> idList = new ArrayList<Integer>();
				HashMap<Integer, Boolean> answer = new HashMap<Integer, Boolean>();
				for (QuestionQueryResult q : questionQueryList) {
					String pointName = q.getPointName().split(">")[1];
					idList.add(q.getQuestionId());
					if (q.getQuestionTypeId() != 1 && q.getQuestionTypeId() != 2
							&& q.getQuestionTypeId() != 3)
						continue;
					if (hm.get(q.getQuestionId()) != null) {
						if (q.getAnswer().equals(hm.get(q.getQuestionId()).getAnswer())) {
							answer.put(q.getQuestionId(), true);
							right++;
							if (reportResultList.containsKey(pointName)) {
								ReportResult r = reportResultList.get(pointName);
								r.sum++;
								r.rightTimes++;
								reportResultList.put(pointName, r);
							} else {
								ReportResult r = new ReportResult();
								r.sum = 1;
								r.rightTimes = 1;
								reportResultList.put(pointName, r);
							}
						} else {
							answer.put(q.getQuestionId(), false);
							wrong++;
							if (reportResultList.containsKey(pointName)) {
								ReportResult r = reportResultList.get(pointName);
								r.sum++;
								r.wrongTimes++;
								reportResultList.put(pointName, r);
							} else {
								ReportResult r = new ReportResult();
								r.sum = 1;
								r.wrongTimes = 1;
								reportResultList.put(pointName, r);
							}
						}
						hm.remove(q.getQuestionId());
					}
				}
					*/
			}
			rra.setUser(user);
			rra.setTotal(total);
			rra.setWrong(wrong);
			rra.setRight(right);
			rraList.add(rra);	
		}
		//int totalPage=totalCount/10+1;
		//page.setTotalPage(totalPage);
		String pageStr = PagingUtil.getPagelink(index, page.getTotalPage(), "", "admin/exam-paper-report/"+examPaperId);

		//model.addAttribute("total", total);
		//model.addAttribute("wrong", wrong);
		//model.addAttribute("right", right);
		model.addAttribute("rraList", rraList);
		model.addAttribute("pageStr", pageStr);
		//model.addAttribute("create_time", examHistory.getCreateTime());
		//model.addAttribute("answer", answer);
		//model.addAttribute("idList", idList);
		//model.addAttribute("examPaperId", examPaperId);
		return "admin/exam-paper-report";
	}
	
	@RequestMapping(value="admin/exam-paper-refreshAllRight",method=RequestMethod.POST)
	@ResponseBody
	public Object refreshAllRight(){
		Page<User> page = new Page<User>();
		page.setPageNo(1); //默认第一页起始，
		page.setPageSize(10000000); //给了一个一千万
		List<User> userList=userService.getUserListByRoleIdAndStatus(3, 1, page);
		
		for(User user:userList){			
			ExamHistory examHistory = examService
					.getUserExamHistoryByUserIdAndExamPaperId(user.getId(),1); //默认试题
			int total = 60;
			//int wrong = 0;
			int right = 0;
			
			if(null!=examHistory){				
				
				right =  (int) examHistory.getPointGet();
				//wrong = total-right;
				
				/*
				HashMap<Integer, AnswerSheetItem> hm = Object2Xml.toBean(
						examHistory.getAnswerSheet(), HashMap.class);
				HashMap<String, ReportResult> reportResultList = new HashMap<String, ReportResult>();
				List<QuestionQueryResult> questionQueryList = Object2Xml.toBean(
						examHistory.getContent(), List.class);
				List<Integer> idList = new ArrayList<Integer>();
				HashMap<Integer, Boolean> answer = new HashMap<Integer, Boolean>();
				for (QuestionQueryResult q : questionQueryList) {
					String pointName = q.getPointName().split(">")[1];
					idList.add(q.getQuestionId());
					if (q.getQuestionTypeId() != 1 && q.getQuestionTypeId() != 2
							&& q.getQuestionTypeId() != 3)
						continue;
					if (hm.get(q.getQuestionId()) != null) {
						if (q.getAnswer().equals(hm.get(q.getQuestionId()).getAnswer())) {
							answer.put(q.getQuestionId(), true);
							right++;
							if (reportResultList.containsKey(pointName)) {
								ReportResult r = reportResultList.get(pointName);
								r.sum++;
								r.rightTimes++;
								reportResultList.put(pointName, r);
							} else {
								ReportResult r = new ReportResult();
								r.sum = 1;
								r.rightTimes = 1;
								reportResultList.put(pointName, r);
							}
						} else {
							answer.put(q.getQuestionId(), false);
							wrong++;
							if (reportResultList.containsKey(pointName)) {
								ReportResult r = reportResultList.get(pointName);
								r.sum++;
								r.wrongTimes++;
								reportResultList.put(pointName, r);
							} else {
								ReportResult r = new ReportResult();
								r.sum = 1;
								r.wrongTimes = 1;
								reportResultList.put(pointName, r);
							}
						}
						hm.remove(q.getQuestionId());
					}
				}*/
				
				User userc=new User();
				userc.setId(user.getId());
				if(total==right){	
					userc.setIsall("1");				
				}else{
					userc.setIsall("0");
				}
				userService.updateIsall(userc);
			}			
		}
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("status", "success");
		return map;
	}
	
	@RequestMapping(value="admin/exam-paper-isall",method=RequestMethod.GET)
	public String allRight(Model model,HttpServletRequest request){
		int index = 1;
		if(request.getParameter("page") != null)
			index = Integer.parseInt(request.getParameter("page"));
		Page<User> page = new Page<User>();
		page.setPageNo(index);
		page.setPageSize(10);
		String isall="1";
		List<User> userList=userService.getUserListByIsall(isall, page);
		String pageStr = PagingUtil.getPagelink(index, page.getTotalPage(), "", "admin/exam-paper-isall");
		model.addAttribute("userList", userList);
		model.addAttribute("pageStr", pageStr);
		return "admin/exam-paper-isall";
	}
	
	@RequestMapping(value = "student/exam-history", method = RequestMethod.GET)
	public String userExamHistPage(Model model, HttpServletRequest request){
		
		int index = 1;
		if(request.getParameter("page") != null)
			index = Integer.parseInt(request.getParameter("page"));
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Page<ExamHistory> pageModel = new Page<ExamHistory>();
		//pageModel.setPageSize(1);
		pageModel.setPageNo(index);
		List<ExamHistory> hisList = examService.getUserExamHistoryListByUserId(userInfo.getUserid(),pageModel);
		model.addAttribute("hisList", hisList);
		String pageStr = PagingUtil.getPagelink(index, pageModel.getTotalPage(), "", "student/exam-his");
		model.addAttribute("pageStr", pageStr);
		return "student/exam-history";
	}
}
