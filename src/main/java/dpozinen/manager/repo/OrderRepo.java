package dpozinen.manager.repo;

import dpozinen.manager.model.order.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author dpozinen
 */
@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

	@Query("SELECT o FROM Order o WHERE o.workState = 'QUEUED'")
	Set<Order> findQueued();

	@Query("DELETE FROM Order WHERE workState = 'DONE'")
	void deleteAllDone();

	@Query("DELETE FROM Order WHERE workState = 'DONE' and worker_id = :id")
	void deleteAllDoneOfWorker(@Param("id") Long id);

	@Query("DELETE FROM Order WHERE workState = 'DONE' and client_id = :id")
	void deleteAllDoneOfClient(@Param("id") Long id);
}
