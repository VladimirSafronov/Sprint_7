package constant;

/**
 * Класс с константами
 */
public class Constants {

  /**
   * Базовый url приложения
   */
  public static final String URL = "https://qa-scooter.praktikum-services.ru";

  /**
   * Заголовок при ответе с кодом 201
   */
  public static final String CREATED_STATUS_LINE = "HTTP/1.1 201 Created";

  /**
   * Заголовок при ответе с кодом 200
   */
  public static final String OK_STATUS_LINE = "HTTP/1.1 200 OK";

  /**
   * Тело ответа при успешном создании/удалении курьера
   */
  public static final String SUCCESS_BODY = "{\"ok\":true}";

  /**
   * Ожидаемое соообщение при попытке удаления курьера с несуществующим id ("в доках без '.' ")
   */
  public static final String NO_COURIER_WITH_ID = "Курьера с таким id нет.";

  /**
   * Ожидаемое соообщение при попытке принять заказ с несуществующим id курьера
   */
  public static final String NO_COURIER_WITH_THAT_ID = "Курьера с таким id не существует";

  /**
   * Ожидаемое соообщение при попытке удаления курьера без id ("Недостаточно данных для удаления
   * курьера")
   */
  public static final String NO_DATA_DELETE_COURIER = "Not Found.";

  public static final String TOO_LITTLE_DATA = "Недостаточно данных для поиска";

  /**
   * Ожидаемое соообщение при попытке принять заказ с несуществующим id заказа
   */
  public static final String WRONG_ORDER_ID = "Заказа с таким id не существует";

  /**
   * Ожидаемое соообщение при попытке получить заказ с несуществующим track номером
   */
  public static final String WRONG_TRACK_ORDER = "Заказ не найден";

  /**
   * Данные для тестового курьера
   */
  public static final String TEST_COURIER_LOGIN = "testCourier4";
  public static final String TEST_COURIER_PASSWORD = "4444";
  public static final String TEST_COURIER_FIRST_NAME = "testCourier";
  /**
   * Данные для тестового заказа
   */
  public static final String NAME_IN_ORDER = "Иван";
  public static final String SURNAME_IN_ORDER = "Тестов";
  public static final String ADDRESS_IN_ORDER = "Адрес";
  public static final String UNDERGROUND_STATION_IN_ORDER = "25";
  public static final String PHONE_NUMBER_IN_ORDER = "+78003553535";
  public static final int DAYS_ORDER = 3;
  public static final String DATE_ORDER = "2024-02-15";
  public static final String COMMENT_ORDER = "коммент";

  /**
   * Количество полей в созданном заказе (не принятом курьером)
   */
  public static final int NOT_ACCEPT_ORDER_FIELDS_COUNT = 17;
  /**
   * Количество полей в созданном заказе (принятом курьером)
   */
  public static final int ACCEPT_ORDER_FIELDS_COUNT = 18;
}
