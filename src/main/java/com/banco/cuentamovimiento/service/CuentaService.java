package com.banco.cuentamovimiento.service;

import com.banco.cuentamovimiento.model.Cuenta;

import java.util.List;

public interface CuentaService {
    Cuenta createCuenta(Cuenta cuenta);

    Cuenta updateCuenta(Long cuentaId, Cuenta cuenta);

    void deleteCuenta(Long cuentaId);

    List<Cuenta> getAllCuentas();

    Cuenta getCuentaById(Long cuentaId);

}
