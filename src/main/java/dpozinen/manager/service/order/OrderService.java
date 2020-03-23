package dpozinen.manager.service.order;

import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.user.User;

import java.util.Map;
import java.util.Set;

/**
 * @author dpozinen
 */
public interface OrderService {

	Set<Order> orders();
	Set<Order> ordersOf(User user);
	Set<Order> ordersOfUserById(Long id);

	Order getById(Long id);

	Order save(Order order);
	Order edit(Order order);
	Order edit(Map<String, Object> order);
}
