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

	Client saveClient(Client client);
	Worker saveWorker(Worker worker);

	Client getClient(Long id);
	Worker getWorker(Long id);
}
