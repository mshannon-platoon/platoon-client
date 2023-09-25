package ie.plat.platoonclient;

import ie.plat.platoonclient.http.PlatoonMonoServiceClient;
import ie.plat.platoonclient.register.RegisterController;
import ie.plat.platoonclient.register.ServiceTest;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class PlatoonClient extends Application {

  private double xOffset = 0;
  private double yOffset = 0;

  private ConfigurableApplicationContext applicationContext;

  @Override
  public void init() {
    String[] args = getParameters().getRaw().toArray(new String[0]);

    this.applicationContext = new SpringApplicationBuilder()
        .sources(PlatoonClientApplication.class)
        .run(args);
  }

  @Override
  public void stop() {
    this.applicationContext.close();
    Platform.exit();
  }

  @Override
  public void start(Stage stage) throws IOException {
    // load the FXML
    FXMLLoader fxmlLoader = new FXMLLoader(PlatoonClientApplication.class.getResource("/register/register.fxml"));

    // load the parent root
    Parent root = fxmlLoader.load();

    root.setOnMousePressed(event -> {
      xOffset = event.getSceneX();
      yOffset = event.getSceneY();
    });

    root.setOnMouseDragged(event -> {
      stage.setX(event.getScreenX() - xOffset);
      stage.setY(event.getScreenY() - yOffset);
    });

    // get the controller as we can't make it a spring bean and inject our service bean
    RegisterController registerController = fxmlLoader.getController();
    PlatoonMonoServiceClient client = applicationContext.getBean(PlatoonMonoServiceClient.class);
    registerController.setPlatoonMonoServiceClient(client);

    // Initialise the scene
    Scene scene = new Scene(root, 600, 500);
    stage.setTitle("Platoon");
    stage.setScene(scene);
    stage.getIcons().add(new Image("/icon.png"));

    stage.show();
  }
}