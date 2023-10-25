package com.banco.cuentamovimiento.controller;

import com.banco.cuentamovimiento.model.Cuenta;
import com.banco.cuentamovimiento.service.CuentaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
@AllArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<Cuenta>> getAllCuentas() {
        List<Cuenta> cuentas = cuentaService.getAllCuentas();
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable Long cuentaId) {
        Cuenta cuenta = cuentaService.getCuentaById(cuentaId);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody Cuenta cuenta) {
        Cuenta nueva = cuentaService.createCuenta(cuenta);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @PutMapping("/{cuentaId}")
    public ResponseEntity<Cuenta> updateCuenta(
            @PathVariable Long cuentaId,
            @RequestBody Cuenta cuenta) {
        Cuenta actualizada = cuentaService.updateCuenta(cuentaId, cuenta);
        return new ResponseEntity<>(actualizada, HttpStatus.OK);
    }

    @DeleteMapping("/{cuentaId}")
    public ResponseEntity<String> deleteCuenta(@PathVariable Long cuentaId) {
        cuentaService.deleteCuenta(cuentaId);
        return new ResponseEntity<>("Cuenta eliminada con éxito", HttpStatus.OK);
    }
}
