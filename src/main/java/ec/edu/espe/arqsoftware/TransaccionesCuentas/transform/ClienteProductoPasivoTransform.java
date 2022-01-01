/*
 * Copyright (c) 2021 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.transform;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteProductoPasivoRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import static ec.edu.espe.arqsoftware.TransaccionesCuentas.transform.ProductoPasivoRSTransform.buildProductoPasivoRS;

public class ClienteProductoPasivoTransform {

    public static ClienteProductoPasivoRS buildClienteProductoPasivoRS(ClienteProductoPasivo clienteProducto) {
        return ClienteProductoPasivoRS.builder()
                .cuentaId(clienteProducto.getCuentaId())
                .codCliente(clienteProducto.getCodCliente())
                .saldoContable(clienteProducto.getSaldoContable())
                .saldoDisponible(clienteProducto.getSaldoDisponible())
                .estado(clienteProducto.getEstado())
                .fechaCreacion(clienteProducto.getFechaCreacion())
                .productoPasivo(buildProductoPasivoRS(clienteProducto.getProductoPasivo()))
                .build();
    }
}
