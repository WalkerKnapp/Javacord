package org.javacord.core.util.rest;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.function.Consumer;

public class HttpClientWrapper {
    private HttpClient client;
    private HashMap<String, String> universalHeaders;
    private Consumer<String> logger;

    public HttpClientWrapper(HttpClient httpClient){
        this.client = httpClient;
        this.universalHeaders = new HashMap<>();
        this.logger = message -> { /* Only applicable when no logger is set */ };
    }

    public <T> HttpResponse<T> sendRequest(HttpRequest.Builder requestBuilder, HttpResponse.BodyHandler<T> responseHandler)
            throws IOException, InterruptedException {
        universalHeaders.forEach(requestBuilder::header);
        HttpRequest request = requestBuilder.build();
        logger.accept("--> " + request.method() + ' ' + request.uri().toString()
                + request.bodyPublisher().map(publisher -> " (" + publisher.contentLength() + "-byte body)")
                .orElse(" "));
        request.headers().map().forEach((name, values) -> logger.accept(name + ": " + String.join(",", values)));
        logger.accept("--> END");
        return client.send(request, responseHandler);
    }

    public void addUniversalHeader(String key, String value){
        universalHeaders.put(key, value);
    }

    public void setLogger(Consumer<String> logger){
        this.logger = logger;
    }
}
