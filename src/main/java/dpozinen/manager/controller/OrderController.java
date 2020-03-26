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

	@GetMapping("/queued")
	public String queuedOrders(Model model, Authentication authentication) {
		model.addAttribute("orders", orderService.getQueuedOrders());
		model.addAttribute("clients", userService.clients());
		return "/order/orders";
	}

	@GetMapping("/mine")
	public String myOrders(Model model, Authentication authentication) {
		userService.getByUsername(authentication.getName())
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
	public ResponseEntity<Order> create(@RequestBody Map<String, Object> order, Authentication authentication) {
		boolean successful = orderService.save(order, authentication.getName());
		if (successful) return new ResponseEntity<>(HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Order> delete(@PathVariable Long id) {
		boolean successful = orderService.delete(id);
		if (successful) return new ResponseEntity<>(HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
