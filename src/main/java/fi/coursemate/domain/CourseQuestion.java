package fi.coursemate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CourseQuestion {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)	
	private long id;
    
	private String title;
	private int questionorder;

    @ManyToOne
	@JoinColumn(name = "courseid")
	private Course course;

    public CourseQuestion() {}
    
	public CourseQuestion(Course course) {
		super();
		this.course = course;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getQuestionorder() {
		return questionorder;
	}

	public void setQuestionorder(int questionorder) {
		this.questionorder = questionorder;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	
}
