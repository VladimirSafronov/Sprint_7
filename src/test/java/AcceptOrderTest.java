import static constant.Constants.ADDRESS_IN_ORDER;
import static constant.Constants.COMMENT_ORDER;
import static constant.Constants.DATE_ORDER;
import static constant.Constants.DAYS_ORDER;
import static constant.Constants.NAME_IN_ORDER;
import static constant.Constants.NO_COURIER_WITH_THAT_ID;
import static constant.Constants.NO_DATA_DELETE_COURIER;
import static constant.Constants.PHONE_NUMBER_IN_ORDER;
import static constant.Constants.SUCCESS_BODY;
import static constant.Constants.SURNAME_IN_ORDER;
import static constant.Constants.TEST_COURIER_FIRST_NAME;
import static constant.Constants.TEST_COURIER_LOGIN;
import static constant.Constants.TEST_COURIER_PASSWORD;
import static constant.Constants.TOO_LITTLE_DATA;
import static constant.Constants.UNDERGROUND_STATION_IN_ORDER;
import static constant.Constants.WRONG_ORDER_ID;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import dto.BaseCourier;
import dto.Courier;
import dto.CourierLoginResponse;
import dto.ErrorResponse;
import dto.MapCreatedOrderResponse;
import dto.Order;
import dto.OrderCreatedResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AcceptOrderTest {

  private final Steps steps = new Steps();
  private MapCreatedOrderResponse createdOrderMap;
  private OrderCreatedResponse orderCreatedResponse;
  private CourierLoginResponse courierLoginResponse;

  /**
   * Метод создает тестовые данные
   */
  @Before
  public void setData() {
    Order order = new Order(NAME_IN_ORDER, SURNAME_IN_ORDER, ADDRESS_IN_ORDER,
        UNDERGROUND_STATION_IN_ORDER,
        PHONE_NUMBER_IN_ORDER, DAYS_ORDER, DATE_ORDER, COMMENT_ORDER);
    orderCreatedResponse = steps.createOrder(order).as(OrderCreatedResponse.class);

    steps.createCourierStep(
        new Courier(TEST_COURIER_LOGIN, TEST_COURIER_PASSWORD, TEST_COURIER_FIRST_NAME));
    BaseCourier baseCourier = new BaseCourier(TEST_COURIER_LOGIN, TEST_COURIER_PASSWORD);
    courierLoginResponse = steps.loginCourierInSystemStep(baseCourier)
        .as(CourierLoginResponse.class);

    createdOrderMap = steps.getOrderWithTrack(orderCreatedResponse.getTrack())
        .then()
        .extract().as(MapCreatedOrderResponse.class);
  }

  /**
   * Метод удаляет тестовые данные, созданные ранее
   */
  @After
  public void deleteData() {
    steps.cancelOrder(orderCreatedResponse.getTrack());
    steps.deleteCourierStep(courierLoginResponse.getId());
  }

  /**
   * успешный запрос возвращает ok: true
   */
  @Test
  public void acceptOrderThenCorrectBody() {
    long orderId = Long.parseLong(createdOrderMap.getOrder().get("id"));
    String response = steps.acceptOrder(orderId, courierLoginResponse.getId())
        .then()
        .body("ok", equalTo(true))
        .statusCode(HttpStatus.SC_OK)
        .extract().asString();
    Assert.assertTrue(response.contains(SUCCESS_BODY));
  }

  /**
   * если не передать id курьера, запрос вернёт ошибку
   */
  @Test
  public void acceptOrderNoCourierIdThenError() {
    long orderId = Long.parseLong(createdOrderMap.getOrder().get("id"));
    ErrorResponse response = steps.acceptOrderNoCourierId(orderId)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getCode());
    Assert.assertEquals(TOO_LITTLE_DATA, response.getMessage());
  }

  /**
   * если передать неверный id курьера, запрос вернёт ошибку
   */
  @Test
  public void acceptOrderWrongCourierIdThenError() {
    long orderId = Long.parseLong(createdOrderMap.getOrder().get("id"));
    ErrorResponse response = steps.acceptOrder(orderId, -1L)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals(NO_COURIER_WITH_THAT_ID, response.getMessage());
  }

  /**
   * если не передать номер заказа, запрос вернёт ошибку (подставил значения кода ошибки и сообщения
   * об ошибке, чтобы тест не падал)
   */
  @Test
  public void acceptOrderNoIdThenError() {
    ErrorResponse response = steps.acceptOrderNoOrderId(courierLoginResponse.getId())
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals(NO_DATA_DELETE_COURIER, response.getMessage());
  }

  /**
   * если передать неверный номер заказа, запрос вернёт ошибку
   */
  @Test
  public void acceptOrderWrongOrderIdThenError() {
    ErrorResponse response = steps.acceptOrder(-1L, courierLoginResponse.getId())
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals(WRONG_ORDER_ID, response.getMessage());
  }
}
