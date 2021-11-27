package capstone.gitime.chat_websocket.chat.handler;

import capstone.gitime.chat_websocket.chat.repository.ChatMessage;
import capstone.gitime.chat_websocket.chat.repository.ChatRoomRepository;
import capstone.gitime.chat_websocket.chat.repository.ChattingRoom;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ChatRoomRepository chatRoomRepository;

    private final ObjectMapper objectMapper;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("메세지 전송 = " + session + ":" + message.getPayload());
        String msg = message.getPayload(); // JSON 형태
        log.info("msg : " + msg);
        ChatMessage chatMessage = objectMapper.readValue(msg,ChatMessage.class); // JSON 형태를 ChatMessage.class 형태로
        ChattingRoom chatRoom = chatRoomRepository.findRoomById(chatMessage.getChatRoomId());
        chatRoom.handleMessage(session,chatMessage,objectMapper);
    }
}
