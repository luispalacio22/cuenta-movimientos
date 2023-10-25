package com.banco.cuentamovimiento.repository;

import com.banco.cuentamovimiento.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Long> {

//    @Query("SELECT c FROM Cuenta c WHERE c.idCliente = :cliente AND c.fecha BETWEEN :fechaInicio AND :fechaFin")
//    List<Cuenta> findCuentasByClienteAndFechas(
//            @Param("cliente") Long cliente,
//            @Param("fechaInicio") Date fechaInicio,
//            @Param("fechaFin") Date fechaFin
//    );
List<Cuenta> findByClienteId(Long clienteId);


}
