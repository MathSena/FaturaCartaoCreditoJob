package com.mathsena.faturaCartaoCreditoJob.Step;

import com.mathsena.faturaCartaoCreditoJob.dominio.FaturaCartao;
import com.mathsena.faturaCartaoCreditoJob.dominio.Transacao;
import com.mathsena.faturaCartaoCreditoJob.reader.FaturaCartaoReader;
import com.mathsena.faturaCartaoCreditoJob.writer.TotalTransacoesFooterCallback;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class FaturaCartaoCreditoStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    private ItemStreamReader<Transacao> lerTransacoesReader;
    private ItemProcessor<FaturaCartao, FaturaCartao> carregarDadosClienteProcessor;
    private ItemWriter<FaturaCartao> escreverFaturaCartaoCredito;
    private TotalTransacoesFooterCallback listener;

    @Bean
    public Step faturaCartaoCreditoStep(
            ItemStreamReader<Transacao> lerTransacoesReader,
            ItemProcessor<FaturaCartao, FaturaCartao> carregarDadosClienteProcessor,
            ItemWriter<FaturaCartao> escreverFaturaCartaoCredito,
            TotalTransacoesFooterCallback listener) {
        this.lerTransacoesReader = lerTransacoesReader;
        this.carregarDadosClienteProcessor = carregarDadosClienteProcessor;
        this.escreverFaturaCartaoCredito = escreverFaturaCartaoCredito;
        this.listener = listener;
        return stepBuilderFactory
                .get("faturaCartaoCreditoStep")
                .<FaturaCartao, FaturaCartao>chunk(1)
                .reader(new FaturaCartaoReader(lerTransacoesReader))
                .processor(carregarDadosClienteProcessor)
                .writer(escreverFaturaCartaoCredito)
                .listener(listener)
                .build();
    }

}
