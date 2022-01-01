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
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductoPasivoRS {

    private String codProductoPasivo;

    private String nombre;

    private BigDecimal tasa;

    private String acreditacion;
    
    private List<ProductoPasivoCanalRS> productoPasivoCanal;

}
