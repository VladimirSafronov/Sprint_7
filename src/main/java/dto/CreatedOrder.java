package dto;

/**
 * Класс dto созданного заказа
 */
public class CreatedOrder extends Order {

  private Long id;
  private Long courierId;
  private String deliveryDate;
  private Long track;
  private String createdAt;
  private String updatedAt;
  private int status;

  public CreatedOrder() {
  }

  public CreatedOrder(String firstName, String lastName, String address, String metroStation,
      String phone, int rentTime, String deliveryDate, String comment, String[] color, Long id,
      Long courierId, String deliveryDate1, Long track, String createdAt, String updatedAt,
      int status) {
    super(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment,
        color);
    this.id = id;
    this.courierId = courierId;
    this.deliveryDate = deliveryDate1;
    this.track = track;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourierId() {
    return courierId;
  }

  public void setCourierId(Long courierId) {
    this.courierId = courierId;
  }

  @Override
  public String getDeliveryDate() {
    return deliveryDate;
  }

  @Override
  public void setDeliveryDate(String deliveryDate) {
    this.deliveryDate = deliveryDate;
  }

  public Long getTrack() {
    return track;
  }

  public void setTrack(Long track) {
    this.track = track;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "CreatedOrder{" +
        "id=" + id +
        ", courierId=" + courierId +
        ", deliveryDate='" + deliveryDate + '\'' +
        ", track=" + track +
        ", createdAt='" + createdAt + '\'' +
        ", updatedAt='" + updatedAt + '\'' +
        ", status=" + status +
        '}';
  }
}
