package ie.plat.platoonclient.login;

import ie.plat.platoonclient.PlatoonClientApplication;
import ie.plat.platoonclient.http.PlatoonMonoServiceClient;
import ie.plat.platoonclient.http.model.req.LoginRequest;
import ie.plat.platoonclient.register.RegisterController;
import ie.plat.platoonclient.ui.UIErrorMessages;
import ie.plat.platoonclient.ui.UIMessages;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

@Getter
@Setter
@Slf4j
public class LoginController {

  @FXML
  private VBox loginStage;
  @FXML
  private TextField usernameOrEmail;
  @FXML
  private TextField password;
  @FXML
  private Label errorLabel;

  private PlatoonMonoServiceClient platoonMonoServiceClient;

  @FXML
  protected void login() {
    errorLabel.setText("");

    if(Objects.isNull(usernameOrEmail) || Objects.isNull(password)){
      renderErrorMessage(UIErrorMessages.USER_AUTH_ERROR);
      return;
    }

    try {
      var authenticatedUser = platoonMonoServiceClient.login(new LoginRequest(usernameOrEmail.getText(), password.getText()));

      log.info("AuthenticatedUser: {}", authenticatedUser);
      renderSuccessMessage(UIMessages.SUCCESS);
    } catch (HttpClientErrorException e) {

      if (e.getStatusCode().is4xxClientError()) {
        renderErrorMessage(UIErrorMessages.USER_AUTH_ERROR);
      }

    } catch (RuntimeException e) {

      renderErrorMessage(UIErrorMessages.SERVICE_UNAVAILABLE);
    }
  }

  protected void renderSuccessMessage(String successMessage){
    errorLabel.setTextFill(Color.DARKGREEN);
    errorLabel.setText("Success");
  }

  protected void renderErrorMessage(String errorMessage){
    errorLabel.setTextFill(Color.RED);
    errorLabel.setText(errorMessage);
  }

  @FXML
  protected void renderRegisterScreen() throws IOException {
    Stage stage = (Stage) loginStage.getScene().getWindow();
    stage.close();


    log.info("register text pressed");

    Stage registerStage = new Stage();

    FXMLLoader fxmlLoader = new FXMLLoader(PlatoonClientApplication.class.getResource("/register/register.fxml"));
    Parent root = fxmlLoader.load();

    RegisterController registerController = fxmlLoader.getController();
    registerController.setPlatoonMonoServiceClient(platoonMonoServiceClient);

    Scene scene = new Scene(root, 600, 525);
    registerStage.setTitle("Platoon");
    registerStage.setScene(scene);
    registerStage.getIcons().add(new Image("/icon.png"));

    registerStage.show();

  }

}
