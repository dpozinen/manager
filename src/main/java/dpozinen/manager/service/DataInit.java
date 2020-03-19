package dpozinen.manager.service;

import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.order.OrderState;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.repo.OrderRepo;
import dpozinen.manager.repo.UserRepo;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author dpozinen
 */
@Component
public class DataInit {

	private final UserRepo userRepo;
	private final OrderRepo orderRepo;

	public DataInit(UserRepo userRepo, OrderRepo orderRepo) {
		this.userRepo = userRepo;
		this.orderRepo = orderRepo;
	}

	@EventListener
	public void populateOrdersAndUsers(ContextRefreshedEvent event) {
		Worker workerA = (Worker) new Worker().salary(BigDecimal.ZERO).name("Kate").lastName("Park")
											  .fatherName("Andrii").phone("+38(066) 207 0746");

		Worker workerB = (Worker) new Worker().salary(BigDecimal.TEN).name("Dar").lastName("Poz")
											  .fatherName("Andrii").phone("+38(050) 385 0660");

		Order orderA = new Order().isPayed(false).price(BigDecimal.valueOf(12)).state(OrderState.QUEUED);
		Order orderB = new Order().isPayed(true).price(BigDecimal.valueOf(12)).state(OrderState.DELAYED);
		workerA.orders().add(orderA);
		workerA.orders().add(orderB);

		orderRepo.save(orderA);
		orderRepo.save(orderB);

		userRepo.save(workerA);
		userRepo.save(workerB);
	}
}
