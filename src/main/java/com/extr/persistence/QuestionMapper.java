package com.extr.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.extr.controller.domain.QuestionFilter;
import com.extr.controller.domain.QuestionImproveResult;
import com.extr.controller.domain.QuestionQueryResult;
import com.extr.domain.question.Field;
import com.extr.domain.question.KnowledgePoint;
import com.extr.domain.question.Question;
import com.extr.domain.question.QuestionStruts;
import com.extr.domain.question.QuestionTag;
import com.extr.domain.question.QuestionType;
import com.extr.domain.question.Tag;
import com.extr.domain.question.UserQuestionHistory;
import com.extr.util.Page;

/**
 * @author Ocelot
 * @date 2014年6月8日 下午8:32:33
 */
public interface QuestionMapper {

	List<Field> getAllField(@Param("page") Page<Field> page);

	List<KnowledgePoint> getKnowledgePointByFieldId(
			@Param("fieldId") int fieldId,
			@Param("page") Page<KnowledgePoint> page);

	List<QuestionQueryResult> getQuestionAnalysisListByIdList(
			@Param("array") List<Integer> idList);

	List<QuestionQueryResult> getQuestionAnalysisListByPointIdAndTypeId(
			@Param("typeId") int typeId, @Param("pointId") int pointId);

	List<Question> getQuestionListByIdListNew(
			@Param("array") List<Integer> idList);

	List<Question> getQuestionListByQuestionTypeIdAndReferenceId(
			@Param("questionTypeId") int questionTypeId,
			@Param("fieldId") int fieldId, @Param("limitNum") int limitNum);

	List<QuestionType> getQuestionTypeList();

	/**
	 * 按知识点获取试题
	 * 
	 * @param idList
	 * @return
	 */
	List<QuestionStruts> getQuestionListByPointId(
			@Param("array") List<Integer> idList);

	List<Question> getQuestionList(@Param("filter") QuestionFilter filter,
			@Param("page") Page<Question> page);

	Question getQuestionByQuestionId(@Param("questionId") int questionId);

	/**
	 * 获取题目的知识点，知识点名由专业名fieldname和知识点pointname名拼接
	 * 
	 * @param questionId
	 * @return
	 */
	List<KnowledgePoint> getQuestionKnowledgePointListByQuestionId(
			@Param("questionId") int questionId);

	public void addQuestionKnowledgePoint(@Param("questionId") int questionId,
			@Param("pointId") int pointId) throws Exception;

	public void insertQuestion(Question question) throws Exception;

	public void deleteQuestionByQuestionId(@Param("questionId") int questionId)
			throws Exception;

	public void deleteQuestionPointByQuestionId(
			@Param("questionId") int questionId) throws Exception;

	public KnowledgePoint getKnowledgePointByName(
			@Param("pointName") String pointName,
			@Param("fieldName") String fieldName);

	public KnowledgePoint getKnowledgePointByPointNameAndFieldId(
			@Param("pointName") String pointName, @Param("fieldId") int fieldId);

	/**
	 * 添加学员练习试题的记录
	 * 
	 * @param userQuestionHistory
	 */
	public void addUserQuestionHistory(UserQuestionHistory userQuestionHistory);

	/**
	 * 获取学院练习试题的记录
	 * 
	 * @param userId
	 * @return
	 */
	public UserQuestionHistory getUserQuestionHistoryByUserId(int userId);

	/**
	 * 更新学员练习试题记录
	 * 
	 * @param userQuestionHistory
	 */
	public void updateUserQuestionHistory(
			UserQuestionHistory userQuestionHistory);

	/**
	 * 强化练习获取分类信息
	 * 
	 * @return
	 */
	public List<QuestionImproveResult> getQuestionImproveResultByQuestionPointIdList(
			@Param("array") List<Integer> questionPointIdList);

	public List<QuestionQueryResult> getQuestionAnalysisListByFieldIdList(
			@Param("array") List<Integer> fieldIdList,
			@Param("typeIdList") List<Integer> questionTypeIdList);

	public void addField(Field field);

	public void addKnowledgePoint(KnowledgePoint point);

	public void deleteFieldByIdList(@Param("array") List<Integer> idList);

	public void deleteKnowledgePointByIdList(
			@Param("array") List<Integer> idList);

	/**
	 * 获取一个最小的，具有point的fieldid，用于首页取默认field
	 * 
	 * @return
	 */
	public Integer getMinFieldId();

	/**
	 * 获取tag列表，包含所有公有的或者自己私有的
	 * 
	 * @param userId
	 * @param page
	 * @return
	 */
	public List<Tag> getTagByUserId(@Param("userId") int userId,
			@Param("page") Page<Tag> page);

	/**
	 * 增加一个标签
	 * 
	 * @param tag
	 */
	public void addTag(Tag tag);

	/**
	 * 获取试题的tag
	 * @param questionId
	 * @param userId
	 * @param page
	 * @return
	 */
	public List<QuestionTag> getQuestionTagByQuestionIdAndUserId(
			@Param("questionId") int questionId, @Param("userId") int userId,
			@Param("page") Page<QuestionTag> page);
	
	/**
	 * 给题目打标签
	 */
	public void addQuestionTag(@Param("array") List<QuestionTag> array);
	
	public void deleteQuestionTag(@Param("questionId") int questionId,@Param("userId") int userId,@Param("array") List<QuestionTag> array);
}
