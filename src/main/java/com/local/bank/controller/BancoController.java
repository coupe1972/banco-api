package com.local.bank.controller;

import com.local.bank.dto.BancoDTO;
import com.local.bank.entity.Banco;
import com.local.bank.service.BancoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banco")
public class BancoController {

    @Autowired
    private BancoService service;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${url.complete.bank}")
    private String urlCompleta;

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody BancoDTO bancoDTO) {
        try {
            Banco banco = service.crear(bancoDTO);
            return new ResponseEntity<>(banco, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("Error", e.getMessage()));
        }
    }

    @GetMapping
    public List<Banco> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        try {
            Banco banco = service.obtenerPorId(id);
            return ResponseEntity.ok(banco);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody BancoDTO bancoDTO) {
        try {
            Banco banco = service.actualizar(id, bancoDTO);
            return ResponseEntity.ok(banco);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("Error", e.getMessage()));
        }
    }

    // Endpoint que se llama a sí mismo
    @GetMapping("/autollamado/{id}")
    public ResponseEntity<?> autollamado(@PathVariable Long id) {
        String url = urlCompleta + id;
        try {
            ResponseEntity<Banco> response = restTemplate.getForEntity(url, Banco.class);
            return response;
        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        }
    }
}
