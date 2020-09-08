package edu.fjnu.online.domain;

public class Question {
	/**问题编号*/
	private int questionId;
	/**问题名称*/
	private String quesName;
	/**选项A*/
	private String optionA;
	/**选项B*/
	private String optionB;
	/**选项C*/
	private String optionC;
	/**选项D*/
	private String optionD;
	/**标准答案*/
	private String answer;
	/**学生答案*/
	private String userAnswer;
	/**对应课程*/
	private String courseId;
	/**题型*/
	private String typeId;
	/**难度（0：容易，1：中等，2：难）*/
	private float difficulty;
	/**备注*/
	private String remark;
	private String answerDetail;
	private String gradeId;
	
//	Newly added
	private String topic;
	
	private String subtopic;
	
	private String subtopicId;
	
	private String optionOther;
	
	private boolean isPastPaper;
	
	private int rawQuestionId;
	
	private int attachmentId;
	
	private String attachmentFile;
	
	private String script;
	
	private String scriptParameter;
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getSubtopic() {
		return subtopic;
	}
	public void setSubtopic(String subtopic) {
		this.subtopic = subtopic;
	}
	public String getSubtopicId() {
		return subtopicId;
	}
	public void setSubtopicId(String subtopicId) {
		this.subtopicId = subtopicId;
	}
	public String getOptionOther() {
		return optionOther;
	}
	public void setOptionOther(String optionOther) {
		this.optionOther = optionOther;
	}
	public boolean isPastPaper() {
		return isPastPaper;
	}
	public void setPastPaper(boolean isPastPaper) {
		this.isPastPaper = isPastPaper;
	}
	public int getRawQuestionId() {
		return rawQuestionId;
	}
	public void setRawQuestionId(int rawQuestionId) {
		this.rawQuestionId = rawQuestionId;
	}
	public int getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(int attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getAttachmentFile() {
		return attachmentFile;
	}
	public void setAttachmentFile(String attachmentFile) {
		this.attachmentFile = attachmentFile;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getScriptParameter() {
		return scriptParameter;
	}
	public void setScriptParameter(String scriptParameter) {
		this.scriptParameter = scriptParameter;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuesName() {
		return quesName;
	}
	public void setQuesName(String quesName) {
		this.quesName = quesName;
	}
	public String getOptionA() {
		return optionA;
	}
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	public String getOptionB() {
		return optionB;
	}
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	public String getOptionC() {
		return optionC;
	}
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	public String getOptionD() {
		return optionD;
	}
	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getUserAnswer() {
		return userAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public float getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(float difficulty) {
		this.difficulty = difficulty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAnswerDetail() {
		return answerDetail;
	}
	public void setAnswerDetail(String answerDetail) {
		this.answerDetail = answerDetail;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public Question() {
		
	}
	public Question(int questionId, String quesName, String optionA, String optionB, String optionC, String optionD,
			String answer, String userAnswer, String courseId, String typeId, float difficulty, String remark,
			String answerDetail, String gradeId, String topic, String subtopic, String subtopicId, String optionOther,
			boolean isPastPaper, int rawQuestionId, int attachmentId, String attachmentFile, String script,
			String scriptParameter) {
		super();
		this.questionId = questionId;
		this.quesName = quesName;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
		this.answer = answer;
		this.userAnswer = userAnswer;
		this.courseId = courseId;
		this.typeId = typeId;
		this.difficulty = difficulty;
		this.remark = remark;
		this.answerDetail = answerDetail;
		this.gradeId = gradeId;
		this.topic = topic;
		this.subtopic = subtopic;
		this.subtopicId = subtopicId;
		this.optionOther = optionOther;
		this.isPastPaper = isPastPaper;
		this.rawQuestionId = rawQuestionId;
		this.attachmentId = attachmentId;
		this.attachmentFile = attachmentFile;
		this.script = script;
		this.scriptParameter = scriptParameter;
	}
	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", quesName=" + quesName + ", optionA=" + optionA + ", optionB="
				+ optionB + ", optionC=" + optionC + ", optionD=" + optionD + ", answer=" + answer + ", userAnswer="
				+ userAnswer + ", courseId=" + courseId + ", typeId=" + typeId + ", difficulty=" + difficulty
				+ ", remark=" + remark + ", answerDetail=" + answerDetail + ", gradeId=" + gradeId + ", topic=" + topic
				+ ", subtopic=" + subtopic + ", subtopicId=" + subtopicId + ", optionOther=" + optionOther
				+ ", isPastPaper=" + isPastPaper + ", rawQuestionId=" + rawQuestionId + ", attachmentId=" + attachmentId
				+ ", attachmentFile=" + attachmentFile + ", script=" + script + ", scriptParameter=" + scriptParameter
				+ "]";
	}
	
}
