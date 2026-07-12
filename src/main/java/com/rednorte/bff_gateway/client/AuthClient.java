package com.rednorte.bff_gateway.client;

import com.rednorte.bff_gateway.dto.LoginRequestDTO;
import com.rednorte.bff_gateway.dto.LoginResponseDTO;
import com.rednorte.bff_gateway.dto.RegistroRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-auth", url = "${ms.auth.url}")
public interface AuthClient {
    @PostMapping("/api/v1/auth/login")
    LoginResponseDTO login(@RequestBody LoginRequestDTO request);

    @PostMapping("/api/v1/auth/registro")
    LoginResponseDTO registrar(@RequestBody RegistroRequestDTO request);
}
