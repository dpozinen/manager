package dpozinen.manager.controller;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dpozinen
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping("/{id}")
	public String getUser(@PathVariable Long id, Model model) {
		model.addAttribute("user", service.getWorker(id));
		return "user";
	}

	@PostMapping("/client/save")
	public void saveClient(Client client) {
		service.saveClient(client);
	}

	@PostMapping("/worker/save")
	public void saveWorker(Worker worker) {
		service.saveWorker(worker);
	}

	@GetMapping("/all")
	public String all(Model model) {
		model.addAttribute("users", service.users());
		return "table/users";
	}

}
