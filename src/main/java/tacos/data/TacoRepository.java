package tacos.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tacos.Taco;

@Repository
public interface TacoRepository extends CrudRepository<Taco, Long> {
}
