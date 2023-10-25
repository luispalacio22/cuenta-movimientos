package com.banco.cuentamovimiento.service;

import com.banco.cuentamovimiento.exeptions.CuentaNotFoundException;
import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class CuentaServiceImplTest {

    private CuentaRepository cuentaRepository;
    private CuentaServiceImpl cuentaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cuentaRepository = mock(CuentaRepository.class);
        cuentaService = new CuentaServiceImpl(cuentaRepository);
    }

    @Test
    public void testCreateCuenta() {
        Cuenta cuenta = new Cuenta(1L, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true,1L);

        Mockito.when(cuentaRepository.save(cuenta)).thenReturn(cuenta);

        Cuenta createdCuenta = cuentaService.createCuenta(cuenta);
        assertEquals(cuenta, createdCuenta);
    }

    @Test
    public void testUpdateCuenta() {
        Long cuentaId = 1L;
        Cuenta cuenta = new Cuenta(cuentaId, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true,1L);
        Cuenta updatedCuenta = new Cuenta(cuentaId, 9876543210L, "Cuenta Corriente", BigDecimal.valueOf(2000.0), false,1L);

        Mockito.when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));
        Mockito.when(cuentaRepository.save(cuenta)).thenReturn(updatedCuenta);

        Cuenta result = cuentaService.updateCuenta(cuentaId, updatedCuenta);
        assertEquals(updatedCuenta, result);
    }

    @Test
    public void testUpdateCuentaCuentaNotFoundException() {
        Long cuentaId = 1L;
        Cuenta updatedCuenta = new Cuenta(cuentaId, 9876543210L, "Cuenta Corriente", BigDecimal.valueOf(2000.0), false,1L);

        Mockito.when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.updateCuenta(cuentaId, updatedCuenta));
    }

    @Test
    public void testDeleteCuenta() {
        Long cuentaId = 1L;
        Cuenta cuenta = new Cuenta(cuentaId, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true,1L);

        Mockito.when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));

        assertDoesNotThrow(() -> cuentaService.deleteCuenta(cuentaId));
    }

    @Test
    public void testDeleteCuentaCuentaNotFoundException() {
        Long cuentaId = 1L;

        Mockito.when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.deleteCuenta(cuentaId));
    }

    @Test
    public void testGetAllCuentas() {
        List<Cuenta> cuentas = Arrays.asList(
                new Cuenta(1L, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true,1L),
                new Cuenta(2L, 9876543210L, "Cuenta Corriente", BigDecimal.valueOf(2000.0), true,1L)
        );

        Mockito.when(cuentaRepository.findAll()).thenReturn(cuentas);

        List<Cuenta> result = cuentaService.getAllCuentas();
        assertEquals(cuentas, result);
    }

    @Test
    public void testGetCuentaById() {
        Long cuentaId = 1L;
        Cuenta cuenta = new Cuenta(cuentaId, 1234567890L, "Cuenta de Ahorro", BigDecimal.valueOf(1000.0), true,1L);

        Mockito.when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.of(cuenta));

        Cuenta result = cuentaService.getCuentaById(cuentaId);
        assertEquals(cuenta, result);
    }

    @Test
    public void testGetCuentaByIdCuentaNotFoundException() {
        Long cuentaId = 1L;

        Mockito.when(cuentaRepository.findById(cuentaId)).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.getCuentaById(cuentaId));
    }
}
