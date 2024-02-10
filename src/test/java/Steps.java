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
  static final String createCourierUrl = "/api/v1/courier";
  /**
   * Адрес ручки удаления курьера
   */
  static final String deleteCourierUrl = "/api/v1/courier/";
  /**
   * Адрес ручки логина курьера в системе
   */
  static final String loginCourierInSystemUrl = "/api/v1/courier/login";
  /**
   * Адрес ручки создания заказа, и получения списка заказов
   */
  static final String orderUrl = "/api/v1/orders";
  /**
   * Адрес ручки отмены заказа
   */
  static final String cancelOrderUrl = "/api/v1/orders/cancel";
  /**
   * Адрес ручки принятия заказа
   */
  static final String acceptOrderUrl = "/api/v1/orders/accept/";
  /**
   * Адрес ручки получения заказа по его track номеру
   */
  static final String getOrderWithTrackUrl = "/api/v1/orders/track";

  /**
   * Создание курьера
   */
  @Step
  public Response createCourierStep(Object body) {
    return doPostRequest(createCourierUrl, body);
  }

  /**
   * Регистрация курьера в системе
   */
  @Step
  public Response loginCourierInSystemStep(Object body) {
    return doPostRequest(loginCourierInSystemUrl, body);
  }

  /**
   * Удаление курьера
   */
  @Step
  public Response deleteCourierStep(Long id) {
    return doDeleteRequest(deleteCourierUrl, id);
  }

  /**
   * Удаление курьера без id
   */
  @Step
  public Response deleteCourierStep() {
    return doDeleteRequest(deleteCourierUrl);
  }

  /**
   * Создание заказа
   */
  @Step
  public Response createOrder(Object body) {
    return doPostRequest(orderUrl, body);
  }

  /**
   * Получение списка заказов
   */
  @Step
  public Response getListOrders() {
    return doGetRequest(orderUrl);
  }

  /**
   * Отменить заказ
   */
  @Step
  public void cancelOrder(Long id) {
    doPutRequest(cancelOrderUrl, id);
  }

  /**
   * Принять заказ
   */
  @Step
  public Response acceptOrder(Long orderId, Long courierId) {
    return doPutRequest(acceptOrderUrl, orderId, courierId);
  }

  /**
   * Принять заказ без id курьера
   */
  @Step
  public Response acceptOrderNoCourierId(Long orderId) {
    return doPutRequestNoCourierId(acceptOrderUrl, orderId);
  }

  /**
   * Принять заказ без id заказа
   */
  @Step
  public Response acceptOrderNoOrderId(Long courierId) {
    return doPutRequestNoOrderId(acceptOrderUrl, courierId);
  }

  @Step
  public Response getOrderWithTrack(Long track) {
    return doGetRequest(getOrderWithTrackUrl, track);
  }

  @Step
  public Response getOrderWithoutTrack() {
    return doGetRequest(getOrderWithTrackUrl);
  }
}
