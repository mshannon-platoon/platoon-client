package ie.plat.platoonclient.register;

import ie.plat.platoonclient.http.PlatoonMonoServiceClient;
import ie.plat.platoonclient.http.model.req.UserToRegister;
import ie.plat.platoonclient.http.model.res.RegisteredUserDTO;
import ie.plat.platoonclient.ui.UIErrorMessages;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

@Getter
@Setter
public class RegisterController {

  private PlatoonMonoServiceClient platoonMonoServiceClient;

  private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

  @FXML
  private TextField username;
  @FXML
  private TextField email;
  @FXML
  private TextField password;
  @FXML
  private Button registerButton;
  @FXML
  private Label errorLabel;

  @FXML
  protected void registerUser() {
    errorLabel.setText("");

    log.info("username: {} email:{} password: {}",
        username.getText(),
        email.getText(),
        password.getText());

    UserToRegister user = UserToRegister
        .builder()
        .username(username.getText())
        .email(email.getText())
        .password(password.getText())
        .build();

    try {
      RegisteredUserDTO registeredUserDTO = platoonMonoServiceClient.registeredUser(user);
      errorLabel.setTextFill(Color.DARKGREEN);
      errorLabel.setText("Success");


      log.info("response from api:  {}", registeredUserDTO);
    } catch (
        HttpClientErrorException e) {
      if (e.getMessage().contains("User email already registered!")) {
        log.info("PlatoonMonoServiceClient - registerUser - User email already registered");
        renderErrorMessage(UIErrorMessages.EMAIL_ALREADY_REGISTERED);
      }

      if (e.getMessage().contains("Username already registered!")) {
        log.info("PlatoonMonoServiceClient - registerUser - Username already registered!");
        renderErrorMessage(UIErrorMessages.USER_ALREADY_REGISTERED);
      }

      if (e.getMessage().contains("This email is invalid to our standards!")) {
        log.info("PlatoonMonoServiceClient - registerUser - Email is invalid!");
        renderErrorMessage(UIErrorMessages.EMAIL_INVALID);
      }
    }
  }

  public void renderErrorMessage(String errorMessage) {
    errorLabel.setText(errorMessage);
  }

}