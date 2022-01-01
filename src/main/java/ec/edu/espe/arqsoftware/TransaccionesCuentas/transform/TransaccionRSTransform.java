/*
 * Copyright (c) 2021 yazbe.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.transform;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.TransaccionRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.Transaccion;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TransaccionRSTransform {

    public static TransaccionRS buildTransformRS(Transaccion transaccion) {
        LocalDateTime fecha = transaccion.getFecha().atZone(ZoneId.of("America/Chicago")).toLocalDate().atStartOfDay();

        return TransaccionRS.builder()
                .codTransaccion(transaccion.getCodTransaccion())
                .cuentaSalida(transaccion.getCuentaSalida())
                .descripcion(transaccion.getDescripcion())
                .tipo(transaccion.getTipo())
                .monto(transaccion.getMonto())
                .saldoAnterior(transaccion.getSaldoAnterior())
                .saldoActual(transaccion.getSaldoActual())
                .fecha(fecha)
                .build();
    }
}
