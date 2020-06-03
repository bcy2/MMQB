package edu.fjnu.online.controller.user;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.fjnu.online.domain.Course;
import edu.fjnu.online.domain.ErrorBook;
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
import edu.fjnu.online.service.EmailService;
import edu.fjnu.online.util.Computeclass;
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
	EmailService emailService;
	
	//跳转到Review Papers页面
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
		List<Paper> paper = paperService.getUserPaperById(user.getUserId());
		Course course = null;
		for(Paper p : paper){
			course = courseService.get(Integer.parseInt(p.getCourseId()));
			p.setCourseId(course.getCourseName());
		}
		model.addAttribute("user", user);
		model.addAttribute("paper", paper);
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
		List<Question> selList = new ArrayList<Question>();
		List<Question> inpList = new ArrayList<Question>();
		List<Question> desList = new ArrayList<Question>();
		for(int i = 0;i<ids.length;i++){
			question = questionService.get(Integer.parseInt(ids[i]));
			if("1".equals(question.getTypeId())){//MCQ
				selList.add(question);
			}
			if("4".equals(question.getTypeId())){//FBQ
				inpList.add(question);
			}
			if("5".equals(question.getTypeId())){//简答题
				desList.add(question);
			}
		}
		
		if(selList.size()>0){
			model.addAttribute("selectQ", "单项选择题（每题5分）");
			model.addAttribute("selList", selList);
		}
		
		if(inpList.size()>0){
			model.addAttribute("inpQ", "填空题（每题5分）");
			model.addAttribute("inpList", inpList);
		}
		
		if(desList.size()>0){
			model.addAttribute("desQ", "简答题（每题5分）");
			model.addAttribute("desList", desList);
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
		List<Question> quesList = new ArrayList<Question>();
		List<Question> selList = new ArrayList<Question>();
		List<Question> inpList = new ArrayList<Question>();
		List<Question> desList = new ArrayList<Question>();
		for(int i = 0;i<ids.length;i++){
			question = questionService.get(Integer.parseInt(ids[i]));
			quesList.add(question);
			if("1".equals(question.getTypeId())){//单选
				selList.add(question);
			}
			if("4".equals(question.getTypeId())){//填空
				inpList.add(question);
			}
			if("5".equals(question.getTypeId())){//简答题
				desList.add(question);
			}
		}
		
		model.addAttribute("questions", "All questions");
		model.addAttribute("quesList", quesList);
		
		if(selList.size()>0){
			model.addAttribute("selectQ", "单项选择题（每题5分）");
			model.addAttribute("selList", selList);
		}
		
		if(inpList.size()>0){
			model.addAttribute("inpQ", "填空题（每题5分）");
			model.addAttribute("inpList", inpList);
		}
		
		if(desList.size()>0){
			model.addAttribute("desQ", "简答题（每题5分）");
			model.addAttribute("desList", desList);
		}
		
		model.addAttribute("paper", paper);
		model.addAttribute("user", user);
		session.setAttribute("paperId", paperId);
		
		// newly added
		if(session.getAttribute("currentQuestion") == null){
			session.setAttribute("currentQuestion", 1);
		}

		return "/user/questiondetail.jsp";			
	}
	
	/**
	 * 获取未考试试卷，并将为考试的试卷添加用户信息
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
		
		Course course = null;
//		List<Paper> paper1 = paperService.getUndoPaper(map);
//		for(Paper p : paper1){
//			course = courseService.get(Integer.parseInt(p.getCourseId()));
//			p.setUserId(user.getUserId());
//			p.setPaperstate("1");
//			paperService.insert(p);
//			p.setCourseId(course.getCourseName());
//		}
		List<Paper> paper = paperService.qryUndoPaper(map);
		for(Paper p : paper){
			course = courseService.get(Integer.parseInt(p.getCourseId()));
			p.setCourseId(course.getCourseName());
		}
		model.addAttribute("user", user);
		model.addAttribute("paper", paper);
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
						book.setCourseId(ques.getCourseId());
						book.setGradeId(ques.getGradeId());
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
						book.setCourseId(ques.getCourseId());
						book.setGradeId(ques.getGradeId());
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
	public MsgItem prevQuestion(@RequestBody String json, Paper paper, Model model, HttpSession session) throws UnsupportedEncodingException{
		int currentQ = (Integer) session.getAttribute("currentQuestion");
		
		currentQ -= 1;
		session.setAttribute("currentQuestion", currentQ);
		
		User user = (User) session.getAttribute("user");
		String paperId = paper.getPaperId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
		
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
		int currentQ = (Integer) session.getAttribute("currentQuestion");
		System.out.println("Q:"+ String.valueOf(currentQ));
		
		final User user = (User) session.getAttribute("user");
		String paperId = paper.getPaperId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
		System.out.println("Qs:"+ paper.getQuestionId());
		final String [] paperQuesIdx = paper.getQuestionId().split(",");
		String currentQId = paperQuesIdx[currentQ-1];
		System.out.println("QId:"+ String.valueOf(currentQId));
		Question ques = null;
		ques = questionService.get(Integer.parseInt(currentQId));
		//数据库对应的答案
		String answer = ques.getAnswer();
		answer = URLDecoder.decode(answer,"UTF-8");
		System.out.println("Ans:"+answer);
		//System.out.println(json);//paperId=sj005&answer=%E5%93%A5%E5%93%A5
		String delimiter = "answer=";
		String stuAnswer = json.substring(json.indexOf(delimiter)+delimiter.length());
		stuAnswer = URLDecoder.decode(stuAnswer, "UTF-8");
		System.out.println("Student Ans:"+stuAnswer+"\n================");
		
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
		ErrorBook book = new ErrorBook();
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
		if (answer.equalsIgnoreCase(stuAnswer)) {
			msgItem.setErrorInfo("T");
		}else {
			msgItem.setErrorInfo("F");
//			book.setQuestion(ques);
//			book.setCourseId(ques.getCourseId());
//			book.setGradeId(ques.getGradeId());
//			book.setUserAnswer(stuAnswer);
//			bookService.insert(book);
		}
		
		if(currentQ == paperQuesIdx.length) {
			msgItem.setErrorNo("1");
//			session.removeAttribute("currentQuestion");
			Boolean sendEmailBoolean = false;
			final String parentEmailString = "john.yue@kodingkingdom.com";
			final String emailTitleString = "Your kid sucks:)";
			if (sendEmailBoolean) {
				System.out.println("Sending email to " + parentEmailString);
				new Thread(new Runnable() {
					@Override
					public void run() {
						emailService.sendMail(parentEmailString, emailTitleString, String.format("%s finished %s questions.", user.getUserName(), String.valueOf(paperQuesIdx.length)));//change to parent email
					}
				}).start();
//				emailService.sendMail("john.yue@kodingkingdom.com", "Your kid sucks:)", String.format("%s finished %s questions.", user.getUserName(), String.valueOf(paperQuesIdx.length)));//change to parent email
			}
			return msgItem;	
		}
		
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
		int currentQ = (Integer) session.getAttribute("currentQuestion");
		
		currentQ += 1;
		session.setAttribute("currentQuestion", currentQ);
		
		User user = (User) session.getAttribute("user");
		String paperId = paper.getPaperId();
		Map map = new HashMap();
		map.put("paperId", paperId);
		map.put("userId", user.getUserId());
		paper = paperService.getPaperDetail(map);
		
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
	public String finishQuiz(User user, Model model, HttpSession session){
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
		
		session.removeAttribute("currentQuestion");
		session.removeAttribute("paperId");
		
		return "forward:/toScoreQry.action";
	}
}