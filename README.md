# J.A.R.V.I.S.

Assistente de linha de comando inspirado no J.A.R.V.I.S. do Universo Cinematográfico da Marvel, desenvolvido em Java. O programa se comunica com um modelo de linguagem hospedado pela NVIDIA (API compatível com o formato de chat da OpenAI) e mantém o histórico da conversa durante a sessão, permitindo respostas contextualizadas.

Este projeto tem fins educacionais e serve como exemplo de integração de uma aplicação Java com uma API de inteligência artificial, utilizando `java.net.http` para as requisições HTTP e Gson para a serialização JSON.

## Funcionalidades

- Interface de terminal interativa.
- Manutenção do histórico de mensagens durante a sessão.
- Persona configurada por uma mensagem de sistema, com tom formal e respostas técnicas.
- Tratamento de erros de comunicação e de status HTTP não esperados.

## Requisitos

- **Java Development Kit (JDK) 17** ou superior.
- **Apache Maven 3.6** ou superior (somente para compilar a partir do código-fonte).
- Conexão com a internet.
- Uma chave de API válida da NVIDIA, obtida em [build.nvidia.com](https://build.nvidia.com).

Para verificar as versões instaladas:

```bash
java -version
mvn -version
```

## Instalação

Existem duas formas de executar o projeto.

### Opção 1: Baixar o JAR pronto

1. Acesse a seção **Releases** do repositório.
2. Baixe o arquivo `jarvis-v0.0.0.jar`.
3. Execute no terminal:

```bash
java -jar jarvis-v0.0.0.jar
```

### Opção 2: Compilar a partir do código-fonte

1. Clone o repositório:

```bash
git clone https://github.com/wndelix/jarvis.git
cd jarvis
```

2. Gere o pacote com Maven:

```bash
mvn clean package
```

O build produz um JAR executável (com as dependências embutidas) em `target/jarvis-v0.0.0.jar`.

3. Execute:

```bash
java -jar target/jarvis-v0.0.0.jar
```

## Configuração da chave de API

Atualmente, a chave é definida diretamente no código, na classe `Principal`, na variável `apiKey` dentro do método `main`:

```java
String apiKey = "sua-chave-aqui";
```

Para utilizar sua própria chave, substitua o valor acima e recompile o projeto. Como boa prática, não versione chaves reais em repositórios públicos. Uma alternativa mais segura é ler a chave de uma variável de ambiente, por exemplo:

```java
String apiKey = System.getenv("NVIDIA_API_KEY");
```

## Uso

Após iniciar o programa, digite suas mensagens após o prompt `Você:`. Os comandos disponíveis são:

| Comando | Descrição                          |
|---------|------------------------------------|
| `!sair` | Encerra a sessão.                  |

Mensagens vazias são ignoradas.

## Configurações do modelo

Os parâmetros da requisição estão definidos em `Principal.java`:

| Parâmetro     | Valor                       |
|---------------|-----------------------------|
| Modelo        | `z-ai/glm-5.3-flash`        |
| Temperatura   | `0.5`                       |
| `top_p`       | `1`                         |
| `max_tokens`  | `1024`                      |
| Streaming     | Desativado                  |

## Estrutura do projeto

```
jarvis/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── io/
                └── github/
                    └── wndelix/
                        └── Principal.java
```

## Tecnologias utilizadas

- Java 17
- Maven
- Gson 2.10.1
- Maven Shade Plugin 3.5.3 (geração do JAR executável)
- API de chat completions da NVIDIA

## Limitações conhecidas

- O histórico da conversa cresce a cada mensagem e não é truncado, o que pode exceder o limite de contexto do modelo em sessões longas.
- A chave de API está no código-fonte, o que não é recomendado para uso em produção.
- Não há persistência do histórico entre execuções.

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

## Autor

Desenvolvido por [wndelix](https://github.com/wndelix).
