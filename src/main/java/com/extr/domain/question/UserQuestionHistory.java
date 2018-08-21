package com.extr.domain.question;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserQuestionHistory {

	private int userQuestionHistoryId;
	private int userId;
	private Date modifyTime;
	private String historyStr;
	/**
	 * 最外面一层map记录正确和错误，0错误，1正确
	 * 暂时只使用0，1方便后期扩展
	 */
	private Map<Integer,Map<Integer,QuestionHistory>> history;
	public int getUserQuestionHistoryId() {
		return userQuestionHistoryId;
	}
	public void setUserQuestionHistoryId(int userQuestionHistoryId) {
		this.userQuestionHistoryId = userQuestionHistoryId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getHistoryStr() {
		return historyStr;
	}
	public void setHistoryStr(String historyStr) {
		this.historyStr = historyStr;
	}
	public Map<Integer, Map<Integer, QuestionHistory>> getHistory() {
		return history;
	}
	public void setHistory(Map<Integer, Map<Integer, QuestionHistory>> history) {
		this.history = history;
	}
	
	
}
