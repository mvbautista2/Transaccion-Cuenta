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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author valen
 */
@Data
@Entity
@Table(name = "producto_pasivo_canal")
@NoArgsConstructor
public class ProductoPasivoCanal implements Serializable {

    @EmbeddedId
    private ProductoPasivoCanalPK pk;

    @Column(name = "retiro", length = 1)
    private String retiro;

    @Column(name = "deposito", length = 1)
    private String deposito;

    @Column(name = "transfer_in", length = 1)
    private String transferIn;

    @Column(name = "tranfer_out", length = 1)
    private String tranferOut;

    @Column(name = "pago", length = 1)
    private String pago;

    @JoinColumn(name = "cod_producto_pasivo", 
            referencedColumnName = "cod_producto_pasivo", 
            insertable = false, 
            updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProductoPasivo productoPasivo;

}
