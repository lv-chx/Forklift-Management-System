package cn.zwqh.springboot.model;

import java.io.Serializable;

public class UserEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5237730257103305078L;
	
	private String tel;
	private String userName;
	private String employer;
	private String workNo;
	private String haveCertificate;
	private String carType;
	private String remakes;
	private String auditStatus;
	private String manageRemakes;
	private String startDate;
	private String endDate;
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getEmployer() {
		return employer;
	}
	public void setEmployer(String employer) {
		this.employer = employer;
	}
	public String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	public String getHaveCertificate() {
		return haveCertificate;
	}
	public void setHaveCertificate(String haveCertificate) {
		this.haveCertificate = haveCertificate;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getRemakes() {
		return remakes;
	}
	public void setRemakes(String remakes) {
		this.remakes = remakes;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getManageRemakes() {
		return manageRemakes;
	}
	public void setManageRemakes(String manageRemakes) {
		this.manageRemakes = manageRemakes;
	}
	
}
