/*
 * Copyright (c) 2021
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.service;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dao.ClienteProductoPasivoRepository;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.exception.CreateException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ProductoPasivo;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ClienteProductoPasivoService {

    private final ClienteProductoPasivoRepository clienteProductoRepo;

    public ClienteProductoPasivoService(ClienteProductoPasivoRepository clienteProductoRepo) {
        this.clienteProductoRepo = clienteProductoRepo;
        
    }

    
    public List<ClienteProductoPasivo> listarTodo() {
        return this.clienteProductoRepo.findAll();
    }

    

}
