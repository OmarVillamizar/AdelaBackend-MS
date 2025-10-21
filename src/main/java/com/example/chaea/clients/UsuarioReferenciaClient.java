package com.example.chaea.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.chaea.dto.UsuarioReferenciaRequest;

@FeignClient(name = "ms-grupos", url = "http://localhost:9001", path = "/api/usuario-referencia")
public interface UsuarioReferenciaClient {

    @PostMapping
    void crearReferencia(@RequestBody UsuarioReferenciaRequest request);
}
