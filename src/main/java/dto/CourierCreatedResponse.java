package dto;

/**
 * Класс dto ответа успешного создания курьера
 */
public class CourierCreatedResponse {
  private boolean ok;

  public boolean isOk() {
    return ok;
  }

  public void setOk(boolean ok) {
    this.ok = ok;
  }
}
