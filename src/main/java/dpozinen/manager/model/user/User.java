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

	public static final User EMPTY = new EmptyUser();

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String lastName;
	private String fatherName;
	private String phone;
	private String email;

	private String password;
	private String username;

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

	public boolean isEmpty() {
		return this instanceof EmptyUser;
	}

	public Worker toWorker() {
		return (Worker) this;
	}

	public Client toClient() {
		return (Client) this;
	}

	private static final class EmptyUser extends User {
		EmptyUser() {
			super.setId(0L); super.setName(""); super.setLastName("");
			super.setFatherName(""); super.setPhone(""); super.setEmail("");
			super.setPassword(""); super.setUsername("");
		}

		@Override
		public User setId(Long id) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setName(String name) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setLastName(String lastName) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setFatherName(String fatherName) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setPhone(String phone) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setEmail(String email) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setPassword(String password) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setUsername(String username) {
			throw new UnsupportedOperationException("Can't edit this");
		}

		@Override
		public User setOrders(Set<Order> orders) {
			throw new UnsupportedOperationException("Can't edit this");
		}
	}

}
