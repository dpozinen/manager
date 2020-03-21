package dpozinen.manager.controller;

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
		return "home";
	}

	@RequestMapping("/forbidden")
	public String error403() {
		return "/403";
	}
}
