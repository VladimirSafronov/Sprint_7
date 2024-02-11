import static org.hamcrest.Matchers.notNullValue;

import dto.CreatedOrder;
import dto.ListCreatedOrdersResponse;
import io.qameta.allure.Description;
import java.util.List;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class ListOrdersTest {

  private final Steps steps = new Steps();

  @Test
  @Description("в тело ответа возвращается список заказов")
  public void getListOrdersThanBodyContainsListCreatedOrder() {
    ListCreatedOrdersResponse response = steps.getListOrders()
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("orders", notNullValue())
        .extract().as(ListCreatedOrdersResponse.class);

    List<CreatedOrder> orders = response.getOrders();
    Assert.assertFalse(orders.isEmpty());
  }
}
