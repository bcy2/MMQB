package edu.fjnu.online.domain;
/**
 * 错题本
 * @author hspcadmin
 *
 */
public class ErrorBook {

	/**错题编号*/
	private int bookId;
	/**用户编号*/
	private String userId;//username
	/**科目编号*/
//	private String courseId;
	/**年级编号*/
//	private String gradeId;
	/**学生答案*/
	private String userAnswer;
	/**问题编号*/
	private Question question;
	/**题型编号*/
//	private String typeId;
	
//	Newly added
	private String startTime;
	private String endTime;
	private boolean correctness;
	private int quizId;
	private String quizName;
	private String gradeName;
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserAnswer() {
		return userAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public boolean isCorrectness() {
		return correctness;
	}
	public void setCorrectness(boolean correctness) {
		this.correctness = correctness;
	}
	public int getQuizId() {
		return quizId;
	}
	public void setQuizId(int quizId) {
		this.quizId = quizId;
	}
	public String getQuizName() {
		return quizName;
	}
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public ErrorBook() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ErrorBook(int bookId, String userId, String userAnswer, Question question, String startTime, String endTime,
			boolean correctness, int quizId, String quizName, String gradeName) {
		super();
		this.bookId = bookId;
		this.userId = userId;
		this.userAnswer = userAnswer;
		this.question = question;
		this.startTime = startTime;
		this.endTime = endTime;
		this.correctness = correctness;
		this.quizId = quizId;
		this.quizName = quizName;
		this.gradeName = gradeName;
	}
	@Override
	public String toString() {
		return "ErrorBook [bookId=" + bookId + ", userId=" + userId + ", userAnswer=" + userAnswer + ", question="
				+ question + ", startTime=" + startTime + ", endTime=" + endTime + ", correctness=" + correctness
				+ ", quizId=" + quizId + ", quizName=" + quizName + ", gradeName=" + gradeName + "]";
	}
	
}
