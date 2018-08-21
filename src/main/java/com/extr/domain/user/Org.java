package com.extr.domain.user;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Org implements Serializable {
	private static final long serialVersionUID = 2866441053387084437L;
	private int id;
	private String name;
	private String province;
	private String address;	
	private String isall;
	private String iswin;
	private String winlevel;
	private Integer chance;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIsall() {
		return isall;
	}
	public void setIsall(String isall) {
		this.isall = isall;
	}
	public String getIswin() {
		return iswin;
	}
	public void setIswin(String iswin) {
		this.iswin = iswin;
	}
	public String getWinlevel() {
		return winlevel;
	}
	public void setWinlevel(String winlevel) {
		this.winlevel = winlevel;
	}
	public Integer getChance() {
		return chance;
	}
	public void setChance(Integer chance) {
		this.chance = chance;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
}
