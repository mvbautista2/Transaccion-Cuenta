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
package ec.edu.espe.arqsoftware.TransaccionesCuentas.service;

import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteDto;
import ec.edu.espe.arqsoftware.TransaccionesCuentas.dto.ClienteTransaccionDto;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Data
@Slf4j
public class NotificacionService {
    
    @NonNull
    private final String accountServiceURL = "http://http://34.125.102.171:8200/api";
    private final String clientExternalServiceURL = "http://http://34.125.102.171:8300/api";
    
    public ClienteTransaccionDto sendNotification(ClienteTransaccionDto transaccion) {
        WebClient client = WebClient.create();
        return  client.post()
                .uri(accountServiceURL + "/v1/notifications/transaction/notify/")
                .body(Mono.just(transaccion), ClienteTransaccionDto.class)
                .retrieve()
                .bodyToMono(ClienteTransaccionDto.class)
                .block();
    }
    
    public ClienteDto readClient(String id){
        WebClient client = WebClient.create();

        ClienteDto response = client.get()
                .uri(clientExternalServiceURL + "/cliente/" + id)
                .retrieve()
                .bodyToMono(ClienteDto.class)
                .block();

        return response;
    }
}
