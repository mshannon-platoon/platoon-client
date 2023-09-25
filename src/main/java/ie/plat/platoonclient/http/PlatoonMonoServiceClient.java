package ie.plat.platoonclient.http;

import ie.plat.platoonclient.http.model.req.LoginRequest;
import ie.plat.platoonclient.http.model.req.UserRegistrationRequest;
import ie.plat.platoonclient.http.model.req.UserToRegister;
import ie.plat.platoonclient.http.model.res.AuthenticatedUser;
import ie.plat.platoonclient.http.model.res.RegisteredUserDTO;
import ie.plat.platoonclient.register.RegisterController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Setter
@Service
public class PlatoonMonoServiceClient {

  @Value("${platoon.mono.service.base-url}")
  private String baseURL = "localhost:8080";

  private static final String registerURL = "/v1/register-user";
  private static final String loginURL = "/v1/login";

  private RestTemplate restTemplate = new RestTemplate();

  public PlatoonMonoServiceClient() {
    //TODO: Move this to configuration
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
    messageConverters.add(converter);
    this.restTemplate.setMessageConverters(messageConverters);
  }

  public RegisteredUserDTO registeredUser(UserToRegister userToRegister) {
    log.info("PlatoonMonoServiceClient - registerUser - userToRegister: {}", userToRegister);
    return restTemplate.postForObject(appendUrl(baseURL, registerURL),
        new UserRegistrationRequest(userToRegister),
        RegisteredUserDTO.class);
  }

  public AuthenticatedUser login(LoginRequest loginRequest) {
    log.info("PlatoonMonoServiceClient - login - loginRequest: {}", loginRequest);
    return restTemplate.postForObject(appendUrl(baseURL, loginURL),
        loginRequest,
        AuthenticatedUser.class);
  }

  private String appendUrl(String base, String url) {
    return base + url;
  }
}
