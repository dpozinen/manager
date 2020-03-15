package dpozinen.manager.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author dpozinen
 */
@EqualsAndHashCode(callSuper = true)
public @Data class Worker extends User {

	@Override
	public String toString() {
		return super.toString();
	}
}
