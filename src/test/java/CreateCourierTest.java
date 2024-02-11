import static constant.Constants.CREATED_STATUS_LINE;
import static constant.Constants.LOGIN_IS_USING;
import static constant.Constants.SUCCESS_BODY;
import static constant.Constants.TEST_COURIER_FIRST_NAME;
import static constant.Constants.TEST_COURIER_LOGIN;
import static constant.Constants.TEST_COURIER_PASSWORD;
import static constant.Constants.TOO_LITTLE_DATA_CREATE_COURIER;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import dto.BaseCourier;
import dto.Courier;
import dto.CourierCreatedResponse;
import dto.CourierLoginResponse;
import dto.ErrorResponse;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreateCourierTest {

  private final Steps steps = new Steps();
  private Courier courier;

  /**
   * Метод создает курьера для теста
   */
  @Before
  public void setCourier() {
    courier = new Courier(TEST_COURIER_LOGIN, TEST_COURIER_PASSWORD, TEST_COURIER_FIRST_NAME);
  }

  /**
   * Метод удаляет созданного курьера, перед этим зарегестрировав его в системе, чтобы получить id
   * для удаления
   */
  @After
  public void deleteCreatedCourier() {
    BaseCourier baseCourier = new BaseCourier(courier.getLogin(), courier.getPassword());
    CourierLoginResponse courierLoginResponse = steps.loginCourierInSystemStep(baseCourier)
        .as(CourierLoginResponse.class);
    steps.deleteCourierStep(courierLoginResponse.getId());
  }

  @Test
  @DisplayName("Check created of /api/v1/courier")
  @Description("курьера можно создать")
  public void createCourierWithCorrectDataThenCreated() {
    CourierCreatedResponse response = steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract().as(CourierCreatedResponse.class);

    Assert.assertTrue(response.isOk());
  }

  @Test
  @DisplayName("Check error if two equals data of /api/v1/courier")
  @Description("нельзя создать двух одинаковых курьеров")
  public void createCourierWithSameDataThen409Conflict() {
    steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("ok", equalTo(true))
        .extract();

    int responseCode = steps.createCourierStep(courier)
        .then()
        .extract().statusCode();

    Assert.assertNotEquals(HttpStatus.SC_CREATED, responseCode);
  }

  @Test
  @DisplayName("Check created of /api/v1/courier with all required fields")
  @Description("чтобы создать курьера, нужно передать в ручку все обязательные поля")
  public void createCourierWithRequiredFieldsThen201Created() {
    courier.setFirstName(null);
    String actualStatusLine = steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("ok", equalTo(true))
        .extract().statusLine();
    Assert.assertEquals(CREATED_STATUS_LINE, actualStatusLine);
  }

  @Test
  @DisplayName("Check response body of /api/v1/courier")
  @Description("успешный запрос возвращает {ok:true}")
  public void createCourierWithCorrectDataThenCorrectResponseBody() {
    String response = steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("ok", equalTo(true))
        .extract().asString();

    Assert.assertEquals(SUCCESS_BODY, response);
  }

  @Test
  @DisplayName("Check error if create without login of /api/v1/courier")
  @Description("если логина нет, запрос возвращает ошибку")
  public void createCourierWithoutLoginThen400BadRequest() {
    steps.createCourierStep(courier);
    Courier courierWithoutLogin = new Courier("", courier.getPassword(), courier.getFirstName());
    ErrorResponse response = steps.createCourierStep(courierWithoutLogin)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(TOO_LITTLE_DATA_CREATE_COURIER, response.getMessage());
  }

  @Test
  @DisplayName("Check error if create without password of /api/v1/courier")
  @Description("если пароля нет, запрос возвращает ошибку")
  public void createCourierWithoutPasswordThen400BadRequest() {
    steps.createCourierStep(courier);
    Courier courierWithoutPassword = new Courier(courier.getLogin(), "", courier.getFirstName());

    ErrorResponse response = steps.createCourierStep(courierWithoutPassword)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(TOO_LITTLE_DATA_CREATE_COURIER, response.getMessage());
  }

  @Test
  @DisplayName("Check error if create with exist login of /api/v1/courier")
  @Description("если создать пользователя с логином, который уже есть, возвращается ошибка")
  public void createCourierWithSameLoginThen409Conflict() {
    steps.createCourierStep(courier)
        .then()
        .extract();

    ErrorResponse response = steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CONFLICT)
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);

    Assert.assertEquals(LOGIN_IS_USING, response.getMessage());
  }
}
