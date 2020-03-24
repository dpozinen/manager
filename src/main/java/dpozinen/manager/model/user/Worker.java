package dpozinen.manager.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * @author dpozinen
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public @Data class Worker extends User {

	private BigDecimal salary;
	@Enumerated(value = EnumType.STRING)
	private Role role;
}
