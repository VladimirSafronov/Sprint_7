import static constant.Constants.ADDRESS_IN_ORDER;
import static constant.Constants.COMMENT_ORDER;
import static constant.Constants.DATE_ORDER;
import static constant.Constants.DAYS_ORDER;
import static constant.Constants.NAME_IN_ORDER;
import static constant.Constants.PHONE_NUMBER_IN_ORDER;
import static constant.Constants.SURNAME_IN_ORDER;
import static constant.Constants.UNDERGROUND_STATION_IN_ORDER;
import static org.hamcrest.Matchers.notNullValue;

import dto.Order;
import dto.OrderCreatedResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class CreateOrderTest {

  private final Steps steps = new Steps();
  private Order order;
  private OrderCreatedResponse response;

  @Parameter
  public String[] color;

  @Parameters
  public static Object[][] setData() {
    return new Object[][]{
        {new String[]{"BLACK"}},
        {new String[]{"GREY"}},
        {new String[]{"BLACK", "GREY"}},
        {new String[]{}}
    };
  }

  /**
   * Метод создает заказ для теста
   */
  @Before
  public void setOrder() {
    order = new Order(NAME_IN_ORDER, SURNAME_IN_ORDER, ADDRESS_IN_ORDER, UNDERGROUND_STATION_IN_ORDER,
        PHONE_NUMBER_IN_ORDER, DAYS_ORDER, DATE_ORDER, COMMENT_ORDER, color);
  }

  /**
   * Метод отменяет созданный в тесте заказ
   */
  @After
  public void cancelOrder() {
    steps.cancelOrder(response.getTrack());
  }

  @Test
  public void createOrderCorrectDataThen201Created() {
    response = steps.createOrder(order)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("track", notNullValue())
        .extract().as(OrderCreatedResponse.class);
    Assert.assertTrue(response.getTrack() > 0);
  }
}
