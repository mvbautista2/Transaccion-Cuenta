/*
 * Copyright (c) 2021
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.transform;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ProductoPasivoCanalRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ProductoPasivo;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ProductoPasivoRS;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ProductoPasivoCanal;
import java.util.ArrayList;
import java.util.List;

public class ProductoPasivoRSTransform {

    public static ProductoPasivoRS buildProductoPasivoRS(ProductoPasivo productoPasivo) {
        return ProductoPasivoRS.builder()
                .codProductoPasivo(productoPasivo.getCodProductoPasivo())
                .nombre(productoPasivo.getNombre())
                .tasa(productoPasivo.getTasa())
                .acreditacion(productoPasivo.getAcreditacion())
                .build();
    }

    public static ProductoPasivoRS buildProductoPasivoCompletoRS(ProductoPasivo productoPasivo) {
        List<ProductoPasivoCanal> productoPasivoCanal = productoPasivo.getProductoPasivoCanal();
        List<ProductoPasivoCanalRS> productoPasivoCanalRS = new ArrayList<>();
        productoPasivoCanal.forEach(e -> {
            productoPasivoCanalRS.add(ProductoPasivoCanalRSTransform.buildProductoPasivoCanalRS(e));
        });
        return ProductoPasivoRS.builder()
                .codProductoPasivo(productoPasivo.getCodProductoPasivo())
                .nombre(productoPasivo.getNombre())
                .tasa(productoPasivo.getTasa())
                .acreditacion(productoPasivo.getAcreditacion())
                .productoPasivoCanal(productoPasivoCanalRS)
                .build();
                
    }
}
