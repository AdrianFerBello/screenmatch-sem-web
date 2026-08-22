package br.com.alura.screenmatch.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

public class ConsultaChatGPT {

    private static final OpenAIClient client =
            OpenAIOkHttpClient.fromEnv();

    public static String obterTraducao(String texto) {

        ResponseCreateParams parametros =
                ResponseCreateParams.builder()
                        .input("""
                                Traduza o seguinte texto para o português do Brasil.
                                Retorne apenas a tradução, sem explicações.

                                Texto:
                                """ + texto)
                        .model(ChatModel.GPT_4O_MINI)
                        .build();

        Response resposta = client.responses().create(parametros);

        return resposta.output().stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(content -> content.outputText().stream())
                .map(outputText -> outputText.text())
                .findFirst()
                .orElse("Tradução não disponível");
    }
}