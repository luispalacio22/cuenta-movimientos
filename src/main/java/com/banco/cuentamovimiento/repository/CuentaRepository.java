package com.banco.cuentamovimiento.repository;

import com.banco.cuentamovimiento.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Long> {


List<Cuenta> findByClienteId(Long clienteId);


}
