package dpozinen.manager.repo;

import dpozinen.manager.model.order.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author dpozinen
 */
@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

	@Query("SELECT o FROM Order o WHERE o.workState = 'QUEUED'")
	Set<Order> findQueued();
}
