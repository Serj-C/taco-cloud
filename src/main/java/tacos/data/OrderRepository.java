package tacos.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tacos.TacoOrder;

@Repository
public interface OrderRepository extends CrudRepository<TacoOrder, Long> {
}
