package com.mathsena.faturaCartaoCreditoJob.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import com.mathsena.faturaCartaoCreditoJob.dominio.FaturaCartao;
import com.mathsena.faturaCartaoCreditoJob.dominio.Transacao;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;


@Configuration
public class ArquivoFaturaCartaoWriterConfig {
    @Bean
    public MultiResourceItemWriter<FaturaCartao> arquivosFaturaCartao() {
        return new MultiResourceItemWriterBuilder<FaturaCartao>()
                .name("arquivosFaturaCartao")
                .resource(new FileSystemResource("files/fatura"))
                .itemCountLimitPerResource(1)
                .resourceSuffixCreator(suffixCreator())
                .delegate(arquivoFaturaCartao())
                .build();
    }

    private FlatFileItemWriter<FaturaCartao> arquivoFaturaCartao() {
        return new FlatFileItemWriterBuilder<FaturaCartao>()
                .name("arquivoFaturaCartao")
                .resource(new FileSystemResource("files/fatura.txt"))
                .lineAggregator(lineAggregator())
                .headerCallback(headerCallback())
                .footerCallback(footerCallback())
                .build();
    }

    @Bean
    public FlatFileFooterCallback footerCallback() {
        return new TotalTransacoesFooterCallback();
    }

    private FlatFileHeaderCallback headerCallback() {
        return new FlatFileHeaderCallback() {

            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.append(String.format("%121s\n", "Cartão XPTO"));
                writer.append(String.format("%121s\n\n", "Rua Vergueiro, 131"));
            }
        };
    }

    private LineAggregator<FaturaCartao> lineAggregator() {
        return new LineAggregator<FaturaCartao>() {

            @Override
            public String aggregate(FaturaCartao faturaCartao) {
                StringBuilder writer = new StringBuilder();
                writer.append(String.format("Nome: %s\n", faturaCartao.getCliente().getNome()));
                writer.append(String.format("Endereço: %s\n\n\n", faturaCartao.getCliente().getEndereco()));
                writer.append(String.format("Fatura completa do cartão %d\n", faturaCartao.getCartaoCredito().getNumerCartaoCredito()));
                writer.append("-------------------------------------------------------------------------------------------------------------------------\n");
                writer.append("DATA DESCRICAO VALOR\n");
                writer.append("-------------------------------------------------------------------------------------------------------------------------\n");

                for (Transacao transacao : faturaCartao.getTransacoes()) {
                    writer.append(String.format("\n[%10s] %-80s - %s",
                            new SimpleDateFormat("dd/MM/yyyy").format(transacao.getData()),
                            transacao.getDescricao(),
                            NumberFormat.getCurrencyInstance().format(transacao.getValor())));
                }
                return writer.toString();
            }

        };
    }

    private ResourceSuffixCreator suffixCreator() {
        return new ResourceSuffixCreator() {

            @Override
            public String getSuffix(int index) {
                return index + ".txt";
            }
        };
    }
}