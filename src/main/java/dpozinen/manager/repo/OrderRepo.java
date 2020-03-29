package dpozinen.manager.repo;

import dpozinen.manager.model.order.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Set;

/**
 * @author dpozinen
 */
@Repository
public interface OrderRepo extends CrudRepository<Order, Long> {

	@Query("SELECT o FROM Order o WHERE o.workState = 'QUEUED'")
	Set<Order> findQueued();

	@Transactional @Modifying
	@Query("DELETE FROM Order WHERE workState = 'DONE'")
	void deleteAllDone();

	@Transactional @Modifying
	@Query("DELETE FROM Order WHERE workState = 'DONE' and worker.id = :id")
	void deleteAllDoneOfWorker(@Param("id") Long id);

	@Transactional @Modifying
	@Query("DELETE FROM Order WHERE workState = 'DONE' and client.id = :id")
	void deleteAllDoneOfClient(@Param("id") Long id);
}
