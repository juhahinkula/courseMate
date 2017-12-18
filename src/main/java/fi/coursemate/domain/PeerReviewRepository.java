package fi.coursemate.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PeerReviewRepository extends CrudRepository<PeerReview, Long> {
  
	List<PeerReview> findByStudentAndCourseid(Student studentid, Long courseid);
	
}
