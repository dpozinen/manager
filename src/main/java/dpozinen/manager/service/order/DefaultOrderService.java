package dpozinen.manager.service.order;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.user.User;
import dpozinen.manager.repo.OrderRepo;
import dpozinen.manager.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;

/**
 * @author dpozinen
 */
@Service
@Slf4j
public class DefaultOrderService implements OrderService {

	private final OrderRepo orderRepo;
	private final UserRepo userRepo;

	public DefaultOrderService(OrderRepo orderRepo, UserRepo userRepo) {
		this.orderRepo = orderRepo;
		this.userRepo = userRepo;
	}

	@Override
	public Set<Order> orders() {
		return StreamSupport.stream(orderRepo.findAll().spliterator(), false).collect(toSet());
	}

	@Override
	public Set<Order> ordersOf(User user) {
		return user.getOrders();
	}

	@Override
	public Set<Order> ordersOfUserById(Long id) {
		return userRepo.findById(id).map(User::getOrders).orElseGet(HashSet::new);
	}

	@Override
	public Optional<Order> getById(Long id) {
		return orderRepo.findById(id);
	}

	@Override
	public Optional<Order> save(Order order) {
		log.debug("Saved order with id " + order.getId());
		return Optional.of(orderRepo.save(order));
	}

	@Override
	public boolean edit(Map<String, Object> order) {
		Gson gson = new Gson();
		try {
			Order parsed = gson.fromJson(gson.toJson(order), Order.class);
			Optional.ofNullable(parsed)
					.flatMap(o -> getById(o.getId())
									.map(found -> found.setPayState(o.getPayState())
													   .setWorkState(o.getWorkState())
													   .setNotes(o.getNotes()).setPrice(o.getPrice())
					)).ifPresent(this::save);
			return true;
		} catch (JsonSyntaxException e) {
			log.warn("Could not process edit of order:" + order);
			return false;
		}
	}

	@Override
	public Optional<Order> edit(Order order) {
		throw new UnsupportedOperationException("Edits not implemented yet");
	}
}
