package dpozinen.manager.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

/**
 * @author dpozinen
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public @Data class Client extends User {

	private Float discountPercentage;

}
