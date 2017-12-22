package fi.coursemate.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.PeerReview;
import fi.coursemate.domain.PeerReviewRepository;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;
import fi.coursemate.domain.User;
import fi.coursemate.domain.UserRepository;

@Controller
public class CourseController {
	@Autowired
    private StudentRepository repository; 

	@Autowired
    private CourseRepository crepository; 		

	@Autowired
    private UserRepository urepository; 
	
	@Autowired
    private PeerReviewRepository prepository; 		

	@RequestMapping("/courses")
	public String index(Model model) {
    	return "courses";
    }

	@RequestMapping("/coursestudents/{id}")
	public String coursestudents(@PathVariable("id") Long courseid, Model model) {
		Course course = crepository.findOne(courseid);
		model.addAttribute("students", course.getStudents());
		model.addAttribute("courseid", courseid);
		return "coursestudents";
    }	

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "addcourse")
    public String addCourse(Model model){
    	model.addAttribute("course", new Course());
        return "addCourse";
    }	

	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "/editcourse/{id}")
    public String editCourse(@PathVariable("id") Long courseId, Model model){
    	model.addAttribute("course", crepository.findOne(courseId));
        return "editCourse";
    }	    

    @RequestMapping(value = "/enrollcourse")
    public String enrollCourse(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication.getName();  
    	User user = urepository.findByUsername(username);
    	Student student = repository.findByUser(user);
    	model.addAttribute("student", student);
    	model.addAttribute("courses", crepository.findAll());
    	return "enrollCourse";
    }	
	
	@PreAuthorize("hasAuthority('ADMIN')")    
    @RequestMapping(value = "savecourse", method = RequestMethod.POST)
    public String save(Course course){
        crepository.save(course);
    	return "redirect:/courses";
    }
    
	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "/deletecourse/{id}", method = RequestMethod.GET)
    public String deleteCourse(@PathVariable("id") Long courseId, Model model) {
    	crepository.delete(courseId);
        return "redirect:/courses";
    }     
	
	@RequestMapping("/findreviews")
	public String reviews(Model model) {
    	return "findreviews";
    }	

    /**
     * Create peer review
     * 
     * @param studentId
     * @param courseId
     * @param reviewer
     * @param model
     * @return review view
     */
    @RequestMapping(value = "/review/{id}/{courseid}")
    public String review(@PathVariable("id") Long studentId, @PathVariable("courseid") Long courseId, Model model){
    	Student s = repository.findOne(studentId);
    	Course c = crepository.findOne(courseId);
    	// Get logged in user = reviewer
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String reviewer = authentication.getName();
    	List<PeerReview> reviews = prepository.findByStudentAndCourseidAndCreatedBy(s, courseId, reviewer);
    	PeerReview review;
    	// Check if review already exist
    	if (!reviews.isEmpty())
    		review = reviews.get(0);
    	else
    		review = new PeerReview(s, courseId);
    	model.addAttribute("review", review);
        return "review";
    }	

    @RequestMapping(value = "savereview", method = RequestMethod.POST)
    public String save(PeerReview review) {
    	Long courseid = review.getCourseid();
        prepository.save(review);
    	return "redirect:/coursestudents/" + Long.toString(courseid);
    }

	/**
	 * Show all reviews
	 * @param model
	 * @return
	 */
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping("/reviews")
	public String reviewList(Model model) {
		List<PeerReview> reviews = (List<PeerReview>) prepository.findAllByOrderByStudentAsc();
		model.addAttribute("reviews", reviews);
    	return "reviews";
    }
    
	/**
	 * Show reviews by course
	 * @param courseId
	 * @param model
	 * @return
	 */
	@RequestMapping("/reviews/{courseid}")
	public String courseReviews(@PathVariable("courseid") Long courseId, Model model) {
		List<PeerReview> reviews = (List<PeerReview>) prepository.findByCourseidOrderByStudentAscCourseidAsc(courseId);
		model.addAttribute("reviews", reviews);
    	return "coursereviews";
    }
    	
}
