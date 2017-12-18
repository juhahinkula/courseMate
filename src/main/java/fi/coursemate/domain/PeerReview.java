package fi.coursemate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PeerReview {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)	
	private long reviewid;
    
    @ManyToOne
	@JoinColumn(name = "id")
	private Student student;
    
    private long courseid;
    private int grade;
    private String description;
	
    public PeerReview() {}
    
    public PeerReview(Student student, long courseid) {
		super();
		this.student = student;
		this.courseid = courseid;
	}
	
	public long getReviewid() {
		return reviewid;
	}

	public void setReviewid(long reviewid) {
		this.reviewid = reviewid;
	}

	public Student getStudent() {
		return student;
	}
	
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public long getCourseid() {
		return courseid;
	}
	
	public void setCourseid(long courseid) {
		this.courseid = courseid;
	}
	
	public int getGrade() {
		return grade;
	}
	
	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
