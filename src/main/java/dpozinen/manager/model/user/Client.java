package dpozinen.manager.model.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author dpozinen
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "client")
public @Data class Client extends User {

	private Float discountPercentage;

}
