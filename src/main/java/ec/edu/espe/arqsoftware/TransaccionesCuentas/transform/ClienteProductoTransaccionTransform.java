/*
 * Copyright (c) 2021 valen.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    valen - initial API and implementation and/or initial documentation
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.transform;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteProductoTransaccionRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import static ec.edu.espe.arqsoftware.TransaccionesCuentas.transform.ProductoPasivoRSTransform.buildProductoPasivoRS;

/**
 *
 * @author valen
 */
public class ClienteProductoTransaccionTransform {
    public static ClienteProductoTransaccionRS buildClienteProductoTransaccionRS(ClienteProductoPasivo clienteProducto) {
        return ClienteProductoTransaccionRS.builder()
                .cuentaId(clienteProducto.getCuentaId())
                .saldoContable(clienteProducto.getSaldoContable())
                .saldoDisponible(clienteProducto.getSaldoDisponible())
                .estado(clienteProducto.getEstado())
                .fechaCreacion(clienteProducto.getFechaCreacion())
                .productoPasivo(buildProductoPasivoRS(clienteProducto.getProductoPasivo()))
                .build();
                
    }
}
