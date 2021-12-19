package capstone.gitime.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendTeamInvitation(String email, String teamName, String acceptCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[GITIME] " + teamName + " Team에 초대합니다.");
        message.setText("인증 코드 : " + acceptCode);
        javaMailSender.send(message);
    }
}
