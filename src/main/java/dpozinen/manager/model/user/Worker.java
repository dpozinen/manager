package dpozinen.manager.model.user;

import dpozinen.manager.model.order.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dpozinen
 */
@EqualsAndHashCode(callSuper = true)
@Entity
public @Data class Worker extends User {

	private BigDecimal salary;
	@Enumerated(value = EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "worker", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@EqualsAndHashCode.Exclude
	private Set<Order> orders = new HashSet<>();

	@Override
	public User addOrder(Order order) {
		this.orders.add(order.setWorker(this));
		return this;
	}

	@Override
	public User deleteOrder(Order order) {
		this.orders.remove(order);
		order.setWorker(null);
		return null;
	}
}
