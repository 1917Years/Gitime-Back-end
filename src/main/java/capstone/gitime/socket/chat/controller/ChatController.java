package capstone.gitime.socket.chat.controller;

import capstone.gitime.socket.chat.repository.ChatRoomRepository;
import capstone.gitime.socket.chat.repository.ChattingRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/socket")
public class ChatController {

//    @GetMapping("/")
//    public String chat(){
//        return "chat";
//    }

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/chatrooms")
    public List<ChattingRoom> rooms(Model model){
        List<ChattingRoom> rooms = chatRoomRepository.findAllRoom();

        return rooms;
    }

    @GetMapping("/chatrooms/{id}")
    public ChattingRoom room(@PathVariable String id, Model model){
        ChattingRoom room = chatRoomRepository.findRoomById(id);
        return room;
    }
}