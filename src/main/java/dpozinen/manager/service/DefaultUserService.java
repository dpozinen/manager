package dpozinen.manager.service;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

/**
 * @author dpozinen
 */
@Service @Slf4j
public class DefaultUserService implements UserService {

	private final UserRepo userRepo;

	public DefaultUserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public Set<User> users() {
		return StreamSupport.stream(userRepo.findAll().spliterator(), false).collect(toSet());
	}

	@Override
	public Set<Worker> workers() {
		return StreamSupport.stream(userRepo.findAll().spliterator(), false)
							.filter(u -> u instanceof Worker)
							.map(u -> (Worker)u)
							.collect(toSet());
	}

	@Override
	public Set<Client> clients() {
		return StreamSupport.stream(userRepo.findAll().spliterator(), false)
							.filter(u -> u instanceof Client)
							.map(u -> (Client)u)
							.collect(toSet());
	}

	@Override
	public Client saveClient(Client client) {
		return userRepo.save(client);
	}

	@Override
	public Worker saveWorker(Worker worker) {
		return userRepo.save(worker);
	}

	@Override
	public Client getClient(Long id) {
		return (Client) userRepo.findById(id).orElseGet(() -> {
			log.warn("Could not find user by id" + id);
			return null;
		});
	}

	@Override
	public Worker getWorker(Long id) {
		return (Worker) userRepo.findById(id).orElseGet(() -> {
			log.warn("Could not find user by id" + id);
			return null;
		});
	}
}
