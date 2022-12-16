package com.mathsena.faturaCartaoCreditoJob.Step;

import com.mathsena.faturaCartaoCreditoJob.dominio.FaturaCartao;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class FaturaCartaoCreditoStepConfig {


    private StepBuilderFactory stepBuilderFactory;


    public Step faturaCartaoCreditoStep(
            ItemReader<FaturaCartao> lerTransacoesReader,
            ItemProcessor<FaturaCartao, FaturaCartao> carregaDadosClienteProcessor,
            ItemWriter<FaturaCartao> escreverFaturasCartaoCredito){
        return stepBuilderFactory.get("faturaCartaoCreditoStep")
                .<FaturaCartao, FaturaCartao>chunk(1)
                .reader(lerTransacoesReader)
                .processor(carregaDadosClienteProcessor)
                .writer(escreverFaturasCartaoCredito)
                .build();

    }

}
