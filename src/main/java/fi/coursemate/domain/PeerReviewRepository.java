package fi.coursemate.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PeerReviewRepository extends CrudRepository<PeerReview, Long> {
  
	List<PeerReview> findByStudentidAndCourseid(Long studentid, Long courseid);
	
}
