package dpozinen.manager.repo;

import dpozinen.manager.model.order.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dpozinen
 */
@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {}
