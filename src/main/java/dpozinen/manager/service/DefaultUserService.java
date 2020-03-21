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
							.filter(User::isWorker)
							.map(User::toWorker)
							.collect(toSet());
	}

	@Override
	public Set<Client> clients() {
		return StreamSupport.stream(userRepo.findAll().spliterator(), false)
							.filter(User::isClient)
							.map(User::toClient)
							.collect(toSet());
	}

	@Override
	public Client save(Client client) {
		return userRepo.save(client);
	}

	@Override
	public Worker save(Worker worker) {
		return userRepo.save(worker);
	}

	@Override
	public User getById(Long id) {
		return userRepo.findById(id).orElseGet(() -> {
			log.warn("Could not find user by id: " + id);
			return null;
		});
	}

	@Override
	public User getByUsername(String username) {
		return userRepo.findByUsername(username).orElseGet(() -> {
			log.warn("Could not find user by username: " + username);
			return null;
		});
	}

	@Override
	public User getByEmail(String email) {
		return userRepo.findByUsername(email).orElseGet(() -> {
			log.warn("Could not find user by email: " + email);
			return null;
		});
	}

}
