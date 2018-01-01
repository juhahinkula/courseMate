package fi.coursemate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.coursemate.web.CourseController;
import fi.coursemate.web.CourseQuestionController;
import fi.coursemate.web.StudentController;
import fi.coursemate.web.UserController;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoursemateApplicationTests {
	@Autowired
    private CourseController controller;

	@Autowired
    private StudentController scontroller;

	@Autowired
    private CourseQuestionController qcontroller;

	@Autowired
    private UserController ucontroller;

	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
		assertThat(scontroller).isNotNull();
		assertThat(qcontroller).isNotNull();
		assertThat(ucontroller).isNotNull();		
	}

}
