package com.ja.code.ws;

import com.ja.code.jdi.DebugState;
import com.ja.code.jdi.JDIDebugSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jspecify.annotations.NonNull;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class DebugSocketController extends TextWebSocketHandler {

    private final JDIDebugSession debug = new JDIDebugSession();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(
            @NonNull WebSocketSession session,
            @NonNull TextMessage message
    ) throws Exception {

        String cmd = message.getPayload();

        System.out.println("🔌 WS CMD: " + cmd);

        DebugState state = switch (cmd) {

            case "START" -> {
                debug.start("Main", ".");
                yield debug.step();
            }

            case "STEP" -> debug.step();

            case "CONTINUE" -> {
                debug.resume();
                yield null;
            }

            default -> {
                System.out.println("⚠ Unknown command: " + cmd);
                yield null;
            }
        };

        if (state != null) {
            session.sendMessage(
                    new TextMessage(mapper.writeValueAsString(state))
            );
        }
    }
}
