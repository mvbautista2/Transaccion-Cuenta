/*
 * Copyright (c) 2021 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
public class ProductoPasivoCanalPK implements Serializable {

    @Column(name = "cod_producto_pasivo")
    private String codProductoPasivo;

    @Column(name = "cod_canal")
    private String codCanal;

}
