package fi.coursemate.web;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;
import fi.coursemate.domain.UserRepository;

@RestController
public class CoursemateRestController {
	@Autowired
    private StudentRepository repository; 

	@Autowired
    private CourseRepository crepository; 

	@Autowired
    private UserRepository urepository; 	
	
    @RequestMapping(value = "getstudents", method = RequestMethod.GET)
    public @ResponseBody List<Student> getStudents() {
            return (List<Student>)repository.findAll();
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
