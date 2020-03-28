package dpozinen.manager.repo;

import dpozinen.manager.model.order.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
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

	@Query("""
            SELECT o FROM Order o WHERE
            	o.client.id = :id
            	AND
            	o.createdDate >= DATEADD(:period, :periodLength, CURRENT_DATE)
            	ORDER BY o.createdDate
			""")
	List<Order> findOrdersOfClient(@Param("period") String period,
								   @Param("periodLength") long periodLength,
								   @Param("id") Long id
	);

	@Query("""
            SELECT o FROM Order o WHERE
            	o.worker.id = :id
            	AND
            	o.createdDate >= DATEADD(:period, :periodLength, CURRENT_DATE)
            	ORDER BY o.createdDate
			"""
	)
	List<Order> findOrdersOfWorker(@Param("period") String period,
								   @Param("periodLength") long periodLength,
								   @Param("id") Long id
	);

	@Query(value = """
			          SELECT  * FROM "order" o WHERE
			          "worker_id" = 1
			          AND
			          "created_date" >= DATEADD(month, -10000, CURRENT_DATE)
			""",
			nativeQuery = true
	)
	List<Order> test();
}
