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
package ec.edu.espe.arqsoftware.TransaccionesCuentas.dto;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author valen
 */
@Data
@Builder
public class ProductoPasivoCanalRS {
    
    private String codigoProductoPasivo;
    private String codigoCanal;
    private String retiro;
    private String deposito;    
    private String transferIn;
    private String transferOut;
    private String pago;
    
}
