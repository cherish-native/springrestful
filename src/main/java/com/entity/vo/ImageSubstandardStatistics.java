package com.entity.vo;

public class ImageSubstandardStatistics {
    /**
     * 采集部门代码
     */
    private String departCode;
    /**
     * 采集部门名称
     */
    private String departName;
    /**
     *  采集不达标率
     */
    private int[] substandardPercent; //%
    
	public String getDepartCode() {
		return departCode;
	}
	public void setDepartCode(String departCode) {
		this.departCode = departCode;
	}
	public String getDepartName() {
		return departName;
	}
	public void setDepartName(String departName) {
		this.departName = departName;
	}
	public int[] getSubstandardPercent() {
		return substandardPercent;
	}
	public void setSubstandardPercent(int[] substandardPercent) {
		this.substandardPercent = substandardPercent;
	}

}
