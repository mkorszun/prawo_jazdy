package pl.greenislanddev.prawojazdy.business;

import java.io.Serializable;

public class Question implements Serializable {

	public static final int MAX_ANSWERS = 3;
	private static final long serialVersionUID = 3718578849929988482L;
	
	private final int id;
	private boolean answered;
	private boolean[] userAnswers;
	private boolean[] correctAnswers;
	
	public Question(int id) {
		this.id = id;
		userAnswers = new boolean[MAX_ANSWERS];
	}

	public boolean[] getUserAnswers() {
		return userAnswers;
	}

	public void setUserAnswers(boolean[] userAnswers) {
		this.userAnswers = userAnswers;
	}
	
	public void setCorrectAnswers(boolean[] correctAnswers) {
		this.correctAnswers = correctAnswers;
	}
	
	public boolean[] getCorrectAnswers() {
		return correctAnswers;
	}
	
	public int getId() {
		return id;
	}
	
	public boolean[] validate(){
		boolean[] result = new boolean[MAX_ANSWERS];
		for(int i = 0 ; i < MAX_ANSWERS ; i++){
			result[i] = !(correctAnswers[i] ^ userAnswers[i]);
		}
		return result;
	}
	
	public boolean isCorrect(){
		boolean result = true;
		for(boolean q : validate()){
			result &= q;
		}
		return result;
	}
	
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	
	public boolean isAnswered() {
		return answered;
	}
}
