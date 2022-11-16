package tacos.data;

import tacos.TacoOrder;

public interface TacoOrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}
