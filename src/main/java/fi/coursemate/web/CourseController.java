package fi.coursemate.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import fi.coursemate.domain.PeerReview;
import fi.coursemate.domain.PeerReviewRepository;
import fi.coursemate.domain.Question;
import fi.coursemate.domain.QuestionRepository;
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
		Optional<Course> course = crepository.findById(courseid);
		model.addAttribute("students", course.get().getStudents());
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
	@PreAuthorize("hasAnyAuthority('ADMIN' ,'SUPERUSER')")	
	@RequestMapping("/archivecourse/{id}")
	public String archiveCourse(@PathVariable("id") Long courseid, Model model) {
		Optional<Course> course = crepository.findById(courseid);
		course.get().setStatus("CLOSED");
		crepository.save(course.get());
    	return "redirect:/courses";
    }		
	
	@PreAuthorize("hasAnyAuthority('ADMIN' ,'SUPERUSER')")
    @RequestMapping(value = "addcourse")
    public String addCourse(Model model){
    	model.addAttribute("course", new Course());
        return "addCourse";
    }	

	@PreAuthorize("hasAnyAuthority('ADMIN' ,'SUPERUSER')")	
    @RequestMapping(value = "/editcourse/{id}")
    public String editCourse(@PathVariable("id") Long courseId, Model model){
		Optional<Course> course = crepository.findById(courseId);
		// Course list for copy question functionality
		List<Course> courses = (List<Course>)crepository.findByStatus("OPEN");
    	model.addAttribute("course", course.get());
    	model.addAttribute("courses", courses);
    	// Copy course drop down list
    	model.addAttribute("selectedCourse", new SelectedCourse());
    	model.addAttribute("questions", cqrepository.findByCourseOrderByQuestionorder(course.get()));
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
    	System.out.println("Username: " + username);
    	User user = urepository.findByUsername(username);
    	System.out.println("Username2: " + user.getUsername());    	
    	Student student = repository.findByUser(user);
    	System.out.println("Student email: " + student.getEmail());

    	model.addAttribute("student", student);
    	model.addAttribute("courses", crepository.findAll());
    	return "enrollCourse";
    }	
	
	@PreAuthorize("hasAnyAuthority('ADMIN' ,'SUPERUSER')") 
    @RequestMapping(value = "savecourse", method = RequestMethod.POST)
    public String save(@RequestParam(value="action", required=true) String action, Course course){
        if (action.equals("Save")) {
		crepository.save(course);
		}
    	return "redirect:/courses";
    }
    
	@PreAuthorize("hasAnyAuthority('SUPERUSER')")
    @RequestMapping(value = "/deletecourse/{id}", method = RequestMethod.GET)
    public String deleteCourse(@PathVariable("id") Long courseId, Model model) {
    	crepository.deleteById(courseId);
        return "redirect:/courses";
    }     

	@PreAuthorize("hasAnyAuthority('ADMIN' ,'SUPERUSER')")	
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
    	Optional<Student> s = repository.findById(studentId);
    	Optional<Course> c = crepository.findById(courseId);
    	// Get logged in user = reviewer
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String reviewer = authentication.getName();
    	List<PeerReview> reviews = prepository.findByStudentAndCourseAndCreatedBy(s.get(), c.get(), reviewer);
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
    		review = new PeerReview(s.get(), c.get());
    		prepository.save(review);

    		// Add questions according to course rules
    		List<CourseQuestion> coursequestions = cqrepository.findByCourseOrderByQuestionorder(c.get());
    		
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
		Optional<Course> course = crepository.findById(courseId);
		List<PeerReview> reviews = (List<PeerReview>) prepository.findByCourseOrderByStudentAscCourseAsc(course.get());
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
		List<Question> questions = (List<Question>)qrepository.findByCoursecode(courseId);
		model.addAttribute("questions", questions);
    	return "coursereviews";
    }	
}
