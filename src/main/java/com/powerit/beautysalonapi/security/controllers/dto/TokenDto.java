package com.powerit.beautysalonapi.security.controllers.dto;

import lombok.*;
import org.springframework.lang.NonNull;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    @NonNull
    private String token;
}
