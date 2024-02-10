package dto;

import java.util.Map;

/**
 * Класс-обертка десереализует заказо по track номеру
 */
public class MapCreatedOrderResponse {

  private Map<String,String> order;

  public Map<String, String> getOrder() {
    return order;
  }

  public void setOrder(Map<String, String> order) {
    this.order = order;
  }
}
