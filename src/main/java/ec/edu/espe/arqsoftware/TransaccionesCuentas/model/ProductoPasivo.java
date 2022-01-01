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
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author valen
 */
@Data
@Entity
@Table(name = "producto_pasivo")
@NoArgsConstructor
public class ProductoPasivo implements Serializable {

    @Id
    @Column(name = "cod_producto_pasivo", length = 3)
    private String codProductoPasivo;

    @Column(name = "nombre", length = 64)
    private String nombre;

    @Column(name = "tasa", precision = 3, scale = 2)
    private BigDecimal tasa;

    @Column(name = "acreditacion", length = 3)
    private String acreditacion;

    @Column(name = "saldo_inicial", precision = 18, scale = 2)
    private BigDecimal saldoInicial;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productoPasivo", fetch = FetchType.LAZY)
    private List<ProductoPasivoCanal> productoPasivoCanal;

}
