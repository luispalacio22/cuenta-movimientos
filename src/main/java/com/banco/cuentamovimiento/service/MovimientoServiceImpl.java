package com.banco.cuentamovimiento.service;

import com.banco.cuentamovimiento.dto.Cliente;
import com.banco.cuentamovimiento.dto.ReporteDto;
import com.banco.cuentamovimiento.exeptions.SaldoInsuficienteException;
import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.model.Movimiento;
import com.banco.cuentamovimiento.repository.CuentaRepository;
import com.banco.cuentamovimiento.repository.MovimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {
    private final WebClient webClient;
    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;
    private final CuentaRepository cuentaRepository;

    @Autowired
    public MovimientoServiceImpl(WebClient.Builder webClientBuilder, MovimientoRepository movimientoRepository, CuentaService cuentaService, CuentaRepository cuentaRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/clientes").build();
        this.movimientoRepository = movimientoRepository;
        this.cuentaService = cuentaService;
        this.cuentaRepository = cuentaRepository;
    }
    public Mono<Cliente> getNombreCliente(Long clienteId) {
        return webClient
                .get()
                .uri("/{clienteId}", clienteId)
                .retrieve()
                .bodyToMono(Cliente.class);
    }
    @Override
    public Movimiento createMovimiento(Movimiento movimiento) {
        Cuenta cuenta = cuentaService.getCuentaById(movimiento.getCuenta().getId());
        BigDecimal cantidad = movimiento.getValor();
        if(cantidad.compareTo(BigDecimal.ZERO) > 0){
            movimiento.setTipoMovimiento("Deposito");
        }else {
            movimiento.setTipoMovimiento("Retiro");
            if(cantidad.compareTo(cuenta.getSaldoInicial())<0){
                throw new SaldoInsuficienteException("Saldo no disponible");
            }
        }
        movimiento.setSaldo(cuenta.getSaldoInicial().add(cantidad));
        movimiento.setCuenta(cuenta);
        cuenta.setSaldoInicial(movimiento.getSaldo());
        cuentaService.updateCuenta(cuenta.getId(), cuenta);
        movimiento.setFecha(LocalDate.now());

        return movimientoRepository.save(movimiento);
    }

    @Override
    public Movimiento updateMovimiento(Long movimientoId, Movimiento movimiento) {
        Movimiento movimientoExistente = movimientoRepository.findById(movimientoId)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con el ID: " + movimientoId));
        movimientoExistente.setFecha(movimiento.getFecha());
        movimientoExistente.setTipoMovimiento(movimiento.getTipoMovimiento());
        movimientoExistente.setValor(movimiento.getValor());
        movimientoExistente.setSaldo(movimiento.getSaldo());
        return movimientoRepository.save(movimientoExistente);
    }

    @Override
    public void deleteMovimiento(Long movimientoId) {
        Movimiento movimientoExistente = movimientoRepository.findById(movimientoId)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con el ID: " + movimientoId));
        movimientoRepository.delete(movimientoExistente);
    }

    @Override
    public List<Movimiento> getAllMovimientos() {
        return movimientoRepository.findAll();
    }

    @Override
    public Movimiento getMovimientoById(Long movimientoId) {
        return movimientoRepository.findById(movimientoId)
                .orElseThrow(() -> new EntityNotFoundException("Movimiento no encontrado con el ID: " + movimientoId));
    }

    @Override
    public List<ReporteDto> generarReporte(LocalDate fechaInicio, LocalDate fechaFin, Long cliente) {
        List<ReporteDto> reporte = new ArrayList<>();
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(cliente);

        for (Cuenta cuenta : cuentas) {
            List<Movimiento> movimientos = movimientoRepository.findMovimientosByCuentaAndFechas(cuenta, fechaInicio, fechaFin);

            BigDecimal saldoDisponible = cuenta.getSaldoInicial();
            for (Movimiento movimiento : movimientos) {
                saldoDisponible = saldoDisponible.add(movimiento.getValor());
                Cliente clienteRes = getNombreCliente(cliente).block();

                ReporteDto reporteDto = ReporteDto.builder()
                        .fecha(new Date())
                        .cliente(clienteRes.getNombre())
                        .numeroCuenta(String.valueOf(cuenta.getNumeroCuenta()))
                        .tipo(cuenta.getTipoCuenta())
                        .saldoInicial(cuenta.getSaldoInicial())
                        .estado(cuenta.isEstado())
                        .movimiento(movimiento.getValor())
                        .saldoDisponible(saldoDisponible)
                        .build();
                reporte.add(reporteDto);
            }

        }

        return reporte;
    }
}
