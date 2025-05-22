package com.local.bank.controller;

import com.local.bank.dto.BancoDTO;
import com.local.bank.entity.Banco;
import com.local.bank.service.BancoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BancoControllerTest {

    @Mock
    private BancoService bancoService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BancoController bancoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(bancoController, "urlCompleta", "http://localhost/api/banco/");
        ReflectionTestUtils.setField(bancoController, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(bancoController, "service", bancoService);
    }

    @Test
    public void crear_banco_ok() {
        BancoDTO dto = new BancoDTO("B1", "Banco Test", "Arg", LocalDate.now());
        Banco banco = Banco.builder().id(1L).codigo(dto.getCodigo()).nombre(dto.getNombre()).pais(dto.getPais()).fechaCreacion(dto.getFechaCreacion()).build();

        when(bancoService.crear(dto)).thenReturn(banco);

        ResponseEntity<?> response = bancoController.crear(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(banco, response.getBody());
    }

    @Test
    public void crear_banco_duplicado() {
        BancoDTO dto = new BancoDTO("B1", "Banco Test", "Arg", LocalDate.now());

        when(bancoService.crear(dto)).thenThrow(new IllegalArgumentException("Ya existe un banco con el codigo: B1"));

        ResponseEntity<?> response = bancoController.crear(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).get("Error").toString().contains("Ya existe un banco con el codigo: B1"));
    }

    @Test
    public void obtener_banco_ok() {
        Banco banco = Banco.builder().id(1L).codigo("B2").nombre("Banco Test").pais("Arg").fechaCreacion(LocalDate.now()).build();

        when(bancoService.obtenerPorId(1L)).thenReturn(banco);

        ResponseEntity<?> response = bancoController.obtener(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(banco, response.getBody());
    }

    @Test
    public void obtener_banco_no_encontrado() {
        when(bancoService.obtenerPorId(1L)).thenThrow(new IllegalArgumentException("Banco no encontrado"));

        ResponseEntity<?> response = bancoController.obtener(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(((Map<?, ?>) response.getBody()).get("Error").toString().contains("Banco no encontrado"));
    }

    @Test
    public void autollamado_ok() {
        Banco banco = Banco.builder().id(1L).codigo("B3").nombre("Banco Test").pais("Arg").fechaCreacion(LocalDate.now()).build();
        ResponseEntity<Banco> mockResponse = new ResponseEntity<>(banco, HttpStatus.OK);

        when(restTemplate.getForEntity("http://localhost/api/banco/1", Banco.class)).thenReturn(mockResponse);

        ResponseEntity<?> response = bancoController.autollamado(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(banco, response.getBody());
    }

    @Test
    public void autollamado_error() {
        HttpClientErrorException exception = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, "Banco no encontrado".getBytes(), null);
        when(restTemplate.getForEntity("http://localhost/api/banco/1", Banco.class)).thenThrow(exception);

        ResponseEntity<?> response = bancoController.autollamado(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Banco no encontrado", response.getBody());
    }
}
