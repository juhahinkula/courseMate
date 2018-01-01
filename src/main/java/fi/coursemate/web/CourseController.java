package fi.coursemate.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseQuestion;
import fi.coursemate.domain.CourseQuestionRepository;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.PeerReview;
import fi.coursemate.domain.PeerReviewRepository;
import fi.coursemate.domain.Question;
import fi.coursemate.domain.QuestionRepository;
import fi.coursemate.domain.Response;
import fi.coursemate.domain.SelectedCourse;
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

	@Autowired
    private QuestionRepository qrepository;	

	@Autowired
    private CourseQuestionRepository cqrepository;	
	
	@RequestMapping("/courses")
	public String index(Model model) {
    	return "courses";
    }

	/**
	 * Get students by Course/Group
	 * 
	 * @param courseid
	 * @param model
	 * @return
	 */
	@RequestMapping("/coursestudents/{id}")
	public String coursestudents(@PathVariable("id") Long courseid, Model model) {
		Course course = crepository.findOne(courseid);
		model.addAttribute("students", course.getStudents());
		model.addAttribute("courseid", courseid);
		return "coursestudents";
    }	

	/**
	 * Archive course
	 * 
	 * @param courseid
	 * @param model
	 * @return
	 */
	@RequestMapping("/archivecourse/{id}")
	public String archiveCourse(@PathVariable("id") Long courseid, Model model) {
		Course course = crepository.findOne(courseid);
		course.setStatus("CLOSED");
		crepository.save(course);
    	return "redirect:/courses";
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
		Course course = crepository.findOne(courseId);
		// Course list for copy question functionality
		List<Course> courses = (List<Course>)crepository.findByStatus("OPEN");
    	model.addAttribute("course", course);
    	model.addAttribute("courses", courses);
    	// Copy course drop down list
    	model.addAttribute("selectedCourse", new SelectedCourse());
    	model.addAttribute("questions", cqrepository.findByCourseOrderByQuestionorder(course));
        return "editCourse";
    }	    

	/**
	 * Enroll (or join) to group/course
	 * 
	 * @param model
	 * @return
	 */
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
    public String save(@RequestParam(value="action", required=true) String action, Course course){
        if (action.equals("Save")) {
		crepository.save(course);
		}
    	return "redirect:/courses";
    }
    
	@PreAuthorize("hasAuthority('ADMIN')")	
    @RequestMapping(value = "/deletecourse/{id}", method = RequestMethod.GET)
    public String deleteCourse(@PathVariable("id") Long courseId, Model model) {
    	crepository.delete(courseId);
        return "redirect:/courses";
    }     

	@PreAuthorize("hasAuthority('ADMIN')")		
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
    	System.out.println("Courseid: " + c.getCourseid());
    	// Get logged in user = reviewer
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String reviewer = authentication.getName();
    	List<PeerReview> reviews = prepository.findByStudentAndCourseAndCreatedBy(s, c, reviewer);
    	PeerReview review;
    	Question question = null;
    	List<Question> questions = new ArrayList<Question>(); 
    	// Check if review already exist
    	if (!reviews.isEmpty()) {
    		review = reviews.get(0);
    		// find questions
    		questions = qrepository.findByReview(review);
    		review.setQuestions(questions);
    	}
    	else {
    		review = new PeerReview(s, c);
    		prepository.save(review);

    		// Add questions according to course rules
    		List<CourseQuestion> coursequestions = cqrepository.findByCourseOrderByQuestionorder(c);
    		
    		for (CourseQuestion coursequestion : coursequestions) {
    	   		question = new Question("", coursequestion.getTitle(), review);
        		qrepository.save(question);
        		questions.add(question);    	   	    			
    		}
    		review.setQuestions(questions);
    	}

    	model.addAttribute("review", review);
    	model.addAttribute("questions", questions);
    	return "review";
    }	

    /**
     * Save Peer-review
     * 
     * @param review
     * @return
     */
    @RequestMapping(value = "savereview", method = RequestMethod.POST)
    public String save(@RequestParam(value="action", required=true) String action, PeerReview review) {
		Long courseid = review.getCourse().getCourseid();
		System.out.println("Course: " + courseid.toString());
    	if (action.equals("Save")) {
        	prepository.save(review);
    	}
    	return "redirect:/coursestudents/" + Long.toString(courseid);
    }

    /**
     * Save Peer-review result
     * 
     * @param review
     * @return
     */
    @RequestMapping(value = "savequestion", method = RequestMethod.POST)
    public String saveResult(@RequestParam(value="action", required=true) String action, Question question) {
		Long courseid = question.getReview().getCourse().getCourseid(); 
		if (action.equals("Save")) {
        	qrepository.save(question);
    	}
    	return "redirect:/coursestudents/" + Long.toString(courseid);
    }    


    /**
     * Save Peer-review results
     * 
     * @param review
     * @return
     */
    @RequestMapping(value = "savereviewresults", method = RequestMethod.POST)
    public String saveResult() {
		//System.out.println("Saving Results: " + reviews.getQuestions().size());

    	return "redirect:/coursestudents/1"; // + Long.toString(courseid);
    }     
    
	/**
	 * Show reviews by course
	 * @param courseId
	 * @param model
	 * @return
	 */
	@RequestMapping("/reviews/{courseid}")
	public String courseReviews(@PathVariable("courseid") Long courseId, Model model) {
		Course course = crepository.findOne(courseId);
		List<PeerReview> reviews = (List<PeerReview>) prepository.findByCourseOrderByStudentAscCourseAsc(course);
		model.addAttribute("reviews", reviews);
    	return "coursereviews";
    }

	/**
	 * Show questions by course
	 * @param courseId
	 * @param model
	 * @return
	 */
	@RequestMapping("/questions/{courseid}")
	public String courseQuestions(@PathVariable("courseid") Long courseId, Model model) {
		Course course = crepository.findOne(courseId);
		List<Question> questions = (List<Question>)qrepository.findByCoursecode(courseId);
		model.addAttribute("questions", questions);
    	return "coursereviews";
    }	
}
