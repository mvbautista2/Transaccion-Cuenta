/*
 * Copyright (c) 2021 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.dao;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.Transaccion;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {
    List<Transaccion> findByClienteProductoPasivoCuentaIdAndFechaBetweenOrderByFechaDesc(String cuentaId, LocalDateTime inicio, LocalDateTime fin);

}
