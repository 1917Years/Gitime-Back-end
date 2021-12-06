package capstone.gitime.socket.chat.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Message {
    private String userName;
    private String content;
    private Date date;

    public Message(String userName, String content, Date date) {
        this.userName = userName;
        this.content = content;
        this.date = date;
    }
}

