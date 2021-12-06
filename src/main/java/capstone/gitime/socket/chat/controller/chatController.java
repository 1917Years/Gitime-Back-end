package capstone.gitime.socket.chat.controller;


import capstone.gitime.socket.chat.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class chatController {

    @MessageMapping("/api/v1/socket/chat")
    @SendTo("/topic/roomId")
    public Message broadCast(Message message){
        log.info(message.getUserName());
        return message;
    }

}
