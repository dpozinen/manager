package dpozinen.manager.util;

import dpozinen.manager.service.user.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dpozinen
 */
@Component
public class Validator {

	private static final List<String> PERIODS = List.of("year", "day", "week", "hour", "minute", "month");

	private final UserService userService;

	public Validator(@Lazy UserService userService) {
		this.userService = userService;
	}

	public void getOrders(Map<String, String> params) {
		if (!params.containsKey("period"))
			throw new IllegalArgumentException("Should contain period argument");
		if (!params.containsKey("periodLength"))
			throw new IllegalArgumentException("Should contain period argument");
		if (!params.getOrDefault("username", "username").matches("\\w+"))
			throw new IllegalArgumentException("Invalid username passed: " + params.get("username"));

		var period = params.get("period");
		if (!PERIODS.contains(period.toLowerCase()))
			throw new IllegalArgumentException("Unsupported period passed: " + period);

		var periodLength = params.get("periodLength");
		if (!periodLength.matches("\\d+"))
			throw new IllegalArgumentException("Invalid periodLength passed: " + periodLength);
	}

	public Map<String, String> registerForm(Map<String, String> form) {
		var errors = new HashMap<String, String>();

		var username = form.getOrDefault("username", form.getOrDefault("e-mail", ""));
		if (username.isEmpty())
			errors.put("username", "Username and email are both empty");
		else if (!username.matches("\\w+"))
			errors.put("username", "Username has invalid characters: " + username.replaceAll("\\w+", ""));
		else
			userService.getByUsername(username).ifPresent(u -> errors.put("username", "Username is already taken"));

		var password = form.get("password");
		if (password.equals("password")) errors.put("password", "Password cannot be 'password'");

		List<String> credentials = List.of("name", "lastname", "fathername");
		form.entrySet().stream()
			.filter(e -> credentials.contains(e.getKey()))
			.filter(e -> !e.getValue().matches("\\p{L}+") && !e.getValue().isEmpty())
			.forEach(e -> errors.put(e.getKey(), "Has invalid characters"));

		return errors;
	}
}
