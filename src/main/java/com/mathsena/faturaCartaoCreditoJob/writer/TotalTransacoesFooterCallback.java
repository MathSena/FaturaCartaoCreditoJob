package com.mathsena.faturaCartaoCreditoJob.writer;

import java.io.IOException;
import java.io.Writer;
import java.text.NumberFormat;
import java.util.List;

import com.mathsena.faturaCartaoCreditoJob.dominio.FaturaCartao;
import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.file.FlatFileFooterCallback;

public class TotalTransacoesFooterCallback implements FlatFileFooterCallback {
    private Double total = 0.0;

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.write(String.format("\n%121s", "Total: " + NumberFormat.getCurrencyInstance().format(total)));
    }

    @BeforeWrite
    public void beforeWrite(List<FaturaCartao> faturas) {
        for (FaturaCartao faturaCartao : faturas)
            total += faturaCartao.getTotal();
    }

    @AfterChunk
    public void afterChunk(ChunkContext chunkContext) {
        total = 0.0;
    }
}