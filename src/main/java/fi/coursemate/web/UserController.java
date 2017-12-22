package fi.coursemate.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

import fi.coursemate.domain.SignupForm;
import fi.coursemate.domain.SignupTeacherForm;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;
import fi.coursemate.domain.User;
import fi.coursemate.domain.UserRepository;

@Controller
public class UserController {
	@Autowired
    private UserRepository repository; 

	@Autowired
    private StudentRepository srepository; 
		
    @RequestMapping(value = "signup")
    public String addStudent(Model model){
    	model.addAttribute("signupform", new SignupForm());
        return "signup";
    }	

	@PreAuthorize("hasAuthority('SUPERUSER')")    
    @RequestMapping(value = "signupteacher")
    public String addTeacher(Model model){
    	model.addAttribute("signupform", new SignupForm());
        return "signupTeacher";
    }	   
    
    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult) {
    	if (!bindingResult.hasErrors()) { // validation errors
    		if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) { // check password match		
	    		String pwd = signupForm.getPassword();
		    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		    	String hashPwd = bc.encode(pwd);
	
		    	User newUser = new User();
		    	newUser.setPasswordHash(hashPwd);
		    	newUser.setUsername(signupForm.getUsername());
		    	newUser.setRole("USER");
		    	
		    	Student newStudent = new Student(signupForm.getStudentnumber(), signupForm.getFirstName(), signupForm.getLastName(), signupForm.getDepartment(), signupForm.getEmail());
		    	newStudent.setUser(newUser);
		    	
		    	if (repository.findByUsername(signupForm.getUsername()) == null) {
		    		repository.save(newUser);
			    	srepository.save(newStudent);
		    	}
		    	else {
	    			bindingResult.rejectValue("username", "error.userexists", "Username already exists");    	
	    			return "signup";		    		
		    	}
    		}
    		else {
    			bindingResult.rejectValue("passwordCheck", "error.pwdmatch", "Passwords does not match");    	
    			return "signup";
    		}
    	}
    	else {
    		return "signup";
    	}
    	return "redirect:/login?signup=true";    	
    }    

    @RequestMapping(value = "saveteacher", method = RequestMethod.POST)
    public String saveteacher(@Valid @ModelAttribute("signupform") SignupTeacherForm signupForm, BindingResult bindingResult) {
    	if (!bindingResult.hasErrors()) { // validation errors
    		if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) { // check password match		
	    		String pwd = signupForm.getPassword();
		    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		    	String hashPwd = bc.encode(pwd);
	
		    	User newUser = new User();
		    	newUser.setPasswordHash(hashPwd);
		    	newUser.setUsername(signupForm.getUsername());
		    	newUser.setRole("ADMIN");
		    			    	
		    	if (repository.findByUsername(signupForm.getUsername()) == null) {
		    		repository.save(newUser);
		    	}
		    	else {
	    			bindingResult.rejectValue("username", "error.userexists", "Username already exists");    	
	    			return "signup";		    		
		    	}
    		}
    		else {
    			bindingResult.rejectValue("passwordCheck", "error.pwdmatch", "Passwords does not match");    	
    			return "signup";
    		}
    	}
    	else {
    		return "signup";
    	}
    	return "redirect:/login?signup=true";    	
    }        
}
