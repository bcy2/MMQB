package edu.fjnu.online.controller.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.fjnu.online.domain.Course;
import edu.fjnu.online.domain.ErrorBook;
import edu.fjnu.online.domain.Grade;
import edu.fjnu.online.domain.Question;
import edu.fjnu.online.domain.Type;
import edu.fjnu.online.domain.User;
import edu.fjnu.online.service.AttachmentService;
import edu.fjnu.online.service.CourseService;
import edu.fjnu.online.service.ErrorBookService;
import edu.fjnu.online.service.GradeService;
import edu.fjnu.online.service.TypeService;
import edu.fjnu.online.service.UserService;
import edu.fjnu.online.util.QuestionStuffs;

@Controller
public class ErrorBookController {
	@Autowired
	ErrorBookService bookService;
	@Autowired
	GradeService gradeService;
	@Autowired
	CourseService courseService;
	@Autowired
	TypeService typeService;
	@Autowired
	UserService userService;
	@Autowired
	AttachmentService attachmentService;
	//跳转到Review Misdids页面
	@RequestMapping("/toMyBooksPage.action")
	public String toMyBooksPage(User user, Model model, HttpSession session){
//		temp security implementation
		if(session.getAttribute("user")== null){
			return "redirect:/toLogin.action";
//			session.setAttribute("user", userService.get(user.getUserId()));
		}
//		else {
//			System.out.println("user:"+session.getAttribute("user"));
//		}
		List<ErrorBook> errorBookList = bookService.find(new ErrorBook());
		List<Grade> gradeList = gradeService.findActive(new Grade());
		List<Course> courseList = courseService.find(new Course());
		List<Type> typeList = typeService.find(new Type());
		Map map = new HashMap();
		map.put("userId", user.getUserId());
		List<ErrorBook> bookList = bookService.getBookInfo(map);
		System.out.println("No. of question records: "+String.valueOf(bookList.size()));
		int correctCount = 0;
		
		for (Iterator iterator = bookList.iterator(); iterator.hasNext();) {
			ErrorBook errorBook = (ErrorBook) iterator.next();
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
			
			if (errorBook.isCorrectness()) {
				correctCount+=1;
			}
			
			// set grade name
			Grade grade = gradeService.get(Integer.valueOf(errorBook.getQuestion().getGradeId()));
			errorBook.setGradeName(grade.getGradeName());
		}
		
		double accuracy = (double) correctCount/bookList.size();
		accuracy = (Math.round(accuracy*100)/100.0);
		
		model.addAttribute("accuracy", accuracy);
		model.addAttribute("grade", gradeList);
		model.addAttribute("course", courseList);
		model.addAttribute("type", typeList);
		model.addAttribute("questionRecordList", bookList);
		return "/user/mybooks.jsp";			
	}
	
	//跳转到前台登录页面
//	@RequestMapping("/getBooks.action")
//	public String getBooks(User user, Model model, HttpSession session){
//		List<Grade> gradeList = gradeService.find(new Grade());
//		List<Course> courseList = courseService.find(new Course());
//		List<Type> typeList = typeService.find(new Type());
//		List<ErrorBook> errorBookList = bookService.getBookInfo(new HashMap());
//		model.addAttribute("grade", gradeList);
//		model.addAttribute("course", courseList);
//		model.addAttribute("type", typeList);
//		return "/user/mybooks.jsp";			
//	}
	
}
