package fi.coursemate.domain;


/**
 * AJAX response class for peer-review result entry
 * 
 * @author Juha Hinkula
 *
 */
public class Response {
	private Question question;
	private String status;
	
	public Response(String status, Question question) {
		this.status = status;
		this.question = question;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
