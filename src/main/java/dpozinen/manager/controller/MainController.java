package dpozinen.manager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author dpozinen
 */
@Controller
public class MainController {

	@GetMapping({"", "/", "home"})
	public String home() {
		return "redirect:/me";
	}

	@RequestMapping("/forbidden")
	public String error403() {
		return "/error/403";
	}

	@RequestMapping({"/500", "/error"})
	public String error500() {
		return "/error/500";
	}

	@GetMapping("/me")
	public String me(Authentication authentication) {
		if (authentication != null) {
			String name = authentication.getName();
			return "forward:/user/%s".formatted(name);
		} else return "/user/login";
	}
}
