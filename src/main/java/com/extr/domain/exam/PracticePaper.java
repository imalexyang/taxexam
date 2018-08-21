package com.extr.domain.exam;

import java.io.Serializable;
import java.util.Date;

public class PracticePaper extends ExamPaper implements Serializable {

	private static final long serialVersionUID = 2687135464278770588L;

	private int userId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}
