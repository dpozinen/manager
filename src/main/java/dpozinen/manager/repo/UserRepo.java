package dpozinen.manager.repo;

import dpozinen.manager.model.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author dpozinen
 */
@Repository
public interface UserRepo extends CrudRepository<User, Long> {

	Optional<User> findByName(String name);
	Optional<User> findByLastName(String name);
	Optional<User> findByFatherName(String name);
	Optional<User> findByPhone(String phone);

}
