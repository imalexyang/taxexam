package com.extr.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.extr.controller.domain.AnswerSheetItem;
import com.extr.controller.domain.Message;
import com.extr.controller.domain.PaperCreatorParam;
import com.extr.controller.domain.QuestionQueryResult;
import com.extr.domain.exam.ExamPaper;
import com.extr.domain.question.Field;
import com.extr.domain.question.Question;
import com.extr.domain.question.QuestionStruts;
import com.extr.security.UserInfo;
import com.extr.service.ExamService;
import com.extr.service.QuestionService;
import com.extr.util.Page;
import com.extr.util.PagingUtil;
import com.extr.util.QuestionAdapter;
import com.extr.util.xml.Object2Xml;

@Controller
public class ExamPaperController {

	@Autowired
	private ExamService examService;
	@Autowired
	private QuestionService questionService;

	private static final String SUCCESS_Message = "success";
	private static final String failed_Message = "failed";

	@RequestMapping(value = "/admin/exampaper-list", method = RequestMethod.GET)
	public String exampaperListPage(Model model,
			HttpServletRequest httpServletRequest) {

		return "redirect:exampaperfilter-0-1.html";
	}

	@RequestMapping(value = "/admin/exampaperfilter-{papertype}-{page}.html", method = RequestMethod.GET)
	public String exampaperListFilterPage(Model model,
			@PathVariable("papertype") String papertype,
			@PathVariable("page") int page) {

		Page<ExamPaper> pageModel = new Page<ExamPaper>();
		pageModel.setPageNo(page);
		pageModel.setPageSize(10);
		List<ExamPaper> paper = examService.getExamPaperListByPaperType(
				papertype, pageModel);
		String pageStr = PagingUtil.getPageBtnlink(page,
				pageModel.getTotalPage());
		model.addAttribute("papertype", papertype);
		model.addAttribute("paper", paper);
		model.addAttribute("pageStr", pageStr);
		return "admin/exampaper-list";
	}

	@RequestMapping(value = "/admin/exampaper-add", method = RequestMethod.GET)
	public String exampaperAddPage(Model model) {
		List<Field> fieldList = questionService.getAllField(null);
		model.addAttribute("fieldList", fieldList);
		return "admin/exampaper-add";
	}

	@RequestMapping(value = "/admin/exampaper-edit/{exampaperid}", method = RequestMethod.GET)
	public String exampaperEditPage(Model model,
			@PathVariable("exampaperid") int exampaperid,HttpServletRequest request) {
		
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		
		ExamPaper examPaper = examService.getExamPaperById(exampaperid);
		StringBuilder sb = new StringBuilder();
		if(examPaper.getContent() != null && !examPaper.getContent().equals("")){
			List<QuestionQueryResult> questionList = Object2Xml.toBean(examPaper.getContent(), List.class);
			for(QuestionQueryResult question : questionList){
				/*AnswerSheetItem as = new AnswerSheetItem();
				as.setAnswer(question.getAnswer());
				as.setQuestion_type_id(question.getQuestionTypeId());
				as.setPoint(question.getQuestionPoint());*/
				QuestionAdapter adapter = new QuestionAdapter(question,strUrl);
				sb.append(adapter.getStringFromXML());
			}
		}
		
		model.addAttribute("htmlStr", sb);
		model.addAttribute("exampaperid", exampaperid);
		model.addAttribute("exampapername", examPaper.getName());
		return "admin/exampaper-edit";
	}

	@RequestMapping(value = "/admin/update-exampaper/{exampaperid}", method = RequestMethod.POST)
	public @ResponseBody
	Message exampaperOnUpdate(Model model,
			@PathVariable("exampaperid") int exampaperid,
			@RequestBody HashMap<Integer, Float> questionPointMap) {
		
		
		Message message = new Message();
		try{
			ExamPaper examPaper = new ExamPaper();
			List<Integer> idList = new ArrayList<Integer>();
			Iterator<Integer> it = questionPointMap.keySet().iterator();
			float sum = 0;
			while(it.hasNext()){
				int key = it.next();
				idList.add(key);
			}
			List<QuestionQueryResult> questionList = examService
					.getQuestionDescribeListByIdList(idList);
			for(QuestionQueryResult q : questionList){
				q.setQuestionPoint(questionPointMap.get(q.getQuestionId()));
				sum += questionPointMap.get(q.getQuestionId());
			}
			String content = Object2Xml.toXml(questionList);
			examPaper.setContent(content);
			examPaper.setTotal_point(sum);
			examPaper.setId(exampaperid);
			examService.updateExamPaper(examPaper);
		}catch(Exception e){
			message.setResult(e.getLocalizedMessage());
		}
		return message;
	}
	
	@RequestMapping(value = "admin/exampaper-add", method = RequestMethod.POST)
	public @ResponseBody
	Message createExamPaper(@RequestBody PaperCreatorParam param) {

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Message message = new Message();
		ExamPaper examPaper = new ExamPaper();
		examPaper.setName(param.getPaperName());
		examPaper.setDuration(param.getTime()*60); //这里需要变成秒
		examPaper.setPass_point(param.getPassPoint());
		examPaper.setPaper_type(param.getPaperType());
		examPaper.setCreator(userInfo.getUsername());
		examPaper.setTotal_point(param.getPaperPoint());
		
		
		//手工组卷
		if(param.getQuestionKnowledgePointRate().size() == 0){
			try{
				examService.insertExamPaper(examPaper);
			}catch(Exception ex){
				message.setResult(ex.getMessage());
			}
			message.setGeneratedId(examPaper.getId());
			return message;
		}
		List<Integer> idList = new ArrayList<Integer>();

		HashMap<Integer, Float> knowledgeMap = param
				.getQuestionKnowledgePointRate();
		Iterator<Integer> it = knowledgeMap.keySet().iterator();
		while(it.hasNext()){
			idList.add(it.next());
		}

		HashMap<Integer, HashMap<Integer, List<QuestionStruts>>> questionMap = questionService
				.getQuestionStrutsMap(idList);
		
		try{
			examService.createExamPaper(questionMap, param.getQuestionTypeNum(),
					param.getQuestionTypePoint(),
					param.getQuestionKnowledgePointRate(), examPaper);
			message.setGeneratedId(examPaper.getId());
		}catch(Exception e){
			e.printStackTrace();
			message.setResult(e.getMessage());
		}
		
		
		return message;
	}
	
	@RequestMapping(value = "admin/paper-publish", method = RequestMethod.POST)
	public @ResponseBody Message publishExamPaper(@RequestBody Integer examPaperId){
		
		Message message = new Message();
		ExamPaper examPaper = new ExamPaper();
		examPaper.setId(examPaperId);
		examPaper.setStatus(1);
		try{
			examService.updateExamPaper(examPaper);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
			
		}
		
		return message;
	}
	
	@RequestMapping(value = "admin/paper-update", method = RequestMethod.POST)
	public @ResponseBody Message updateExamPaper(@RequestBody ExamPaper examPaper){
		
		Message message = new Message();
		examPaper.setStatus(-1);
		try{
			examService.updateExamPaper(examPaper);
			message.setObject(examPaper);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
			
		}
		
		return message;
	}
	
	@RequestMapping(value = "/admin/exampaper-preview/{exampaperid}", method = RequestMethod.GET)
	public String exampaperPreviewPage(Model model,
			@PathVariable("exampaperid") int exampaperid,HttpServletRequest request) {
		
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		
		ExamPaper examPaper = examService.getExamPaperById(exampaperid);
		StringBuilder sb = new StringBuilder();
		if(examPaper.getContent() != null && !examPaper.getContent().equals("")){
			List<QuestionQueryResult> questionList = Object2Xml.toBean(examPaper.getContent(), List.class);
			for(QuestionQueryResult question : questionList){
				/*AnswerSheetItem as = new AnswerSheetItem();
				as.setAnswer(question.getAnswer());
				as.setQuestion_type_id(question.getQuestionTypeId());
				as.setPoint(question.getQuestionPoint());*/
				QuestionAdapter adapter = new QuestionAdapter(question,strUrl);
				sb.append(adapter.getStringFromXML());
			}
		}
		
		model.addAttribute("htmlStr", sb);
		model.addAttribute("exampaperid", exampaperid);
		model.addAttribute("exampapername", examPaper.getName());
		return "admin/exampaper-preview";
	}
	
	@RequestMapping(value = "admin/paper-delete", method = RequestMethod.POST)
	public @ResponseBody Message deleteExamPaper(@RequestBody Integer examPaperId){
		Message message = new Message();
		try{
			ExamPaper examPaper = examService.getExamPaperById(examPaperId);
			if(examPaper.getStatus() == 1){
				message.setResult("已发布的试卷不允许删除");
				return message;
			}
			examService.deleteExamPaper(examPaperId);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
		}
		return message;
	}
	
	@RequestMapping(value = "admin/paper-offline", method = RequestMethod.POST)
	public @ResponseBody Message offlineExamPaper(@RequestBody Integer examPaperId){
		Message message = new Message();
		ExamPaper examPaper = new ExamPaper();
		examPaper.setId(examPaperId);
		examPaper.setStatus(2);
		try{
			examService.updateExamPaper(examPaper);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
			
		}
		
		return message;
	}

}
