package fi.coursemate.web;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;
import fi.coursemate.domain.User;
import fi.coursemate.domain.UserRepository;

@Controller
public class StudentController {
	@Autowired
    private StudentRepository repository; 

	@Autowired
    private CourseRepository crepository; 		

	@Autowired
    private UserRepository urepository; 
	
	@RequestMapping("/login")
	public String login() {
    	return "login";
    }	
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping("/students")
	public String students(Model model) {
    	return "students";
    }

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "add")
    public String addStudent(Model model){
    	model.addAttribute("student", new Student());
        return "addStudent";
    }	

    @RequestMapping(value = "/edit/{id}")
    public String editStudent(@PathVariable("id") Long studentId, Model model){
    	model.addAttribute("student", repository.findOne(studentId));
        return "editStudent";
    }	    
    
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(Student student){
        repository.save(student);
    	return "redirect:/students";
    }
    
	@PreAuthorize("hasAuthority('ADMIN')")    
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable("id") Long studentId, Model model) {
    	repository.delete(studentId);
        return "redirect:/students";
    }    
    
	/**
	 * Admin user add course to student
	 * 
	 * @param studentId
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "addStudentCourse/{id}", method = RequestMethod.GET)
    public String addCourse(@PathVariable("id") Long studentId, Model model) {
    	model.addAttribute("courses", crepository.findAll());
		model.addAttribute("student", repository.findOne(studentId));
    	return "addStudentCourse";
    }
    
	/**
	 * Student course enrollment
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasAuthority('USER')")	
    @RequestMapping(value = "enrollCourse", method = RequestMethod.GET)
    public String addCourse(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication.getName();  
    	System.out.println("USER: " + username);
    	User user = urepository.findByUsername(username);
    	Student student = user.getStudent();
    	model.addAttribute("courses", crepository.findAll());
		model.addAttribute("student", repository.findOne(student.getId()));
		return "addStudentCourse";
    }

	
    @RequestMapping(value="/student/{id}/{courseId}", method=RequestMethod.GET)
	public String studentsAddCourse(@PathVariable Long id, @PathVariable Long courseId, Model model) {
		Course course = crepository.findOne(courseId);
		Student student = repository.findOne(id);

		if (student != null) {
			if (!student.hasCourse(course)) {
				student.getCourses().add(course);
			}
			repository.save(student);
			model.addAttribute("student", crepository.findOne(id));
			model.addAttribute("courses", crepository.findAll());
			if (hasRole("ADMIN"))
				return "redirect:/students";
			else
				return "redirect:/courses";				
		}

		model.addAttribute("developers", repository.findAll());
		if (hasRole("ADMIN"))
			return "redirect:/students";
		else
			return "redirect:/courses";		
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
