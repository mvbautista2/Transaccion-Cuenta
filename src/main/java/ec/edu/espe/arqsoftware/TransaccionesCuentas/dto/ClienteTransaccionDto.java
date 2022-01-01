/*
 * Copyright (c) 2021 mafer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mafer - initial API and implementation and/or initial documentation
 */
package ec.edu.espe.arqsoftware.TransaccionesCuentas.dto;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.ClienteProductoPasivo;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.model.Transaccion;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ClienteTransaccionDto {
    private String accountFrom;
    private BigDecimal amount;
    private String email;
    private String fullName;
    private String subject;
    private String type;
    
    public static ClienteTransaccionDto buildTransaccion(Transaccion transaccion, ClienteProductoPasivo cliente, ClienteDto clienteDto){
        ClienteTransaccionDto clienteTransaccion = new ClienteTransaccionDto();
            clienteTransaccion.setAccountFrom(cliente.getCuentaId());
            clienteTransaccion.setAmount(transaccion.getMonto());
            clienteTransaccion.setEmail(clienteDto.getEmail());
            clienteTransaccion.setFullName(clienteDto.getNombre1()+" " + clienteDto.getNombre2()+" "
                                            + clienteDto.getApellidoMaterno()+" " + clienteDto.getApellidoPaterno());
            clienteTransaccion.setSubject("Transaccion exitosa");
            clienteTransaccion.setType(transaccion.getTipo());
            
            return clienteTransaccion;
    }
}
