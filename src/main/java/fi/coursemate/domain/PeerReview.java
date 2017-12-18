package fi.coursemate.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
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
	
	@CreatedDate
	private Date createdDate;	

	@CreatedBy
	private String createdBy;
	
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
