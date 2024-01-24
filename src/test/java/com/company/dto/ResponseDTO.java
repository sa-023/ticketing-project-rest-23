package com.company.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDTO {

    private String access_token; // format is important because keycloak send us field name as access_token

}
