package reactiongame;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import reactiongame.application.SessionResponse;
import reactiongame.application.SessionResultResponse;
import reactiongame.application.SessionStatusResponse;
import reactiongame.domain.ReactionHistory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class ConsoleClient implements AutoCloseable {

    private static final String HOST = "http://localhost:8080";
    private static final int SESSION_ID = 1;
    private static final String X_PLAYER_TOKEN = "";

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private final String host;

    private final int sessionId;

    private final String xPlayerToken;

    public ConsoleClient() {
        this(HOST, SESSION_ID, X_PLAYER_TOKEN);
    }

    public ConsoleClient(
            final String host,
            final int sessionId,
            final String xPlayerToken
    ) {
        this.host = host;
        this.sessionId = sessionId;
        this.xPlayerToken = xPlayerToken;
    }

    public static void main(final String... args) {
        final var host = args.length > 0 ? args[0] : HOST;
        final var sessionId = args.length > 1 ? Integer.parseInt(args[1]) : SESSION_ID;
        final var xPlayerToken = args.length > 2 ? args[2] : X_PLAYER_TOKEN;

        try (
                final var scanner = new Scanner(System.in);
                final var client = new ConsoleClient(host, sessionId, xPlayerToken)
        ) {
            run(scanner, client);
        }
    }

    private static void run(
            final Scanner scanner,
            final ConsoleClient client
    ) {
        final var session = client.findSession();
        System.out.println("해당 게임은 " + session.startDate() + "부터 " + session.endDate() + "까지 진행됩니다.");

        while (true) {
            System.out.println("""
                    1. React
                    2. List reactions
                    3. My reactions
                    4. My status
                    5. Result
                    6. Exit
                    """);

            try {
                final var command = scanner.nextLine();
                switch (command) {
                    case "1" -> System.out.println(client.react());
                    case "2" -> System.out.println(client.listReactions());
                    case "3" -> System.out.println(client.myReactions());
                    case "4" -> System.out.println(client.myStatus());
                    case "5" -> System.out.println(client.result());
                    case "6" -> {
                        System.out.println("Exit client");
                        return;
                    }
                    default -> System.out.println("Unknown command: " + command);
                }
            } catch (final Exception e) {
                System.out.println("Failed to execute command: " + e);
            }
        }
    }

    public ReactionHistory react() {
        final var response = request("/reactions", "POST");
        if (response.statusCode() == HttpStatus.OK.value()) {
            return toEntity(response.body(), ReactionHistory.class);
        }
        throw new RuntimeException("Failed to react: " + response.body());
    }


    public List<ReactionHistory> listReactions() {
        final var response = request("/reactions", "GET");
        if (response.statusCode() == HttpStatus.OK.value()) {
            return toEntities(response.body());
        }
        throw new RuntimeException("Failed to list reactions: " + response.body());
    }

    public List<ReactionHistory> myReactions() {
        final var response = request("/reactions/mine", "GET");
        if (response.statusCode() == HttpStatus.OK.value()) {
            return toEntities(response.body());
        }
        throw new RuntimeException("Failed to list my reactions: " + response.body());
    }

    public SessionStatusResponse myStatus() {
        final var response = request("/my-status", "GET");
        if (response.statusCode() == HttpStatus.OK.value()) {
            return toEntity(response.body(), SessionStatusResponse.class);
        }
        throw new RuntimeException("Failed to get my status: " + response.body());
    }

    public SessionResponse findSession() {
        final var response = request("", "GET");
        if (response.statusCode() == HttpStatus.OK.value()) {
            return toEntity(response.body(), SessionResponse.class);
        }
        throw new RuntimeException("Failed to get session: " + response.body());
    }

    public SessionResultResponse result() {
        final var response = request("/result", "GET");
        if (response.statusCode() == HttpStatus.OK.value()) {
            return toEntity(response.body(), SessionResultResponse.class);
        }
        throw new RuntimeException("Failed to get result: " + response.body());
    }

    private HttpResponse<String> request(
            final String path,
            final String method
    ) {
        final var request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .header("X-Player-Token", xPlayerToken)
                .uri(URI.create(host + "/sessions/" + sessionId + path))
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (final IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T toEntity(
            final String response,
            final Class<T> clazz
    ) {
        try {
            return objectMapper.readValue(response, clazz);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> toEntities(final String response) {
        try {
            return objectMapper.readValue(response, new TypeReference<>() {
            });
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (final Exception e) {
            System.out.println("Failed to close HttpClient" + e);
        }
    }
}
