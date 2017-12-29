package fi.coursemate.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Course / Group entity 
 * Collect students to groups
 * @author Juha Hinkula
 *
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Course {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)	
	private long courseid;

	private String coursecode;
    
    @Column(name="coursename")
	private String name; 
     
	@CreatedDate
	private Date createdDate;	

	@CreatedBy
	private String createdBy;    
    
    @ManyToMany(mappedBy = "courses")    
    private Set<Student> students;  

	@OneToMany(cascade = CascadeType.ALL, mappedBy="course")
    @JsonIgnore
	private List<PeerReview> reviews;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="course")
    @JsonIgnore
	private List<CourseQuestion> coursequestions;	
	
    public Course() {
	}

	public Course(String name) {
		this.name = name;
	}     

	public long getCourseid() {
		return courseid;
	}

	public void setCourseid(long courseid) {
		this.courseid = courseid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
	
	public String getCoursecode() {
		return coursecode;
	}

	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
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

	public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

	public List<PeerReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<PeerReview> reviews) {
		this.reviews = reviews;
	}

	public List<CourseQuestion> getCoursequestions() {
		return coursequestions;
	}

	public void setCoursequestions(List<CourseQuestion> coursequestions) {
		this.coursequestions = coursequestions;
	}	
}
