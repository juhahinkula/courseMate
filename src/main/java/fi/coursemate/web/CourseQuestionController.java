package fi.coursemate.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseQuestion;
import fi.coursemate.domain.CourseQuestionRepository;
import fi.coursemate.domain.CourseRepository;

@Controller
public class CourseQuestionController {
	@Autowired
    private CourseRepository crepository; 
	
	@Autowired
    private CourseQuestionRepository cqrepository; 		

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "addcoursequestion/{id}")
    public String addCourseQuestion(@PathVariable("id") Long courseid, Model model){
    	Course c = crepository.findOne(courseid);
    	CourseQuestion cq = new CourseQuestion(c);
    	cqrepository.save(cq);
		model.addAttribute("coursequestion", cq);
        return "addCourseQuestion";
    }
	
	@PreAuthorize("hasAuthority('ADMIN')")    
    @RequestMapping(value = "savecoursequestion", method = RequestMethod.POST)
    public String save(@RequestParam(value="action", required=true) String action, CourseQuestion courseq){
        System.out.println("Course: " + courseq.getTitle());
		if (action.equals("Save")) {
        	cqrepository.save(courseq);
		}
    	return "redirect:/editcourse/" + courseq.getCourse().getCourseid();
    }	
}
