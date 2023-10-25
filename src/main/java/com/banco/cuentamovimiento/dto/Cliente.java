package com.banco.cuentamovimiento.dto;


import lombok.*;




@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cliente extends Persona{

    private Long clienteId;
    private String contrasena;
    private boolean estado;
}
