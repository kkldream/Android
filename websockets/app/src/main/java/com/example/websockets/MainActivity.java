package com.example.websockets;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    EditText edWebSocket;
    TextView tvRes;
    Button btWebSocket;
    WebSocketTool webSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edWebSocket = findViewById(R.id.editTextTextPersonName);
        tvRes = findViewById(R.id.textView);
        btWebSocket = findViewById(R.id.button);
        webSocket = new WebSocketTool("ws://192.168.1.10:9999");
        webSocket.setWebSocketListener(webSocketListener);
        webSocket.run();
        btWebSocket.setOnClickListener(view -> {
            webSocket.send(edWebSocket.getText().toString());
        });
    }

    WebSocketListener webSocketListener = new WebSocketListener() {
        /**回傳WebSocket已關閉時做的事情*/
        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        /**回傳WebSocket關閉時所做的事情*/
        @Override
        public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
        }

        /**回傳WebSocket連線失敗時的回傳*/
        @Override
        public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
            tvRes.setText("WebSocket回傳(錯誤)：\n" + response + "\n" + t);
        }

        /**回傳WebSocket取得到的String回傳*/
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            tvRes.setText("WebSocket回傳：\n" + text);
        }

        /**回傳WebSocket取得到的ByteArray回傳*/
        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        /**回傳WebSocket開始時所需做的動作*/
        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            webSocket.send(edWebSocket.getText().toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocket.release();
    }
}