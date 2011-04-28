package pl.greenislanddev.prawojazdy.exam.state;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import pl.greenislanddev.prawojazdy.business.Question;

public class ExamState implements Serializable {
	
	private static final long serialVersionUID = -6148131240864807991L;
	
	public static final int INITIAL_STATE = -1;
	public static final String KEY = "STATE_KEY";
	
	private long examStartTime;
	private String timerDisplay;
	private int pageCounter = 1;
	private int questionId = -1;
	private int questionsNumber;
	
	private boolean isExam = false;
	private boolean isFinished = false;
	private HashMap<Integer, Question> answersCache = new HashMap<Integer, Question>();
	
	public void initialize(int id){
		if (questionId == INITIAL_STATE) {
			questionId = id;
		}
	}
	
	public HashMap<Integer, Question> getAnswersCache() {
		return answersCache;
	}
	
	public int getPageCounter() {
		return pageCounter;
	}
	
	public int getQuestionId() {
		return questionId;
	}
		
	public void setPageCounter(int pageCounter) {
		this.pageCounter = pageCounter;
	}
	
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
	
	public int nextPageNumber(){
		return ++pageCounter;
	}
	
	public int previousPageNumber(){
		return --pageCounter;
	}
	
	public int getQuestionsNumber() {
		return questionsNumber;
	}
	
	public void setQuestionsNumber(int questionsNumber) {
		this.questionsNumber = questionsNumber;
	}
	
	public Collection<Question> getAnswers(){
		return answersCache.values();
	}
	
	public boolean isLastQuestion(){
		return pageCounter == questionsNumber;
	}
	
	public long getExamStartTime() {
		return examStartTime;
	}
	
	public void setExamStartTime(long examStartTime) {
		this.examStartTime = examStartTime;
	}
	
	public void setExam(boolean isExam) {
		this.isExam = isExam;
	}
	
	public boolean isExam() {
		return isExam;
	}
	
	public String getTimerDisplay() {
		return timerDisplay;
	}
	
	public void setTimerDisplay(String timerDisplay) {
		this.timerDisplay = timerDisplay;
	}
	
	public long getElapsedTime(){
		return System.currentTimeMillis() - examStartTime;
	}
	
	@Override
	public String toString() {
		return "[ "+pageCounter+" / "+questionsNumber+" ]";
	}
}
