/*
 * Copyright (c) 2021
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.service;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dao.ClienteProductoPasivoRepository;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dao.TransaccionRepository;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteDto;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteTransaccionDto;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.Transaccion;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepo;
    private final ClienteProductoPasivoRepository clienteProdRepo;
    private final NotificacionService serv;

    public TransaccionService(TransaccionRepository transaccionRepo, ClienteProductoPasivoRepository clienteProdRepo, NotificacionService serv) {
        this.transaccionRepo = transaccionRepo;
        this.clienteProdRepo = clienteProdRepo;
        this.serv = serv;
    }

    public List<Transaccion> listarPorCuentaFecha(String cuentaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        LocalDateTime ldInicio = fechaInicio.atZone(ZoneId.of("America/Chicago")).toLocalDate().atStartOfDay();
        LocalDateTime ldFin = fechaFin.atZone(ZoneId.of("America/Chicago")).toLocalDate().atTime(23, 59, 59);
        Period diff = Period.between(
                fechaInicio.atZone(ZoneId.of("America/Chicago")).toLocalDate(),
                fechaFin.atZone(ZoneId.of("America/Chicago")).toLocalDate());
        log.info("Meses de diferencia {}:", diff.getMonths());
        log.info("Va a buscar transacciones desde: {} hasta: {}, de la cuenta {}", ldInicio, ldFin, cuentaId);
        if (ldInicio.isBefore(ldFin) && diff.getMonths() <= 6) {
            return this.transaccionRepo.findByClienteProductoPasivoCuentaIdAndFechaBetweenOrderByFechaDesc(
                    cuentaId, ldInicio, ldFin);
        } else {
            log.error("Error en las fechas recibidas: fechaInicio: {}, fechaFin: {}", fechaInicio, fechaFin);
            throw new IllegalArgumentException("Fechas invalidas");
        }
    }

    public List<Transaccion> obtenerUltimosMovimientos(String cuentaId) {
        Calendar fechaFin = Calendar.getInstance();
        Calendar fechaInicio = Calendar.getInstance();
        Optional<ClienteProductoPasivo> clienteProductoPasivoOpt = this.clienteProdRepo.findById(cuentaId);
        if (clienteProductoPasivoOpt.isPresent()) {
            fechaFin.set(Calendar.DAY_OF_MONTH, 17);
            fechaInicio.add(Calendar.MONTH, -1);
            fechaInicio.set(Calendar.DAY_OF_MONTH, 16);
            fechaFin.add(Calendar.DAY_OF_WEEK, -1);
        }
        LocalDateTime ldInicio = fechaInicio.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate().atStartOfDay();
        LocalDateTime ldFin = fechaFin.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate().atTime(23, 59, 59);
        Period diff = Period.between(
                fechaInicio.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate(),
                fechaFin.toInstant().atZone(ZoneId.of("America/Chicago")).toLocalDate());
        log.info("Meses de diferencia: {}", diff.getMonths());
        log.info("Va a buscar transacciones desde: {} hasta: {}, de la cuenta {}", ldInicio, ldFin, cuentaId);
        if (ldInicio.isBefore(ldFin) && diff.getMonths() <= 6) {
            return this.transaccionRepo.findByClienteProductoPasivoCuentaIdAndFechaBetweenOrderByFechaDesc(cuentaId, ldInicio, ldFin);
        } else {
            log.error("Error en las fechas recibidas: fechaInicio: {}, fechaFin: {}", fechaInicio, fechaFin);
            throw new IllegalArgumentException("Fechas invalidas");
        }
    }

    @Transactional
    public void crearTransaccion(Transaccion transaccion) {
        LocalDateTime fecha = LocalDateTime.now(ZoneId.of("America/Chicago")).withNano(0);
        Optional<ClienteProductoPasivo> cuentaActual = this.clienteProdRepo
                .findById(transaccion.getClienteProductoPasivo().getCuentaId());
        ClienteDto clienteInfo = new ClienteDto();
        if (cuentaActual.isPresent()) {
            if ("0000000000".equals(cuentaActual.get().getCuentaId())) {
                log.info("Cuenta del Banco Banquito");
            } else {
                try {
                    clienteInfo = this.serv.readClient(cuentaActual.get().getCodCliente());
                    log.info("{}", clienteInfo);
                } catch (Exception e) {
                    log.info("No se puede encontrar al cliente");
                }
            }
            switch (transaccion.getTipo()) {
                case "TRO":
                    if (!transaccion.getCuentaSalida().isEmpty() && !transaccion.getCuentaSalida().isEmpty()) {
                        Optional<ClienteProductoPasivo> cuentaDestino = this.clienteProdRepo
                                .findById(transaccion.getCuentaSalida());

                        if (cuentaActual.get().getSaldoDisponible().compareTo(transaccion.getMonto()) == -1) {
                            throw new IllegalArgumentException("No tiene suficiente dinero para realizar esta operación");
                        }
                        if (cuentaDestino.isPresent()) {
                            transaccion.setSaldoAnterior(cuentaActual.get().getSaldoDisponible());
                            BigDecimal saldo = cuentaActual.get().getSaldoDisponible().subtract(transaccion.getMonto());
                            transaccion.setSaldoActual(saldo);
                            transaccion.setFecha(fecha);
                            this.transaccionRepo.save(transaccion);

                            try {
                                if (!("0000000000".equals(cuentaActual.get().getCuentaId()))) {
                                    ClienteTransaccionDto clienteTransaccion = ClienteTransaccionDto.buildTransaccion(transaccion, cuentaActual.get(), clienteInfo);
                                    this.serv.sendNotification(clienteTransaccion);
                                }
                            } catch (Exception e) {
                                log.info("No se puede enviar una notificación");
                            }

                            cuentaActual.get().setSaldoContable(cuentaActual.get().getSaldoContable().subtract(transaccion.getMonto()));
                            cuentaActual.get().setSaldoDisponible(saldo);
                            this.clienteProdRepo.save(cuentaActual.get());

                            Transaccion transaccionDestino = new Transaccion();
                            transaccionDestino.setClienteProductoPasivo(cuentaDestino.get());
                            transaccionDestino.setCuentaSalida(cuentaActual.get().getCuentaId());
                            transaccionDestino.setMonto(transaccion.getMonto());
                            transaccionDestino.setDescripcion(transaccion.getDescripcion());
                            transaccionDestino.setTipo("TRI");
                            transaccionDestino.setFecha(fecha);
                            crearTransaccion(transaccionDestino);

                        } else {
                            throw new IllegalArgumentException("La cuenta saliente no existe");
                        }
                    }
                    break;

                case "TRI":
                    if (!transaccion.getCuentaSalida().isEmpty() && !transaccion.getCuentaSalida().isEmpty()) {
                        Optional<ClienteProductoPasivo> cuentaDestino = this.clienteProdRepo
                                .findById(transaccion.getCuentaSalida());

                        if (cuentaDestino.isPresent()) {
                            transaccion.setSaldoAnterior(cuentaActual.get().getSaldoDisponible());
                            BigDecimal saldo = cuentaActual.get().getSaldoDisponible().add(transaccion.getMonto());
                            transaccion.setSaldoActual(saldo);
                            transaccion.setFecha(fecha);
                            this.transaccionRepo.save(transaccion);
                            try {
                                if (!("0000000000".equals(cuentaActual.get().getCuentaId()))) {
                                    ClienteTransaccionDto clienteTransaccion = ClienteTransaccionDto.buildTransaccion(transaccion, cuentaActual.get(), clienteInfo);
                                    this.serv.sendNotification(clienteTransaccion);
                                }
                            } catch (Exception e) {
                                log.info("No se puede enviar una notificación");
                            }
                            cuentaActual.get().setSaldoContable(cuentaActual.get().getSaldoContable().add(transaccion.getMonto()));
                            cuentaActual.get().setSaldoDisponible(saldo);
                            this.clienteProdRepo.save(cuentaActual.get());

                        } else {
                            throw new IllegalArgumentException("La cuenta saliente no existe");
                        }
                    }
                    break;

                case "DEP":
                    transaccion.setSaldoAnterior(cuentaActual.get().getSaldoDisponible());
                    BigDecimal saldo = cuentaActual.get().getSaldoDisponible().add(transaccion.getMonto());
                    transaccion.setSaldoActual(saldo);
                    transaccion.setFecha(fecha);
                    this.transaccionRepo.save(transaccion);
                    try {
                        if (!("0000000000".equals(cuentaActual.get().getCuentaId()))) {
                            ClienteTransaccionDto clienteTransaccion = ClienteTransaccionDto.buildTransaccion(transaccion, cuentaActual.get(), clienteInfo);
                            this.serv.sendNotification(clienteTransaccion);
                        }
                    } catch (Exception e) {
                        log.info("No se puede enviar una notificación");
                    }
                    cuentaActual.get().setSaldoContable(cuentaActual.get().getSaldoContable().add(transaccion.getMonto()));
                    cuentaActual.get().setSaldoDisponible(saldo);
                    this.clienteProdRepo.save(cuentaActual.get());

                    break;

                case "RET":
                    if (cuentaActual.get().getSaldoDisponible().compareTo(transaccion.getMonto()) == -1) {
                        throw new IllegalArgumentException("No tiene suficiente dinero para realizar esta operación");
                    }
                    transaccion.setSaldoAnterior(cuentaActual.get().getSaldoDisponible());
                    transaccion.setSaldoActual(cuentaActual.get().getSaldoDisponible().subtract(transaccion.getMonto()));
                    transaccion.setFecha(fecha);
                    this.transaccionRepo.save(transaccion);
                    try {
                        if (!("0000000000".equals(cuentaActual.get().getCuentaId()))) {
                            ClienteTransaccionDto clienteTransaccion1 = ClienteTransaccionDto.buildTransaccion(transaccion, cuentaActual.get(), clienteInfo);
                            this.serv.sendNotification(clienteTransaccion1);
                        }
                    } catch (Exception e) {
                        log.info("No se puede enviar una notificación");
                    }
                    cuentaActual.get().setSaldoContable(cuentaActual.get().getSaldoContable().subtract(transaccion.getMonto()));
                    cuentaActual.get().setSaldoDisponible(cuentaActual.get().getSaldoDisponible().subtract(transaccion.getMonto()));
                    this.clienteProdRepo.save(cuentaActual.get());
                    break;

                case "PAI":
                    if (!transaccion.getCuentaSalida().isEmpty() && !transaccion.getCuentaSalida().isEmpty()) {
                        Optional<ClienteProductoPasivo> cuentaDestino = this.clienteProdRepo
                                .findById(transaccion.getCuentaSalida());

                        if (cuentaActual.get().getSaldoDisponible().compareTo(transaccion.getMonto()) == -1) {
                            throw new IllegalArgumentException("No tiene suficiente dinero para realizar esta operación");
                        }
                        if (cuentaDestino.isPresent()) {
                            transaccion.setSaldoAnterior(cuentaActual.get().getSaldoDisponible());

                            transaccion.setSaldoActual(cuentaActual.get().getSaldoDisponible().subtract(transaccion.getMonto()));
                            transaccion.setFecha(fecha);
                            this.transaccionRepo.save(transaccion);
                            try {
                                if (!("0000000000".equals(cuentaActual.get().getCuentaId()))) {
                                    ClienteTransaccionDto clienteTransaccion2 = ClienteTransaccionDto.buildTransaccion(transaccion, cuentaActual.get(), clienteInfo);
                                    this.serv.sendNotification(clienteTransaccion2);
                                }
                            } catch (Exception e) {
                                log.info("No se puede enviar una notificación");
                            }

                            cuentaActual.get().setSaldoContable(cuentaActual.get().getSaldoContable().subtract(transaccion.getMonto()));
                            cuentaActual.get().setSaldoDisponible(cuentaActual.get().getSaldoDisponible().subtract(transaccion.getMonto()));
                            this.clienteProdRepo.save(cuentaActual.get());

                            Transaccion transaccionDestino = new Transaccion();
                            transaccionDestino.setClienteProductoPasivo(cuentaDestino.get());
                            transaccionDestino.setCuentaSalida(cuentaActual.get().getCuentaId());
                            transaccionDestino.setMonto(transaccion.getMonto());
                            transaccionDestino.setDescripcion(transaccion.getDescripcion());
                            transaccionDestino.setTipo("TRI");
                            transaccionDestino.setFecha(fecha);
                            crearTransaccion(transaccionDestino);

                        } else {
                            throw new IllegalArgumentException(" Error la cuenta saliente no existe");
                        }
                    }
                    break;

                default:
                    throw new IllegalArgumentException("No existe este tipo de transacción");
            }
        } else {
            throw new IllegalArgumentException("Error la cuenta no existe");
        }

    }

}
