package fi.coursemate.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PeerReviewRepository extends CrudRepository<PeerReview, Long> {
  
	List<PeerReview> findByStudentAndCourseid(Student student, Long courseid);

	List<PeerReview> findByStudentAndCourseidAndCreatedBy(Student student, Long courseid, String reviewer);

	List<PeerReview> findAllByOrderByStudentAsc();
	
}
