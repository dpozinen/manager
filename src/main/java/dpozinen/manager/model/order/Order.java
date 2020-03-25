package dpozinen.manager.model.order;

import dpozinen.manager.model.user.Client;
import dpozinen.manager.model.user.Worker;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author dpozinen
 */
@Entity
public @Data class Order {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private BigDecimal price;
	private LocalDateTime createdDate;
	private LocalDateTime dueDate;
	@Lob
	private String notes;

	@Enumerated(value = EnumType.STRING)
	private OrderState workState;
	@Enumerated(value = EnumType.STRING)
	private OrderState payState;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private Worker worker;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@EqualsAndHashCode.Exclude @ToString.Exclude
	private Client client;
}
