package edu.fjnu.online.controller.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.fjnu.online.domain.Course;
import edu.fjnu.online.domain.ErrorBook;
import edu.fjnu.online.domain.Grade;
import edu.fjnu.online.domain.MsgItem;
import edu.fjnu.online.domain.Paper;
import edu.fjnu.online.domain.Question;
import edu.fjnu.online.domain.User;
import edu.fjnu.online.service.CourseService;
import edu.fjnu.online.service.GradeService;
import edu.fjnu.online.service.PaperService;
import edu.fjnu.online.service.QuestionService;
import edu.fjnu.online.service.UserService;
import edu.fjnu.online.service.ErrorBookService;
import edu.fjnu.online.util.MD5Util;
import edu.fjnu.online.util.QuestionStuffs;
import jxl.read.biff.Record;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	@Autowired
	PaperService paperService;
	@Autowired
	ErrorBookService bookService;
	
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
		List<Course> curricuLumList = courseService.findActive(new Course());
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
		String userParentName = user.getParentName();
		String userPwd = user.getUserPwd();
		String userGrade = user.getGrade();
		String userEmail = user.getEmail();
		String userTel = user.getTelephone();
		String userCurriculum = user.getCurriculum();
		if (userId == null || userFirstName == null || userLastName == null || 
				userParentName == null || userPwd == null || userGrade == null || 
				userEmail == null || userTel == null || userCurriculum == null) {
			return "/user/badReg.jsp";
		}
		
		userId = userId.trim();//username
		userFirstName = userFirstName.trim();
		userLastName = userLastName.trim();
		userParentName = userParentName.trim();
		userPwd = userPwd.trim();
		userGrade = userGrade.trim();
		userEmail = userEmail.trim();
		userTel = userTel.trim();
		userCurriculum = userCurriculum.trim();
		
		String emailPatternString = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
		String numPattern = "^[0-9]{8}$";
		
		if (userId.length()<6 || userFirstName.equals("") || userLastName.equals("") || 
				userParentName.equals("") || userPwd.length()<6 || userGrade.equals("") || 
				!userEmail.matches(emailPatternString) || !userTel.matches(numPattern) || userCurriculum.equals("")) {
			return "/user/badReg.jsp";
		}
		
		user.setUserId(userId);//username
		user.setUserFirstName(userFirstName);
		user.setUserLastName(userLastName);
		user.setParentName(userParentName);
		user.setUserName(userLastName.toUpperCase() + " " + userFirstName);
		user.setUserPwd(userPwd);
		user.setGrade(userGrade);
		user.setEmail(userEmail);
		user.setTelephone(userTel);
		user.setCurriculum(userCurriculum);
		
		user.setUserState(0);
		user.setUserType(0);
		user.setRewardPoints(0);
		user.setAuthority("{\"sendEmail\": true}");
		user.setParentPwd("123456");//default parent pwd
		
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
		
		String userNickName = user.getUserName();
		String userParentName = user.getParentName();
		String parentEmail = user.getParentEmail(); 
		String userEmail = user.getEmail();
		String userTel = user.getTelephone();
		
		if (userNickName == null || userParentName == null || parentEmail == null || 
				userEmail == null || userTel == null) {
			return "redirect:/toUserInfo.action";
		}
		
		userNickName = userNickName.trim();
		userParentName = userParentName.trim();
		parentEmail = parentEmail.trim();
		userEmail = userEmail.trim();
		userTel = userTel.trim();
		
		String emailPatternString = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
		String numPattern = "^[0-9]{8}$";
		
		if (userNickName.equals("") || !userEmail.matches(emailPatternString) || !userTel.matches(numPattern) || userParentName.equals("")) {
			return "redirect:/toUserInfo.action";
		}
		
		if (!parentEmail.equals("")) {
			if(!parentEmail.matches(emailPatternString)) {
				return "redirect:/toUserInfo.action";
			}
		}
		
		if(!newPwd.equals("")){//password length >= 6
			if (newPwd.trim().length() < 6) {
				return "redirect:/toUserInfo.action";
			}
			//密码加密
			newPwd = MD5Util.getData(newPwd);
			user.setUserPwd(newPwd);
		}
		
		user.setUserName(userNickName);
		user.setParentName(userParentName);
		user.setParentEmail(parentEmail);
		user.setEmail(userEmail);
		user.setTelephone(userTel);
		user.setRewardPoints(user.getRewardPoints());
		
		userService.update(user);
		user = userService.get(user.getUserId());
		if(session.getAttribute("user")== null){
			session.setAttribute("user", userService.getStu(user));
		}
		session.setAttribute("userName", user.getUserName());
		return "redirect:/toIndex.action";			
	}
	
	@RequestMapping("/toUserStatistics.action")
	public String toUserStatistics(User user, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
//		System.out.println("user"+session.getAttribute("user"));//null when logged out
		
		User loginUser = (User) session.getAttribute("user");
		List<Grade> gradeList = gradeService.findActive(new Grade());
		Map<String,String> gradeMap = new HashMap();
		for (Grade grade : gradeList) gradeMap.put(String.valueOf(grade.getGradeId()),grade.getGradeName());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		//==================================
		
		user = userService.getStu(loginUser);
		
		Map curriculumSubtopicMap = new HashMap();
		List<Question> uniqueSubtopics = questionService.findSubtopic(user.getCurriculum());
		for (Question q: uniqueSubtopics) {
			if (!curriculumSubtopicMap.containsKey(q.getGradeId())) {
				Map topicMap = new HashMap();
				curriculumSubtopicMap.put(q.getGradeId(), topicMap);
			}
			Map topicMap = (Map) curriculumSubtopicMap.get(q.getGradeId());
	
			if (!topicMap.containsKey(q.getTopic())) {
				Map subtopicMap = new HashMap();
				topicMap.put(q.getTopic(), subtopicMap);
			}
			Map subtopicMap = (Map) topicMap.get(q.getTopic());
	
			if (!subtopicMap.containsKey(q.getSubtopic())) {
//				Map typeMap = new HashMap();
				subtopicMap.put(q.getSubtopic(), q.getSubtopicId());
			}
//			Map typeMap = (Map) subtopicMap.get(q.getSubtopic());
		}
		
		List<Paper> paperDone = paperService.getUserPaperById(user.getUserId());
		Map map = new HashMap();
		map.put("userId", user.getUserId());
		List<Paper> paperInProgress = paperService.qryInProgressPaper(map);
		List<Paper> paperUndo = paperService.qryUndoPaper(map);
		
//		Map quizDoneMap = new HashMap();
//		for(Paper paper : paperDone){
//			Map quizMap = new HashMap();
//			quizMap.put("userId", user.getUserId());
//			quizMap.put("quizId", paper.getPaperId());
//			
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//			Date quizStartTime = null;
//			try {
//				quizStartTime = formatter.parse(paper.getBeginTime());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Date quizEndTime = null;
//			try {
//				quizEndTime = formatter.parse(paper.getEndTime());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			List<ErrorBook> bookList = bookService.getBookInfoForQuiz(quizMap);
//			
//			for (Iterator iterator = bookList.iterator(); iterator.hasNext();) {
//				ErrorBook errorBook = (ErrorBook) iterator.next();
//				
//				String questionEndTimeString = errorBook.getEndTime();
//				if (questionEndTimeString == null) {
//					iterator.remove();
//					continue;
//				}
//				
//				Date questionEndTime = null;
//				try {
//					questionEndTime = formatter.parse(questionEndTimeString);
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (questionEndTime.before(quizStartTime) || questionEndTime.after(quizEndTime)) {
//					iterator.remove();
//				}
//				
//				errorBook.setUserId(null);
//				errorBook.setUserAnswer(null);
//				Question origQuestion = errorBook.getQuestion();
//				Question abridgedQuestion = new Question();
////				abridgedQuestion.setQuestionId(origQuestion.getQuestionId());
////				abridgedQuestion.setCourseId(origQuestion.getCourseId());
//				abridgedQuestion.setGradeId(origQuestion.getGradeId());
//				abridgedQuestion.setDifficulty(origQuestion.getDifficulty());
//				abridgedQuestion.setTopic(origQuestion.getTopic());
//				abridgedQuestion.setSubtopic(origQuestion.getSubtopic());
//				abridgedQuestion.setSubtopicId(origQuestion.getSubtopicId());
//				errorBook.setQuestion(abridgedQuestion);
//				
//				// set grade name
////				Grade grade = gradeService.get(Integer.valueOf(errorBook.getQuestion().getGradeId()));
////				errorBook.setGradeName(grade.getGradeName());
////				errorBook.setGradeName(errorBook.getQuestion().getGradeId());
//				
//			}
//			
//			List<Object> quizDoneNAccuracy = new ArrayList <Object>();
//			double quizDoneAccuracy = QuestionStuffs.calcAccuracyForQuesSet(bookList);
//			quizDoneNAccuracy.add(quizDoneAccuracy);
//			quizDoneNAccuracy.add(bookList.size());
////			System.out.println(String.format("Accuracy for %s: %f",paper.getPaperName(),quizDoneAccuracy));
//			
//			quizDoneMap.put(paper.getPaperName(), quizDoneNAccuracy);
//		}
//		model.addAttribute("quizDoneMap", quizDoneMap);
		
		List<ErrorBook> allBooksList = bookService.getBookInfo(map);
		
		List<Integer> periodsInDay = new ArrayList<Integer>(Arrays.asList(-1,1,2,7,14,30,90,180));
		model.addAttribute("periodsInDay", periodsInDay.stream().map(String::valueOf).collect(Collectors.toList()));
		
		Map paperMap = new HashMap();
		Map allGradesAccuraciesInPeriod =  new HashMap();
		Map gradeAccuraciesInPeriod =  new HashMap();
		Map topicAccuraciesInPeriod =  new HashMap();
		Map subtopicAccuraciesInPeriod =  new HashMap();
		
		for (Integer integer : periodsInDay) {
			String periodString = String.valueOf(integer);
			
			List<Paper> paperDoneInPeriod = paperDone.stream()
					.filter(record -> QuestionStuffs.quizAfterDate(record, integer))
					.collect(Collectors.toList());
			List<Paper> paperInProgressInPeriod = paperInProgress.stream()
					.filter(record -> QuestionStuffs.quizAfterDate(record, integer))
					.collect(Collectors.toList());
			List<Paper> paperUndoInPeriod = paperUndo.stream()
					.filter(record -> QuestionStuffs.quizAfterDate(record, integer))
					.collect(Collectors.toList());
			
			List<Object> paperNumList = new ArrayList<Object>();
			paperNumList.add(paperDoneInPeriod.size());
			paperNumList.add(paperInProgressInPeriod.size());
			paperNumList.add(paperUndoInPeriod.size());
			paperMap.put(periodString, paperNumList);
			
			List<ErrorBook> bookListInPeriod = allBooksList.stream()
											.filter(record -> QuestionStuffs.quesAfterDate(record, integer))
											.collect(Collectors.toList());
			
			double allGradesAccuracy = QuestionStuffs.calcAccuracyForQuesSet(bookListInPeriod);
			int allGradesQuesNum = bookListInPeriod.size();
			List<Object> allGradesAccuracyNQuesNum = new ArrayList<Object>();
			allGradesAccuracyNQuesNum.add(allGradesAccuracy);
			allGradesAccuracyNQuesNum.add(allGradesQuesNum);
			allGradesAccuraciesInPeriod.put(periodString, allGradesAccuracyNQuesNum);
			
			Map gradeAccuracies = QuestionStuffs.calcAccuracyForAllGrades(curriculumSubtopicMap, bookListInPeriod, gradeMap);
			gradeAccuraciesInPeriod.put(periodString, gradeAccuracies);
			
			Map topicAccuracies = QuestionStuffs.calcAccuracyForAllTopics(curriculumSubtopicMap, bookListInPeriod);
			topicAccuraciesInPeriod.put(periodString, topicAccuracies);
			
			Map subtopicAccuracies = QuestionStuffs.calcAccuracyForAllSubtopics(curriculumSubtopicMap, bookListInPeriod);
			subtopicAccuraciesInPeriod.put(periodString, subtopicAccuracies);
		}
		
		System.out.println("===============\nPapers:");
		System.out.println(paperMap);
		model.addAttribute("paperMap", paperMap);
		
		System.out.println("===============\nOverall:");
		System.out.println(allGradesAccuraciesInPeriod);
		model.addAttribute("allGradesAccuraciesInPeriod", allGradesAccuraciesInPeriod);
		
//		Map gradeAccuracies = QuestionStuffs.calcAccuracyForAllGrades(curriculumSubtopicMap, allBooksList, gradeMap, 1);
		System.out.println("===============\nGrade:");
		System.out.println(gradeAccuraciesInPeriod);
		model.addAttribute("gradeAccuraciesInPeriod", gradeAccuraciesInPeriod);
		
//		Map topicAccuracies = QuestionStuffs.calcAccuracyForAllTopics(curriculumSubtopicMap, allBooksList,1);
		System.out.println("===============\nTopic:");
		System.out.println(topicAccuraciesInPeriod);
		model.addAttribute("topicAccuraciesInPeriod", topicAccuraciesInPeriod);
		
//		Map subtopicAccuracies = QuestionStuffs.calcAccuracyForAllSubtopics(curriculumSubtopicMap, allBooksList,1);
		System.out.println("===============\nSubtopic:");
		System.out.println(subtopicAccuraciesInPeriod);
		model.addAttribute("subtopicAccuraciesInPeriod", subtopicAccuraciesInPeriod);

//		Grade grade = gradeService.get(Integer.parseInt(user.getGrade()));
//		user.setGrade(grade.getGradeName());
//		model.addAttribute("userGradeName", grade.getGradeName());
		model.addAttribute("user", user);
		return "/user/userStatistics.jsp";			
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
