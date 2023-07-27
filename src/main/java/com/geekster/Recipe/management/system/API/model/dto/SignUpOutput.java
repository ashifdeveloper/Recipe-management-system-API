package com.geekster.Recipe.management.system.API.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpOutput {

    private boolean signUpStatus;
    private String signUpStatusMessage;

}
