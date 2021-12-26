package com.example.websockets;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.ByteString;

public class WebSocketTool {
    Request request;
    OkHttpClient client;
    okhttp3.WebSocket webSocket;
    WebSocketListener webSocketListener;
    public WebSocketTool(String url) {
        request = new Request.Builder()
            .url(url)
            .build();
        client = new OkHttpClient().newBuilder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build();
    }

    void setWebSocketListener(WebSocketListener listener) {
        webSocketListener = listener;
    }

    void run(){
        webSocket = client.newWebSocket(request, webSocketListener);
    }

    void send(String str) {
        webSocket.send(str);
    }

    void release() {
        webSocket.cancel();
//        client.dispatcher().executorService().shutdown();
    }
}
