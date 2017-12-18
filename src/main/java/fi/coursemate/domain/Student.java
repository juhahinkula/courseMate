package fi.coursemate.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)	
	private long id;	 

    @Column(name = "firstname")     
	private String firstName;	

    @Column(name = "lastname")
    private String lastName;

    private String studentNumber;
	private String department;    
    private String email;    

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;
    
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "student_course", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns = { @JoinColumn(name = "courseid") })
	private Set<Course> courses = new HashSet<Course>(0);    
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy="student")
	private List<PeerReview> reviews;
		
	
    public Student() {
    }

	public Student(String studentNumber, String firstName, String lastName, String department, String email) {
		super();
		this.studentNumber = studentNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
  	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<PeerReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<PeerReview> reviews) {
		this.reviews = reviews;
	}

	public Set<Course> getCourses() {
		return this.courses;
	}
	
	public void setCourses(Set<Course> courses) {
		this.courses = courses;
	}
	
	public boolean hasCourse(Course course) {
		for (Course studentCourse: getCourses()) {
			if (studentCourse.getCourseid() == course.getCourseid()) {
				return true;
			}
		}
		return false;
	}	
}
