package fi.coursemate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import fi.coursemate.domain.Course;
import fi.coursemate.domain.CourseRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CourseTest {
	@Autowired
	private CourseRepository repository;

    @Test
    public void createNewCourse() {
    	Course course = new Course("Test course");
    	repository.save(course);
    	assertThat(course.getCourseid()).isNotNull();
    }    
}
