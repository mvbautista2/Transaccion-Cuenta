/*
 * Copyright (c) 2021 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClienteProductoPasivoRS {

    private String cuentaId;

    private String codCliente;

    private BigDecimal saldoContable;

    private BigDecimal saldoDisponible;

    private String estado;

    private LocalDateTime fechaCreacion;

    private ProductoPasivoRS productoPasivo;

}
