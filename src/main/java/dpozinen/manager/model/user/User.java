package dpozinen.manager.model.user;

import dpozinen.manager.model.order.Order;
import lombok.Data;

import javax.persistence.*;
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
	private String email;

	private String password;
	private String username;

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

	public abstract Set<Order> getOrders(); // maybe change to work in sql statements instead of collections

	public abstract User addOrder(Order order);

	public abstract User deleteOrder(Order order);

	public String shortInfo() {
		return "%s %s".formatted(name, lastName == null ? "" : lastName);
	}

}
