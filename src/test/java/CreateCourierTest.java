import static constant.Constants.CREATED_STATUS_LINE;
import static constant.Constants.SUCCESS_BODY;
import static constant.Constants.TEST_COURIER_FIRST_NAME;
import static constant.Constants.TEST_COURIER_LOGIN;
import static constant.Constants.TEST_COURIER_PASSWORD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import dto.BaseCourier;
import dto.Courier;
import dto.CourierCreatedResponse;
import dto.CourierLoginResponse;
import dto.ErrorResponse;
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

  /**
   * курьера можно создать
   */
  @Test
  public void createCourierWithCorrectDataThenCreated() {
    CourierCreatedResponse response = steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract().as(CourierCreatedResponse.class);

    Assert.assertTrue(response.isOk());
  }

  /**
   * нельзя создать двух одинаковых курьеров
   */
  @Test
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

  /**
   * чтобы создать курьера, нужно передать в ручку все обязательные поля
   */
  @Test
  public void createCourierWithRequiredFieldsThen201Created() {
    courier.setFirstName(null);
    String actualStatusLine = steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .body("ok", equalTo(true))
        .extract().statusLine();
    Assert.assertEquals(CREATED_STATUS_LINE, actualStatusLine);
  }

  /**
   * запрос возвращает правильный код ответа
   */
  @Test
  public void createCourierWithCorrectDataThen201Created() {
    int actualCode = steps.createCourierStep(courier)
        .then()
        .body("ok", equalTo(true))
        .extract().statusCode();
    Assert.assertEquals(HttpStatus.SC_CREATED, actualCode);
  }

  /**
   * успешный запрос возвращает {ok:true}
   */
  @Test
  public void createCourierWithCorrectDataThenCorrectResponseBody() {
    String response = steps.createCourierStep(courier)
        .then()
        .statusCode(HttpStatus.SC_CREATED)
        .extract().asString();

    Assert.assertEquals(SUCCESS_BODY, response);
  }

  /**
   * если логина нет, запрос возвращает ошибку
   */
  @Test
  public void createCourierWithoutLoginThen400BadRequest() {
    steps.createCourierStep(courier);
    Courier courierWithoutLogin = new Courier("", courier.getPassword(), courier.getFirstName());
    ErrorResponse response = steps.createCourierStep(courierWithoutLogin)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals("Недостаточно данных для создания учетной записи", response.getMessage());
  }

  /**
   * если пароля нет, запрос возвращает ошибку
   */
  @Test
  public void createCourierWithoutPasswordThen400BadRequest() {
    steps.createCourierStep(courier);
    Courier courierWithoutPassword = new Courier(courier.getLogin(), "", courier.getFirstName());

    ErrorResponse response = steps.createCourierStep(courierWithoutPassword)
        .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body("code", notNullValue())
        .body("message", notNullValue())
        .extract().as(ErrorResponse.class);
    Assert.assertEquals("Недостаточно данных для создания учетной записи", response.getMessage());
  }

  /**
   * если создать пользователя с логином, который уже есть, возвращается ошибка
   */
  @Test
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

    Assert.assertEquals("Этот логин уже используется. Попробуйте другой.", response.getMessage());
  }
}
