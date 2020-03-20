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
public @Data abstract class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String lastName;
	private String fatherName;
	private String phone;
	private String email;
	private String password;

	@ManyToMany
	@JoinTable(name = "user_order",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "order_id"))
	private Set<Order> orders = new HashSet<>();

	public boolean isClient() {
		return this instanceof Client;
	}

	public boolean isWorker() {
		return this instanceof Worker;
	}

	public Worker toWorker() {
		return (Worker) this;
	}

	public Client toClient() {
		return (Client) this;
	}
}
