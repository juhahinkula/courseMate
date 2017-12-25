package fi.coursemate.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
    User findByUsername(String username);

    List<User> findByRole(String role);
}
