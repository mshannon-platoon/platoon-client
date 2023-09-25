package ie.plat.platoonclient.http.model.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

  private Integer id;
  private String username;
  private String email;
  private String mobileNumber;
  private String roles;

}
