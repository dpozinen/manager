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
		return "/403";
	}

	@GetMapping("/me")
	public String me(Authentication authentication) {
		String name = authentication.getName();
		return "forward:/user/%s".formatted(name);
	}
}
