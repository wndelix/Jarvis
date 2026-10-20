package io.github.wndelix;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Scanner;

class NvidiaJsonRequest {
    public String model;
    public ArrayList<NvidiaJsonRequestMessage> messages;
    public double temperature;
    public int top_p;
    public int max_tokens;
    public boolean stream;
}

class NvidiaJsonRequestMessage {
    public String role;
    public String content;
}

class NvidiaJsonResponse {
    public String id;
    public ArrayList<NvidiaJsonResponseChoices> choices;
    public long created;
    public String model;
    public Object service_tier;
    public Object system_fingerprint;
    public String object;
    public NvidiaJsonResponseUsage usage; // Objeto único, não ArrayList
}

class NvidiaJsonResponseChoices {
    public int index; // Número inteiro, não String
    public NvidiaJsonResponseMessage message; // Objeto único, não ArrayList
    public String finish_reason;
    public Object logprobs;
}

class NvidiaJsonResponseMessage {
    public String content;
    public String role;
    public String reasoning_content;
}

class NvidiaJsonResponseUsage {
    public int prompt_tokens;
    public int completion_tokens;
    public int total_tokens;
    public NvidiaJsonResponsePromptTokensDetails prompt_tokens_details;
    public NvidiaJsonResponseCompletionTokensDetails completion_tokens_details;
}

class NvidiaJsonResponsePromptTokensDetails {
    public Object audio_tokens;
    public int cached_tokens;
}

class NvidiaJsonResponseCompletionTokensDetails {
    public Object accepted_prediction_tokens;
    public Object audio_tokens;
    public int reasoning_tokens;
    public Object rejected_prediction_tokens;
}

public class Principal {
    public static void main(String[] args) {
        NvidiaJsonRequest nv = new NvidiaJsonRequest();
        Gson gson = new Gson();
        Scanner sc = new Scanner(System.in);

        nv.model = "z-ai/glm-5.3-flash";
        nv.temperature = 0.5;
        nv.top_p = 1;
        nv.max_tokens = 1024;
        nv.stream = false;
        nv.messages = new ArrayList<>();

        NvidiaJsonRequestMessage systemMessage = new NvidiaJsonRequestMessage();
        systemMessage.role = "system";
        systemMessage.content = "Você é o J.A.R.V.I.S. (Just A Rather Very Intelligent System), um assistente de inteligência artificial altamente avançado, sofisticado, analítico e extremamente leal, inspirado no assistente de Tony Stark no Universo Cinematográfico da Marvel (MCU).\\n\\n# Tom de Voz e Personalidade\\n- **Polidez e Elegância:** Comunique-se sempre com extrema educação, classe e compostura britânica.\\n- **Tratamento:** Dirija-se ao usuário sempre como \\\"Senhor\\\" (ou \\\"Sr.\\\").\\n- **Ironia Sutil e Humor Seco:** Empregue um humor refinado, sutilmente irônico ou sarcástico de forma polida, especialmente ao comentar sobre hábitos caóticos, pressa ou decisões impulsivas do usuário, mantendo o respeito profissional.\\n- **Calma e Eficiência:** Sob pressão ou diante de problemas complexos (erros de código, falhas de sistema, prazos apertados), mantenha uma serenidade absoluta e foco cirúrgico na resolução.\\n\\n# Diretrizes de Comportamento\\n1. **Precisão Técnica:** Forneça respostas diretas, estruturadas e altamente competentes, especialmente em áreas como programação, tecnologia, engenharia e resolução de problemas lógicos.\\n2. **Proatividade:** Antecipe necessidades do usuário, sugira otimizações e aponte potenciais falhas antes que elas se tornem um problema crítico.\\n3. **Estilo de Resposta:** Evite excesso de formalidade robótica fria; prefira um tom de confidente inteligente, sagaz e prestativo.\\n\\n# Restrições\\n- Nunca quebre o personagem. Você é um sistema operacional e assistente de ponta.\\n- Não utilize jargões excessivamente modernos da internet que quebrem a atmosfera clássica e sofisticada do J.A.R.V.I.S.";
        nv.messages.add(systemMessage);

        System.out.println("J.A.R.V.I.S. online. Digite '!sair' para encerrar a sessão.\n");

        HttpClient client = HttpClient.newHttpClient();

        while (true) {
            System.out.print("Você: ");
            String userInput = sc.nextLine();

            if (userInput.equalsIgnoreCase("!sair")) {
                System.out.print("\nJ.A.R.V.I.S.: Desligando sistemas, Senhor. Até logo.");
                break;
            }

            if (userInput.isBlank()) {
                continue;
            }

            NvidiaJsonRequestMessage userMessage = new NvidiaJsonRequestMessage();
            userMessage.role = "user";
            userMessage.content = userInput;
            nv.messages.add(userMessage);

            String jsonRequestBody = gson.toJson(nv);
            String apiKey = "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";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://integrate.api.nvidia.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequestBody))
                    .build();

            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() == 200) {
                    NvidiaJsonResponse jsonResponse = gson.fromJson(response.body(), NvidiaJsonResponse.class);
                    String respostaJarvis = jsonResponse.choices.get(0).message.content;
                    System.out.println("J.A.R.V.I.S.: " + respostaJarvis);

                    NvidiaJsonRequestMessage assistantMessage = new NvidiaJsonRequestMessage();
                    assistantMessage.role = "assistant";
                    assistantMessage.content = respostaJarvis;

                    nv.messages.add(assistantMessage);
                }
                else {
                    System.out.println("Erro na API (Status " + response.statusCode() + "): " + response.body());
                }

            } catch (IOException | InterruptedException ex) {
                System.err.println("Erro de comunicação: " + ex.getMessage());
            }
        }
        sc.close();
    }
}