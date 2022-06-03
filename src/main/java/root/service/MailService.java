package root.service;

import root.model.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailService {

    public void sendVerificationMail(User user) {
        log.info("### Verification code: " + user.getVerificationCode());
    }

    public void sendPasswordResetMail(String email, String password) {
        log.info("### New password: " + password);
    }
}
