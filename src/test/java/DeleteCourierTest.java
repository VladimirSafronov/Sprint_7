import static constant.Constants.NO_COURIER_WITH_ID;
import static constant.Constants.NO_DATA_DELETE_COURIER;
import static constant.Constants.SUCCESS_BODY;
import static constant.Constants.TEST_COURIER_FIRST_NAME;
import static constant.Constants.TEST_COURIER_LOGIN;
import static constant.Constants.TEST_COURIER_PASSWORD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import dto.BaseCourier;
import dto.Courier;
import dto.CourierLoginResponse;
import dto.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;

public class DeleteCourierTest {

  private final Steps steps = new Steps();

  /**
   * запрос с несуществующим id возвращает соответствующую ошибку
   */
  @Test
  public void deleteCourierWithNotExistIdThen404NotFound() {
    ErrorResponse response = steps.deleteCourierStep(-1L)
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals(NO_COURIER_WITH_ID, response.getMessage());
  }

  /**
   * запрос без id возвращает соответствующую ошибку (в доках иные данные)
   */
  @Test
  public void deleteCourierWithoutIdThen400BadRequest() {
    ErrorResponse response = steps.deleteCourierStep()
        .then()
        .statusCode(HttpStatus.SC_NOT_FOUND)
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals(NO_DATA_DELETE_COURIER, response.getMessage());
  }

  /**
   * успешный запрос возвращает ok: true
   */
  @Test
  public void deleteCourierThenCorrectBody() {
    long courierId = getIdCreatedCourier();
    String responseBody = steps.deleteCourierStep(courierId)
        .then()
        .body("ok", equalTo(true))
        .statusCode(HttpStatus.SC_OK)
        .extract().asString();
    Assert.assertEquals(SUCCESS_BODY, responseBody);
  }

  /**
   * Вспомогательный метод для создания курьера, и получения его id
   */
  private long getIdCreatedCourier() {
    Courier courier = new Courier(TEST_COURIER_LOGIN, TEST_COURIER_PASSWORD,
        TEST_COURIER_FIRST_NAME);
    steps.createCourierStep(courier);
    BaseCourier baseCourier = new BaseCourier(TEST_COURIER_LOGIN, TEST_COURIER_PASSWORD);
    CourierLoginResponse courierLoginResponse = steps.loginCourierInSystemStep(baseCourier)
        .as(CourierLoginResponse.class);
    return courierLoginResponse.getId();
  }
}
