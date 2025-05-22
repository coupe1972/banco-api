package com.local.bank.service;

import com.local.bank.dto.BancoDTO;
import com.local.bank.entity.Banco;
import com.local.bank.repository.BancoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BancoServiceImplTest {

    @InjectMocks
    private BancoServiceImpl service;

    @Mock
    private BancoRepository repository;

    private Banco banco;
    private BancoDTO bancoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        banco = Banco.builder()
                .id(1L)
                .nombre("Banco Galicia")
                .codigo("B1")
                .pais("Arg")
                .fechaCreacion(LocalDate.now())
                .build();

        bancoDTO = BancoDTO.builder()
                .nombre("Banco Galicia")
                .codigo("B1")
                .pais("Arg")
                .fechaCreacion(LocalDate.now())
                .build();
    }

    @Test
    void crear_exitoso() {
        when(repository.findByCodigo("B1")).thenReturn(Optional.empty());
        when(repository.save(any(Banco.class))).thenReturn(banco);

        Banco result = service.crear(bancoDTO);

        assertNotNull(result);
        assertEquals("Banco Galicia", result.getNombre());
        verify(repository).save(any(Banco.class));
    }

    @Test
    void crear_falla_por_duplicado() {
        when(repository.findByCodigo("B1")).thenReturn(Optional.of(banco));

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.crear(bancoDTO));
        assertEquals("Ya existe un banco con el código: B1", e.getMessage());
    }

    @Test
    void listarTodos_ok() {
        when(repository.findAll()).thenReturn(List.of(banco));

        List<Banco> bancos = service.listarTodos();

        assertFalse(bancos.isEmpty());
        assertEquals(1, bancos.size());
    }

    @Test
    void obtenerPorId_ok() {
        when(repository.findById(1L)).thenReturn(Optional.of(banco));

        Banco result = service.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void obtenerPorId_no_existe_id() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.obtenerPorId(1L));
        assertEquals("No existe un banco el id: 1", e.getMessage());
    }

    @Test
    void actualizar_ok() {
        when(repository.findById(1L)).thenReturn(Optional.of(banco));
        when(repository.save(any(Banco.class))).thenReturn(banco);

        Banco result = service.actualizar(1L, bancoDTO);

        assertEquals("Banco Galicia", result.getNombre());
        verify(repository).save(banco);
    }

    @Test
    void actualizar_no_existe_id() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.actualizar(1L, bancoDTO));
        assertEquals("Banco no encontrado", e.getMessage());
    }

    @Test
    void eliminar_ok() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> service.eliminar(1L));
        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_no_existe_id() {
        when(repository.existsById(1L)).thenReturn(false);

        Exception e = assertThrows(IllegalArgumentException.class, () -> service.eliminar(1L));
        assertEquals("Banco no encontrado con id: 1", e.getMessage());
    }
}
