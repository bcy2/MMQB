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
	/**用户账号*/
	private String userId;
	/**用户昵称*/
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
		this.userPwd = encryptData(userPwd);
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
	
	
	public User(String userId, String userName, String userPwd, String grade,
			int userType, int userState, String email, String telephone,
			String address, String remark) {
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
	}
	
	/**
	 * Encodes a string 2 MD5
	 * 
	 * @param str String to encode
	 * @return Encoded String
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptData(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
}
