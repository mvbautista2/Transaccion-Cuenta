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

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ProductoPasivoCanalRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ProductoPasivoCanal;

/**
 *
 * @author valen
 */
class ProductoPasivoCanalRSTransform {
    public static ProductoPasivoCanalRS buildProductoPasivoCanalRS(ProductoPasivoCanal productoPasivoCanal) {
        return ProductoPasivoCanalRS .builder().codigoProductoPasivo(productoPasivoCanal.getProductoPasivo().getCodProductoPasivo())
                .codigoCanal(productoPasivoCanal.getPk().getCodCanal())
                .retiro(productoPasivoCanal.getRetiro())
                .deposito(productoPasivoCanal.getDeposito())
                .transferIn(productoPasivoCanal.getTransferIn())
                .transferOut(productoPasivoCanal.getTranferOut())
                .pago(productoPasivoCanal.getPago())
                .build();
    }
    
}
