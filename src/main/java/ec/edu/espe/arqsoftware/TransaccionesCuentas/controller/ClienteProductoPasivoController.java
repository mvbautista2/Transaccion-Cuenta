/*
 * Copyright (c) 2021 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.controller;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteProductoPasivoRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteProductoTransaccionRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.GenericDetailSerializer;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.Serializador;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.TransaccionRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.exception.FindException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.Transaccion;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.service.ClienteProductoPasivoService;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.service.TransaccionService;
import static ec.edu.espe.arqsoftware.TransaccionesCuentas.transform.ClienteProductoPasivoTransform.buildClienteProductoPasivoRS;
import static ec.edu.espe.arqsoftware.TransaccionesCuentas.transform.ClienteProductoTransaccionTransform.buildClienteProductoTransaccionRS;
import static ec.edu.espe.arqsoftware.TransaccionesCuentas.transform.TransaccionRSTransform.buildTransformRS;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/api/clienteProductoPasivo")
public class ClienteProductoPasivoController {

    private final ClienteProductoPasivoService service;
    private final TransaccionService transaccionesService;

    public ClienteProductoPasivoController(ClienteProductoPasivoService service, TransaccionService transaccionesService) {
        this.service = service;
        this.transaccionesService = transaccionesService;
    }

   
    
    @ApiOperation(value = "Listar todas las cuentas del cliente con sus ultimas transacciones",
            notes = "Listar ultimas transacciones de cada una de las cuentas del cliente")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK. Las transacciones se obtiene correctamente"),
        @ApiResponse(code = 400, message = "Bad Request. El formato de fecha es: yyyy-MM-dd"),
        @ApiResponse(code = 500, message = "Error inesperado del sistema")})
    @GetMapping(value = "")

    public ResponseEntity cuentasUltimasTransacciones() {
        List<ClienteProductoPasivo> clienteProductoPasivo = this.service.listarTodo();
        List<ClienteProductoTransaccionRS> clienteProductoTransaccionRS = new ArrayList<>();
        for (ClienteProductoPasivo c : clienteProductoPasivo) {
            List<TransaccionRS> transaccionesRS = new ArrayList<>();
             String errorMessage = "Error en la consulta de últimas transacciones";
            try {
                List<Transaccion> transacciones = this.transaccionesService.obtenerUltimosMovimientos(c.getCuentaId());
                log.info("Transacciones obtenidas {} con la cuentaId: {}",
                        transacciones.size(), c.getCuentaId());
                transacciones.forEach(t -> {
                    transaccionesRS.add(buildTransformRS(t));
                });
                ClienteProductoTransaccionRS clienteRS = buildClienteProductoTransaccionRS(c);
                clienteRS.setTransaccion(transaccionesRS);
                clienteProductoTransaccionRS.add(clienteRS);

            } catch (Exception e) {
                GenericDetailSerializer errorResponse;
            errorResponse = new GenericDetailSerializer(
                    errorMessage, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        return ResponseEntity.ok(clienteProductoTransaccionRS);
    }

    
}
