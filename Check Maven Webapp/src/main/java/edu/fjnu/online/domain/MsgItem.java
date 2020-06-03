package edu.fjnu.online.domain;

public class MsgItem {
	private String remark;
    private String errorNo;
    private String errorInfo;
    
    private String quesAns;
    private String quesExp;
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getErrorNo() {
		return errorNo;
	}
	public void setErrorNo(String errorNo) {
		this.errorNo = errorNo;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	
	public String getQuesAns() {
		return quesAns;
	}
	public void setQuesAns(String quesAns) {
		this.quesAns = quesAns;
	}
	
	public String getQuesExp() {
		return quesExp;
	}
	public void setQuesExp(String quesExp) {
		this.quesExp = quesExp;
	}
    
    
}
