package dpozinen.manager.controller;


import dpozinen.manager.model.order.Order;
import dpozinen.manager.service.order.OrderService;
import dpozinen.manager.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author dpozinen
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderService;
	private final UserService userService;

	public OrderController(OrderService orderService, UserService userService) {
		this.orderService = orderService;
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity<List<Order>> orders(@RequestParam Map<String, String> params, Authentication auth) {
		var data = orderService.getOrders(params, auth);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/count")
	public ResponseEntity<Map<Integer, Long>> ordersCount(@RequestParam Map<String, String> params, Authentication auth) {
		var data = orderService.getOrdersCount(params, auth);
		return new ResponseEntity<>(data, HttpStatus.OK);
	}

	@GetMapping("/queued")
	public String queuedOrders(Model model, Authentication auth) {
		model.addAttribute("orders", orderService.getQueuedOrders());
		model.addAttribute("clients", userService.clients());
		return "/order/orders";
	}

	@GetMapping("/mine")
	public String myOrders(Model model, Authentication auth) {
		userService.getByUsername(auth.getName())
				   .ifPresent(u -> {
					   model.addAttribute("orders", u.getOrders());
					   model.addAttribute("clients", userService.clients());
				   });
		return "/order/orders";
	}

	@GetMapping("/all")
	public String orders(Model model) {
		model.addAttribute("orders", orderService.orders());
		model.addAttribute("clients", userService.clients());
		return "/order/orders";
	}

	@PostMapping("/edit")
	public ResponseEntity<Order> edit(@RequestBody Map<String, Object> order) {
		boolean successful = orderService.edit(order);
		if (successful) return new ResponseEntity<>(HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@PostMapping("/create")
	public ResponseEntity<Order> create(@RequestBody Map<String, Object> order, Authentication auth) {
		boolean successful = orderService.save(order, auth.getName());
		if (successful) return new ResponseEntity<>(HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Order> delete(@PathVariable Long id) {
		boolean successful = orderService.delete(id);
		if (successful) return new ResponseEntity<>(HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@DeleteMapping("/mine/deleteDone")
	public ResponseEntity<Order> deleteMineDone(Authentication auth) {
		orderService.deleteDoneOfUserByUsername(auth.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
