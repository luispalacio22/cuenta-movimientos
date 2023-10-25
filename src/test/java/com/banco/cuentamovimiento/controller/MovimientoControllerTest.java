package com.banco.cuentamovimiento.controller;

import com.banco.cuentamovimiento.dto.ReporteDto;
import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.model.Movimiento;
import com.banco.cuentamovimiento.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class MovimientoControllerTest {

    private MovimientoService movimientoService;
    private MovimientoController movimientoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        movimientoService = mock(MovimientoService.class);
        movimientoController = new MovimientoController(movimientoService);
    }

    @Test
    public void testGetAllMovimientos() {
        List<Movimiento> movimientos = Arrays.asList(
                new Movimiento(1L, LocalDate.now(), "Depósito", BigDecimal.valueOf(100.0), BigDecimal.valueOf(1000.0), new Cuenta()),
                new Movimiento(2L, LocalDate.now(), "Retiro", BigDecimal.valueOf(50.0), BigDecimal.valueOf(950.0), new Cuenta())
        );

        Mockito.when(movimientoService.getAllMovimientos()).thenReturn(movimientos);

        ResponseEntity<List<Movimiento> > response = movimientoController.getAllMovimientos();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movimientos, response.getBody());
    }

    @Test
    public void testGetMovimientoById() {
        Movimiento movimiento = new Movimiento(1L, LocalDate.now(), "Depósito", BigDecimal.valueOf(100.0), BigDecimal.valueOf(1000.0),  new Cuenta());

        Mockito.when(movimientoService.getMovimientoById(1L)).thenReturn(movimiento);

        ResponseEntity<Movimiento> response = movimientoController.getMovimientoById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movimiento, response.getBody());
    }

    @Test
    public void testCreateMovimiento() {
        Movimiento movimiento = new Movimiento(1L, LocalDate.now(), "Depósito", BigDecimal.valueOf(100.0), BigDecimal.valueOf(1000.0),  new Cuenta());

        Mockito.when(movimientoService.createMovimiento(movimiento)).thenReturn(movimiento);

        ResponseEntity<Movimiento> response = movimientoController.createMovimiento(movimiento);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(movimiento, response.getBody());
    }

    @Test
    public void testUpdateMovimiento() {
        Movimiento movimiento = new Movimiento(1L, LocalDate.now(), "Depósito", BigDecimal.valueOf(100.0), BigDecimal.valueOf(1000.0),  new Cuenta());

        Mockito.when(movimientoService.updateMovimiento(1L, movimiento)).thenReturn(movimiento);

        ResponseEntity<Movimiento> response = movimientoController.updateMovimiento(1L, movimiento);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movimiento, response.getBody());
    }

    @Test
    public void testDeleteMovimiento() {
        Mockito.doNothing().when(movimientoService).deleteMovimiento(1L);

        ResponseEntity<String> response = movimientoController.deleteMovimiento(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Movimiento eliminado con éxito", response.getBody());
    }

    @Test
    public void testGenerarReporte() {
        LocalDate fechaInicio = LocalDate.of(2023, 1, 1);
        LocalDate fechaFin = LocalDate.of(2023, 12, 31);
        Long cliente = 1L;

        List<ReporteDto> reporte = Arrays.asList(
                new ReporteDto(
                        new Date(),
                        "Cliente 1",
                        "1234567890",
                        "Depósito",
                        BigDecimal.valueOf(1000.0),
                        true,
                        BigDecimal.valueOf(100.0),
                        BigDecimal.valueOf(1100.0)
                ),
                new ReporteDto(
                        new Date(),
                        "Cliente 1",
                        "1234567890",
                        "Retiro",
                        BigDecimal.valueOf(1100.0),
                        true,
                        BigDecimal.valueOf(50.0),
                        BigDecimal.valueOf(1050.0)
                )
        );

        Mockito.when(movimientoService.generarReporte(fechaInicio, fechaFin, cliente)).thenReturn(reporte);

        ResponseEntity<List<ReporteDto>> response = movimientoController.generarReporte(fechaInicio, fechaFin, cliente);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reporte, response.getBody());
    }

}
