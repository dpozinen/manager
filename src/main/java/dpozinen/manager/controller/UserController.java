package dpozinen.manager.controller;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.User;
import dpozinen.manager.model.user.Worker;
import dpozinen.manager.service.user.UserService;
import dpozinen.manager.util.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.Optional;

/**
 * @author dpozinen
 */
@Controller
@RequestMapping("/user")
public class UserController {

	private final UserService service;
	private final Validator validate;

	public UserController(UserService service, Validator validate) {
		this.service = service;
		this.validate = validate;
	}

	@GetMapping("/{username}")
	public String getUser(@PathVariable String username, Model model) {
		Optional<Long> id = service.getByUsername(username).map(User::getId);

		if (id.isPresent())
			return "forward:/user/id/%d".formatted(id.get());
		else {
			model.addAttribute("msg", "User by username %s was not found".formatted(username));
			return "/error/404";
		}
	}

	@GetMapping("/id/{id}")
	public String getUser(@PathVariable Long id, Model model) {
		var user = service.getById(id);

		if (user.isEmpty()) {
			model.addAttribute("msg", "User by id %d was not found".formatted(id));
			return "/error/404";
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
	public String registerClient(Authentication authentication) {
		if (authentication != null) return "redirect:/me";
		else return "/user/register";
	}

	@PostMapping("/register")
	public ResponseEntity<Void> registerClient(@RequestBody Map<String, String> client) {
		if (validate.registerForm(client).isEmpty()) {
			var username = client.getOrDefault("username", client.getOrDefault("email", "").replaceAll("@(?<=@).+", ""));
			client.put("username", username); // if username was empty - default to email before @
			service.saveClient(client);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@GetMapping("/login")
	public String login(Authentication authentication) {
		if (authentication != null) return "redirect:/me";
		else return "/user/login";
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

	@PostMapping("/checkForm")
	public ResponseEntity<Map<String, String>> checkForm(@RequestBody Map<String, String> form) {
		var errors = validate.registerForm(form);

		return new ResponseEntity<>(errors, errors.isEmpty() ? HttpStatus.OK : HttpStatus.UNPROCESSABLE_ENTITY);
	}

}
