package capstone.gitime.chat_websocket.chat.repository;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class ChatRoomRepository {
    private static Map<String, ChattingRoom> chatRoomMap;

    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
    }

    public List<ChattingRoom> findAllRoom(){
        List chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChattingRoom findRoomById(String id){
        return chatRoomMap.get(id);
    }

    public ChattingRoom createChatRoom(String name){
        ChattingRoom chatRoom = ChattingRoom.create(name);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;

    }

}
