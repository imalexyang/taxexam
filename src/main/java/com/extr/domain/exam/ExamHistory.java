package com.extr.domain.exam;

import java.util.Date;

public class ExamHistory {

	private int histId;
	private int userId;
	private int examPaperId;
	private String content;
	private Date createTime;
	private String answerSheet;
	private int duration;
	private String paperName;
	private float pointGet;
	public Date submitTime;
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public float getPointGet() {
		return pointGet;
	}
	public void setPointGet(float pointGet) {
		this.pointGet = pointGet;
	}
	public String getPaperName() {
		return paperName;
	}
	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getHistId() {
		return histId;
	}
	public void setHistId(int histId) {
		this.histId = histId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getExamPaperId() {
		return examPaperId;
	}
	public void setExamPaperId(int examPaperId) {
		this.examPaperId = examPaperId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAnswerSheet() {
		return answerSheet;
	}
	public void setAnswerSheet(String answerSheet) {
		this.answerSheet = answerSheet;
	}
	
}
