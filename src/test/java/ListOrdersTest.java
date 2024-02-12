import static org.hamcrest.Matchers.notNullValue;

import dto.CreatedOrder;
import dto.ListCreatedOrdersResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import java.util.List;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class ListOrdersTest {

  private final Steps steps = new Steps();

  @Test
  @DisplayName("Check list orders in response of /api/v1/orders")
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
