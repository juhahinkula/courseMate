package fi.coursemate.domain;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Long> {

	@Query(value="Select * from question where reviewid in (select reviewid from peer_review where courseid=?1);", nativeQuery = true)
	List<Question> findByCoursecode(long courseid);
	
	List<Question> findByReview(PeerReview review);
}
