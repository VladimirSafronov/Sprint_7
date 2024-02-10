package dto;

/**
 * Класс dto ответа успешного совершения заказа
 */
public class OrderCreatedResponse {

  private Long track;

  public OrderCreatedResponse() {
  }

  public OrderCreatedResponse(Long track) {
    this.track = track;
  }

  public Long getTrack() {
    return track;
  }

  public void setTrack(Long track) {
    this.track = track;
  }
}
