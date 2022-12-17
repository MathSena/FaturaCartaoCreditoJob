package com.mathsena.faturaCartaoCreditoJob.dominio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FaturaCartao {
    private Cliente cliente;
    private CartaoCredito cartaoCredito;
    private List<Transacao> transacoes = new ArrayList<>();

    public Double getTotal() {
        return transacoes
                .stream()
                .mapToDouble(Transacao::getValor)
                .reduce(0.0, Double::sum);
    }
}
