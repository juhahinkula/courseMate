package fi.coursemate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Peer-review questions
 * 
 * @author Juha Hinkula
 *
 */
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)	
	private long id;

    private String type;
	private String title;
	private String description;
	private int grade;
	
    @ManyToOne
	@JoinColumn(name = "reviewid")
    private PeerReview review;    	
	
    public Question() {}
    
	public Question(String type, String title, PeerReview review) {
		super();
		this.type = type;
		this.title = title;
		this.review = review;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public PeerReview getReview() {
		return review;
	}

	public void setReview(PeerReview review) {
		this.review = review;
	}
}
