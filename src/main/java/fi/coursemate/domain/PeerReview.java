package fi.coursemate.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PeerReview {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)	
	private long id;
    
    private long studentid;
    private long courseid;
    private int grade;
    private String description;
	
    public PeerReview() {}
    
    public PeerReview(long studentid, long courseid) {
		super();
		this.studentid = studentid;
		this.courseid = courseid;
	}

	public long getId() {
		return id;
	}
	
    public void setId(long id) {
		this.id = id;
	}
	
	public long getStudentid() {
		return studentid;
	}
	
	public void setStudentid(long studentid) {
		this.studentid = studentid;
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
