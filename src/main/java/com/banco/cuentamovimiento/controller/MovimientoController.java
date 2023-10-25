package com.banco.cuentamovimiento.controller;

import com.banco.cuentamovimiento.dto.ReporteDto;
import com.banco.cuentamovimiento.exeptions.CuentaNotFoundException;
import com.banco.cuentamovimiento.exeptions.SaldoInsuficienteException;
import com.banco.cuentamovimiento.model.Movimiento;
import com.banco.cuentamovimiento.service.MovimientoService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@AllArgsConstructor
public class MovimientoController {
    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<Movimiento>> getAllMovimientos() {
        List<Movimiento> movimientos = movimientoService.getAllMovimientos();
        return new ResponseEntity<>(movimientos, HttpStatus.OK);
    }

    @GetMapping("/{movimientoId}")
    public ResponseEntity<Movimiento> getMovimientoById(@PathVariable Long movimientoId) {
        Movimiento movimiento = movimientoService.getMovimientoById(movimientoId);
        return new ResponseEntity<>(movimiento, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Movimiento> createMovimiento(@RequestBody Movimiento movimiento) {
        Movimiento nuevo = movimientoService.createMovimiento(movimiento);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{movimientoId}")
    public ResponseEntity<Movimiento> updateMovimiento(
            @PathVariable Long movimientoId,
            @RequestBody Movimiento movimiento) {
        Movimiento actualizado = movimientoService.updateMovimiento(movimientoId, movimiento);
        return new ResponseEntity<>(actualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{movimientoId}")
    public ResponseEntity<String> deleteMovimiento(@PathVariable Long movimientoId) {
        movimientoService.deleteMovimiento(movimientoId);
        return new ResponseEntity<>("Movimiento eliminado con éxito", HttpStatus.OK);
    }
    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteDto>> generarReporte(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam Long cliente) {
        List<ReporteDto> reporte = movimientoService.generarReporte(fechaInicio, fechaFin, cliente);
        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }

    @ExceptionHandler(CuentaNotFoundException.class)
    public ResponseEntity<String> handleCuentaNotFoundException(CuentaNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}