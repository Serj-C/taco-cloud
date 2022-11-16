package tacos.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import tacos.Taco;
import tacos.TacoOrder;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcTacoOrderRepository implements TacoOrderRepository {
    private SimpleJdbcInsert tacoOrderInserter;
    private SimpleJdbcInsert tacoOrderTacosInserter;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcTacoOrderRepository(JdbcTemplate jdbcTemplate) {
        this.tacoOrderInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");
        this.tacoOrderTacosInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Taco_Order_Tacos");
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public TacoOrder save(TacoOrder tacoOrder) {
        tacoOrder.setPlacedAt(new Date());
        long orderId = saveOrderDetails(tacoOrder);
        tacoOrder.setId(orderId);
        List<Taco> tacos = tacoOrder.getTacos();
        for (Taco taco :
                tacos) {
            saveTacoToOrder(taco, orderId);
        }

        return tacoOrder;
    }

    private long saveOrderDetails(TacoOrder tacoOrder) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values = objectMapper.convertValue(tacoOrder, Map.class);
        values.put("placedAt", tacoOrder.getPlacedAt());

        return tacoOrderInserter
                .executeAndReturnKey(values)
                .longValue();
    }

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        tacoOrderTacosInserter.execute(values);
    }
}
