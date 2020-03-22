package dpozinen.manager.controller;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
		var user = service.getById(id);

		if (user instanceof Worker) {
			model.addAttribute("worker", user);
			return "/user/worker";
		} else if (user instanceof Client) {
			model.addAttribute("client", user);
			return "/user/client";
		}
		return "/error";
	}

	@PostMapping("/client/save")
	public ModelAndView save(@ModelAttribute Client client) {
		service.save(client);
		return new ModelAndView("redirect:/user/" + client.getId());
	}

	@PostMapping("/worker/save")
	public ModelAndView save(@ModelAttribute Worker worker) {
		service.save(worker);
		return new ModelAndView("redirect:/user/" + worker.getId());
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
