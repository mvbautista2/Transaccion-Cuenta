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

import lombok.Data;

@Data
public class ClienteDto {
    private String id;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String nombre1;
    private String nombre2;
    private String email;
}
