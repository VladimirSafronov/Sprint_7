package dto;

/**
 * Расширенный класс dto Courier с полем firstName
 */
public class Courier extends BaseCourier {

  private String firstName;

  public Courier() {
  }

  public Courier(String login, String password, String firstName) {
    super(login, password);
    this.firstName = firstName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
}
