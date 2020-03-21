package dpozinen.manager.model.user;

/**
 * @author dpozinen
 */
public enum Role {
	OWNER, ADMIN, PEASANT, USER;

	public String prefixed() {
		return "ROLE_" + toString();
	}
}
