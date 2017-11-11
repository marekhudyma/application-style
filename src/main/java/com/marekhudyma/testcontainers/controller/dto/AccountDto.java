package com.marekhudyma.testcontainers.controller.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class AccountDto {

    private String name;

    private int scoring;

}
