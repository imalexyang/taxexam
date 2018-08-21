package com.extr.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.extr.controller.domain.Message;
import com.extr.controller.domain.QuestionFilter;
import com.extr.controller.domain.QuestionQueryResult;
import com.extr.domain.question.Field;
import com.extr.domain.question.KnowledgePoint;
import com.extr.domain.question.Question;
import com.extr.domain.question.QuestionTag;
import com.extr.domain.question.Tag;
import com.extr.file.util.FileUploadUtil;
import com.extr.security.UserInfo;
import com.extr.service.ExamService;
import com.extr.service.QuestionService;
import com.extr.util.Page;
import com.extr.util.PagingUtil;
import com.extr.util.QuestionAdapter;
import com.extr.util.xml.Object2Xml;

@Controller
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private ExamService examService;

	/**
	 * 题库页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/question-list", method = RequestMethod.GET)
	public String questionListPage(Model model) {

		return "redirect:questionfilter-0-0-0-0-1.html";
	}

	/**
	 * 题库页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/questionfilter-{fieldId}-{knowledge}-{questionType}-{searchParam}-{page}.html", method = RequestMethod.GET)
	public String questionListFilterPage(Model model,
			@PathVariable("fieldId") int fieldId,
			@PathVariable("knowledge") int knowledge,
			@PathVariable("questionType") int questionType,
			@PathVariable("searchParam") String searchParam,
			@PathVariable("page") int page) {
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		QuestionFilter qf = new QuestionFilter();
		qf.setFieldId(fieldId);
		qf.setKnowledge(knowledge);
		qf.setQuestionType(questionType);
		if (searchParam.equals("0"))
			searchParam = "-1";
		qf.setSearchParam(searchParam);

		Page<Question> pageModel = new Page<Question>();
		pageModel.setPageNo(page);
		pageModel.setPageSize(20);

		List<Question> questionList = questionService.getQuestionList(
				pageModel, qf);

		String pageStr = PagingUtil.getPageBtnlink(page,
				pageModel.getTotalPage());

		List<Field> fieldList = questionService.getAllField(null);
		model.addAttribute("fieldList", fieldList);

		/*if(fieldList.size() > 0)
			fieldId = fieldList.get(0).getFieldId();*/
		model.addAttribute("knowledgeList",
				questionService.getKnowledgePointByFieldId(fieldId,null));

		model.addAttribute("questionTypeList",
				questionService.getQuestionTypeList());

		model.addAttribute("questionFilter", qf);
		model.addAttribute("questionList", questionList);
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("tagList", questionService.getTagByUserId(userInfo.getUserid(), null));
		//保存筛选信息，删除后跳转页面时使用
		model.addAttribute("fieldId", fieldId);
		model.addAttribute("knowledge", knowledge);
		model.addAttribute("questionType", questionType);
		model.addAttribute("searchParam", searchParam);

		return "admin/question-list";
	}
	
	/**
	 * 题库页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/questionfilterdialog-{fieldId}-{knowledge}-{questionType}-{searchParam}-{page}.html", method = RequestMethod.GET)
	public String questionListFilterDialogPage(Model model,
			@PathVariable("fieldId") int fieldId,
			@PathVariable("knowledge") int knowledge,
			@PathVariable("questionType") int questionType,
			@PathVariable("searchParam") String searchParam,
			@PathVariable("page") int page) {
		
		
		QuestionFilter qf = new QuestionFilter();
		qf.setFieldId(fieldId);
		qf.setKnowledge(knowledge);
		qf.setQuestionType(questionType);
		if (searchParam.equals("0"))
			searchParam = "-1";
		qf.setSearchParam(searchParam);

		Page<Question> pageModel = new Page<Question>();
		pageModel.setPageNo(page);
		pageModel.setPageSize(20);

		List<Question> questionList = questionService.getQuestionList(
				pageModel, qf);

		String pageStr = PagingUtil.getPageBtnlink(page,
				pageModel.getTotalPage());

		model.addAttribute("fieldList", questionService.getAllField(null));

		model.addAttribute("knowledgeList",
				questionService.getKnowledgePointByFieldId(fieldId,null));

		model.addAttribute("questionTypeList",
				questionService.getQuestionTypeList());

		model.addAttribute("questionFilter", qf);
		model.addAttribute("questionList", questionList);
		model.addAttribute("pageStr", pageStr);
		
		//保存筛选信息，删除后跳转页面时使用
		model.addAttribute("fieldId", fieldId);
		model.addAttribute("knowledge", knowledge);
		model.addAttribute("questionType", questionType);
		model.addAttribute("searchParam", searchParam);

		return "admin/question-list-dialog";
	}

	/**
	 * 试题导入页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/question-import", method = RequestMethod.GET)
	public String questionImportPage(Model model) {
		
		List<Field> fieldList = questionService.getAllField(null);
		model.addAttribute("fieldList", fieldList);
		return "admin/question-import";
	}

	/**
	 * 添加试题页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/question-add", method = RequestMethod.GET)
	public String questionAddPage(Model model) {
		List<Field> fieldList = questionService.getAllField(null);
		model.addAttribute("fieldList", fieldList);
		return "admin/question-add";
	}

	@RequestMapping(value = "/admin/get-knowledge-point/{fieldId}", method = RequestMethod.GET)
	public @ResponseBody
	Message getQuestionPointByFieldId(@PathVariable int fieldId) {
		Message message = new Message();
		HashMap<Integer, String> pointMap = new HashMap<Integer, String>();
		List<KnowledgePoint> pointList = questionService
				.getKnowledgePointByFieldId(fieldId,null);
		for (KnowledgePoint point : pointList) {
			pointMap.put(point.getPointId(), point.getPointName());
		}
		message.setObject(pointMap);
		return message;
	}

	@RequestMapping(value = "/admin/question-add", method = RequestMethod.POST)
	public @ResponseBody
	Message addQuestion(@RequestBody Question question) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		Message message = new Message();
		question.setContent(Object2Xml.toXml(question.getQuestionContent()));
		question.setCreate_time(new Date());
		question.setCreator(userDetails.getUsername());
		try {
			questionService.addQuestion(question);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			message.setResult("error");
			message.setMessageInfo(e.getClass().getName());
			e.printStackTrace();
		}
		
		return message;
	}

	@RequestMapping(value = "/admin/question-modify/{questionId}", method = RequestMethod.GET)
	public String questionModifyPage(Model model,
			@PathVariable("questionId") int questionId) {
		List<Field> fieldList = questionService.getAllField(null);
		model.addAttribute("fieldList", fieldList);
		Question question = questionService.getQuestionByQuestionId(questionId);
		List<KnowledgePoint> pointList = questionService.getQuestionKnowledgePointListByQuestionId(questionId);
		model.addAttribute("pointList", pointList);
		model.addAttribute("question", question);
		return "admin/question-add";
	}
	
	@RequestMapping(value = "/admin/question-preview/{questionId}", method = RequestMethod.GET)
	public String questionPreviewPage(Model model,
			@PathVariable("questionId") int questionId, HttpServletRequest request){
		String strUrl = "http://" + request.getServerName() //服务器地址  
                + ":"   
                + request.getServerPort() + "/";
		Question question = questionService.getQuestionByQuestionId(questionId);
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(questionId);
		List<QuestionQueryResult> questionQueryList = examService.getQuestionDescribeListByIdList(idList);
		HashMap<Integer, QuestionQueryResult> questionMap = new HashMap<Integer, QuestionQueryResult>();
		for (QuestionQueryResult qqr : questionQueryList) {
			if (questionMap.containsKey(qqr.getQuestionId())) {
				QuestionQueryResult a = questionMap.get(qqr.getQuestionId());
				questionMap.put(qqr.getQuestionId(), a);
			} else {
				questionMap.put(qqr.getQuestionId(), qqr);
			}
		}
		QuestionAdapter adapter = new QuestionAdapter(question,null,questionMap.get(questionId),strUrl);
		String strHtml = adapter.getStringFromXML(true, false, true);
		model.addAttribute("strHtml", strHtml);
		model.addAttribute("question", question);
		return "admin/question-preview";
	}
	
	@RequestMapping(value = "/admin/upload-uploadify-img", method = RequestMethod.POST)
	public @ResponseBody String uploadImg(HttpServletRequest request, HttpServletResponse response){
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		List<String> filePathList = new ArrayList<String>();
		try {
			filePathList = FileUploadUtil.uploadImg(request, response, userInfo.getUsername());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		if(filePathList.size() == 0){
			return "系统错误";
		}
				
		
		return filePathList.get(0);
	}
	
	@RequestMapping(value = "/admin/upload-uploadify", method = RequestMethod.POST)
	public @ResponseBody String uploadFile(HttpServletRequest request, HttpServletResponse response){
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		List<String> filePathList = new ArrayList<String>();
		try {
			filePathList = FileUploadUtil.uploadFile(request, response, userInfo.getUsername());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		if(filePathList.size() == 0){
			return "系统错误";
		}
				
		
		return filePathList.get(0);
	}
	
	@RequestMapping(value = "/admin/delete-question-list/{fieldId}-{knowledge}-{questionType}-{searchParam}-{page}/{questionId}", method = RequestMethod.GET)
	public String deleteQuestionList(Model model,
			@PathVariable("fieldId") int fieldId,
			@PathVariable("knowledge") int knowledge,
			@PathVariable("questionType") int questionType,
			@PathVariable("searchParam") String searchParam,
			@PathVariable("page") int page,
			@PathVariable("questionId") int questionId){
		questionService.deleteQuestionByQuestionId(questionId);
		return "admin/questionfilter-" + fieldId + "-" + knowledge + "-" + questionType + "-" + searchParam + "-" + page + ".html";
	}
	
	@RequestMapping(value = "/admin/delete-question/{questionId}", method = RequestMethod.GET)
	public @ResponseBody Message deleteQuestion(Model model,
			@PathVariable("questionId") int questionId){
		
		//UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Message message = new Message();
		try{
			questionService.deleteQuestionByQuestionId(questionId);
		}catch(Exception ex){
			message.setResult("error");
		}
		
		return message;
	}
	
	/**
	 * 批量添加试题
	 * @param model
	 * @param idList
	 * @return
	 */
	@RequestMapping(value = "/admin/get-question-detail4add", method = RequestMethod.POST)
	public @ResponseBody List<QuestionQueryResult> getQuestion5add(Model model, HttpServletRequest request, @RequestBody List<Integer> idList) {
		String strUrl = "http://" + request.getServerName() // 服务器地址
				+ ":" + request.getServerPort() + "/";
		
		Set<Integer> set = new TreeSet<Integer>();
		for(int id : idList){
			set.add(id);
		}
		idList.clear();
		Iterator<Integer> it = set.iterator();
		while(it.hasNext()){
			idList.add(it.next());
		}
		List<QuestionQueryResult> returnList = examService.getQuestionDescribeListByIdList(idList);
		
		for(QuestionQueryResult question : returnList){
			QuestionAdapter adapter = new QuestionAdapter(question, strUrl);
			question.setContent(adapter.getStringFromXML());
		}
		return returnList;
	}
	
	
	
	/**
	 * 修改试题分类
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/admin/question-update/{questionId}/{pointId}", method = RequestMethod.POST)
	public @ResponseBody Message updateQuestion(@PathVariable int questionId, @PathVariable int pointId, @RequestBody List<QuestionTag> questionTagList) {
		
		Message message = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Question question = new Question();
		question.setId(questionId);
		List<Integer> pointIdList = new ArrayList<Integer>();
		pointIdList.add(pointId);
		question.setPointList(pointIdList);
		try{
			questionService.updateQuestionPoint(question,userInfo.getUserid(),questionTagList);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
		}
		
		return message;
	}
	
	@RequestMapping(value = "/admin/field-list-{index}", method = RequestMethod.GET)
	public String fieldListPage(Model model,@PathVariable("index") int index){
		
		Page<Field> page = new Page<Field>();
		page.setPageNo(index);
		page.setPageSize(8);
		List<Field> fieldList = questionService.getAllField(page);
		String pageStr = PagingUtil.getPageBtnlink(index,
				page.getTotalPage());
		model.addAttribute("fieldList", fieldList);
		model.addAttribute("pageStr", pageStr);
		return "admin/field-list";
	}
	
	@RequestMapping(value = "/admin/point-list-{fieldId}-{index}", method = RequestMethod.GET)
	public String knowledgePointPage(Model model,@PathVariable("fieldId") int fieldId,@PathVariable("index") int index){
		
		Page<KnowledgePoint> page = new Page<KnowledgePoint>();
		page.setPageNo(index);
		page.setPageSize(8);
		
		List<Field> fieldList = questionService.getAllField(null);
		
		List<KnowledgePoint> pointList = questionService.getKnowledgePointByFieldId(fieldId,page);
		String pageStr = PagingUtil.getPageBtnlink(index,
				page.getTotalPage());
		model.addAttribute("pointList", pointList);
		model.addAttribute("fieldList", fieldList);
		model.addAttribute("fieldId", fieldId);
		model.addAttribute("pageStr", pageStr);
		return "admin/point-list";
	}
	
	@RequestMapping(value = "/admin/add-point", method = RequestMethod.GET)
	public String addPointPage(Model model){
		
				
		List<Field> fieldList = questionService.getAllField(null);
		
		model.addAttribute("fieldList", fieldList);
		return "admin/add-point";
	}
	
	@RequestMapping(value = "/admin/delete-field-{fieldId}", method = RequestMethod.GET)
	public @ResponseBody Message deleteField(Model model,@PathVariable("fieldId") int fieldId){
		//TODO 严欢完善下
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(fieldId);
		questionService.deleteFieldByIdList(idList);
		return new Message();
	}
	
	@RequestMapping(value = "/admin/delete-point-{pointId}", method = RequestMethod.GET)
	public @ResponseBody Message deleteKnowledgePoint(Model model,@PathVariable("pointId") int pointId){
		//TO.DO 严欢完善下
		List<Integer> idList = new ArrayList<Integer>();
		idList.add(pointId);
		questionService.deleteKnowledgePointByIdList(idList);
		return new Message();
	}
	
	@RequestMapping(value = "/admin/field-add", method = RequestMethod.POST)
	public @ResponseBody Message addField(@RequestBody Field field){
		
		Message message = new Message();
		try{
			questionService.addField(field);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
			e.printStackTrace();
		}
		
		return message;
	}
	
	@RequestMapping(value = "/admin/point-add", method = RequestMethod.POST)
	public @ResponseBody Message addPoint(@RequestBody KnowledgePoint point){
		
		Message message = new Message();
		try{
			questionService.addKnowledgePoint(point);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
			e.printStackTrace();
		}
		
		return message;
	}
	
	@RequestMapping(value = "/admin/add-field", method = RequestMethod.GET)
	public String addFieldPage(Model model){
		
		
		return "admin/add-field";
	}
	
	@RequestMapping(value = "/admin/question-import/{id}", method = RequestMethod.POST)
	public @ResponseBody Message courseImport(@RequestBody String filePath, @PathVariable("id") int id) {
		Message message = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if(id == 0){
			message.setResult("error");
			message.setMessageInfo("请选择题库");
			return message;
		}
		try{
			questionService.uploadQuestions(filePath, userInfo.getUsername(),id);
		}catch(RuntimeException e){
			message.setResult(e.getClass().getName() + ":" + e.getMessage());
			message.setMessageInfo(e.getMessage());
		}
		
		return message;
	}
	
	@RequestMapping(value = "/teacher/tag-list-{index}", method = RequestMethod.GET)
	public String tagListPage(Model model,@PathVariable("index") int index){
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Page<Tag> page = new Page<Tag>();
		page.setPageNo(index);
		page.setPageSize(8);
		List<Tag> tagList = questionService.getTagByUserId(userInfo.getUserid(), page);
		String pageStr = PagingUtil.getPageBtnlink(index,
				page.getTotalPage());
		model.addAttribute("tagList", tagList);
		model.addAttribute("pageStr", pageStr);
		return "teacher/tag-list";
	}
	
	@RequestMapping(value = "/teacher/add-tag", method = RequestMethod.GET)
	public String addTagPage(Model model){
		
		return "teacher/add-tag";
	}
	
	@RequestMapping(value = "/teacher/tag-add", method = RequestMethod.POST)
	public @ResponseBody Message addTag(@RequestBody Tag tag){
		
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		tag.setCreator(userInfo.getUserid());
		Message message = new Message();
		try{
			questionService.addTag(tag);
		}catch(Exception e){
			message.setResult(e.getClass().getName());
			e.printStackTrace();
		}
		
		return message;
	}
	
	@RequestMapping(value = "/teacher/question-tag/{questionId}", method = RequestMethod.GET)
	public @ResponseBody Message getQuestionTag(@PathVariable("questionId") int questionId){
		Message message = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		List<QuestionTag> tagList = questionService.getQuestionTagByQuestionIdAndUserId(questionId, userInfo.getUserid(), null);
		message.setObject(tagList);
		return message;
	}
	
	@RequestMapping(value = "/teacher/add-question-tag", method = RequestMethod.POST)
	public @ResponseBody Message addQuestionTag(@RequestBody int questionId,@RequestBody List<QuestionTag> questionTagList){
		Message message = new Message();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		try{
			questionService.addQuestionTag(questionId, userInfo.getUserid(), questionTagList);
		}catch(Exception e){
			e.printStackTrace();
			message.setResult(e.getClass().getName());
		}
		
		return message;
	}
	
}
