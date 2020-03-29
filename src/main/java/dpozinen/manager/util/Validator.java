package dpozinen.manager.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author dpozinen
 */
@Component
public class Validator {

	private static final List<String> PERIODS = List.of("year", "day", "week", "hour", "minute", "month");

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

}
