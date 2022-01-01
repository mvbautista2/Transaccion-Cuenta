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
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cliente_producto_pasivo")

public class ClienteProductoPasivo implements Serializable {

    @Id
    @Column(name = "cuenta_id", length = 10)
    private String cuentaId;
    
    @Column(name = "cod_producto_pasivo", length = 3)
    private String codProductoPasivo;

    @Column(name = "cod_cliente", length = 24)
    private String codCliente;

    @Column(name = "saldo_contable", precision = 18, scale = 2)
    private BigDecimal saldoContable;

    @Column(name = "saldo_disponible", precision = 18, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "estado", length = 3)
    private String estado;

    @JoinColumn(name = "cod_producto_pasivo", referencedColumnName = "cod_producto_pasivo", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ProductoPasivo productoPasivo;

    @OneToMany(mappedBy = "clienteProductoPasivo", fetch = FetchType.LAZY)
    private List<Transaccion> transaccion;


}
