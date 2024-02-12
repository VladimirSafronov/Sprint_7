import static specification.Specification.doDeleteRequest;
import static specification.Specification.doGetRequest;
import static specification.Specification.doPostRequest;
import static specification.Specification.doPutRequest;
import static specification.Specification.doPutRequestNoCourierId;
import static specification.Specification.doPutRequestNoOrderId;

import io.qameta.allure.Step;
import io.restassured.response.Response;

/**
 * Класс со степами
 */
public class Steps {

  /**
   * Адрес ручки создания курьера
   */
  static final String CREATE_COURIER_URL = "/api/v1/courier";
  /**
   * Адрес ручки удаления курьера
   */
  static final String DELETE_COURIER_URL = "/api/v1/courier/";
  /**
   * Адрес ручки логина курьера в системе
   */
  static final String LOGIN_COURIER_IN_SYSTEM_URL = "/api/v1/courier/login";
  /**
   * Адрес ручки создания заказа, и получения списка заказов
   */
  static final String ORDER_URL = "/api/v1/orders";
  /**
   * Адрес ручки отмены заказа
   */
  static final String CANCEL_ORDER_URL = "/api/v1/orders/cancel";
  /**
   * Адрес ручки принятия заказа
   */
  static final String ACCEPT_ORDER_URL = "/api/v1/orders/accept/";
  /**
   * Адрес ручки получения заказа по его track номеру
   */
  static final String GET_ORDER_WITH_TRACK_URL = "/api/v1/orders/track";

  @Step("Создание курьера")
  public Response createCourierStep(Object body) {
    return doPostRequest(CREATE_COURIER_URL, body);
  }

  @Step("Регистрация курьера в системе")
  public Response loginCourierInSystemStep(Object body) {
    return doPostRequest(LOGIN_COURIER_IN_SYSTEM_URL, body);
  }

  @Step("Удаление курьера")
  public Response deleteCourierStep(Long id) {
    return doDeleteRequest(DELETE_COURIER_URL, id);
  }

  @Step("Удаление курьера без id")
  public Response deleteCourierStep() {
    return doDeleteRequest(DELETE_COURIER_URL);
  }

  @Step("Создание заказа")
  public Response createOrder(Object body) {
    return doPostRequest(ORDER_URL, body);
  }

  @Step("Получение списка заказов")
  public Response getListOrders() {
    return doGetRequest(ORDER_URL);
  }

  @Step("Отменить заказ")
  public void cancelOrder(Long id) {
    doPutRequest(CANCEL_ORDER_URL, id);
  }

  @Step("Принять заказ")
  public Response acceptOrder(Long orderId, Long courierId) {
    return doPutRequest(ACCEPT_ORDER_URL, orderId, courierId);
  }

  @Step("Принять заказ без id курьера")
  public Response acceptOrderNoCourierId(Long orderId) {
    return doPutRequestNoCourierId(ACCEPT_ORDER_URL, orderId);
  }

  @Step("Принять заказ без id заказа")
  public Response acceptOrderNoOrderId(Long courierId) {
    return doPutRequestNoOrderId(ACCEPT_ORDER_URL, courierId);
  }

  @Step("Принять заказ по track номеру")
  public Response getOrderWithTrack(Long track) {
    return doGetRequest(GET_ORDER_WITH_TRACK_URL, track);
  }

  @Step("Принять заказ без track номера")
  public Response getOrderWithoutTrack() {
    return doGetRequest(GET_ORDER_WITH_TRACK_URL);
  }
}
