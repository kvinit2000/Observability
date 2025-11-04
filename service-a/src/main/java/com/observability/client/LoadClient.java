package com.observability.client;

import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.Random;
/*
mvn -q exec:java -Dexec.mainClass=com.observability.client.LoadClient
 */
public class LoadClient {
    static final String BASE = System.getenv().getOrDefault("BASE_URL", "http://localhost:8080");
    static final long HELLO_INTERVAL_MS = Long.parseLong(System.getenv().getOrDefault("HELLO_MS","1000"));
    static final long COMPUTE_INTERVAL_MS = Long.parseLong(System.getenv().getOrDefault("COMPUTE_MS","500"));
    static final String[] NAMES = {"Labib","Rishit","Niharika","World","Nebula"};
    static final Random R = new Random();

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2)).build();

        System.out.println("LoadClient → " + BASE + "  (Ctrl+C to stop)");

        long tHello = 0, tCompute = 0;
        while (true) {
            long now = System.currentTimeMillis();
            if (now - tHello >= HELLO_INTERVAL_MS) {
                String name = NAMES[R.nextInt(NAMES.length)];
                fire(client, "/api/hello?name=" + name);
                tHello = now;
            }
            if (now - tCompute >= COMPUTE_INTERVAL_MS) {
                int n = 1 + R.nextInt(30);
                fire(client, "/api/compute?n=" + n);
                tCompute = now;
            }

            Thread.sleep(50); // small loop sleep
        }
    }

    private static void fire(HttpClient client, String path) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE + path))
                    .timeout(Duration.ofSeconds(3))
                    .GET().build();

            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            System.out.println(path + " → " + res.statusCode() + " | " + res.body());
        } catch (Exception e) {
            System.out.println("ERR " + path + " → " + e.getMessage());
        }
    }



}

