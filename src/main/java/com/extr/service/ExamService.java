package com.extr.service;

import java.util.HashMap;
import java.util.List;
import com.extr.controller.domain.QuestionQueryResult;
import com.extr.domain.exam.ExamHistory;
import com.extr.domain.exam.ExamPaper;
import com.extr.domain.question.Question;
import com.extr.domain.question.QuestionStruts;
import com.extr.util.Page;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午5:52:44
 */
public interface ExamService {

	List<QuestionQueryResult> getQuestionDescribeListByIdList(
			List<Integer> idList);

	List<Question> getQuestionListByIdListNew(List<Integer> idList);

	public void createExamPaper(
			HashMap<Integer, HashMap<Integer, List<QuestionStruts>>> questionMap,
			HashMap<Integer, Integer> questionTypeNum,
			HashMap<Integer, Float> questionTypePoint,
			HashMap<Integer, Float> knowledgePointRate, ExamPaper examPaper);

	public List<ExamPaper> getExamPaperListByPaperType(String paperType,
			Page<ExamPaper> page);

	public void updateExamPaper(ExamPaper examPaper);

	public ExamPaper getExamPaperById(int examPaperId);

	public void insertExamPaper(ExamPaper examPaper);

	public List<ExamPaper> getExamPaperList4Exam(int paperType);

	public void addUserExamHistory(ExamHistory examHistory);

	public ExamHistory getUserExamHistoryByUserIdAndExamPaperId(int userId,
			int examPaperId);

	public void updateExamHistory(ExamHistory examHistory);
	
	public ExamHistory getUserExamHistoryByHistId(int histId);
	
	public List<ExamHistory> getUserExamHistoryListByUserId(int userId,Page<ExamHistory> page);
	
	public void deleteExamPaper(int id);
}
