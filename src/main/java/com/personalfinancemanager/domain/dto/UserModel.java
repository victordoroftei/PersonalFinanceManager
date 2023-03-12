package com.personalfinancemanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModel {

    private Integer id;

    private String firstname;

    private String lastname;

    private String emailAddress;

    private String password;
}
