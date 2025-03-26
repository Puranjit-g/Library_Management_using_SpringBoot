package org.jbd.JBD_MINOR1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TxnRequest {

    @NotBlank(message = "User phone number should not be blank")
    private String userPhoneNo;

    @NotBlank(message = "Book no  should not be blank")
    private String bookNo;
}
