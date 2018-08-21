package com.extr.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.extr.controller.domain.QuestionQueryResult;
import com.extr.domain.exam.ExamHistory;
import com.extr.domain.exam.ExamPaper;
import com.extr.domain.exam.Paper;
import com.extr.domain.exam.PracticePaper;
import com.extr.domain.question.Question;
import com.extr.domain.question.QuestionStruts;
import com.extr.persistence.ExamMapper;
import com.extr.persistence.ExamPaperMapper;
import com.extr.persistence.QuestionMapper;
import com.extr.util.MyInterceptor;
import com.extr.util.Page;
import com.extr.util.xml.Object2Xml;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午8:20:23
 */
@Service("examService")
public class ExamServiceImpl implements ExamService {

	private static Logger log = Logger.getLogger(ExamServiceImpl.class);
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private ExamPaperMapper examPaperMapper;
	@Autowired
	private ExamMapper examMapper;

	@Override
	public List<QuestionQueryResult> getQuestionDescribeListByIdList(
			List<Integer> idList) {
		List<QuestionQueryResult> questionList = questionMapper
				.getQuestionAnalysisListByIdList(idList);
		return questionList;
	}

	@Override
	public List<Question> getQuestionListByIdListNew(List<Integer> idList) {
		List<Question> questionList = questionMapper
				.getQuestionListByIdListNew(idList);
		return questionList;
	}

	@Override
	@Transactional
	public void createExamPaper(
			HashMap<Integer, HashMap<Integer, List<QuestionStruts>>> questionMap,
			HashMap<Integer, Integer> questionTypeNum,
			HashMap<Integer, Float> questionTypePoint,
			HashMap<Integer, Float> knowledgePointRate, ExamPaper examPaper) {
		// TODO Auto-generated method stub

		Paper paper = new Paper(questionMap, questionTypeNum, questionTypePoint,knowledgePointRate);
		try {
			paper.createPaper();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e1.getMessage());
		}

		try {
			HashMap<Integer, QuestionStruts> paperQuestionMap = paper
					.getPaperQuestionMap();

			Iterator<Integer> it = paperQuestionMap.keySet().iterator();
			List<Integer> idList = new ArrayList<Integer>();
			while (it.hasNext()) {
				idList.add(it.next());
			}
			List<QuestionQueryResult> questionList = questionMapper
					.getQuestionAnalysisListByIdList(idList);
			for(QuestionQueryResult qqr : questionList){
				qqr.setQuestionPoint(questionTypePoint.get(qqr.getQuestionTypeId()));
			}
			examPaper.setContent(Object2Xml.toXml(questionList));
			examPaperMapper.insertExamPaper(examPaper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}

	@Override
	public List<ExamPaper> getExamPaperListByPaperType(String paperType,
			Page<ExamPaper> page) {
		// TODO Auto-generated method stub
		return examPaperMapper.getExamPaperListByPaperType(paperType, page);
	}

	@Override
	public void updateExamPaper(ExamPaper examPaper) {
		// TODO Auto-generated method stub
		examPaperMapper.updateExamPaper(examPaper);
	}

	@Override
	public ExamPaper getExamPaperById(int examPaperId) {
		// TODO Auto-generated method stub
		return examPaperMapper.getExamPaperById(examPaperId);
	}

	@Override
	public void insertExamPaper(ExamPaper examPaper) {
		// TODO Auto-generated method stub
		examPaperMapper.insertExamPaper(examPaper);
	}

	@Override
	public List<ExamPaper> getExamPaperList4Exam(int paperType) {
		// TODO Auto-generated method stub
		return examPaperMapper.getExamPaperList4Exam(paperType);
	}

	@Override
	public void addUserExamHistory(ExamHistory examHistory) {
		// TODO Auto-generated method stub
		examMapper.addUserExamHistory(examHistory);
	}

	@Override
	public ExamHistory getUserExamHistoryByUserIdAndExamPaperId(int userId,
			int examPaperId) {
		// TODO Auto-generated method stub
		return examMapper.getUserExamHistoryByUserIdAndExamPaperId(userId, examPaperId);
	}

	@Override
	public void updateExamHistory(ExamHistory examHistory) {
		// TODO Auto-generated method stub
		examMapper.updateExamHistory(examHistory);
	}

	@Override
	public ExamHistory getUserExamHistoryByHistId(int histId) {
		// TODO Auto-generated method stub
		return examMapper.getUserExamHistoryByHistId(histId);
	}

	@Override
	public List<ExamHistory> getUserExamHistoryListByUserId(int userId,Page<ExamHistory> page) {
		// TODO Auto-generated method stub
		return examMapper.getUserExamHistoryListByUserId(userId,page);
	}

	@Override
	public void deleteExamPaper(int id) {
		// TODO Auto-generated method stub
		examPaperMapper.deleteExamPaper(id);
	}

}
