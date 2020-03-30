package dpozinen.manager.service.user;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author dpozinen
 */
public interface UserService {

	Set<User> users();
	Set<Worker> workers();
	Set<Client> clients();

	Client save(Client client);
	Worker save(Worker worker);

	Optional<User> getById(Long id);
	Optional<User> getByUsername(String username);
	Optional<User> getByEmail(String email);

	void saveClient(Map<String, String> client);
}
