package dpozinen.manager.service.order;

import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.user.User;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author dpozinen
 */
public interface OrderService {

	Set<Order> orders();
	Set<Order> ordersOf(User user);
	Set<Order> ordersOfUserById(Long id);

	Optional<Order> getById(Long id);

	Optional<Order> save(Order order);
	Optional<Order> edit(Order order);
	boolean edit(Map<String, Object> order);
	boolean delete(Long id);
}
