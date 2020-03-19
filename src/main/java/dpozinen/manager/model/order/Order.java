package dpozinen.manager.model.order;

import dpozinen.manager.model.user.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dpozinen
 */
@Component
@Entity
public @Data class Order {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(value = EnumType.STRING)
	private OrderState state;
	private BigDecimal price;
	private Boolean isPayed;

	@ManyToMany
	private Set<User> users = new HashSet<>();
}
