package fi.coursemate.web;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.PeerReview;
import fi.coursemate.domain.PeerReviewRepository;
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
    @RequestMapping(value = "getcoursereview/{id}", method = RequestMethod.GET)
    public @ResponseBody List<PeerReview> getCourseReviews(@PathVariable("id") long courseid) {
		Course course = crepository.findOne(courseid);
        return (List<PeerReview>)prepository.findByCourseOrderByStudentAscCourseAsc(course);
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
    		courses = (List<Course>)crepository.findByCreatedBy(userName);
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
    	return (List<Course>)crepository.findAll();
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
