package com.banco.cuentamovimiento.service;

import com.banco.cuentamovimiento.dto.ReporteDto;
import com.banco.cuentamovimiento.exeptions.SaldoNoDisponibleException;
import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.model.Movimiento;
import com.banco.cuentamovimiento.repository.CuentaRepository;
import com.banco.cuentamovimiento.repository.MovimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;
    private final CuentaRepository cuentaRepository;
    @Override
    public Movimiento createMovimiento(Movimiento movimiento) {
        Cuenta cuenta = cuentaService.getCuentaById(movimiento.getCuenta().getId());
        BigDecimal cantidad = movimiento.getValor();
        if(cantidad.compareTo(BigDecimal.ZERO) > 0){
            movimiento.setTipoMovimiento("Deposito");
        }else {
            movimiento.setTipoMovimiento("Retiro");
            if(cantidad.compareTo(cuenta.getSaldoInicial())>0){
                throw new SaldoNoDisponibleException("Saldo no disponible");
            }
        }
        movimiento.setSaldo(cuenta.getSaldoInicial().add(cantidad));
        movimiento.setCuenta(cuenta);
        cuenta.setSaldoInicial(movimiento.getSaldo());
        cuentaService.updateCuenta(cuenta.getId(), cuenta);
        movimiento.setFecha(new Date());

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
    public List<ReporteDto> generarReporte(Date fechaInicio, Date fechaFin, Long cliente) {
        // Lógica para consultar la base de datos y generar el reporte
        List<ReporteDto> reporte = new ArrayList<>();

        // Consulta las cuentas y movimientos en el rango de fechas y para el cliente especificado
        List<Cuenta> cuentas = cuentaRepository.findByClienteId(cliente);

        for (Cuenta cuenta : cuentas) {
            List<Movimiento> movimientos = movimientoRepository.findMovimientosByCuentaAndFechas(cuenta, fechaInicio, fechaFin);

            // Calcula el saldo disponible sumando los movimientos
            BigDecimal saldoDisponible = cuenta.getSaldoInicial();
            for (Movimiento movimiento : movimientos) {
                saldoDisponible = saldoDisponible.add(movimiento.getValor());
                ReporteDto reporteDto = ReporteDto.builder()
                        .fecha(new Date())
                        .cliente("Luis")
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
