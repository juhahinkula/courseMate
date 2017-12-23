package fi.coursemate.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PeerReviewRepository extends CrudRepository<PeerReview, Long> {
  
	List<PeerReview> findByStudentAndCourse(Student student, Course course);

	List<PeerReview> findByCourseOrderByStudentAscCourseAsc(Course course);
	
	List<PeerReview> findByStudentAndCourseAndCreatedBy(Student student, Course course, String reviewer);

	List<PeerReview> findAllByOrderByStudentAsc();
	
}
