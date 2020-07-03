package edu.fjnu.online.controller.user;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.text.StringEscapeUtils;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.fjnu.online.domain.Course;
import edu.fjnu.online.domain.ErrorBook;
import edu.fjnu.online.domain.Grade;
import edu.fjnu.online.domain.MsgItem;
import edu.fjnu.online.domain.Paper;
import edu.fjnu.online.domain.Question;
import edu.fjnu.online.domain.Type;
import edu.fjnu.online.domain.User;
import edu.fjnu.online.service.CourseService;
import edu.fjnu.online.service.ErrorBookService;
import edu.fjnu.online.service.GradeService;
import edu.fjnu.online.service.PaperService;
import edu.fjnu.online.service.QuestionService;
import edu.fjnu.online.service.TypeService;
import edu.fjnu.online.service.UserService;
import edu.fjnu.online.service.EmailService;
import edu.fjnu.online.service.TypeService;
import edu.fjnu.online.util.Computeclass;

import edu.fjnu.online.domain.Attachment;
import edu.fjnu.online.service.AttachmentService;
import edu.fjnu.online.util.QuestionStuffs;
//import edu.fjnu.online.util.EmailThread;
/**
 * 试卷综合管理
 * @author hspcadmin
 *
 */
@Controller
public class PaperMgController {

	@Autowired
	UserService userService;
	@Autowired
	GradeService gradeService;
	@Autowired
	PaperService paperService;
	@Autowired
	CourseService courseService;
	@Autowired
	QuestionService questionService;
	@Autowired
	ErrorBookService bookService;
	@Autowired
	TypeService typeService;
	
	@Autowired
	EmailService emailService;
	@Autowired
	AttachmentService attachmentService;
	
	/**
	 * reset quiz to available
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/resetQuiz.action")
	@ResponseBody
	public MsgItem resetQuiz(@RequestBody String json, Paper paper, Model model, HttpSession session) throws UnsupportedEncodingException{
		
		User user = (User) session.getAttribute("user");
		String paperId = paper.getPaperId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
//		int currentQ = paper.getCurrentQuestion();
//		currentQ += 1;
//		map.put("currentQuestion", currentQ);
//		paperService.updateUserPaper(map);
		
		model.addAttribute("user", user);
		
		map.put("paperState", 0);
		map.put("score", "-1");		
		map.put("currentQuestion", 1);
		map.put("endTime", "");
		map.put("beginTime", "");
		paperService.updateUserPaper(map);
		
		MsgItem msgItem = new MsgItem();
		msgItem.setErrorNo("0");
		
//		if(currentQ == paperQuesIdx.length) {
//			msgItem.setErrorNo("1");
//			session.removeAttribute("currentQuestion");
//		}
		
		return msgItem;	
	}
	
	//Review Papers
	@RequestMapping("/toScoreQry.action")
	public String toScoreQry(User user, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		
		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
		}
//		if(session.getAttribute("user")== null){
//			session.setAttribute("user", userService.get(user.getUserId()));
//		}
		
		user = userService.getStu(user);
		List<Paper> paperDone = paperService.getUserPaperById(user.getUserId());
		for(Paper p : paperDone){
			String[] gradeId = p.getGradeId().split(",");
			String gradeNameString = "";
			for (String id : gradeId) {
				Grade grade = gradeService.get(Integer.valueOf(id));
				gradeNameString += grade.getGradeName()+",";
			}
			if(!gradeNameString.isEmpty()){
				gradeNameString = QuestionStuffs.removeLast(gradeNameString);
			}
			p.setGradeId(gradeNameString);
			p.setQuestionId(String.valueOf(p.getQuestionId().split(",").length));
		}
		model.addAttribute("user", user);
		model.addAttribute("paper", paperDone);
		return "/user/scorequery.jsp";			
	}
	
	/**
	 * 查看试卷详情
	 * @param paperId
	 * @param userId
	 * @param model
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/qrypaper.action")
	public String qrypaper(User user,String paperId,String userId,Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		
		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
		}
//		if(session.getAttribute("user")== null){
//			session.setAttribute("user", userService.get(user.getUserId()));
//		}
		user = userService.getStu(user);
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", userId);
		Paper paper = paperService.getPaperDetail(map);
		Question question = null;
		String []ids = paper.getQuestionId().split(",");
		List<Question> questionList = new ArrayList<Question>();
//		List<Question> selList = new ArrayList<Question>();
//		List<Question> inpList = new ArrayList<Question>();
//		List<Question> desList = new ArrayList<Question>();
		for(int i = 0;i<ids.length;i++){
			question = questionService.get(Integer.parseInt(ids[i]));
			if ("1".equalsIgnoreCase(question.getTypeId())) {
				question = QuestionStuffs.convertAnsForMCQ(question);
			}
			question = QuestionStuffs.replaceLatexAnsWithUnderscore(question);
			
			if (question.getAttachmentId() != 0) {
				question.setAttachmentFile(attachmentService.get(question.getAttachmentId()).getAttachmentFile());
			}
//			if("1".equals(question.getTypeId())){//MCQ
//				selList.add(question);
//			}
//			if("2".equals(question.getTypeId())){//FBQ
//				inpList.add(question);
//			}
//			if("5".equals(question.getTypeId())){//简答题
//				desList.add(question);
//			}
			questionList.add(question);
		}
		
		Map quizMap = new HashMap();
		quizMap.put("userId", user.getUserId());
		quizMap.put("quizId", paperId);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date quizStartTime = null;
		try {
			quizStartTime = formatter.parse(paper.getBeginTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date quizEndTime = null;
		try {
			quizEndTime = formatter.parse(paper.getEndTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ErrorBook> bookList = bookService.getBookInfoForQuiz(quizMap);
		
		for (Iterator iterator = bookList.iterator(); iterator.hasNext();) {
			ErrorBook errorBook = (ErrorBook) iterator.next();
			Date questionEndTime = null;
			try {
				questionEndTime = formatter.parse(errorBook.getEndTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(questionEndTime);
			if (questionEndTime.before(quizStartTime) || questionEndTime.after(quizEndTime)) {
				iterator.remove();
			}
			
		}
		
//		System.out.println(bookList.size());
		
//		if(selList.size()>0){
//			model.addAttribute("selectQ", "Multiple Choice Questions");
//			model.addAttribute("selList", selList);
//		}
//		
//		if(inpList.size()>0){
//			model.addAttribute("inpQ", "Fill-in-Blank Questions");
//			model.addAttribute("inpList", inpList);
//		}
//		
//		if(desList.size()>0){
//			model.addAttribute("desQ", "简答题（每题5分）");
//			model.addAttribute("desList", desList);
//		}
		if(questionList.size()>0){
			model.addAttribute("questionList", questionList);
		}
		
//		bookList.sort(Comparator.comparing(b->Arrays.asList(ids).indexOf(b)));
		
		if(bookList.size()>0) {
			model.addAttribute("questionRecordList", bookList);
		}
		
		model.addAttribute("paper", paper);
		model.addAttribute("user", user);
		return "/user/qrypaper.jsp";			
	}
	
	/**
	 * 考试页面
	 * @param paperId
	 * @param userId
	 * @param model
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/qryPaperDetail.action")
	public String qryPaperDetail(User user,String paperId,String userId,Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		
		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
		}
//		if(session.getAttribute("user")== null){
//			session.setAttribute("user", userService.get(user.getUserId()));
//		}

		user = userService.getStu(user);
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", userId);
		Paper paper = paperService.getPaperDetail(map);
		Question question = null;
		String []ids = paper.getQuestionId().split(",");
		if(paper.getPaperState() == 2) {
			return "redirect:/toMyPaperPage.action";
		}else {
			if(paper.getPaperState() == 0 || paper.getBeginTime() == null || paper.getBeginTime().isEmpty()) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				String beginTime = formatter.format(new Date());
				map.put("beginTime", beginTime);
			}
			map.put("paperState", 1);
		}
		paperService.updateUserPaper(map);
		
		List<Question> questionList = new ArrayList<Question>();
		List<Question> quesList = new ArrayList<Question>();
		List<Question> selList = new ArrayList<Question>();
		List<Question> inpList = new ArrayList<Question>();
		List<Question> desList = new ArrayList<Question>();
		for(int i = 0;i<ids.length;i++){
			question = questionService.get(Integer.parseInt(ids[i]));
			if ("1".equalsIgnoreCase(question.getTypeId())) {
				question = QuestionStuffs.convertAnsForMCQ(question);
			}
			question = QuestionStuffs.replaceLatexAnsWithUnderscore(question);
			
			if (question.getAttachmentId() != 0) {
				question.setAttachmentFile(attachmentService.get(question.getAttachmentId()).getAttachmentFile());
			}
			//question.setQuesName(StringEscapeUtils.escapeJava(question.getQuesName()));
			quesList.add(question);
			if("1".equals(question.getTypeId())){//MCQ
				selList.add(question);
			}
			if("2".equals(question.getTypeId())){//FBQ
				inpList.add(question);
			}
			if("5".equals(question.getTypeId())){//
				desList.add(question);
			}
			questionList.add(question);
		}
		
		model.addAttribute("questions", "All questions");
		model.addAttribute("quesList", quesList);
		
		if(selList.size()>0){
			model.addAttribute("selectQ", "Multiple Choice Questions");
			model.addAttribute("selList", selList);
		}
		
		if(inpList.size()>0){
			model.addAttribute("inpQ", "Fill-in-Blank Questions");
			model.addAttribute("inpList", inpList);
		}
		
		if(desList.size()>0){
			model.addAttribute("desQ", "简答题（每题5分）");
			model.addAttribute("desList", desList);
		}
		if(questionList.size()>0){
			model.addAttribute("questionList", questionList);
		}
		
		model.addAttribute("paper", paper);
		model.addAttribute("user", user);
		session.setAttribute("paperId", paperId);
		
		System.out.println("================");
		int currentQ = paper.getCurrentQuestion();
		System.out.println("Q:"+ String.valueOf(currentQ));
		System.out.println("Qs:"+ paper.getQuestionId());
//		final String [] ids = paper.getQuestionId().split(",");
		String currentQId = ids[currentQ-1];
		System.out.println("QId:"+ String.valueOf(currentQId));
		Question ques = null;
		ques = questionService.get(Integer.parseInt(currentQId));
		
		if ("1".equalsIgnoreCase(ques.getTypeId())) {
			ques = QuestionStuffs.convertAnsForMCQ(ques);
		}
		
		String testString = ques.getQuesName();
		System.out.println("Question: "+testString);
		String optionAString = ques.getOptionA();
		String optionBString = ques.getOptionB();
		String optionCString = ques.getOptionC();
		String optionDString = ques.getOptionD();
		System.out.println("A: "+optionAString);
		System.out.println("B: "+optionBString);
		System.out.println("C: "+optionCString);
		System.out.println("D: "+optionDString);
		System.out.println("Ans:"+ques.getAnswer());
		
		// newly added
//		if(session.getAttribute("currentQuestion") == null){
//			session.setAttribute("currentQuestion", 1);
//		}

		return "/user/questiondetail.jsp";
	}
	
	/**
	 * Start working!
	 * @param user
	 * @param model
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/toMyPaperPage.action")
	public String toMyPaperPage(User user, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
		}
//		if(session.getAttribute("user")== null){
//			session.setAttribute("user", userService.get(user.getUserId()));
//		}
		
		user = userService.getStu(user);
		Map map =new HashMap();
		map.put("userId", user.getUserId());
		//List<Paper> paper = paperService.getUserPaperById(user.getUserId());
		
//		List<Paper> paper1 = paperService.getUndoPaper(map);
//		for(Paper p : paper1){
//			course = courseService.get(Integer.parseInt(p.getCourseId()));
//			p.setUserId(user.getUserId());
//			p.setPaperstate(1);
//			paperService.insert(p);
//			p.setCourseId(course.getCourseName());
//		}
		List<Paper> paperUndo = paperService.qryUndoPaper(map);
		List<Paper> paperInProgress = paperService.qryInProgressPaper(map);
//		List<Paper> papers = new ArrayList<Paper>();
//		papers.addAll(paperUndo);
//		papers.addAll(paperInProgress);
		for(Paper p : paperUndo){
			String[] gradeId = p.getGradeId().split(",");
			String gradeNameString = "";
			for (String id : gradeId) {
				Grade grade = gradeService.get(Integer.valueOf(id));
				gradeNameString += grade.getGradeName()+",";
			}
			if(!gradeNameString.isEmpty()){
				gradeNameString = QuestionStuffs.removeLast(gradeNameString);
			}
			p.setGradeId(gradeNameString);
			p.setQuestionId(String.valueOf(p.getQuestionId().split(",").length));
		}
		for(Paper p : paperInProgress){
			String[] gradeId = p.getGradeId().split(",");
			String gradeNameString = "";
			for (String id : gradeId) {
				Grade grade = gradeService.get(Integer.valueOf(id));
				gradeNameString += grade.getGradeName()+",";
			}
			if(!gradeNameString.isEmpty()){
				gradeNameString = QuestionStuffs.removeLast(gradeNameString);
			}
			p.setGradeId(gradeNameString);
			p.setQuestionId(String.valueOf(p.getQuestionId().split(",").length));
		}
		model.addAttribute("user", user);
		model.addAttribute("paper", paperUndo);
		model.addAttribute("paperInProgress", paperInProgress);
		return "/user/mypaper.jsp";
	}
	
	/**
	 * 自动paper评分
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/dealPaper.action")
	@ResponseBody
	public MsgItem dealPaper(Paper paper, Model model, HttpSession session) throws UnsupportedEncodingException{
		String paperId = paper.getPaperId();
		//答案临时存放
		String ans = paper.getScore();//ans:paperId=sj005&3=A&33=A&27=A&26=A&32=A&12=&9=&19=&10=&18=
		ans = URLDecoder.decode(ans,"UTF-8");
		String [] answer = null;
		if(ans.contains("&")){
			answer = ans.split("&");
		}
		Map map = new HashMap();
		User user = (User) session.getAttribute("user");
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		Paper paperInfo = paperService.getPaperDetail(map);
		String []ids = paperInfo.getQuestionId().split(",");
		List<Question> question = new ArrayList<Question>();
		Question ques = null;
		int endScore = 0;
		ErrorBook book = new ErrorBook();
		book.setUserId(user.getUserId());
		for(int i = 1 ;i<answer.length;i++){
			String[] str = answer[i].split("=");
			//题号
			String str1 = str[0];
			ques = questionService.get(Integer.parseInt(str1));
			//数据库对应的答案
			String answer1 = ques.getAnswer();
			if(str.length>1){
				//学生的答案
				String str2 = str[1];
				if(!"5".equals(ques.getTypeId())){//判断是否为简答题
					if(str2.equals(answer1)){//如果用户答案和数据库中的答案一致
						endScore+=5;
					}else{//插入错题本
						book.setQuestion(ques);
//						book.setCourseId(ques.getCourseId());
//						book.setGradeId(ques.getGradeId());
						book.setUserAnswer(str2);
						bookService.insert(book);
					}
				}else{//为简答题的时候
					String strA = answer1;
					String strB = URLDecoder.decode(str2, "UTF-8");//转码
					//计算相似
					double d = Computeclass.SimilarDegree(strA, strB);
					BigDecimal bg = new BigDecimal(d*5).setScale(1, RoundingMode.DOWN);
			        d = bg.doubleValue();
			        endScore+=d;
			        if(d<=2){//如果小于2分，认定错误
			        	book.setQuestion(ques);
//						book.setCourseId(ques.getCourseId());
//						book.setGradeId(ques.getGradeId());
						book.setUserAnswer(str2);
						bookService.insert(book);
			        }
				}
			}
		}
		System.out.println("最后得分："+endScore);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date currentTime = new Date();//得到当前系统时间  
		String endTime = formatter.format(currentTime); //将日期时间格式化  
		map.put("beginTime", paper.getBeginTime());
		map.put("endTime", endTime);
		map.put("score", endScore);
		//将考试的试卷状态改为2
		map.put("paperState", "2");
		paperService.updateUserPaper(map);
		if(session.getAttribute("user")== null){
			session.setAttribute("user", user);
		}
		MsgItem msgItem = new MsgItem();
		msgItem.setErrorNo("1");
		msgItem.setErrorInfo("试卷提交成功，本次考试得分："+endScore +"分");
		return msgItem;		
	}
	
	/**
	 * previous question
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/prevQuestion.action")
	@ResponseBody
	public MsgItem prevQuestion(@RequestBody String json, int currentQuestion, Paper paper, Model model, HttpSession session) throws UnsupportedEncodingException{
//		int currentQ = (Integer) session.getAttribute("currentQuestion");
		
//		currentQ -= 1;
//		session.setAttribute("currentQuestion", currentQ);
		
		User user = (User) session.getAttribute("user");
		String paperId = paper.getPaperId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
		int currentQ = paper.getCurrentQuestion();
		if (currentQ > currentQuestion) {
			currentQ = currentQuestion - 1;
		}else {
			currentQ -= 1;
		}
		map.put("currentQuestion", currentQ);
		paperService.updateUserPaper(map);
		
		model.addAttribute("user", user);
		model.addAttribute("paper", paper);
		
		MsgItem msgItem = new MsgItem();
		msgItem.setErrorNo("0");
		
		return msgItem;	
	}
	
	/**
	 * 自动question评分
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/dealQuestion.action")
	@ResponseBody
	public MsgItem dealQuestion(@RequestBody String json, Paper paper, Model model, HttpSession session) throws UnsupportedEncodingException{
//		int currentQ = (Integer) session.getAttribute("currentQuestion");
//		System.out.println("Q:"+ String.valueOf(currentQ));
		
		final User user = (User) session.getAttribute("user");
		String paperId = paper.getPaperId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
		int currentQ = paper.getCurrentQuestion();
		final String [] paperQuesIdx = paper.getQuestionId().split(",");
		String currentQId = paperQuesIdx[currentQ-1];
		Question ques = null;
		ques = questionService.get(Integer.parseInt(currentQId));
		if ("1".equalsIgnoreCase(ques.getTypeId())) {
			ques = QuestionStuffs.convertAnsForMCQ(ques);
		}
		//数据库对应的答案
		String answer = ques.getAnswer();
		String answerNoSpace = "";//for checking FBQs
		answer = URLDecoder.decode(answer,"UTF-8");
		//System.out.println(json);//paperId=sj005&answer=%E5%93%A5%E5%93%A5
		String delimiter = "answer=";
		String stuAnswer = json.substring(json.indexOf(delimiter)+delimiter.length());
		String stuAnswerNoSpace = "";//for checking FBQs
		stuAnswer = URLDecoder.decode(stuAnswer, "UTF-8");
		if ("2".equalsIgnoreCase(ques.getTypeId())) {
			answerNoSpace = QuestionStuffs.removeSpace(answer);
			stuAnswerNoSpace = QuestionStuffs.removeSpace(stuAnswer);
			System.out.println("Ans after removing Space:"+answerNoSpace);
			System.out.println("Student Ans after removing Space:"+stuAnswerNoSpace);
		}else {
			answerNoSpace = answer;
			stuAnswerNoSpace = stuAnswer;
			System.out.println("Student Ans:"+stuAnswer);
		}
		
//		String paperId = paper.getPaperId();
//		//答案临时存放
//		String ans = paper.getScore();
//		ans = URLDecoder.decode(ans,"UTF-8");
//		String [] answer = null;
//		if(ans.contains("&")){
//			answer = ans.split("&");
//		}
//		Map map = new HashMap();
//		User user = (User) session.getAttribute("user");
//		map.put("paperId", paperId);
//		map.put("userId", user.getUserId());
//		Paper paperInfo = paperService.getPaperDetail(map);
//		String []ids = paperInfo.getQuestionId().split(",");
//		List<Question> question = new ArrayList<Question>();
//		Question ques = null;
		int endScore = 0;
		ErrorBook questionRecord = new ErrorBook();
//		book.setUserId(user.getUserId());
//		System.out.println("answer:"+answer);
//		for(int i = 1 ;i<answer.length;i++){
//			System.out.println("==========");
//			String[] str = answer[i].split("=");
//			System.out.println("str:"+str);
//			//题号
//			String str1 = str[0];
//			System.out.println("str1:"+str1);
//			ques = questionService.get(Integer.parseInt(str1));
//			//数据库对应的答案
//			String answer1 = ques.getAnswer();
//			if(str.length>1){
//				//学生的答案
//				String str2 = str[1];
//				System.out.println("str2:"+str2);
//				if(!"5".equals(ques.getTypeId())){//判断是否为简答题
//					if(str2.equals(answer1)){//如果用户答案和数据库中的答案一致
//						endScore+=5;
//					}else{//插入错题本
//						book.setQuestion(ques);
//						book.setCourseId(ques.getCourseId());
//						book.setGradeId(ques.getGradeId());
//						book.setUserAnswer(str2);
//						bookService.insert(book);
//					}
//				}else{//为简答题的时候
//					String strA = answer1;
//					String strB = URLDecoder.decode(str2, "UTF-8");//转码
//					//计算相似
//					double d = Computeclass.SimilarDegree(strA, strB);
//					BigDecimal bg = new BigDecimal(d*5).setScale(1, RoundingMode.DOWN);
//			        d = bg.doubleValue();
//			        endScore+=d;
//			        if(d<=2){//如果小于2分，认定错误
//			        	book.setQuestion(ques);
//						book.setCourseId(ques.getCourseId());
//						book.setGradeId(ques.getGradeId());
//						book.setUserAnswer(str2);
//						bookService.insert(book);
//			        }
//				}
//			}
//		}
//		System.out.println("最后得分："+endScore);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//		Date currentTime = new Date();//得到当前系统时间  
//		String endTime = formatter.format(currentTime); //将日期时间格式化  
//		map.put("beginTime", paper.getBeginTime());
//		map.put("endTime", endTime);
//		map.put("score", endScore);
//		//将考试的试卷状态改为2
//		map.put("paperState", "2");
//		paperService.updateUserPaper(map);
//		if(session.getAttribute("user")== null){
//			session.setAttribute("user", user);
//		}
//		MsgItem msgItem = new MsgItem();
//		msgItem.setErrorNo("1");
//		msgItem.setErrorInfo("试卷提交成功，本次考试得分："+endScore +"分");
//		return msgItem;		
		model.addAttribute("user", user);
		model.addAttribute("paper", paper);
		
		MsgItem msgItem = new MsgItem();
		msgItem.setErrorNo("0");
//		msgItem.setRemark(String.format("The answer is: %s %nExplanation: %s %nRemarks: %s", answer, ques.getAnswerDetail(), ques.getRemark()));
		msgItem.setQuesAns(answer);
		msgItem.setQuesExp(ques.getAnswerDetail());
		msgItem.setRemark(ques.getRemark());
		boolean correctness = false;
		if(!"5".equalsIgnoreCase(ques.getTypeId())){//Operational question
			if (answerNoSpace.equalsIgnoreCase(stuAnswerNoSpace)) {
				correctness = true;
				System.out.println("Correct");
			}else {
				System.out.println("Wrong");
			}
		}else{
			// TODO
		}
		msgItem.setErrorInfo(String.valueOf(correctness));
		//insert to question record
		questionRecord.setUserId(user.getUserId());
		questionRecord.setQuestion(ques);
		questionRecord.setUserAnswer(stuAnswerNoSpace);
		questionRecord.setCorrectness(correctness);
		questionRecord.setQuizId(Integer.parseInt(paperId));
		questionRecord.setQuizName(paper.getPaperName());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String questionEndTime = formatter.format(new Date());
		questionRecord.setStartTime(questionEndTime);// TO-DO
		questionRecord.setEndTime(questionEndTime);
		bookService.insert(questionRecord);
		
		if(paper.getCurrentQuestion() == paperQuesIdx.length) {
			map.put("paperState", 2);
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			String endTime = formatter.format(new Date());
			map.put("endTime", endTime);
			msgItem.setErrorNo("1");
//			session.removeAttribute("currentQuestion");
			Boolean sendEmailBoolean = false;
			final String parentEmailString = user.getParentEmail();
			final String emailTitleString = "[Major Maths] Reminder";
			if (sendEmailBoolean) {
				System.out.println("Sending email to " + parentEmailString);
				new Thread(new Runnable() {
					@Override
					public void run() {
						emailService.sendMail(parentEmailString, emailTitleString, String.format("%s finished %s questions.", user.getUserName(), String.valueOf(paperQuesIdx.length)));//change to parent email
					}
				}).start();
				emailService.sendMail(parentEmailString, emailTitleString, String.format("%s finished %s questions.", user.getUserName(), String.valueOf(paperQuesIdx.length)));//change to parent email
			}
		}else {
//			if(paper.getPaperState() == 0 || paper.getBeginTime() == null || paper.getBeginTime().isEmpty()) {
////				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
//				String beginTime = formatter.format(new Date());
//				map.put("beginTime", beginTime);
//			}
			currentQ += 1;
			map.put("currentQuestion", currentQ);
			map.put("paperState", 1);
		}

		paperService.updateUserPaper(map);
		
		return msgItem;	
	}
	
	/**
	 * next question
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/nextQuestion.action")
	@ResponseBody
	public MsgItem nextQuestion(@RequestBody String json, Paper paper, Model model, HttpSession session) throws UnsupportedEncodingException{
//		int currentQ = (Integer) session.getAttribute("currentQuestion");

//		currentQ += 1;
//		session.setAttribute("currentQuestion", currentQ);
		
		User user = (User) session.getAttribute("user");
		String paperId = paper.getPaperId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
//		int currentQ = paper.getCurrentQuestion();
//		currentQ += 1;
//		map.put("currentQuestion", currentQ);
//		paperService.updateUserPaper(map);
		
		model.addAttribute("user", user);
		model.addAttribute("paper", paper);
		
		MsgItem msgItem = new MsgItem();
		msgItem.setErrorNo("0");
		
//		if(currentQ == paperQuesIdx.length) {
//			msgItem.setErrorNo("1");
//			session.removeAttribute("currentQuestion");
//		}
		
		return msgItem;	
	}
	
	/**
	 * finish quiz
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/finishQuiz.action")
	public String finishQuiz(User user, Paper paper, String userId, String paperId, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		
		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
		}
		
		user = userService.getStu(user);
		model.addAttribute("user", user);
//		model.addAttribute("paper", paper);

		if(paper.getPaperId() == null){
			paperId = (String) session.getAttribute("paperId");
		}else {
			paperId = paper.getPaperId();
		}
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String endTime = formatter.format(new Date());
		map.put("endTime", endTime);
		map.put("paperState", 2);
		paperService.updateUserPaper(map);
		
//		session.removeAttribute("currentQuestion");
		session.removeAttribute("paperId");
		
		return "redirect:/toScoreQry.action";
	}
	
	/**
	 * To user generate quiz page
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/toQuizGeneratePage.action")
	public String toQuizGeneratePage(User user, Paper paper,Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		
		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
		}
		
		user = userService.getStu(user);
		List<Question> subtopic = questionService.findSubtopic(user.getCurriculum());
		List<Grade> gradeList = gradeService.findActive(new Grade());
		Map curriculumSubtopicMap = new HashMap();
		for (Question q: subtopic) {
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
	
//			if (!typeMap.containsKey(q.getTypeId())) {
//				typeMap.put(q.getTypeId(), q.getSubtopicId());
//	
//			}
		}
		QuestionStuffs.iterate(curriculumSubtopicMap, 0);
		
		model.addAttribute("course", courseService.find(new Course()));
		model.addAttribute("grade", gradeList);
		model.addAttribute("curriculumSubtopicMap", curriculumSubtopicMap);
		
		model.addAttribute("type", typeService.find(new Type()));
		return "/user/myquizgenerate.jsp";
	}
	
	/**
	 * User generate quiz
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/quizGenerate.action")
	@ResponseBody
	public MsgItem quizGenerate(@RequestBody Map map, Paper paper,Model model, HttpSession session) throws UnsupportedEncodingException{
//		@RequestParam String quizName,@RequestParam int quesNo, @RequestParam int difficulty, @RequestParam float allowTime, 
		User user = (User) session.getAttribute("user");
		MsgItem msgItem = new MsgItem();
		msgItem.setErrorNo("1");
//		json = URLDecoder.decode(json, "UTF-8");
//		System.out.println(json);
		QuestionStuffs.iterate(map, 0);
		if (QuestionStuffs.isAnyEndEmpty(map)) {
			msgItem.setErrorInfo("Error: Empty areas.");
			return msgItem;
		}
		
		int totalQuesNo = Integer.valueOf(map.get("quesNo").toString());
		if(totalQuesNo <= 0){
			msgItem.setErrorInfo("Error: Invalid No. of questions.");
			return msgItem;
		}
		
		Map questionSelectMap = new HashMap();
//		List<Question> selectList = null;
//		List<Question> inputList = null;
//		List<Question> descList = null;
//		map.put("gradeId", paper.getGradeId());
		questionSelectMap.put("courseId", user.getCurriculum());
		
		List<Question> questionList = new ArrayList<Question>();
		List<String> subtopicIdList = (List<String>) map.get("subtopicIds");
		List<Integer> addedQuestionIdList = new ArrayList<Integer>();
		
		for(int i = 0; i < totalQuesNo; i+=1) {
			int j = i % subtopicIdList.size();
			String subtopicId = subtopicIdList.get(j);
			
			System.out.println("Dealing with: " + subtopicId);
			questionSelectMap.put("num", 1);
			questionSelectMap.put("subtopicId", subtopicId);
			List<Question> subtopicQuestionList = new ArrayList<Question>();
			Question question = new Question();
			subtopicQuestionList = questionService.createPaper(questionSelectMap);
			question = subtopicQuestionList.get(0);
			while(addedQuestionIdList.contains(question.getQuestionId())) {
				subtopicQuestionList = questionService.createPaper(questionSelectMap);
				question = subtopicQuestionList.get(0);
			}
			addedQuestionIdList.add(question.getQuestionId());
			System.out.println(question.getQuestionId());
			System.out.println("================");
//			questionList.addAll(subtopicQuestionList);
			questionList.add(question);
		}
//		if(inputNum>0){//判断题
//			map.put("num", inputNum);
//			map.put("typeId", 4);
//			inputList = questionService.createPaper(map);
//			paperList.addAll(inputList);
//		}
//		if(descNum > 0 ){//描述题
//			map.put("num", descNum);
//			map.put("typeId", 5);
//			descList = questionService.createPaper(map);
//			paperList.addAll(descList);
//		}
		List<String> gradeIdList = new ArrayList<String>();
		String gradeId = "";
		String quesId = "";
		for(Question ques : questionList){
			quesId+=ques.getQuestionId()+",";
			gradeIdList.add(ques.getGradeId());
		}
		if(!quesId.isEmpty()){
			quesId = QuestionStuffs.removeLast(quesId);
		}
		
		Set<String> gradeIdSet = new HashSet<String>(gradeIdList);
		for (String string : gradeIdSet) {
			gradeId+=string+",";
		}
		if(!gradeId.isEmpty()){
			gradeId = QuestionStuffs.removeLast(gradeId);
		}
		
		if(map.get("quizName").toString().isEmpty()) {
			paper.setPaperName("Untitled");
		}else {
			paper.setPaperName(map.get("quizName").toString());
		}
		
		paper.setUserId(user.getUserId());
		paper.setCourseId(user.getCurriculum());
		paper.setGradeId(gradeId);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		String createTime = formatter.format(new Date());
		paper.setCreateTime(createTime);
		paper.setAllowTime(map.get("expectTime").toString());
		paper.setPaperState(0);
		paper.setScore("-1");
		paper.setCurrentQuestion(1);
		paper.setQuestionId(quesId);
		System.out.println(paper.toString());
		paperService.insert(paper);
		msgItem.setErrorNo("0");
		return msgItem;
	}
}