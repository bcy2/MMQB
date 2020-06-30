package edu.fjnu.online.controller.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.fjnu.online.domain.Course;
import edu.fjnu.online.domain.Grade;
import edu.fjnu.online.domain.MsgItem;
import edu.fjnu.online.domain.Question;
import edu.fjnu.online.domain.User;
import edu.fjnu.online.service.CourseService;
import edu.fjnu.online.service.GradeService;
import edu.fjnu.online.service.QuestionService;
import edu.fjnu.online.service.UserService;
import edu.fjnu.online.util.MD5Util;
import jnr.ffi.Struct.int16_t;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.*;

@Controller
public class StuController {
	@Autowired
	UserService userService;
	@Autowired
	GradeService gradeService;
	@Autowired
	CourseService courseService;
	@Autowired
	QuestionService questionService;
	
	public void showAllAttributes(HttpSession session){
		Enumeration<String> attributes = session.getAttributeNames();
		System.out.println("All attributes:");
		while (attributes.hasMoreElements()) {
		    String attribute = (String) attributes.nextElement();
		    System.out.println(attribute+" : "+session.getAttribute(attribute));
		}
		System.out.println("End.");
	}
	
	@RequestMapping("/testUser.action")
	public String toNewDBTest(User user, Model model, HttpSession session){
		List<User> dataList = userService.find(user);
		model.addAttribute("dataList", dataList);
		return "/user/testUser.jsp";			
	}
	
//	public void getUniqueEntry(Question q, Map curriculumMap) {
//		if (!curriculumMap.containsKey(q.getCourseId())) {
//			Map gradeMap = new HashMap();
//			curriculumMap.put(q.getCourseId(),gradeMap);
//		}
//		Map gradeMap = (Map) curriculumMap.get(q.getCourseId());
//
//		if (!gradeMap.containsKey(q.getGradeId())) {
//			Map topicMap = new HashMap();
//			gradeMap.put(q.getGradeId(), topicMap);
//		}
//		Map topicMap = (Map) gradeMap.get(q.getGradeId());
//
//		if (!topicMap.containsKey(q.getTopic())) {
//			Map subtopicMap = new HashMap();
//			topicMap.put(q.getTopic(), subtopicMap);
//		}
//		Map subtopicMap = (Map) topicMap.get(q.getTopic());
//
//		if (!subtopicMap.containsKey(q.getSubtopic())) {
//			Map typeMap = new HashMap();
//			subtopicMap.put(q.getSubtopic(), typeMap);
//		}
//		Map typeMap = (Map) subtopicMap.get(q.getSubtopic());
//
//		if (!typeMap.containsKey(q.getTypeId())) {
//			//
//			typeMap.put(q.getTypeId(), q.getQuestionId());
//
//		}
//		return;
//	}
	
	@RequestMapping("/testQuesType.action")
	public String toQuesTypeTest(User user, Model model, HttpSession session){
//		List<Question> dataList = questionService.find(new Question());
//		Map curriculumMap = new HashMap();
//		for (Question q:dataList) {
//			getUniqueEntry(q, curriculumMap);
//		}
//		iterate(curriculumMap,0);
//		
//		model.addAttribute("dataList", dataList);
		
		List<Question> subtopic = questionService.findSubtopic("1");
		model.addAttribute("subtopic", subtopic);
		return "/user/testQuesType.jsp";			
	}
	
	//跳转到前台登录页面
	@RequestMapping("/toLogin.action")
	public String toUserLogin(User user, Model model, HttpSession session){
		if(session.getAttribute("userName")!= null){
			return "/user/index.jsp";
		}
		if(session.getAttribute("user")== null){
			session.setAttribute("user", userService.get(user.getUserId()));
		}
		List<User> dataList = userService.find(user);
		model.addAttribute("dataList", dataList);
		return "/user/login.jsp";			
	}
		
	/**
	 * 前台用户登录
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/toIndex.action")
	public String toIndex(User user, Model model, HttpSession session){
		if(session.getAttribute("user")!= null){
			return "/user/index.jsp";
		}else{
			return "redirect:/toLogin.action";
		}
	}
	
	/**
	 * 用户账号密码检查
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/checkPwd.action")
	@ResponseBody
	public MsgItem checkPwd(User user, Model model, HttpSession session){
		MsgItem item = new MsgItem();
		User loginUser = userService.login(user);
		if(loginUser!=null && loginUser.getUserType() ==0){
			if(loginUser.getUserState()==0 ){
				item.setErrorNo("1");
				item.setErrorInfo("Account not verified!");
			}else{
				item.setErrorNo("0");
				item.setErrorInfo("login successful!");
				session.setAttribute("userName", loginUser.getUserName());
				session.setAttribute("user", loginUser);
			}
		}else{
			item.setErrorNo("1");
			item.setErrorInfo("Wrong username / password!");
		}
		return item;
	}
	
	@RequestMapping("/toRegistPage.action")
	public String toRegistPage(Model model, HttpSession session){
		List<Grade> gradeList = gradeService.findActive(new Grade());
		model.addAttribute("grade", gradeList);
		List<Course> curricuLumList = courseService.find(new Course());
		model.addAttribute("curriculum", curricuLumList);
		return "/user/regist.jsp";
	}
	
	/**
	 * 添加用户信息
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("/addUserInfo.action")
	public String addUserInfo(User user, Model model, HttpSession session){
		String userId = user.getUserId();//username
		String userFirstName = user.getUserFirstName();
		String userLastName = user.getUserLastName();
		user.setUserName(userLastName.toUpperCase() + " " + userFirstName);
		String userPwd = user.getUserPwd();
		String userGrade = user.getGrade();
		String userEmail = user.getEmail();
		String userTel = user.getTelephone();
//		String userAddr = user.getAddress();
		String userCurriculum = user.getCurriculum();
		
		user.setUserState(0);
		user.setUserType(0);
		user.setRewardPoints(0);
		user.setAuthority("{\"sendEmail\": true}");
		
		String emailPatternString = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
		String numPattern = "^[0-9]{8}$";
		
		if (userId.length()<6 || userFirstName.equals("") || userLastName.equals("") || userPwd.length()<6 || userGrade.equals("") || !userEmail.matches(emailPatternString) || !userTel.matches(numPattern) || userCurriculum.equals("")) {
			return "/user/badReg.jsp";
		}
		userService.insert(user);
		return "redirect:/toLogin.action";
	}
	
	//跳转到前台登录页面
	@RequestMapping("/toUserInfo.action")
	public String toUserInfo(User user, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
//		System.out.println("user"+session.getAttribute("user"));//null when logged out
		
		User loginUser = (User) session.getAttribute("user");
		user = userService.getStu(loginUser);
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);

//		Grade grade = gradeService.get(Integer.parseInt(user.getGrade()));
//		user.setGrade(grade.getGradeName());
//		model.addAttribute("userGradeName", grade.getGradeName());
		model.addAttribute("user", user);
		return "/user/userinfo.jsp";			
	}
	
	/**
	 * 更新学生信息
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/updateUserInfo.action")
	public String updateUserInfo(String newPwd,User user, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		if(newPwd!= null && newPwd.trim().length() >= 6){//password length >= 6
			//密码加密
			newPwd = MD5Util.getData(newPwd);
			user.setUserPwd(newPwd);
		}
		userService.update(user);
		user = userService.get(user.getUserId());
		if(session.getAttribute("user")== null){
			session.setAttribute("user", userService.getStu(user));
		}
		session.setAttribute("userName", user.getUserName());
		return "redirect:/toIndex.action";			
	}
	
	// 跳转到登录页面
	 @RequestMapping("/user/exitSys.action")
	public String exitSystem(User user, Model model, HttpSession session){
		if(session.getAttribute("user")!= null){
//			System.out.print(model.toString());
//			Enumeration<String> haha = session.getAttributeNames();
//			while (haha.hasMoreElements()){
//		         System.out.print(haha.nextElement());
//		         System.out.print(":");
//		         System.out.println(session.getAttribute(haha.nextElement()));
//		      }
			session.removeAttribute("userName");
			session.removeAttribute("user");
//			return "/user/login.jsp";
		}
//		newly added
		session.invalidate();
		return "redirect:/toLogin.action";			
	}
	
	//跳转到前台登录页面
	@RequestMapping("/toAbout.action")
	public String toAbout(User user, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		User loginUser = (User) session.getAttribute("user");
		model.addAttribute("user", loginUser);
		return "/user/about.jsp";			
	}
}
