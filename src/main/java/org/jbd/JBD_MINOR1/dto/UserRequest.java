package org.jbd.JBD_MINOR1.dto;

import com.fasterxml.jackson.datatype.jsr310.deser.JSR310StringParsableDeserializer;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.jbd.JBD_MINOR1.Model.User;
import org.jbd.JBD_MINOR1.Model.UserStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserRequest {


    private String userName;

    @NotBlank(message = "User phoneNo should not be blank")
    private String phoneNo;

    private String email;

    @NotBlank(message = "User password should not be blank")
    private String password;

    private String address;


    public User toUser() {
        return User.
                builder().
                name(this.userName).
                email(this.email).
                phoneNo(this.phoneNo).
                address(this.address).
                userStatus(UserStatus.ACTIVE).
                build();
    }
}
