package dpozinen.manager.model.user;

import dpozinen.manager.model.order.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private Set<Order> orders = new HashSet<>();

	public Client addOrder(Order order) {
		this.orders.add(order.setClient(this));
		return this;
	}

	@Override
	public User deleteOrder(Order order) {
		this.orders.remove(order);
		order.setClient(null);
		return null;
	}
}
