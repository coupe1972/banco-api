package com.local.bank.service;

import com.local.bank.dto.BancoDTO;
import com.local.bank.entity.Banco;

import java.util.List;

public interface BancoService {
    Banco crear(BancoDTO banco);

    List<Banco> listarTodos();

    Banco obtenerPorId(Long id);

    Banco actualizar(Long id, BancoDTO banco);

    void eliminar(Long id);
}