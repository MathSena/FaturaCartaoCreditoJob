package com.mathsena.faturaCartaoCreditoJob.reader;

import com.mathsena.faturaCartaoCreditoJob.dominio.FaturaCartao;
import com.mathsena.faturaCartaoCreditoJob.dominio.Transacao;
import org.springframework.batch.item.*;

public class FaturaCartaoReader implements ItemStreamReader<FaturaCartao> {

    private ItemStreamReader<Transacao> delegate;
    private Transacao transacaoAtual;

    @Override
    public FaturaCartao read() throws Exception {
        if (transacaoAtual == null)
            transacaoAtual = delegate.read();

        FaturaCartao faturaCartao = null;
        Transacao transacao = transacaoAtual;
        transacaoAtual = null;

        if (transacao != null) {
            faturaCartao = new FaturaCartao();
            faturaCartao.setCartaoCredito(transacao.getCartaoCredito());
            faturaCartao.setCliente(transacao.getCartaoCredito().getCliente());
            faturaCartao.getTransacoes().add(transacao);

            while (isTransacaoRelacionada(transacao))
                faturaCartao.getTransacoes().add(transacaoAtual);
        }
        return faturaCartao;
    }

    private boolean isTransacaoRelacionada(Transacao transacao) throws Exception {
        return peek() != null && transacao.getCartaoCredito().getNumerCartaoCredito() == transacaoAtual.getCartaoCredito().getNumerCartaoCredito();
    }

    private Transacao peek() throws Exception {
        transacaoAtual = delegate.read();
        return transacaoAtual;
    }

    public FaturaCartaoReader(ItemStreamReader<Transacao> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }


}
