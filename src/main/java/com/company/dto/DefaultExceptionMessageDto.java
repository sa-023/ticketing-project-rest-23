package com.company.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DefaultExceptionMessageDto {

    // This field will be used to store the message from defaultExceptionMessage.defaultMessage() to display on the Postman as an object.
    private String message;

}
