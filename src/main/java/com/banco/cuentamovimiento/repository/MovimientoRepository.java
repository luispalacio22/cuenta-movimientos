package com.banco.cuentamovimiento.repository;

import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento,Long> {

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta = :cuenta AND DATE(m.fecha) BETWEEN DATE(:fechaInicio) AND DATE(:fechaFin)")
    List<Movimiento> findMovimientosByCuentaAndFechas(
            @Param("cuenta") Cuenta cuenta,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );
}
