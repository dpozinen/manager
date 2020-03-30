package dpozinen.manager.service.user;

import com.google.gson.Gson;
import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.repo.UserRepo;
import dpozinen.manager.util.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

/**
 * @author dpozinen
 */
@Service @Slf4j
public class DefaultUserService implements UserService {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private Validator validate;

	public DefaultUserService(UserRepo userRepo, @Lazy PasswordEncoder passwordEncoder, Validator validate) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.validate = validate;
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
		client.setPassword(passwordEncoder.encode(client.getPassword()));
		return userRepo.save(client);
	}

	@Override
	public Worker save(Worker worker) {
		worker.setPassword(passwordEncoder.encode(worker.getPassword()));
		return userRepo.save(worker);
	}

	@Override
	public Optional<User> getById(Long id) {
		return userRepo.findById(id);
	}

	@Override
	public Optional<User> getByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public Optional<User> getByEmail(String email) {
		return userRepo.findByUsername(email);
	}

	@Override
	public void saveClient(Map<String, String> form) {
		var gson = new Gson();

		Client client = gson.fromJson(gson.toJson(form), Client.class);
		save(client);
	}

}
