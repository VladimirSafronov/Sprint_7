import static constant.Constants.ACCEPT_ORDER_FIELDS_COUNT;
import static constant.Constants.ADDRESS_IN_ORDER;
import static constant.Constants.COMMENT_ORDER;
import static constant.Constants.DATE_ORDER;
import static constant.Constants.DAYS_ORDER;
import static constant.Constants.NAME_IN_ORDER;
import static constant.Constants.NOT_ACCEPT_ORDER_FIELDS_COUNT;
import static constant.Constants.PHONE_NUMBER_IN_ORDER;
import static constant.Constants.SURNAME_IN_ORDER;
import static constant.Constants.TOO_LITTLE_DATA;
import static constant.Constants.UNDERGROUND_STATION_IN_ORDER;
import static constant.Constants.WRONG_TRACK_ORDER;
import static org.hamcrest.Matchers.notNullValue;

import dto.ErrorResponse;
import dto.MapCreatedOrderResponse;
import dto.Order;
import dto.OrderCreatedResponse;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GetOrderTest {

  private final Steps steps = new Steps();
  private OrderCreatedResponse orderCreatedResponse;

  @Before
  public void createTestData() {
    Order order = new Order(NAME_IN_ORDER, SURNAME_IN_ORDER, ADDRESS_IN_ORDER,
        UNDERGROUND_STATION_IN_ORDER,
        PHONE_NUMBER_IN_ORDER, DAYS_ORDER, DATE_ORDER, COMMENT_ORDER);
    orderCreatedResponse = steps.createOrder(order).as(OrderCreatedResponse.class);
  }

  @After
  public void deleteTestData() {
    steps.cancelOrder(orderCreatedResponse.getTrack());
  }

  /**
   * успешный запрос возвращает объект с заказом
   */
  @Test
  public void getOrderThanReturnOrder() {
    MapCreatedOrderResponse response = steps.getOrderWithTrack(orderCreatedResponse.getTrack())
        .then()
        .body("order", notNullValue())
        .statusCode(HttpStatus.SC_OK)
        .extract().as(MapCreatedOrderResponse.class);
    Map<String, String> orderData = response.getOrder();
    Assert.assertTrue(orderData.size() <= ACCEPT_ORDER_FIELDS_COUNT
        && orderData.size() >= NOT_ACCEPT_ORDER_FIELDS_COUNT);
  }

  /**
   * запрос без номера заказа возвращает ошибку
   */
  @Test
  public void getOrderNoTrackThenError() {
    ErrorResponse response = steps.getOrderWithoutTrack()
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getCode());
    Assert.assertEquals(TOO_LITTLE_DATA, response.getMessage());
  }

  /**
   * запрос с несуществующим заказом возвращает ошибку
   */
  @Test
  public void getOrderBadTrackThenError() {
    ErrorResponse response = steps.getOrderWithTrack((long) Integer.MAX_VALUE)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals(WRONG_TRACK_ORDER, response.getMessage());
  }
}
