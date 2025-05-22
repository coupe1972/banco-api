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

    @Autowired
    private MessageService messageService;

    @Override
    public Banco crear(BancoDTO dto) {
        if (!repository.findByCodigo(dto.getCodigo()).isEmpty()) {
            throw new IllegalArgumentException(
                    messageService.getMessage("error.banco.existente", dto.getCodigo()));
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
                .orElseThrow(() -> new IllegalArgumentException(
                        messageService.getMessage("error.banco.inexistente", id)));
    }

    @Override
    public Banco actualizar(Long id, BancoDTO bancoDTO) {
        Banco banco = repository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                messageService.getMessage("error.banco.inexistente", id)));
        banco.setNombre(bancoDTO.getNombre());
        banco.setCodigo(bancoDTO.getCodigo());
        banco.setPais(bancoDTO.getPais());
        banco.setFechaCreacion(bancoDTO.getFechaCreacion());
        return repository.save(banco);
    }

    @Override
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                    messageService.getMessage("error.banco.inexistente", id));
        }
        repository.deleteById(id);
    }
}