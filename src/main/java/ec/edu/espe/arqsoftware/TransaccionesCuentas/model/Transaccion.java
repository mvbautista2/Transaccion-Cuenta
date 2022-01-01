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
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author valen
 */
@Data
@Entity
@Table(name = "transaccion")
@NoArgsConstructor
public class Transaccion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_transaccion")
    private Integer codTransaccion;
    
    @Column(name = "cuenta_salida", length = 10)
    private String cuentaSalida;
    
    @Column(name = "descripcion", length = 150)
    private String descripcion;
    
    @Column(name = "tipo", length = 3)
    private String tipo;
    
    @Column(name = "monto", precision = 18, scale = 2)
    private BigDecimal monto;
    
    @Column(name = "saldo_anterior", precision = 18, scale = 2)
    private BigDecimal saldoAnterior;
    
    @Column(name = "saldo_actual", precision = 18, scale = 2)
    private BigDecimal saldoActual;
    
    @Column(name = "fecha")
    private LocalDateTime fecha;
    
    @JoinColumn(name = "cuenta_id", referencedColumnName = "cuenta_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ClienteProductoPasivo clienteProductoPasivo;
    
}
