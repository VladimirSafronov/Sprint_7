package specification;

import static constant.Constants.URL;
import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Класс спецификаций тестов
 */
public class Specification {

  /**
   * Создание базовых параметров запроса
   */
  private static RequestSpecification requestSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(URL)
        .setContentType("application/json")
        .addFilter(new RequestLoggingFilter())
        .addFilter(new ResponseLoggingFilter())
        .build();
  }

  /**
   * Создание параметров POST запроса
   */
  public static Response doPostRequest(String path, Object body) {
    return given()
        .spec(requestSpec())
        .body(body)
        .when()
        .post(path)
        .thenReturn();
  }

  /**
   * Создание параметров DELETE запроса
   */
  public static Response doDeleteRequest(String path, Long id) {
    return given()
        .spec(requestSpec())
        .when()
        .delete(path + id.toString())
        .thenReturn();
  }

  /**
   * Создание параметров DELETE запроса без id
   */
  public static Response doDeleteRequest(String path) {
    return given()
        .spec(requestSpec())
        .when()
        .delete(path)
        .thenReturn();
  }

  /**
   * Создание параметров GET запроса
   */
  public static Response doGetRequest(String path) {
    return given()
        .spec(requestSpec())
        .when()
        .get(path)
        .thenReturn();
  }

  public static Response doGetRequest(String path, Long track) {
    return given()
        .spec(requestSpec())
        .when()
        .queryParam("t", track)
        .get(path)
        .thenReturn();
  }

  /**
   * Создание параметров PUT запроса
   */
  public static Response doPutRequest(String path, Long id) {
    return given()
        .spec(requestSpec())
        .when()
        .put(path + "?track=" + id)
        .thenReturn();
  }

  public static Response doPutRequest(String path, Long orderId, Long courierId) {
    return given()
        .spec(requestSpec())
        .when()
        .put(path + orderId + "?courierId=" + courierId)
        .thenReturn();
  }

  public static Response doPutRequestNoCourierId(String path, Long orderId) {
    return given()
        .spec(requestSpec())
        .when()
        .put(path + orderId)
        .thenReturn();
  }

  public static Response doPutRequestNoOrderId(String path, Long courierId) {
    return given()
        .spec(requestSpec())
        .when()
        .put(path + "?courierId=" + courierId)
        .thenReturn();
  }

}
