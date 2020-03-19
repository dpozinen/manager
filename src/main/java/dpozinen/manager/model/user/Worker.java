package dpozinen.manager.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author dpozinen
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "worker")
public @Data class Worker extends User {

	private BigDecimal salary;

}
