package com.banco.cuentamovimiento.service;

import com.banco.cuentamovimiento.exeptions.CuentaNotFoundException;
import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.repository.CuentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CuentaServiceImpl implements CuentaService{

    private final CuentaRepository cuentaRepository;

    @Override
    public Cuenta createCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta updateCuenta(Long cuentaId, Cuenta cuenta) {
        Cuenta cuentaExistente = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con el ID: " + cuentaId));
        cuentaExistente.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaExistente.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaExistente.setSaldoInicial(cuenta.getSaldoInicial());
        cuentaExistente.setEstado(cuenta.isEstado());
        return cuentaRepository.save(cuentaExistente);
    }

    @Override
    public void deleteCuenta(Long cuentaId) {
        Cuenta cuentaExistente = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con el ID: " + cuentaId));
        cuentaRepository.delete(cuentaExistente);
    }

    @Override
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta getCuentaById(Long cuentaId) {
        return cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con el ID: " + cuentaId));
    }
}
