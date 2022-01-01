/*
 * Copyright (c) 2021 yazbe.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.controller;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteDto;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteTransaccionDto;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.GenericDetailSerializer;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.TransaccionRQ;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.TransaccionRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.Transaccion;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.service.NotificacionService;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.service.TransaccionService;
import static ec.edu.espe.arqsoftware.TransaccionesCuentas.transform.TransaccionRSTransform.buildTransformRS;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/transaccion")
public class TransaccionController {

    private final TransaccionService service;
    private final NotificacionService serv;

    public TransaccionController(TransaccionService service, NotificacionService serv) {
        this.service = service;
        this.serv = serv;
    }

    
    @GetMapping(value = "{cuentaId}/{fechaInicio}/{fechaFin}")
    @ApiOperation(value = "Obtiene una lista de transacciones", notes = "Obtiene una lista de tracciones de acuerdo al ID de la cuenta, fecha de Inicio y fecha de Fin")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK, Cuando obtiene una lista de tracciones de acuerdo al ID de la cuenta, fecha de Inicio y fecha de Fin"),
        @ApiResponse(code = 404, message = "No existe transacciones  de acuerdo a su Id de cuenta  y fechas enviadas")
    })
    public ResponseEntity listarTransaccionesFechas(
            @PathVariable("cuentaId") String cuentaId,
            @PathVariable("fechaInicio") String fechaInicio,
            @PathVariable("fechaFin") String fechaFin) {
        String errorMessage = "Error en la consulta de transacciónes";
        String errorMessage1 = "Error en las fechas de inicio y fin";
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fechaInicioD;
        LocalDateTime fechaFinD;
        try {
            fechaInicioD = LocalDate.parse(fechaInicio, sdf).atStartOfDay();
            fechaFinD = LocalDate.parse(fechaFin, sdf).atStartOfDay();
        } catch (Exception pe) {
            GenericDetailSerializer errorResponse;
            errorResponse = new GenericDetailSerializer(
                    errorMessage1, pe.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        List<TransaccionRS> transaccionesRS = new ArrayList<>();
        try {
            List<Transaccion> transacciones = this.service.listarPorCuentaFecha(cuentaId, fechaInicioD, fechaFinD);
            log.info("Transacciones obtenidas {} con los parametros: {} - {}",
                    transacciones.size(), fechaInicio, fechaFin);
            transacciones.forEach(t -> {
                transaccionesRS.add(buildTransformRS(t));
            });
        } catch (Exception e) {
            GenericDetailSerializer errorResponse;
            errorResponse = new GenericDetailSerializer(
                    errorMessage, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return ResponseEntity.ok(transaccionesRS);
    }

    @GetMapping(value = "/ultimasTransacciones/{cuentaId}")
    @ApiOperation(value = "Obtiene ultimas Transacciones", notes = "Obtiene las ultimas transacciones de acuerdo a al ID de la cuenta")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK, Cuando obtiene las ultimas transacciones de acuerdo a al ID de la cuenta"),
        @ApiResponse(code = 404, message = "No existe transacciones del ID de la cuenta que se busca")
    })
    public ResponseEntity listarUltimasTransacciones(@PathVariable("cuentaId") String cuentaId) {
        LocalDateTime fechaFinD = LocalDateTime.now(ZoneId.of("America/Chicago")).withNano(0);
        List<TransaccionRS> transaccionesRS = new ArrayList<>();
        String errorMessage = "Error en la consulta de transacciónes";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, -1);
            Date fecha = calendar.getTime();
            LocalDateTime fechaInicioD = fecha.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDateTime().withNano(0);
            List<Transaccion> transacciones = this.service.listarPorCuentaFecha(cuentaId, fechaInicioD, fechaFinD);
            log.info("Transacciones obtenidas {} con los parametros: {} - {}",
                    transacciones.size(), fechaInicioD, fechaFinD);
            transacciones.forEach(t -> {
                transaccionesRS.add(buildTransformRS(t));
            });
        } catch (Exception e) {
            GenericDetailSerializer errorResponse;
            errorResponse = new GenericDetailSerializer(
                    errorMessage, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        return ResponseEntity.ok(transaccionesRS);
    }

    @PostMapping
    @ApiOperation(value = "Crear una transaccion", notes = "Crea una nueva transaccion")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "OK, Cuando crea una nueva transaccion"),
        @ApiResponse(code = 404, message = "Transaccion recibida con errores")
    })
    public ResponseEntity crearTransaccion(@RequestBody TransaccionRQ request) {
        String errorMessage = "Error al crear una transacciónes";
        try {
            log.info("Se va a crear una consulta con la siguiente informacion: {}", request);
            Transaccion transaccion = new Transaccion();
            ClienteProductoPasivo clienteProd = new ClienteProductoPasivo();
            clienteProd.setCuentaId(request.getCuentaId());
            transaccion.setClienteProductoPasivo(clienteProd);
            transaccion.setCuentaSalida(request.getCuentaSalida());
            transaccion.setDescripcion(request.getDescripcion());
            transaccion.setTipo(request.getTipo());
            transaccion.setMonto(request.getMonto());
            this.service.crearTransaccion(transaccion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            GenericDetailSerializer errorResponse;
            errorResponse = new GenericDetailSerializer(
                    errorMessage, e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
