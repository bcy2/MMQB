/**
 * 
 */
package edu.fjnu.online.domain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用户表
 * @author hspcadmin
 * @CreateDate:	2017-3-11
 */
public class User
{
	/**用户账号, unique string, in db is username*/
	private String userId;
	/**用户昵称for display, by default userId, in db is nickname*/
	private String userName;
	/**用户密码*/
	private String userPwd;
	/**年级*/
	private String grade;
	/**账户类型（0：学生，1：老师，2：管理员）*/
	private int userType;
	/**账户状态（0：待审核，1：在用，2：注销）*/
	private int userState;
	/**邮箱*/
	private String email;
	/**联系电话*/
	private String telephone;
	/**联系地址*/
	private String address;
	/**备注*/
	private String remark;
	
//	Newly added for all roles
	private int userIdGlobal;
	
	private String userFirstName;
	
	private String userLastName;
	
	private String authority;
	
//	Newly added for students
	private String parentName;
	
	private String parentEmail;
	
	private String parentPwd;
	
	private int rewardPoints;//game_time in sql
	
	private String curriculum;
	
	
	public int getUserIdGlobal() {
		return userIdGlobal;
	}
	public void setUserIdGlobal(int userIdGlobal) {
		this.userIdGlobal = userIdGlobal;
	}
	public String getUserFirstName() {
		return userFirstName;
	}
	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}
	public String getUserLastName() {
		return userLastName;
	}
	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getParentEmail() {
		return parentEmail;
	}
	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}
	public String getParentPwd() {
		return parentPwd;
	}
	public void setParentPwd(String parentPwd) {
		this.parentPwd = parentPwd;
	}
	public int getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
	public String getCurriculum() {
		return curriculum;
	}
	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
//		this.userPwd = MD5Util.encryptData(userPwd);
		this.userPwd = userPwd;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public int getUserState() {
		return userState;
	}
	public void setUserState(int userState) {
		this.userState = userState;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public User() {
		
	}
	
	public User(String userId, String userName, String userPwd, String grade, int userType, int userState, String email,
			String telephone, String address, String remark, int userIdGlobal, String userFirstName,
			String userLastName, String authority, String parentName, String parentEmail, String parentPwd,
			int rewardPoints, String curriculumId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userPwd = userPwd;
		this.grade = grade;
		this.userType = userType;
		this.userState = userState;
		this.email = email;
		this.telephone = telephone;
		this.address = address;
		this.remark = remark;
		this.userIdGlobal = userIdGlobal;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.authority = authority;
		this.parentName = parentName;
		this.parentEmail = parentEmail;
		this.parentPwd = parentPwd;
		this.rewardPoints = rewardPoints;
		this.curriculum = curriculum;
	}
	/**
	 * Encodes a string 2 MD5
	 * 
	 * @param str String to encode
	 * @return Encoded String
	 * @throws NoSuchAlgorithmException
	 */
//	public static String encryptData(String str) {
//		if (str == null || str.length() == 0) {
//			throw new IllegalArgumentException("String to encript cannot be null or zero length");
//		}
//		StringBuffer hexString = new StringBuffer();
//		try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			md.update(str.getBytes());
//			byte[] hash = md.digest();
//			for (int i = 0; i < hash.length; i++) {
//				if ((0xff & hash[i]) < 0x10) {
//					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
//				} else {
//					hexString.append(Integer.toHexString(0xFF & hash[i]));
//				}
//			}
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
//		return hexString.toString();
//	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
}
