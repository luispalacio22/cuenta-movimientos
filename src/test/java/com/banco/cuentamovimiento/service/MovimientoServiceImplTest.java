package com.banco.cuentamovimiento.service;

import com.banco.cuentamovimiento.dto.Cliente;
import com.banco.cuentamovimiento.dto.ReporteDto;
import com.banco.cuentamovimiento.exeptions.SaldoInsuficienteException;
import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.model.Movimiento;
import com.banco.cuentamovimiento.repository.CuentaRepository;
import com.banco.cuentamovimiento.repository.MovimientoRepository;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovimientoServiceImplTest {


    private MovimientoServiceImpl movimientoService;
    @InjectMocks
    private CuentaServiceImpl cuentaService;

    private WebClient.Builder webClientBuilder;


    private WebClient webClient;


    private CuentaRepository cuentaRepository;

    private MovimientoRepository movimientoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        webClientBuilder = WebClient.builder();
        webClientBuilder.baseUrl("http://localhost:8081/clientes");
        webClient = mock(WebClient.class);
        cuentaRepository = mock(CuentaRepository.class);
        movimientoRepository = mock(MovimientoRepository.class);
        cuentaService = new CuentaServiceImpl(cuentaRepository);
        movimientoService = new MovimientoServiceImpl(webClientBuilder, movimientoRepository,cuentaService,cuentaRepository);
    }

    @Test
    public void testCreateMovimientoDeposito() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000.0));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setValor(BigDecimal.valueOf(200.0)); // Depósito

        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        when(movimientoRepository.findById(1L)).thenReturn(java.util.Optional.of(movimiento));
        when(cuentaRepository.findById(1L)).thenReturn(java.util.Optional.of(cuenta));


        Movimiento createdMovimiento = movimientoService.createMovimiento(movimiento);
        assertEquals("Deposito", createdMovimiento.getTipoMovimiento());
    }

    @Test
    public void testCreateMovimientoRetiro() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000.0));

        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setValor(BigDecimal.valueOf(-1200.0)); // Retiro
        when(cuentaRepository.findById(1L)).thenReturn(java.util.Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);
        when(movimientoRepository.findById(1L)).thenReturn(java.util.Optional.of(movimiento));

        // Prueba de excepción por saldo insuficiente
        try {
            movimientoService.createMovimiento(movimiento);
        } catch (SaldoInsuficienteException e) {
            assertEquals("Saldo no disponible", e.getMessage());
        }
    }

    @Test
    @Disabled
    public void testGenerarReporte() {
        Cliente cliente = new Cliente(1L, "Cliente 1", true);
        cliente.setNombre("Luis");
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta(1234567890L);
        cuenta.setTipoCuenta("Cuenta de Ahorro");
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000.0));
        cuenta.setEstado(true);

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDate.now());
        movimiento.setTipoMovimiento("Deposito");
        movimiento.setValor(BigDecimal.valueOf(200.0));
        movimiento.setSaldo(BigDecimal.valueOf(1200.0));
        movimiento.setCuenta(cuenta);

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);

        when(movimientoRepository.findMovimientosByCuentaAndFechas(eq(cuenta), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(movimiento));

        when(cuentaRepository.findByClienteId(1L)).thenReturn(cuentas);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));


        LocalDate fechaInicio = LocalDate.of(2023, 1, 1);
        LocalDate fechaFin = LocalDate.of(2023, 12, 31);
        when(webClient.get().uri("/{clienteId}", 1L).retrieve().bodyToMono(Cliente.class))
                .thenReturn(Mono.just(cliente));

        List<ReporteDto> reporte = movimientoService.generarReporte(fechaInicio, fechaFin, 1L);
        assertEquals(1, reporte.size());

        ReporteDto reporteDto = reporte.get(0);
        assertEquals("Deposito", reporteDto.getTipo());
    }
}
