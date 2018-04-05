package fi.coursemate.web;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.PeerReview;
import fi.coursemate.domain.PeerReviewRepository;
import fi.coursemate.domain.Question;
import fi.coursemate.domain.QuestionRepository;
import fi.coursemate.domain.QuestionRequest;
import fi.coursemate.domain.Response;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;
import fi.coursemate.domain.UserRepository;

/**
 * Rest controller for DataTables
 * 
 * @author Juha Hinkula
 */
@RestController
public class CoursemateRestController {
	@Autowired
    private StudentRepository repository; 

	@Autowired
    private CourseRepository crepository; 

	@Autowired
    private PeerReviewRepository prepository; 

	@Autowired
    private UserRepository urepository; 	
	
	@Autowired
    private QuestionRepository qrepository; 	
		
    @RequestMapping(value = "getstudents", method = RequestMethod.GET)
    public @ResponseBody List<Student> getStudents() {
            return (List<Student>)repository.findAll();
    }  
    
	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "getreviews", method = RequestMethod.GET)
    public @ResponseBody List<PeerReview> getReviews() {
        return (List<PeerReview>)prepository.findAll();
    }  

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "getquestions", method = RequestMethod.GET)
    public @ResponseBody List<Question> getQuestions() {
        return (List<Question>)qrepository.findAll();
    } 	
	
	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "getcoursereview/{id}", method = RequestMethod.GET)
    public @ResponseBody List<PeerReview> getCourseReviews(@PathVariable("id") long courseid) {
		Optional<Course> course = crepository.findById(courseid);
        return (List<PeerReview>)prepository.findByCourseOrderByStudentAscCourseAsc(course.get());
    }  

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "getcoursequetions/{id}", method = RequestMethod.GET)
    public @ResponseBody List<Question> getCourseQuestions(@PathVariable("id") long courseid) {
        return (List<Question>)qrepository.findByCoursecode(courseid);
    }  	
	
    @RequestMapping(value = "savereviewresult", method = RequestMethod.POST)
	public Response postCustomer(@RequestBody QuestionRequest question) {	
    	Response response = null;
    	Question q = null;
    	try {
	    	q = new Question();
	    	q.setId(Long.parseLong(question.getId()));
	    	q.setDescription(question.getDesc());
	    	q.setGrade(Integer.parseInt(question.getGrade()));
	    	q.setTitle(question.getTitle());
	    	q.setReview(prepository.findById(Long.parseLong(question.getReviewid())).get());
	  
	    	qrepository.save(q);
			response = new Response("Success", q);
    	} catch(Exception E) {
			response = new Response("Error", q);    		
    	} finally {
    		return response;    		
    	}
	}
	
	
    /**
     * Get logged in userid and fetch courses they are linked
     * If user has role ADMIN then show all course by createdBy
     * 
     * @return
     */
    @RequestMapping(value = "getcourses", method = RequestMethod.GET)
    public @ResponseBody List<Course> getCourses() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String userName = authentication.getName();
    	authentication.getAuthorities();
    	long id = urepository.findByUsername(userName).getId();
    	List<Course> courses = null;
    	if (hasRole("ADMIN")) {
    		courses = (List<Course>)crepository.findByCreatedByAndStatus(userName, "OPEN");
    	}
    	else if (hasRole("SUPERUSER")) {
    		courses = (List<Course>)crepository.findAll();    		
    	}
    	else {
    		courses = (List<Course>)crepository.findByCourseMember(id);
    	}
    	
    	return courses;
    }  

    /**
     * Get all courses
     * 
     * @return
     */
    @RequestMapping(value = "getallcourses", method = RequestMethod.GET)
    public @ResponseBody List<Course> getAllCourses() {    	
    	return (List<Course>)crepository.findByStatus("OPEN");
    }    
    
    /**
     * Check if user has role given in parameter
     * 
     * @param role
     * @return
     */
    private boolean hasRole(String role) {
    	Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)
    	SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    	boolean hasRole = false;
    	for (GrantedAuthority authority : authorities) {
    		hasRole = authority.getAuthority().equals(role);
    	    if (hasRole) {
    		  break;
    	    }
    	 }
    	 return hasRole;
    }      
}
