package com.mathsena.faturaCartaoCreditoJob.dominio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartaoCredito {

    private int numerCartaoCredito;
    private Cliente cliente;
}
