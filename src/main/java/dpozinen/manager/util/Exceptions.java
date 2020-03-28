package dpozinen.manager.util;

import java.util.function.Supplier;

/**
 * @author dpozinen
 */
public class Exceptions {

	public static Supplier<IllegalStateException> currentUserNotFound(String name) {
		return () -> {
			String log = "Could not find currently logged in user by name %s".formatted(name);
			return new IllegalStateException(log);
		};
	}

	public static Supplier<IllegalArgumentException> userNotFound(String username) {
		return () -> {
			String log = "Could not find user by username %s".formatted(username);
			return new IllegalArgumentException(log);
		};
	}

	public static Supplier<IllegalArgumentException> userNotFound(Long id) {
		return () -> {
			String log = "Could not find user by username %d".formatted(id);
			return new IllegalArgumentException(log);
		};
	}

}
