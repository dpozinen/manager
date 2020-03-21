package dpozinen.manager.service;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;

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

	User getById(Long id);
	User getByUsername(String username);
	User getByEmail(String email);
}
