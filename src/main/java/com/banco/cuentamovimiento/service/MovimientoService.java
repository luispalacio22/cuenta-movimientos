package com.banco.cuentamovimiento.service;

import com.banco.cuentamovimiento.dto.ReporteDto;
import com.banco.cuentamovimiento.model.Movimiento;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface MovimientoService {
    Movimiento createMovimiento(Movimiento movimiento);

    Movimiento updateMovimiento(Long movimientoId, Movimiento movimiento);

    void deleteMovimiento(Long movimientoId);

    List<Movimiento> getAllMovimientos();

    Movimiento getMovimientoById(Long movimientoId);
    List<ReporteDto> generarReporte(LocalDate fechaInicio, LocalDate fechaFin, Long cliente);

}
