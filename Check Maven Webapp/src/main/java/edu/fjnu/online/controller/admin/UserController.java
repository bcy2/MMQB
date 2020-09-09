package edu.fjnu.online.controller.admin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.record.UserSViewEnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;

import edu.fjnu.online.controller.BaseController;
import edu.fjnu.online.domain.Course;
import edu.fjnu.online.domain.ErrorBook;
import edu.fjnu.online.domain.Grade;
import edu.fjnu.online.domain.MsgItem;
import edu.fjnu.online.domain.Paper;
import edu.fjnu.online.domain.Question;
import edu.fjnu.online.domain.User;
import edu.fjnu.online.service.CourseService;
import edu.fjnu.online.service.ErrorBookService;
import edu.fjnu.online.service.GradeService;
import edu.fjnu.online.service.PaperService;
import edu.fjnu.online.service.QuestionService;
import edu.fjnu.online.service.UserService;
import edu.fjnu.online.util.QuestionStuffs;
/**
 * 用户管理
 * @author hspcadmin
 *
 */
@Controller
public class UserController extends BaseController{

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
	
	ObjectMapper mapper = new ObjectMapper();
	
	//跳转到登录页面
	@RequestMapping("/admin/login.action")
	public String toLoin(User user, Model model, HttpSession session){
		if(session.getAttribute("userName")!= null){
			return "/admin/index.jsp";
		}
		List<User> dataList = userService.find(user);
		model.addAttribute("dataList", dataList);
		return "/admin/login.jsp";			
	}
	
	@RequestMapping("/admin/userLogin.action")
	public String checkUser(User user, Model model, HttpSession session){
		User loginUser = userService.login(user);
		
		if(session.getAttribute("userName")!= null){
			return "/admin/index.jsp";
		}
		
		if(loginUser!=null && loginUser.getUserType() == 2){
			session.setAttribute("userName", loginUser.getUserName());
			session.setAttribute("user", loginUser);
			return "redirect:/admin/toIndex.action";
		}else{
			model.addAttribute("message", "Wrong username / password!");
			return "/admin/login.jsp";
		}
	}
	
	/**
	 * 后台用户登录
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/toIndex.action")
	public String toIndex(User user, Model model, HttpSession session){
		if(session.getAttribute("user")!= null){
			return "/admin/index.jsp";
		}else{
			return "redirect:/admin/login.action";
		}
	}
	
	/**
	 * 判断账户信息是否存在
	 * @param name
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/checkAccount.action")
	public String checkAccount(String userId, Model model){
		User userInfo = userService.get(userId);
		if(userInfo!= null){
			model.addAttribute("message", "Account already exists.");
		}else{
			model.addAttribute("message", "<font color='green'>OK!</font>");
		}
		model.addAttribute("userId", userId);
		return "/admin/info-reg.jsp";
	}
	
	/**
	 * ajax验证用户账号是否存在
	 * @param userId
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/userRegist.action")
	@ResponseBody
	public MsgItem userRegist(String userId, Model model, HttpSession session){
		MsgItem msgItem = new MsgItem();
		User user = userService.get(userId);
		if(user!=null){
			msgItem.setErrorNo("1");
			msgItem.setErrorInfo("Account already exists.");
		}else{
			msgItem.setErrorNo("0");
			msgItem.setErrorInfo("<font color='green'>OK!</font>");
//			msgItem.setErrorInfo("验证通过");
		}
		return msgItem;
	} 
	
	//跳转到登录页面
	@RequestMapping("/admin/exitSys.action")
	public String exitSys(User user, Model model, HttpSession session){
		if(session.getAttribute("userName")!= null){
			session.removeAttribute("userName");
			session.removeAttribute("user");	
		}
		session.invalidate();
		return "redirect:/admin/login.action";				
	}
	
	//跳转到题库录入页面
	@RequestMapping(value="/admin/toQueDep.action",method=RequestMethod.POST)
	public String toQueDep(Model model, HttpSession session){
		return "/admin/info-reg.jsp";			
	}
	
	//获取所有的用户信息
	@RequestMapping("/admin/getAllUser.action")
	public String getAllUserInfo(@RequestParam(value="page", defaultValue="1") int page,
			User user, Model model, HttpSession session){
//		List<User> dataList = userService.find(user);
		PageInfo<User> pageInfo = userService.findByPage(user, page, 10);
		List<User> dataList = pageInfo.getList();
		model.addAttribute("dataList", dataList);
		model.addAttribute("pageInfo", pageInfo);
		
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		return "/admin/info-mgt.jsp";			
	}
	
	//获取所有的用户信息
	@RequestMapping("/admin/qryAllUser.action")
	@ResponseBody
	public List<User> qryAllUser(@RequestParam(value="page", defaultValue="1") int page,
			User user, Model model, HttpSession session){
//			List<User> dataList = userService.find(user);
		PageInfo<User> pageInfo = userService.findByPage(user, page, 10);
		List<User> dataList = pageInfo.getList();
		model.addAttribute("dataList", dataList);
		model.addAttribute("pageInfo", pageInfo);
		return dataList;			
	}
	
	/**
	 * 跳转到添加用户信息页面
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/toAddUser.action")
	public String toAddUserInfo(User user, Model model, HttpSession session){
//		List<User> dataList = userService.find(user);
//		model.addAttribute("grade", gradeService.find(new Grade()));
//		model.addAttribute("dataList", dataList);
		
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		return "/admin/info-reg.jsp";			
	}
	
	/**
	 * 添加用户信息
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/addUser.action")
	public String addUser(User user, Model model){
		String userId = user.getUserId();//username
//		String userFirstName = user.getUserFirstName();
//		String userLastName = user.getUserLastName();
		String userNickName = user.getUserName();
		String userParentName = user.getParentName();
		String userPwd = user.getUserPwd();
		String userGrade = user.getGrade();
		String userEmail = user.getEmail();
		String userTel = user.getTelephone();
		String userCurriculum = user.getCurriculum();
		if (userId == null || 
				userParentName == null || userPwd == null || userGrade == null || 
				userEmail == null || userTel == null || userCurriculum == null) {
			System.out.println("Null fields.");
			return "/admin/badReg.jsp";
		}
		
		userId = userId.trim();//username
//		userFirstName = userFirstName.trim();
//		userLastName = userLastName.trim();
		userNickName = userNickName.trim();
		userParentName = userParentName.trim();
		userPwd = userPwd.trim();
		userGrade = userGrade.trim();
		userEmail = userEmail.trim();
		userTel = userTel.trim();
		userCurriculum = userCurriculum.trim();
		
		String emailPatternString = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
		String numPattern = "^[0-9]{8}$";
		
		if (userId.length()<6 || userNickName.equals("") ||
				userParentName.equals("") || userPwd.length()<6 || userGrade.equals("") || 
				!userEmail.matches(emailPatternString) || !userTel.matches(numPattern) || userCurriculum.equals("")) {
			System.out.println("Bad formats.");
			return "/admin/badReg.jsp";
		}
		
		user.setUserId(userId);//username
//		user.setUserFirstName(userFirstName);
//		user.setUserLastName(userLastName);
		user.setParentName(userParentName);
		user.setUserName(userNickName);
		user.setUserPwd(userPwd);
		user.setGrade(userGrade);
		user.setEmail(userEmail);
		user.setTelephone(userTel);
		user.setCurriculum(userCurriculum);
		
		user.setUserState(0);
//		user.setUserType(0);
		user.setRewardPoints(0);
		user.setAuthority("{\"sendEmail\": true}");
		user.setParentPwd("123456");//default parent pwd
		
		userService.insert(user);
		return "redirect:/admin/getAllUser.action";			
	}
	
	/**
	 * 删除用户信息
	 * @param userId	用户账号，删除多个是，id用逗号分隔开
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/deleteUser.action")
	public String deleteUser(String userId, Model model){
		if(userId != null){
			String ids[] = userId.split(",");
			for(int i=0;i<ids.length;i++){
				userService.delete(ids[i]);
			}
		}
		return "redirect:/admin/getAllUser.action";
	} 
	
	/**
	 * 获取所有待审核信息
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/getFindPending.action")
	public String findPending(@RequestParam(value="page", defaultValue="1") int page,User user, Model model){
		PageInfo<User> pageInfo = userService.findPendingByPage(user, page, 10);
		List<User> dataList = pageInfo.getList();
		model.addAttribute("dataList", dataList);
		model.addAttribute("pageInfo", pageInfo);
		
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		return "/admin/info-deal.jsp";
	}
	
	//获取所有的用户信息
	@RequestMapping("/admin/qryFindPending.action")
	@ResponseBody
	public List<User> qryFindPending(@RequestParam(value="page", defaultValue="1") int page,
			User user, Model model, HttpSession session){
//				List<User> dataList = userService.find(user);
		PageInfo<User> pageInfo = userService.findPendingByPage(user, page, 10);
		List<User> dataList = pageInfo.getList();
		model.addAttribute("dataList", dataList);
		model.addAttribute("pageInfo", pageInfo);
		return dataList;			
	}
	
	/**
	 * 用户身份信息审核(通过)
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/passinfo.action")
	public String passUserInfo(User user, Model model){
		User us = new User();
		if(user != null){
			String ids[] = user.getUserId().split(",");
			for(int i=0;i<ids.length;i++){
				us.setUserId(ids[i]);
				us.setUserState(1);
				userService.update(us);
			}
		}
		return "redirect:/admin/getFindPending.action";
	}
	
	/**
	 * 用户身份信息审核(不通过)
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("/admin/failinfo.action")
	public String failUserInfo(User user, Model model){
		User us = new User();
		if(user != null){
			String ids[] = user.getUserId().split(",");
			for(int i=0;i<ids.length;i++){
				us.setUserId(ids[i]);
				us.setUserState(2);
				userService.update(us);
			}
		}
		return "redirect:/admin/getFindPending.action";
	}
	
	/**
	 * 跳转到添加用户信息页面
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/toUpdateUser.action")
	public String toUpdateUser(User user, Model model, HttpSession session){
		String userId = user.getUserId().trim();
		User userInfo = userService.get(userId);
		model.addAttribute("user", userInfo);
		
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		return "/admin/info-upd.jsp";			
	}
	
	/**
	 * 用户个人信息查询(信息审核)
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/toQryUser.action")
	public String toQryUser(User user, Model model, HttpSession session){
		String userId = user.getUserId().trim();
		User userInfo = userService.get(userId);
		model.addAttribute("user", userInfo);
//		model.addAttribute("grade", gradeService.find(new Grade()));
		
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		return "/admin/info-det.jsp";			
	}
	
	/**
	 * 用户个人信息查询(信息审核)
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/toQryUserInfo.action")
	public String toQryUserInfo(User user, Model model, HttpSession session){
		String userId = user.getUserId().trim();
		User userInfo = userService.get(userId);
		model.addAttribute("user", userInfo);
//		model.addAttribute("grade", gradeService.find(new Grade()));
		
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		return "/admin/info-qry.jsp";			
	}
	
	/**
	 * 用户学习数据统计查询
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/toUserStatistics.action")
	public String toUserStatistics(User user, Model model, HttpSession session){
		User loginUser = userService.getStu(user);
		List<Grade> gradeList = gradeService.findActive(new Grade()).stream()
															.filter(item -> item.getCourseId().equals(loginUser.getCurriculum()))
															.collect(Collectors.toList());
		Map<String,String> gradeMap = new HashMap();
		for (Grade grade : gradeList) gradeMap.put(String.valueOf(grade.getGradeId()),grade.getGradeName());
		List<Course> courseList = courseService.find(new Course());
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		
		String gradeJSON = null;
		try {
			gradeJSON = mapper.writeValueAsString(gradeList);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		model.addAttribute("gradeJSON", gradeJSON);
		
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
//			List<Paper> paperInProgressInPeriod = paperInProgress.stream()
//					.filter(record -> QuestionStuffs.quizAfterDate(record, integer))
//					.collect(Collectors.toList());
//			List<Paper> paperUndoInPeriod = paperUndo.stream()
//					.filter(record -> QuestionStuffs.quizAfterDate(record, integer))
//					.collect(Collectors.toList());
			
			List<Object> paperNumList = new ArrayList<Object>();
			paperNumList.add(paperDoneInPeriod.size());
//			paperNumList.add(paperInProgressInPeriod.size());
			paperNumList.add(paperInProgress.size());
//			paperNumList.add(paperUndoInPeriod.size());
			paperNumList.add(paperUndo.size());
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
		
//		System.out.println("===============\nPapers:");
//		System.out.println(paperMap);
		model.addAttribute("paperMap", paperMap);
		
//		System.out.println("===============\nOverall:");
//		System.out.println(allGradesAccuraciesInPeriod);
		model.addAttribute("allGradesAccuraciesInPeriod", allGradesAccuraciesInPeriod);
		String allGradesJSON = null;
		try {
			allGradesJSON = mapper.writeValueAsString(allGradesAccuraciesInPeriod);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("allGradesJSON", allGradesJSON);
		
//		Map gradeAccuracies = QuestionStuffs.calcAccuracyForAllGrades(curriculumSubtopicMap, allBooksList, gradeMap, 1);
//		System.out.println("===============\nGrade:");
//		System.out.println(gradeAccuraciesInPeriod);
		model.addAttribute("gradeAccuraciesInPeriod", gradeAccuraciesInPeriod);
		String gradeAccuraciesJSON = null;
		try {
			gradeAccuraciesJSON = mapper.writeValueAsString(gradeAccuraciesInPeriod);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("gradeAccuraciesJSON", gradeAccuraciesJSON);
		
//		Map topicAccuracies = QuestionStuffs.calcAccuracyForAllTopics(curriculumSubtopicMap, allBooksList,1);
//		System.out.println("===============\nTopic:");
//		System.out.println(topicAccuraciesInPeriod);
		model.addAttribute("topicAccuraciesInPeriod", topicAccuraciesInPeriod);
		
//		Map subtopicAccuracies = QuestionStuffs.calcAccuracyForAllSubtopics(curriculumSubtopicMap, allBooksList,1);
//		System.out.println("===============\nSubtopic:");
//		System.out.println(subtopicAccuraciesInPeriod);
		model.addAttribute("subtopicAccuraciesInPeriod", subtopicAccuraciesInPeriod);

//		Grade grade = gradeService.get(Integer.parseInt(user.getGrade()));
//		user.setGrade(grade.getGradeName());
//		model.addAttribute("userGradeName", grade.getGradeName());
		model.addAttribute("user", user);
		return "/admin/userStatistics.jsp";
	}
	
	/**
	 * 用户个人信息查询(信息管理)
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/admin/toQryMgUser.action")
	public String toQryMgUser(User user, Model model, HttpSession session){
		String userId = user.getUserId().trim();
		User userInfo = userService.get(userId);
		model.addAttribute("user", userInfo);
		model.addAttribute("grade", gradeService.find(new Grade()));
		return "/admin/info-qry.jsp";			
	}
	
	@RequestMapping("/admin/updateUser.action")
	public String updateUser(User user, Model model){		
		userService.update(user);
		return "redirect:/admin/getAllUser.action";			
	}
	
}
