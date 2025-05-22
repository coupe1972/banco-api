package com.local.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BancoDTO {

    private String nombre;
    private String codigo;
    private String pais;
    private LocalDate fechaCreacion;
}
