import static constant.Constants.OK_STATUS_LINE;
import static org.hamcrest.Matchers.notNullValue;

import dto.BaseCourier;
import dto.Courier;
import dto.CourierLoginResponse;
import dto.ErrorResponse;
import io.restassured.response.Response;
import io.restassured.response.ResponseBodyExtractionOptions;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoginCourierTest {

  private final Steps steps = new Steps();
  private Courier courier;
  private CourierLoginResponse courierLoginResponse;

  /**
   * Создание курьера для теста
   */
  @Before
  public void createCourier() {
    courier = new Courier("testCourier4", "4444", "testCourier");
    steps.createCourierStep(courier);
  }

  /**
   * Удаление тестоового курьера
   */
  @After
  public void deleteCreatedCourier() {
    steps.deleteCourierStep(courierLoginResponse.getId());
  }

  /**
   * курьер может авторизоваться
   */
  @Test
  public void loginCourierWithCorrectDataThenLogin() {
    BaseCourier baseCourier = new BaseCourier(courier.getLogin(), courier.getPassword());
    courierLoginResponse = steps.loginCourierInSystemStep(baseCourier)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", notNullValue())
        .extract().as(CourierLoginResponse.class);

    Assert.assertNotNull(courierLoginResponse);
  }

  /**
   * для авторизации нужно передать все обязательные поля
   */
  @Test
  public void loginCourierWithRequiredFieldsThenLogin() {
    BaseCourier bodyWithRequiredFields = new BaseCourier(courier.getLogin(), courier.getPassword());
    Response response = steps.loginCourierInSystemStep(bodyWithRequiredFields)
        .then()
        .body("id", notNullValue())
        .extract().response();
    courierLoginResponse = response.as(CourierLoginResponse.class);
    Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    Assert.assertEquals(OK_STATUS_LINE, response.statusLine());
  }

  /**
   * система вернёт ошибку, если неправильно указать логин
   */
  @Test
  public void loginWithIncorrectLoginThen404NotFound() {
    courierLoginResponse = steps.loginCourierInSystemStep(courier).as(CourierLoginResponse.class);
    BaseCourier incorrectLoginBody = new BaseCourier(courier.getLogin() + "a",
        courier.getPassword());
    ErrorResponse response = steps.loginCourierInSystemStep(incorrectLoginBody)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals("Учетная запись не найдена", response.getMessage());
  }

  /**
   * система вернёт ошибку, если неправильно указать пароль
   */
  @Test
  public void loginWithIncorrectPasswordThen404NotFound() {
    courierLoginResponse = steps.loginCourierInSystemStep(courier).as(CourierLoginResponse.class);
    BaseCourier incorrectLoginBody = new BaseCourier(courier.getLogin(),
        courier.getPassword() + "1");
    ErrorResponse response = steps.loginCourierInSystemStep(incorrectLoginBody)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals("Учетная запись не найдена", response.getMessage());
  }

  /**
   * если нет поля login, запрос возвращает ошибку
   */
  @Test
  public void loginWithoutLoginThen400BadRequest() {
    courierLoginResponse = steps.loginCourierInSystemStep(courier).as(CourierLoginResponse.class);
    BaseCourier withoutLogin = new BaseCourier("", courier.getPassword());
    ErrorResponse response = steps.loginCourierInSystemStep(withoutLogin)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getCode());
    Assert.assertEquals("Недостаточно данных для входа", response.getMessage());
  }

  /**
   * если нет поля password, запрос возвращает ошибку
   */
  @Test
  public void loginWithoutPasswordThen400BadRequest() {
    courierLoginResponse = steps.loginCourierInSystemStep(courier).as(CourierLoginResponse.class);
    BaseCourier withoutPassword = new BaseCourier(courier.getLogin(), "");
    ErrorResponse response = steps.loginCourierInSystemStep(withoutPassword)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, response.getCode());
    Assert.assertEquals("Недостаточно данных для входа", response.getMessage());
  }

  /**
   * если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
   */
  @Test
  public void loginNotExistLoginThen404NotFound() {
    courierLoginResponse = steps.loginCourierInSystemStep(courier).as(CourierLoginResponse.class);
    BaseCourier notExistLoginBody = new BaseCourier(courier.getLogin() + "abc",
        courier.getPassword());
    ErrorResponse response = steps.loginCourierInSystemStep(notExistLoginBody)
        .then()
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals(HttpStatus.SC_NOT_FOUND, response.getCode());
    Assert.assertEquals("Учетная запись не найдена", response.getMessage());
  }

  /**
   * успешный запрос возвращает id
   */
  @Test
  public void whenLoginReturnId() {
    courierLoginResponse = steps.loginCourierInSystemStep(courier).as(CourierLoginResponse.class);
    ResponseBodyExtractionOptions response = steps.loginCourierInSystemStep(courier)
        .then()
        .statusCode(HttpStatus.SC_OK)
        .body("id", notNullValue())
        .extract().body();
    String responseBody = response.asString();
    Assert.assertTrue(responseBody.contains("id"));
  }

}
