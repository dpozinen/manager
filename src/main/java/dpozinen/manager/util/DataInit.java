package dpozinen.manager.util;

import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.order.OrderState;
import dpozinen.manager.model.user.Role;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.repo.OrderRepo;
import dpozinen.manager.repo.UserRepo;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dpozinen
 */
@Component
public class DataInit {

	private final UserRepo userRepo;
	private final OrderRepo orderRepo;
	private final PasswordEncoder encoder;

	public DataInit(UserRepo userRepo, OrderRepo orderRepo, PasswordEncoder encoder) {
		this.userRepo = userRepo;
		this.orderRepo = orderRepo;
		this.encoder = encoder;
	}

	@EventListener
	public void populateOrdersAndUsers(ContextRefreshedEvent event) {
		Worker workerA = (Worker) new Worker().setSalary(BigDecimal.ZERO).setRole(Role.USER).setName("Kate").setLastName("Park")
											  .setPassword(encoder.encode("123")).setUsername("immkath")
											  .setFatherName("Andrii").setPhone("+38(066) 207 0746");

		Worker workerB = (Worker) new Worker().setSalary(BigDecimal.TEN).setRole(Role.ADMIN).setName("Dar").setLastName("Poz")
											  .setPassword(encoder.encode("123")).setUsername("dpozinen")
											  .setFatherName("Andrii").setPhone("+38(050) 385 0660");

		Order orderA = new Order().setIsPayed(false).setPrice(BigDecimal.valueOf(12)).setState(OrderState.QUEUED);
		Order orderB = new Order().setIsPayed(true).setPrice(BigDecimal.valueOf(12)).setState(OrderState.DELAYED);
		workerA.getOrders().add(orderA);
		workerA.getOrders().add(orderB);

		orderRepo.save(orderA);
		orderRepo.save(orderB);

		userRepo.save(workerA);
		userRepo.save(workerB);
	}
}
