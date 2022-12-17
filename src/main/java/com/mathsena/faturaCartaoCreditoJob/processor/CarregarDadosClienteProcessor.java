package com.mathsena.faturaCartaoCreditoJob.processor;

import com.mathsena.faturaCartaoCreditoJob.dominio.Cliente;
import com.mathsena.faturaCartaoCreditoJob.dominio.FaturaCartao;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class CarregarDadosClienteProcessor implements ItemProcessor<FaturaCartao, FaturaCartao> {
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public FaturaCartao process(FaturaCartao faturaCartao) throws Exception {
        String uri = String.format("http://my-json-server.typicode.com/giuliana-bezerra/demo/profile/%d", faturaCartao.getCliente().getId());
        ResponseEntity<Cliente> response = restTemplate.getForEntity(uri, Cliente.class);

        if (response.getStatusCode() != HttpStatus.OK)
            throw new ValidationException("Cliente não encontrado!");

        faturaCartao.setCliente(response.getBody());
        return faturaCartao;
    }

}
