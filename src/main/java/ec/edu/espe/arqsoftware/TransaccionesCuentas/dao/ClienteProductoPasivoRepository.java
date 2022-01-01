/*
 * Copyright (c) 2021 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.dao;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ProductoPasivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteProductoPasivoRepository extends JpaRepository<ClienteProductoPasivo, String> {

    ClienteProductoPasivo findByCuentaId(String cuentaId);
    
    List<ClienteProductoPasivo> findByCodClienteOrderByProductoPasivoDesc(String codCliente);

    List<ClienteProductoPasivo> findByCodProductoPasivoOrderByCuentaIdAsc(String codProductoPasivo);
}
