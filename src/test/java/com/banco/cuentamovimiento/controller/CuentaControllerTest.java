package com.banco.cuentamovimiento.controller;

import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class CuentaControllerTest {

    private CuentaService cuentaService;
    private CuentaController cuentaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cuentaService =mock(CuentaService.class);
        cuentaController = new CuentaController(cuentaService);;
    }

    @Test
    public void testGetAllCuentas() {
        List<Cuenta> cuentas = Arrays.asList(
                new Cuenta(1L, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true, 1L),
                new Cuenta(2L, 9876543210L, "Cuenta Corriente", BigDecimal.valueOf(2000.0), true, 2L)
        );

        Mockito.when(cuentaService.getAllCuentas()).thenReturn(cuentas);

        ResponseEntity<List<Cuenta>> response = cuentaController.getAllCuentas();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuentas, response.getBody());
    }

    @Test
    public void testGetCuentaById() {
        Cuenta cuenta = new Cuenta(1L, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true, 1L);

        Mockito.when(cuentaService.getCuentaById(1L)).thenReturn(cuenta);

        ResponseEntity<Cuenta> response = cuentaController.getCuentaById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuenta, response.getBody());
    }

    @Test
    public void testCreateCuenta() {
        Cuenta cuenta = new Cuenta(1L, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true, 1L);

        Mockito.when(cuentaService.createCuenta(cuenta)).thenReturn(cuenta);

        ResponseEntity<Cuenta> response = cuentaController.createCuenta(cuenta);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cuenta, response.getBody());
    }

    @Test
    public void testUpdateCuenta() {
        Cuenta cuenta = new Cuenta(1L, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true, 1L);

        Mockito.when(cuentaService.updateCuenta(1L, cuenta)).thenReturn(cuenta);

        ResponseEntity<Cuenta> response = cuentaController.updateCuenta(1L, cuenta);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cuenta, response.getBody());
    }

    @Test
    public void testDeleteCuenta() {
        Mockito.doNothing().when(cuentaService).deleteCuenta(1L);

        ResponseEntity<String> response = cuentaController.deleteCuenta(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cuenta eliminada con éxito", response.getBody());
    }
}
