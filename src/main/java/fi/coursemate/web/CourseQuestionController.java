package fi.coursemate.web;

import java.util.List;
import java.util.Optional;

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
import fi.coursemate.domain.SelectedCourse;

@Controller
public class CourseQuestionController {
	@Autowired
    private CourseRepository crepository; 
	
	@Autowired
    private CourseQuestionRepository cqrepository; 		

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "addcoursequestion/{id}")
    public String addCourseQuestion(@PathVariable("id") Long courseid, Model model){
    	Optional<Course> c = crepository.findById(courseid);
    	CourseQuestion cq = new CourseQuestion(c.get());
    	cqrepository.save(cq);
		model.addAttribute("coursequestion", cq);
        return "addCourseQuestion";
    }

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "deletequestion/{id}/{courseid}")
    public String deleteCourseQuestion(@PathVariable("id") Long questionid, @PathVariable("courseid") Long courseid, Model model){
    	cqrepository.deleteById(questionid);
       	return "redirect:/editcourse/" + courseid;
    }	

	
	/**
	 * Copy questions from the selected course
	 * 
	 * @param courseid
	 * @param model
	 * @param selcourse
	 * @return
	 */
	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "copyquestions/{courseid}")
    public String copyQuestions(@PathVariable("courseid") Long courseid, Model model, SelectedCourse selcourse){
       	Optional<Course> course = crepository.findById(courseid);
       	Optional<Course> copyCourse = crepository.findById(selcourse.getSelectedcourseid());
       	List<CourseQuestion> courseQuestions = copyCourse.get().getCoursequestions();
		for (CourseQuestion question : courseQuestions) {
			CourseQuestion q = new CourseQuestion(question.getTitle(), question.getQuestionorder(), course.get());
			cqrepository.save(q);
		}
		crepository.save(course.get());
		return "redirect:/editcourse/" + courseid;
    }		
	
	@PreAuthorize("hasAuthority('ADMIN')")    
    @RequestMapping(value = "savecoursequestion", method = RequestMethod.POST)
    public String save(@RequestParam(value="action", required=true) String action, CourseQuestion courseq){
        long courseid = courseq.getCourse().getCourseid();
		if (action.equals("Save")) {
			if (!courseq.getTitle().isEmpty())
				cqrepository.save(courseq);
			
		}
    	return "redirect:/editcourse/" + courseid;
    }	
}
