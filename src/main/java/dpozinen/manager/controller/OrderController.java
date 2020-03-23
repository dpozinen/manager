package dpozinen.manager.controller;


import dpozinen.manager.service.order.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
