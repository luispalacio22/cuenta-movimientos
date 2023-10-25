package com.banco.cuentamovimiento.dto;

import lombok.*;


@Setter
@Getter
@ToString
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Persona {
    private String nombre;
    private String genero;
    private int edad;
    private String identificacion;
    private String direccion;
    private String telefono;

}
