package fi.coursemate;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;
import fi.coursemate.domain.Student;
import fi.coursemate.domain.StudentRepository;
import fi.coursemate.domain.User;
import fi.coursemate.domain.UserRepository;

/**
 * CourseMate
 * 
 * @author Juha Hinkula
 *
 */
@SpringBootApplication
public class CoursemateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoursemateApplication.class, args);
	}
	
	/**
	 * Save students and courses to H2 DB for testing
	 * @param repository
	 * @return
	 */
	@Bean
	public CommandLineRunner demo(StudentRepository repository, CourseRepository crepository, UserRepository urepository) {
		return (args) -> {
			// save students
			Student student1 = new Student("A2323", "John", "Johnson", "IT", "john@john.com"); 
			Student student2 = new Student("A1123", "Mary", "Robinson", "IT", "mary@robinson.com");
			Student student3 = new Student("A3323", "Steve", "Stevens", "IT", "steve.stevent@st.com");
			repository.save(new Student("C4402", "Kate", "Keystone", "Nursery","kate@kate.com"));
			repository.save(new Student("B0701", "Diana", "Doll", "Business","diana@doll.com"));
			
			Course course1 = new Course("Programming Java");
			Course course2 = new Course("Spring Boot basics");
			crepository.save(new Course("Marketing 1"));
			crepository.save(new Course("Marketing 2"));
			
			crepository.save(course1);
			crepository.save(course2);
			
			Set<Course> courses = new HashSet<Course>();
			courses.add(course1);
			courses.add(course2);
			
			student1.setCourses(courses); 
			repository.save(student1);
			
			student2.setCourses(courses);
			repository.save(student2);

			Set<Course> courses2 = new HashSet<Course>();			
			courses2.add(course1);
			
			student3.setCourses(courses2);
			repository.save(student3);
			
			// Create users with BCrypt encoded password (user/user, admin/admin)
			User user1 = new User("user", "$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6", "USER");
			User user2 = new User("admin", "$2a$08$bCCcGjB03eulCWt3CY0AZew2rVzXFyouUolL5dkL/pBgFkUH9O4J2", "ADMIN");
			urepository.save(user1);
			urepository.save(user2); 
		};	
	}
}
