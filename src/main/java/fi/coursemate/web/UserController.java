package fi.coursemate.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import fi.coursemate.domain.PasswordCheck;
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

	@Autowired
    private UserRepository urepository; 
	
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

    @RequestMapping(value = "editprofile")
    public String editProfile(Model model){
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication.getName();  
    	User user = urepository.findByUsername(username);
    	Student student = srepository.findByUser(user);
    	System.out.println("Student: " + student.getId());
    	model.addAttribute("student", student);
    	model.addAttribute("pwdcheck", new PasswordCheck());    	
        return "editProfile";
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

    /**
     * Change password
     * 
     * @param pwdCheck
     * @return
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public String changePassword(PasswordCheck pwdCheck) {
    	String password = pwdCheck.getPassword();
    	String confirm = pwdCheck.getConfirm();
    	System.out.println("PWD: " + password + " " + confirm);
    	if (!password.equals(confirm)) {
        	return "redirect:/editprofile?nomatch";    		
    	}
    	else if (password.length() < 7) {
        	return "redirect:/editprofile?length";    		
    	}
    	
    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
    	String passwordHash = bc.encode(password);
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String username = authentication.getName();  
    	User user = urepository.findByUsername(username);
    	user.setPasswordHash(passwordHash);
    	repository.save(user);
    	return "redirect:/editprofile?pwdchanged";
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
