package dpozinen.manager.model.user;

import dpozinen.manager.model.order.Order;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dpozinen
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract @Data class User {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String lastName;
	private String fatherName;
	private String phone;

	@ManyToMany
	@JoinTable(name = "user_order",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "order_id"))
	private Set<Order> orders = new HashSet<>();

}
