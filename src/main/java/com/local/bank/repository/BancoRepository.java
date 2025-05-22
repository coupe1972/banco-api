package com.local.bank.repository;

import com.local.bank.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BancoRepository extends JpaRepository<Banco, Long> {
    Optional<Banco> findByCodigo(String codigo);
}
