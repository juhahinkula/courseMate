package fi.coursemate.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.PeerReview;
import fi.coursemate.domain.PeerReviewRepository;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;

@Controller
public class CourseController {
	@Autowired
    private StudentRepository repository; 

	@Autowired
    private CourseRepository crepository; 		

	@Autowired
    private PeerReviewRepository prepository; 		

	@RequestMapping("/courses")
	public String index(Model model) {
		List<Course> courses = (List<Course>) crepository.findAll();
		model.addAttribute("courses", courses);
    	return "courses";
    }

	@RequestMapping("/coursestudents/{id}")
	public String coursestudents(@PathVariable("id") Long courseid, Model model) {
		Course course = crepository.findOne(courseid);
		List<Student> students = new ArrayList<Student>();
		students.addAll(course.getStudents());
		model.addAttribute("students", students);
		model.addAttribute("courseid", courseid);
		return "coursestudents";
    }	
	
    @RequestMapping(value = "addcourse")
    public String addCourse(Model model){
    	model.addAttribute("course", new Course());
        return "addCourse";
    }	

    @RequestMapping(value = "/editcourse/{id}")
    public String editCourse(@PathVariable("id") Long courseId, Model model){
    	model.addAttribute("course", crepository.findOne(courseId));
        return "editCourse";
    }	    
    
    @RequestMapping(value = "savecourse", method = RequestMethod.POST)
    public String save(Course course){
        crepository.save(course);
    	return "redirect:/courses";
    }
    
    @RequestMapping(value = "/deletecourse/{id}", method = RequestMethod.GET)
    public String deleteCourse(@PathVariable("id") Long courseId, Model model) {
    	crepository.delete(courseId);
        return "redirect:/courses";
    }       

    /**
     * Create peer review
     * 
     * @param studentId
     * @param courseId
     * @param model
     * @return review view
     */
    @RequestMapping(value = "/review/{id}/{courseid}")
    public String review(@PathVariable("id") Long studentId, @PathVariable("courseid") Long courseId, Model model){
    	List<PeerReview> reviews = prepository.findByStudentidAndCourseid(studentId, courseId);
    	PeerReview review;
    	if (!reviews.isEmpty())
    		review = reviews.get(0);
    	else
    		review = new PeerReview(studentId, courseId);
    	model.addAttribute("review", review);
        return "review";
    }	

    @RequestMapping(value = "savereview", method = RequestMethod.POST)
    public String save(PeerReview review){
        prepository.save(review);
    	return "redirect:/courses";
    }


	@RequestMapping("/reviews")
	public String reviewList(Model model) {
		List<PeerReview> reviews = (List<PeerReview>) prepository.findAll();
		model.addAttribute("reviews", reviews);
    	return "reviews";
    }
    
}
