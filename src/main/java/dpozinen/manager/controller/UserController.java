package dpozinen.manager.controller;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

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

	@GetMapping("/{username}")
	public String getUser(@PathVariable String username) {
		Optional<Long> id = service.getByUsername(username).map(User::getId);

		if (id.isPresent()) return "forward:/user/id/%d".formatted(id.get());
		else return "/user/notFound";
	}

	@GetMapping("/id/{id}")
	public String getUser(@PathVariable Long id, Model model) {
		var user = service.getById(id);

		if (user.isEmpty()) {
			return "/user/notFound";
		} else {
			model.addAttribute("orders", user.get().getOrders());
			model.addAttribute("clients", service.clients());

			if (user.get().isWorker()) {
				model.addAttribute("worker", user.get().toWorker());
				return "/user/worker";
			} else {
				model.addAttribute("client", user.get().toClient());
				return "/user/client";
			}
		}
	}

	@PostMapping("/client/save")
	public ModelAndView save(@ModelAttribute Client client) {
		service.save(client);
		return new ModelAndView("redirect:/user/id/" + client.getId());
	}

	@PostMapping("/worker/save")
	public ModelAndView save(@ModelAttribute Worker worker) {
		service.save(worker);
		return new ModelAndView("redirect:/user/id/" + worker.getId());
	}

	@GetMapping("/all")
	public String all(Model model) {
		model.addAttribute("users", service.users());
		return "/user/users";
	}

	@GetMapping("/register")
	public String registerClient(Model model) {
		model.addAttribute("client", new Client());
		return "/user/register";
	}

	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "/user/login";
	}

	@RequestMapping("/login/fail")
	public RedirectView loginFail(@ModelAttribute("username") String username, RedirectAttributes attributes) {
		attributes.addFlashAttribute("username", username);
		return new RedirectView("/user/login?error");
	}

	@RequestMapping("/forbidden")
	public String error403() {
		return "/403";
	}

}
