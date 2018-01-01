package fi.coursemate.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long>  {
    
	List<Course> findByName(String name);

	@Query(value="Select * from course where status='OPEN' and courseid in (Select distinct courseid from student_course where id=?1);", nativeQuery = true)
	List<Course> findByCourseMember(long userid);
 
	List<Course> findByCreatedByAndStatus(String userName, String status);	

	List<Course> findAllByOrderByCreatedDateAsc();	
	
	List<Course> findByStatus(String status);
}

