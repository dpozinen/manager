package dpozinen.manager.util;

import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.order.OrderState;
import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.Role;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.repo.OrderRepo;
import dpozinen.manager.repo.UserRepo;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author dpozinen
 */
@Component
public class DataInit {

	private final UserRepo userRepo;
	private final PasswordEncoder encoder;
	private OrderRepo orderRepo;

	public DataInit(UserRepo userRepo, OrderRepo orderRepo) {
		this.userRepo = userRepo;
		this.orderRepo = orderRepo;
		this.encoder = new BCryptPasswordEncoder();
	}

	@EventListener
	public void populateOrdersAndUsers(ContextRefreshedEvent event) {
		Worker me = (Worker) new Worker().setSalary(BigDecimal.TEN).setRole(Role.DEV).setName("Dar")
											  .setLastName("Poz").setPassword(encoder.encode("123"))
											  .setUsername("dpozinen").setFatherName("Andrii")
											  .setPhone("+38(050) 385 0660");

		Stream.generate(() -> randOrder().setClient(randClient())).limit(10).forEach(me::addOrder);
		userRepo.save(me);

		Stream.generate(this::randOrderWithClientAndWorker).limit(100).forEach(orderRepo::save);
	}

	private Order randOrder() {
		String notes = IntStream.generate(() -> ThreadLocalRandom.current().nextInt(97, 122)).limit(100)
								.mapToObj(i -> String.valueOf(((char) i)))
								.collect(Collectors.joining());

		return new Order().setCreatedDate(dateGen()).setWorkState(workStateGen())
						  .setPayState(payStateGen()).setPrice(numGen())
						  .setNotes(notes);
	}

	private Order randOrderWithClientAndWorker() {
		return randOrder().setWorker(randWorker()).setClient(randClient());
	}

	private Worker randWorker() {
		return complete(new Worker().setSalary(numGen(1000)));
	}

	private Client randClient() {
		return complete(new Client().setDiscountPercentage(numGen(100).floatValue()));
	}

	@SuppressWarnings("unchecked")
	private <T extends User> T complete(T t) {
		return (T) t.setFatherName(nameGen()).setLastName(surnameGen()).setName(nameGen())
					.setEmail("%s@gmail.com".formatted(numGen().intValue()))
					.setPhone(phoneGen());
	}

	private String phoneGen() {
		return "+38(%d) %d %d".formatted(numGen(999).intValue(), numGen(999).intValue(), numGen(9999).intValue());
	}

	private String nameGen() {
		List<String> names = List.of("Kate", "Austin", "Biff ", "Bo", "Bode", "Braylen", "Bronx", "Brooklyn");
		return names.get(ThreadLocalRandom.current().nextInt(1, names.size()));
	}

	private String surnameGen() {
		List<String> names = List.of("Baggins", "The Grey", "Bane ", "Bale", "Jackson", "Smith", "Gyllenhaal", "Cruise");
		return names.get(ThreadLocalRandom.current().nextInt(names.size()));
	}

	private BigDecimal numGen() {
		return numGen(10_000);
	}

	private BigDecimal numGen(double bound) {
		return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(bound));
	}

	private LocalDateTime dateGen() {
		int y = ThreadLocalRandom.current().nextInt(1000, 2040);
		int mo = ThreadLocalRandom.current().nextInt(1, 12);
		int d = ThreadLocalRandom.current().nextInt(1, 27);
		int h = ThreadLocalRandom.current().nextInt(1, 24);
		int mi = ThreadLocalRandom.current().nextInt(1, 60);
		int s = ThreadLocalRandom.current().nextInt(1, 60);

		return LocalDateTime.of(y, mo, d, h, mi, s);
	}

	private OrderState payStateGen() {
		List<OrderState> states = List.of(OrderState.PAYED, OrderState.NOT_PAYED);
		return states.get(ThreadLocalRandom.current().nextInt(states.size()));
	}

	private OrderState workStateGen() {
		List<OrderState> states = List.of(OrderState.DONE, OrderState.DELAYED, OrderState.IN_PROGRESS, OrderState.QUEUED);
		return states.get(ThreadLocalRandom.current().nextInt(states.size()));
	}

}
