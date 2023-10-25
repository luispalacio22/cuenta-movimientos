package com.banco.cuentamovimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.banco.cuentamovimiento")
public class CuentaMovimientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuentaMovimientoApplication.class, args);
	}

}
