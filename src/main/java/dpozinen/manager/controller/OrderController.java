package dpozinen.manager.controller;


import dpozinen.manager.model.order.Order;
import dpozinen.manager.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @author dpozinen
 */
@Controller @RequestMapping("/order")
public class OrderController {

	private final OrderService service;

	public OrderController(OrderService service) {
		this.service = service;
	}

	@GetMapping("/all")
	public String orders(Model model) {
		model.addAttribute("orders", service.orders());
		return "/order/orders";
	}

	@PostMapping("/edit")
	public ResponseEntity<Order> edit(@RequestBody Map<String, Object> order) {
		boolean successful = service.edit(order);
		if (successful) return new ResponseEntity<>(HttpStatus.OK);
		else return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
