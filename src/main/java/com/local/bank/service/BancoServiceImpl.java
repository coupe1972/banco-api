package com.local.bank.service;

import com.local.bank.dto.BancoDTO;
import com.local.bank.entity.Banco;
import com.local.bank.repository.BancoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BancoServiceImpl implements BancoService {

    @Autowired
    private BancoRepository repository;

    @Override
    public Banco crear(BancoDTO dto) {
        if (!repository.findByCodigo(dto.getCodigo()).isEmpty()) {
            throw new IllegalArgumentException("Ya existe un banco con el código: " + dto.getCodigo());
        }
        Banco banco = Banco.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .pais(dto.getPais())
                .fechaCreacion(dto.getFechaCreacion())
                .build();
        return repository.save(banco);
    }

    @Override
    public List<Banco> listarTodos() {
        return repository.findAll();
    }

    @Override
    public Banco obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe un banco el id: " + id));
    }

    @Override
    public Banco actualizar(Long id, BancoDTO bancoDTO) {
        Banco banco = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Banco no encontrado"));
        banco.setNombre(bancoDTO.getNombre());
        banco.setCodigo(bancoDTO.getCodigo());
        banco.setPais(bancoDTO.getPais());
        banco.setFechaCreacion(bancoDTO.getFechaCreacion());
        return repository.save(banco);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Banco no encontrado con id: " + id);
        }
        repository.deleteById(id);
    }
}