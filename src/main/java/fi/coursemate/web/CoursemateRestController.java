package fi.coursemate.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;

@RestController
public class CoursemateRestController {
	@Autowired
    private StudentRepository repository; 

	@Autowired
    private CourseRepository crepository; 
	
    @RequestMapping(value = "getstudents", method = RequestMethod.GET)
    public @ResponseBody List<Student> getStudents() {
            return (List<Student>)repository.findAll();
    }  

    @RequestMapping(value = "getcourses", method = RequestMethod.GET)
    public @ResponseBody List<Course> getCourses() {
            return (List<Course>)crepository.findAll();
    }  
}
