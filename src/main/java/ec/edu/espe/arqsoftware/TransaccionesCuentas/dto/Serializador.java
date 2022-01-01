/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class Serializador {

    private String message;
    private String detail;

    public static ResponseEntity Error(Exception e, String errorMensaje) {
        Serializador errorResponse;
        errorResponse = new Serializador(
                errorMensaje,
                e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
