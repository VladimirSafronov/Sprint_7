package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * Класс-обертка десереализует список заказов
 */
@JsonIgnoreProperties(value = { "pageInfo", "availableStations" })
public class ListCreatedOrdersResponse {

  private List<CreatedOrder> orders;
  private Object pageInfo;
  private Object availableStations;

  public List<CreatedOrder> getOrders() {
    return orders;
  }

  public void setOrders(List<CreatedOrder> orders) {
    this.orders = orders;
  }
}
