package dpozinen.manager.model.user;

import dpozinen.manager.model.order.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dpozinen
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public @Data class Client extends User {

	private Float discountPercentage;

	@OneToMany(mappedBy = "client", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<Order> orders = new HashSet<>();

	public Client addOrder(Order order) {
		this.orders.add(order.setClient(this));
		return this;
	}

}
