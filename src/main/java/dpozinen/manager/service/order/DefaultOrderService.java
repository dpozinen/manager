package dpozinen.manager.service.order;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dpozinen.manager.model.order.Order;
import dpozinen.manager.model.order.OrderState;
import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.repo.OrderRepo;
import dpozinen.manager.service.user.UserService;
import dpozinen.manager.util.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * @author dpozinen
 */
@Service
@Slf4j
public class DefaultOrderService implements OrderService {

	private final OrderRepo orderRepo;
	private final UserService userService;

	public DefaultOrderService(OrderRepo orderRepo, UserService userService) {
		this.orderRepo = orderRepo;
		this.userService = userService;
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
		return userService.getById(id).map(User::getOrders).orElseGet(HashSet::new);
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
	public boolean save(Map<String, Object> order, String workerName) {
		Gson gson = new Gson();
		try {
			Client[] client = new Client[] { null };
			String receivedClient = order.get("client").toString();

			if (receivedClient.matches("\\d+"))
				findById(client, receivedClient);
			else
				createNewOnTheGo(client, receivedClient);
			order.remove("client");

			String json = gson.toJson(order);
			Order parsed = gson.fromJson(json, Order.class);
			Optional.ofNullable(parsed).ifPresent(o -> {
				o.setClient(client[0]).setWorker(getCurrentWorker(workerName))
				 .setCreatedDate(LocalDateTime.now()).setWorkState(OrderState.QUEUED);
				save(o);
			});
			return true;
		} catch (JsonSyntaxException e) {
			log.warn("Could not process edit of order: " + order);
			return false;
		}
	}

	private void findById(Client[] client, String receivedClient) {
		Long id = Long.valueOf(receivedClient);
		Optional<User> byId = userService.getById(id);
		client[0] = byId.map(User::toClient).orElseThrow(Exceptions.userNotFound(id));
	}

	private Worker getCurrentWorker(String workerName) {
		User user = userService.getByUsername(workerName).orElseThrow(Exceptions.currentUserNotFound(workerName));
		return user.toWorker();
	}

	private void createNewOnTheGo(Client[] client, String receivedClient) {
		String[] split = receivedClient.replaceAll("\\s+", " ").split(" ");
		client[0] = new Client().setName(split[0]).toClient();
		if (split.length > 1) client[0].setLastName(split[1]);
		if (split.length > 3) client[0].setFatherName(split[2]);
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

	@Override @Transactional
	public boolean delete(Long id) {
		if (id != null) {
			orderRepo.deleteById(id);
			return true;
		} else return false;
	}

	@Override
	public void deleteAllDone() {
		orderRepo.deleteAllDone();
	}

	@Override
	public void deleteDoneOfUserById(Long id) {
		Optional<User> user = userService.getById(id);
		deleteDoneOfUser(user);
	}

	@Override
	public void deleteDoneOfUserByUsername(String username) {
		Optional<User> user = userService.getByUsername(username);
		deleteDoneOfUser(user);
	}

	@Override
	public List<Order> getOrders(Map<String, String> params, Authentication auth) {
		if (params.containsKey("period")) {
			var period = params.get("period");
			var periodLength = params.containsKey("periodLength") ? -Long.parseLong(params.get("periodLength")) : -1;

			var username = params.getOrDefault("username", auth == null ? "dpozinen" : auth.getName());
			var user = userService.getByUsername(username).orElseThrow(Exceptions.userNotFound(username));
			var userId = user.getId();

			if (user.isClient())
				return orderRepo.findOrdersOfClient(period, periodLength, userId);
			else
				return orderRepo.findOrdersOfWorker(period, periodLength, userId);
		}
		return List.of();
	}

	@Override
	public Map<Integer, Long> getOrdersCount(Map<String, String> params, Authentication auth) {
		var orders = getOrders(params, auth);
		var period = params.get("period");
		return orders.stream()
					 .map(o -> getPeriod(o, period))
					 .collect(groupingBy(o -> o, Collectors.counting()));
	}

	private int getPeriod(Order o, String period) {
		LocalDateTime createdDate = o.getCreatedDate();
		return switch (period) {
			case "year": yield createdDate.getYear();
			case "day": yield createdDate.getDayOfYear();
			case "week": yield createdDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
			case "hour": yield createdDate.getHour();
			case "minute": yield createdDate.getMinute();
			case "month":
			default: yield createdDate.getMonthValue();
		};
	}

	@Override
	public void deleteDoneOfUser(Optional<User> user) {
		if (user.isPresent()) {
			User u = user.get();
			if (u.isWorker()) orderRepo.deleteAllDoneOfWorker(u.getId());
			if (u.isClient()) orderRepo.deleteAllDoneOfClient(u.getId());
		}
	}

	@Override
	public Set<Order> getQueuedOrders() {
		return orderRepo.findQueued();
	}

	@Override
	public Optional<Order> edit(Order order) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
