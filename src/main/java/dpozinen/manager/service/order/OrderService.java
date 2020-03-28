package dpozinen.manager.service.order;

import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.user.User;
import org.springframework.security.core.Authentication;

import java.util.List;
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
	Set<Order> getQueuedOrders();

	Optional<Order> getById(Long id);

	Optional<Order> save(Order order);
	boolean save(Map<String, Object> order, String workerName);
	Optional<Order> edit(Order order);
	boolean edit(Map<String, Object> order);

	boolean delete(Long id);
	void deleteAllDone();

	void deleteDoneOfUser(Optional<User> id);
	void deleteDoneOfUserById(Long id);
	void deleteDoneOfUserByUsername(String username);

	List<Order> getOrders(Map<String, String> params, Authentication auth);
	Map<Integer, Long> getOrdersCount(Map<String, String> params, Authentication auth);
}
