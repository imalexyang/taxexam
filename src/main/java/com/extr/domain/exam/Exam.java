package com.extr.domain.exam;

import java.io.Serializable;
import java.util.Date;

public class Exam implements Serializable {

	private static final long serialVersionUID = -1595051130848974880L;
	private int id;
	private String name;
	private Date start_time;
	private Date end_time;
	/*private boolean is_fix_location;*/
	private Date create_time;
	private int exam_paper_id;
	private int user_id;
	private String creator;
	
	/**
	 * 刘攀添加location_id,group_id,exam_paper_id和get set 方法
	 * 20130705 如果不需要先注释
	 */
	/*private int location_id;
	private int group_id;*/

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	/*public int getLocation_id() {
		return location_id;
	}

	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}*/
	
	public int getExam_paper_id() {
		return exam_paper_id;
	}

	public void setExam_paper_id(int exam_paper_id) {
		this.exam_paper_id = exam_paper_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	/*public boolean isIs_fix_location() {
		return is_fix_location;
	}

	public void setIs_fix_location(boolean is_fix_location) {
		this.is_fix_location = is_fix_location;
	}*/

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

}
