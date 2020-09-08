package edu.fjnu.online.controller.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.errorprone.annotations.Var;

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
import edu.fjnu.online.util.TexToPDF;
import jnr.ffi.Struct.int16_t;
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
	
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	ObjectMapper mapper = new ObjectMapper();
	
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
		setupQuizzesForDisplay(paperDone);
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
//		Question question = null;
//		String []ids = paper.getQuestionId().split(",");
//		List<Question> questionList = new ArrayList<Question>();
//		List<Question> selList = new ArrayList<Question>();
//		List<Question> inpList = new ArrayList<Question>();
//		List<Question> desList = new ArrayList<Question>();
//		for(int i = 0;i<ids.length;i++){
//			question = questionService.get(Integer.parseInt(ids[i]));
//			if ("1".equalsIgnoreCase(question.getTypeId())) {
//				question = QuestionStuffs.convertAnsForMCQ(question);
//			}
//			question = QuestionStuffs.replaceLatexAnsWithUnderscore(question);
//			
//			if (question.getAttachmentId() != 0) {
//				question.setAttachmentFile(attachmentService.get(question.getAttachmentId()).getAttachmentFile());
//			}
////			if("1".equals(question.getTypeId())){//MCQ
////				selList.add(question);
////			}
////			if("2".equals(question.getTypeId())){//FBQ
////				inpList.add(question);
////			}
////			if("5".equals(question.getTypeId())){//简答题
////				desList.add(question);
////			}
//			questionList.add(question);
//		}
		
		Map quizMap = new HashMap();
		quizMap.put("userId", user.getUserId());
		quizMap.put("quizId", paperId);
		
		 
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
			String questionEndTimeString = errorBook.getEndTime();
			if (questionEndTimeString == null) {
				iterator.remove();
				continue;
			}
			
			Date questionEndTime = null;
			try {
				questionEndTime = formatter.parse(questionEndTimeString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(questionEndTime);
			if (questionEndTime.before(quizStartTime) || questionEndTime.after(quizEndTime)) {
				iterator.remove();
			}
			
			//set question info
			Question question = errorBook.getQuestion();
			
			if ("1".equalsIgnoreCase(question.getTypeId())) {
				question = QuestionStuffs.convertAnsForMCQ(question);
			}
			question = QuestionStuffs.replaceLatexAnsWithUnderscore(question);
			
			if (question.getAttachmentId() != 0) {
				question.setAttachmentFile(attachmentService.get(question.getAttachmentId()).getAttachmentFile());
			}
			errorBook.setQuestion(question);
			
//			if (errorBook.isCorrectness()) {
//				correctCount+=1;
//			}
			
			// set grade name
			Grade grade = gradeService.get(Integer.valueOf(errorBook.getQuestion().getGradeId()));
			errorBook.setGradeName(grade.getGradeName());
			
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
//		if(questionList.size()>0){
//			model.addAttribute("questionList", questionList);
//		}
		
//		bookList.sort(Comparator.comparing(b->Arrays.asList(ids).indexOf(b)));
		
		if(bookList.size()>0) {
			model.addAttribute("questionRecordList", bookList);
		}
		
		model.addAttribute("paper", paper);
		model.addAttribute("user", user);
		return "/user/qrypaper.jsp";			
	}
	
	/**
	 * quiz页面
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
				  
				String beginTime = formatter.format(new Date());
				map.put("beginTime", beginTime);
			}
			map.put("paperState", 1);
		}
		paperService.updateUserPaper(map);
		
		List<Question> quesList = new ArrayList<Question>();
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
			//question.setQuesName(StringEscapeUtils.escapeJava(question.getQuesName()));
			quesList.add(question);
//			if("1".equals(question.getTypeId())){//MCQ
//				selList.add(question);
//			}
//			if("2".equals(question.getTypeId())){//FBQ
//				inpList.add(question);
//			}
//			if("5".equals(question.getTypeId())){//
//				desList.add(question);
//			}
		}
		
		if(quesList.size()>0){
			model.addAttribute("quesList", quesList);
		}
		
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
		
		model.addAttribute("paper", paper);
		model.addAttribute("user", user);
		session.setAttribute("paperId", paperId);
		
		System.out.println("================");
		int currentQ = paper.getCurrentQuestion();
		System.out.println("Q:"+ String.valueOf(currentQ));
//		System.out.println("Qs:"+ paper.getQuestionId());
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
	 * paper页面
	 * @param paperId
	 * @param userId
	 * @param model
	 * @param session
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/qryExamPaperDetail.action")
	public String qryExamPaperDetail(User user,String paperId,String userId,Model model, HttpSession session){
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
		Paper paper = paperService.getExamPaperDetail(map);
		
		Question question = null;
		String []ids = paper.getQuestionId().split(",");
//		if(paper.getPaperState() == 2) {
//			return "redirect:/toMyPaperPage.action";
//		}else {
//			if(paper.getPaperState() == 0 || paper.getBeginTime() == null || paper.getBeginTime().isEmpty()) {
//				  
//				String beginTime = formatter.format(new Date());
//				map.put("beginTime", beginTime);
//			}
//			map.put("paperState", 1);
//		}
//		paperService.updateUserPaper(map);
		
		List<Question> quesList = new ArrayList<Question>();
//		List<Question> selList = new ArrayList<Question>();
//		List<Question> inpList = new ArrayList<Question>();
//		List<Question> desList = new ArrayList<Question>();
		for(int i = 0;i<ids.length;i++){
			question = questionService.get(Integer.parseInt(ids[i]));
			if ("1".equalsIgnoreCase(question.getTypeId())) {
				question = QuestionStuffs.convertAnsForMCQ(question);
			}
			question = QuestionStuffs.replaceLatexAnsWithUnderscore(question);
			question = QuestionStuffs.replaceLineSepWithDblSlash(question);
			
			if (question.getAttachmentId() != 0) {
				question.setAttachmentFile(attachmentService.get(question.getAttachmentId()).getAttachmentFile());
			}
			//question.setQuesName(StringEscapeUtils.escapeJava(question.getQuesName()));
			quesList.add(question);
//			if("1".equals(question.getTypeId())){//MCQ
//				selList.add(question);
//			}
//			if("2".equals(question.getTypeId())){//FBQ
//				inpList.add(question);
//			}
//			if("5".equals(question.getTypeId())){//
//				desList.add(question);
//			}
			System.out.println("================");
			System.out.println("Q:"+ String.valueOf(i+1));
			System.out.println("QId:"+ String.valueOf(question.getQuestionId()));
			
			String testString = question.getQuesName();
			System.out.println("Question: "+testString);
//			String optionAString = question.getOptionA();
//			String optionBString = question.getOptionB();
//			String optionCString = question.getOptionC();
//			String optionDString = question.getOptionD();
//			System.out.println("A: "+optionAString);
//			System.out.println("B: "+optionBString);
//			System.out.println("C: "+optionCString);
//			System.out.println("D: "+optionDString);
			System.out.println("Ans:"+question.getAnswer());
		}
		
		if(quesList.size()>0){
			model.addAttribute("quesList", quesList);
		}
		
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
		
		model.addAttribute("paper", paper);
		model.addAttribute("user", user);
		model.addAttribute("paperId", paperId);
		TexToPDF.toPDF(session.getServletContext().getRealPath("/"), paper, quesList);

		return "/user/showPaperPage.jsp";
	}
	
//	@RequestMapping(value = "/downloadPaper.action",
//			  produces = "application/x-tex")
	@RequestMapping("/downloadPaper.action")
	@ResponseBody
	public byte[] downloadPaper(User user,String paperId,String userId,Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
//		temp security implementation
		if(session.getAttribute("user") == null){
			return null;
		}
		
//		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
//		}
//		if(session.getAttribute("user")== null){
//			session.setAttribute("user", userService.get(user.getUserId()));
//		}
		
		user = userService.getStu(user);
		System.out.println(user);
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", userId);
		Paper paper = paperService.getExamPaperDetail(map);
		
		System.out.println(user.getUserId());
		System.out.println(userId+" asks for paper: " + paperId);
		if (!paper.getUserId().equals(user.getUserId())) {
			System.out.println("Paper " + paperId + "does NOT belong to " + userId);
			return null;
		}
        
		File workingDirectory = new File(session.getServletContext().getRealPath("/")+
//				System.getProperty("user.dir") + 
//				File.separator + "src" + 
//				File.separator + "main" + 
//				File.separator + "resources" + 
				File.separator + "paperTexTemplate");
		File tempDir = new File(workingDirectory.getAbsolutePath() + File.separator + "temp");

        File paperTex = new File(tempDir.getAbsolutePath() + File.separator + paperId +".tex");
	    if(!paperTex.exists()) {
	    	return null;
	    }
	    
	    String mimeType = session.getServletContext().getMimeType(paperTex.getAbsolutePath());
	    System.out.println("File type: " + mimeType);
	    
	    FileInputStream in = new FileInputStream(paperTex);
	    
	    // modifies response
	    response.setContentType(mimeType);
	    response.setContentLength((int) paperTex.length());
	    
	    // forces download
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=%s", paperTex.getName());
        response.setHeader(headerKey, headerValue);
	    
//	    getClass().getClassLoader();
//		InputStream in = ClassLoader.getSystemResourceAsStream(paperTex.getAbsolutePath());
	    return IOUtils.toByteArray(in);
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
		List<Paper> examPaper = paperService.qryExamPaper(map);
//		List<Paper> papers = new ArrayList<Paper>();
//		papers.addAll(paperUndo);
//		papers.addAll(paperInProgress);
		setupQuizzesForDisplay(paperUndo);
		setupQuizzesForDisplay(paperInProgress);
		setupQuizzesForDisplay(examPaper);
		model.addAttribute("user", user);
		model.addAttribute("paper", paperUndo);
		model.addAttribute("paperInProgress", paperInProgress);
		model.addAttribute("examPaper", examPaper);
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
	public MsgItem dealQuestion(@RequestBody String json, String stuAnswer, Paper paper, Model model, HttpSession session) throws UnsupportedEncodingException{
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
//		System.out.println(json);//paperId=17&answer=D
//		String delimiter = "answer=";
//		String stuAnswer = json.substring(json.indexOf(delimiter)+delimiter.length());
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
//		  
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
				User tempUser = new User();
				tempUser.setUserId(user.getUserId());
				tempUser.setUserType(user.getUserType());
				tempUser.setUserState(user.getUserState());
				tempUser.setRewardPoints(user.getRewardPoints()+1);
				userService.update(tempUser);
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
		  
		String questionEndTime = formatter.format(new Date());
		questionRecord.setStartTime(questionEndTime);// TO-DO
		questionRecord.setEndTime(questionEndTime);
		bookService.insert(questionRecord);
		
		if(paper.getCurrentQuestion() == paperQuesIdx.length) {
			map.put("paperState", 2);
//			  
			String endTime = formatter.format(new Date());
			paper.setEndTime(endTime);
			map.put("endTime", endTime);
			msgItem.setErrorNo("1");
//			session.removeAttribute("currentQuestion");
			
			double quizDoneAccuracy = calculateQuizScore(paper, user.getUserId());
			map.put("score", String.format("%d", (int) (quizDoneAccuracy * 100)));
			
			final String parentEmailString = user.getParentEmail();
			final String emailTitleString = "[Major Maths] Reminder";
			final String userNameString = user.getUserName();
			final String paperNameString = paper.getPaperName();
			
			Boolean emailNotEmpty = parentEmailString != null && !parentEmailString.trim().equals("");
			
			Map<String, Boolean> emailMap = null;
			try {
				emailMap = mapper.readValue(user.getAuthority(), Map.class);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (emailNotEmpty && emailMap.get("sendEmail")) {
				System.out.println("Sending email to " + parentEmailString);
				new Thread(new Runnable() {
					@Override
					public void run() {
						emailService.sendMail(parentEmailString, emailTitleString, String.format("%s just finished %s questions from quiz: *%s* and scored *%d* marks.", userNameString, String.valueOf(paperQuesIdx.length), paperNameString, (int) (quizDoneAccuracy * 100)));//change to parent email
					}
				}).start();
//				emailService.sendMail(parentEmailString, emailTitleString, String.format("%s finished %s questions.", user.getUserName(), String.valueOf(paperQuesIdx.length)));//change to parent email
			}else {
				System.out.println("User chooses not to sent email.");
			}
		}else {
//			if(paper.getPaperState() == 0 || paper.getBeginTime() == null || paper.getBeginTime().isEmpty()) {
////				  
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
		userId = user.getUserId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", userId);
		paper = paperService.getPaperDetail(map);
		  
		String endTime = formatter.format(new Date());
		map.put("endTime", endTime);
		map.put("paperState", 2);
		
		if (paper.getScore().equals("-1")) {
			double quizDoneAccuracy = calculateQuizScore(paper, userId);
			map.put("score", String.format("%d", (int) (quizDoneAccuracy * 100)));
		}
		
		paperService.updateUserPaper(map);
		
//		session.removeAttribute("currentQuestion");
		session.removeAttribute("paperId");
		
		return "redirect:/toScoreQry.action";
	}
	
	public double calculateQuizScore(Paper paper, String userId) {
		Map quizMap = new HashMap();
		quizMap.put("userId", userId);
		quizMap.put("quizId", paper.getPaperId());

		String quizStartTimeString = paper.getBeginTime();
		Date quizStartTime = null;
		try {
			quizStartTime = formatter.parse(quizStartTimeString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String quizEndTimeString = paper.getEndTime();
		Date quizEndTime = null;
		if (quizEndTimeString == null || quizEndTimeString.equals("")) {
			quizEndTime = new Date();
		}else {
			try {
				quizEndTime = formatter.parse(quizEndTimeString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		List<ErrorBook> bookList = bookService.getBookInfoForQuiz(quizMap);
		for (Iterator iterator = bookList.iterator(); iterator.hasNext();) {
			ErrorBook errorBook = (ErrorBook) iterator.next();

			String questionEndTimeString = errorBook.getEndTime();
			if (questionEndTimeString == null) {
				iterator.remove();
				continue;
			}

			Date questionEndTime = null;
			try {
				questionEndTime = formatter.parse(questionEndTimeString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (questionEndTime.before(quizStartTime) || questionEndTime.after(quizEndTime)) {
				iterator.remove();
			}
		}

		return QuestionStuffs.calcAccuracyForQuesSet(bookList);

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
		List<Question> uniqueSubtopics = questionService.findSubtopic(user.getCurriculum());
		List<Grade> gradeList = gradeService.findActive(new Grade());
		Map curriculumSubtopicMap = new HashMap();
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
	 * To user generate paper page
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping("/toPaperGeneratePage.action")
	public String toPaperGeneratePage(User user, Paper paper,Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user") == null){
			return "redirect:/toLogin.action";
		}
		
		if("".equals(user.getUserId()) || user.getUserId()==null){
			user = (User) session.getAttribute("user");
		}
		
		user = userService.getStu(user);
		List<Question> uniqueSubtopics = questionService.findSubtopic(user.getCurriculum());
		List<Grade> gradeList = gradeService.findActive(new Grade());
		Map curriculumSubtopicMap = new HashMap();
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
		return "/user/mypapergenerate.jsp";
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
		int maxQuesNo = 50;
		int minQuesNo = 5;
		if(totalQuesNo < minQuesNo || totalQuesNo > maxQuesNo){
			msgItem.setErrorInfo("Error: Invalid No. of questions.");
			return msgItem;
		}
		
		float difficulty = Float.valueOf(map.get("difficulty").toString());
		float maxDifficulty = 3;
		if(difficulty > maxDifficulty) {
			msgItem.setErrorInfo("Error: Invalid difficulty.");
			return msgItem;
		}
		
		difficulty = difficulty/maxDifficulty;
		difficulty = (float) Math.round(difficulty*100)/100;
		
		if(map.get("quizName").toString().isEmpty()) {
			paper.setPaperName("Untitled");
		}else {
			paper.setPaperName(map.get("quizName").toString());
		}
		
		List<Question> questionList = new ArrayList<Question>();
		List<String> subtopicIdList = (List<String>) map.get("subtopicIds");
		List<Integer> addedQuestionIdList = new ArrayList<Integer>();
		List<Integer> temp = new ArrayList<Integer>(addedQuestionIdList);
		temp.add(0);
		
		Map questionSelectMap = new HashMap();
		questionSelectMap.put("courseId", user.getCurriculum());
		questionSelectMap.put("num", 1);
		questionSelectMap.put("addedQuestionIdList", temp);
		
//		long start = System.nanoTime();
		for(int i = 0; i < totalQuesNo; i+=1) {
			int j = i % subtopicIdList.size();
			String subtopicId = subtopicIdList.get(j);
			
			System.out.println("Dealing with: " + subtopicId);
			questionSelectMap.put("subtopicId", subtopicId);
			List<Question> subtopicQuestionList = selectSubtopicQuesWithDifficulty(questionSelectMap, difficulty, maxDifficulty);
	        
			if (subtopicQuestionList.size() == 0) {
				msgItem.setErrorInfo(String.format("Error: No enough questions for subtopic: %s.", subtopicId));
				return msgItem;
			}
			Question question = new Question();
			question = subtopicQuestionList.get(0);
//			while(addedQuestionIdList.contains(question.getQuestionId())) {
//				subtopicQuestionList = selectSubtopicQuesWithDifficulty(questionSelectMap, difficulty, maxDifficulty);
//				question = subtopicQuestionList.get(0);
//			}
			addedQuestionIdList.add(question.getQuestionId());
			questionSelectMap.put("addedQuestionIdList", addedQuestionIdList);
			System.out.println(question.getQuestionId());
			System.out.println("================");
//			questionList.addAll(subtopicQuestionList);
			questionList.add(question);
		}
//		long end = System.nanoTime();
//        long elapsedTime = end - start;
//        double elapsedTimeInSecond = (double) elapsedTime / 1000000000;
//        System.out.println(elapsedTimeInSecond + " seconds");
		System.out.println(String.format("%d questions selected.", addedQuestionIdList.size()));
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
		
		paper.setUserId(user.getUserId());
		paper.setCourseId(user.getCurriculum());
		paper.setGradeId(gradeId);
		  
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
	
	/**
	 * User generate paper
	 * @param paper
	 * @param model
	 * @param session
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/paperGenerate.action")
	@ResponseBody
	public MsgItem paperGenerate(@RequestBody Map map, Paper paper,Model model, HttpSession session) throws UnsupportedEncodingException{
		User user = (User) session.getAttribute("user");
		MsgItem msgItem = new MsgItem();
		msgItem.setErrorNo("1");
		
		int paperGenPointDeduction = 1000;
		if (user.getRewardPoints() < paperGenPointDeduction) {
			msgItem.setErrorInfo(String.format("No enough reward points (currently %d). Need at least %d reward points to use this function. Contact us if you want to purchase more reward points.", user.getRewardPoints(), paperGenPointDeduction));
			return msgItem;
		}
		
		QuestionStuffs.iterate(map, 0);
		if (QuestionStuffs.isAnyEndEmpty(map)) {
			msgItem.setErrorInfo("Error: Empty areas.");
			return msgItem;
		}
		
		int totalQuesNo = Integer.valueOf(map.get("quesNo").toString());
		int maxQuesNo = 50;
		int minQuesNo = 5;
		if(totalQuesNo < minQuesNo || totalQuesNo > maxQuesNo){
			msgItem.setErrorInfo("Error: Invalid No. of questions.");
			return msgItem;
		}
		
		float difficulty = Float.valueOf(map.get("difficulty").toString());
		float maxDifficulty = 3;
		if(difficulty > maxDifficulty) {
			msgItem.setErrorInfo("Error: Invalid difficulty.");
			return msgItem;
		}
		
		difficulty = difficulty/maxDifficulty;
		difficulty = (float) Math.round(difficulty*100)/100;
		
		if(map.get("quizName").toString().isEmpty()) {
			paper.setPaperName("Untitled");
		}else {
			paper.setPaperName(map.get("quizName").toString());
		}
		
		List<Question> questionList = new ArrayList<Question>();
		List<String> subtopicIdList = (List<String>) map.get("subtopicIds");
		List<Integer> addedQuestionIdList = new ArrayList<Integer>();
		List<Integer> temp = new ArrayList<Integer>(addedQuestionIdList);
		temp.add(0);
		
		Map questionSelectMap = new HashMap();
		questionSelectMap.put("courseId", user.getCurriculum());
		questionSelectMap.put("num", 1);
		questionSelectMap.put("addedQuestionIdList", temp);
		
//		long start = System.nanoTime();
		for(int i = 0; i < totalQuesNo; i+=1) {
			int j = i % subtopicIdList.size();
			String subtopicId = subtopicIdList.get(j);
			
			System.out.println("Dealing with: " + subtopicId);
			questionSelectMap.put("subtopicId", subtopicId);
			List<Question> subtopicQuestionList = selectSubtopicQuesWithDifficulty(questionSelectMap, difficulty, maxDifficulty);
	        
			if (subtopicQuestionList.size() == 0) {
				msgItem.setErrorInfo(String.format("Error: No enough questions for subtopic: %s.", subtopicId));
				return msgItem;
			}
			Question question = new Question();
			question = subtopicQuestionList.get(0);
//			while(addedQuestionIdList.contains(question.getQuestionId())) {
//				subtopicQuestionList = selectSubtopicQuesWithDifficulty(questionSelectMap, difficulty, maxDifficulty);
//				question = subtopicQuestionList.get(0);
//			}
			addedQuestionIdList.add(question.getQuestionId());
			questionSelectMap.put("addedQuestionIdList", addedQuestionIdList);
			System.out.println(question.getQuestionId());
			System.out.println("================");
//			questionList.addAll(subtopicQuestionList);
			questionList.add(question);
		}
//		long end = System.nanoTime();
//        long elapsedTime = end - start;
//        double elapsedTimeInSecond = (double) elapsedTime / 1000000000;
//        System.out.println(elapsedTimeInSecond + " seconds");
		System.out.println(String.format("%d questions selected.", addedQuestionIdList.size()));
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
		
		paper.setUserId(user.getUserId());
		paper.setCourseId(user.getCurriculum());
		paper.setGradeId(gradeId);
		  
		String createTime = formatter.format(new Date());
		paper.setCreateTime(createTime);
		paper.setAllowTime(map.get("expectTime").toString());
		paper.setPaperState(0);
		paper.setDifficulty(difficulty);
		paper.setScore("-1");
		paper.setCurrentQuestion(1);
		paper.setQuestionId(quesId);
		System.out.println(paper.toString());
		
		user.setRewardPoints(user.getRewardPoints()-paperGenPointDeduction);
		userService.update(user);
		user = userService.get(user.getUserId());
		session.setAttribute("user", userService.getStu(user));
		
		paperService.insertPaper(paper);
		msgItem.setErrorNo("0");
		return msgItem;
	}
	
	public List<Question> selectSubtopicQuesWithDifficulty(Map questionSelectMap, float difficulty, float maxDifficulty) {
		questionSelectMap.put("difficulty", difficulty);
		List<Question> subtopicQuestionList = questionService.createPaper(questionSelectMap);
		if(subtopicQuestionList.size() == 0) {
			for (int k = 1; k <= (int) maxDifficulty; k++) {
				float tempDifficulty = (float) k/maxDifficulty;
				tempDifficulty = (float) Math.round(tempDifficulty*100)/100;
				if (Math.abs(tempDifficulty-difficulty) < 0.01) {
					continue;
				}
				questionSelectMap.put("difficulty", tempDifficulty);
				subtopicQuestionList = questionService.createPaper(questionSelectMap);
				if (subtopicQuestionList.size() != 0) {
					return subtopicQuestionList;
				}
			}
			return subtopicQuestionList;
		}
		return subtopicQuestionList;
	}
	
	public void setupQuizzesForDisplay(List<Paper> papers) {
		for(Paper p : papers){
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
	}
}