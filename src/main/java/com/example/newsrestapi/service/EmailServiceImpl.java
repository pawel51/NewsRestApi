package com.example.newsrestapi.service;

import com.example.newsrestapi.model.AppUser;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService{
    private Properties properties;
    private Session session;
    private String senderEmail = "yourfavouritecinema@gmail.com";
    private String client_id = "651644141471-05i98bt98akf4cv29k159omlvmumic63.apps.googleusercontent.com";
    private String client_secret_key = "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";
    private String refresh_token = "1//04yeLbmOj4w5rCgYIARAAGAQSNwF-L9IrChWKDYC9PZHXboQnkX8YB7potwgR4Prred7WBfghs7DWv9JjDgdGmGxBcOPgsibKSUw";
    private String token = "";
    private String tokenUrl = "https://oauth2.googleapis.com/token";
    public EmailServiceImpl()
    {
        properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth.mechanisms", "XOAUTH2");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.starttls.required", "true");
        properties.put("mail.smtp.auth.login.disable", "true");
        properties.put("mail.smtp.auth.plain.disable", "true");
        properties.put("mail.debug", "true");
    }
    @Override
    public void sendEmailAboutAnnouncementPublication(AppUser applicationUser) {

        String subject = "Your Announcement has been published!";
        String content = "Congratulations! Your announcement has been published. We wish you luck!\n\nSincerely,\nANONcement Team";

        sendEmail(applicationUser, subject, content);
    }

    @Override
    public void sendEmailAboutAnnouncementEnd(AppUser applicationUser) {

        String subject = "Your Announcement has ended!";
        String content = "Congratulations! Your announcement has ended. We wish you luck!\n\nSincerely,\nANONcement Team";

        sendEmail(applicationUser, subject, content);
    }

    private void sendEmail(AppUser applicationUser, String subject, String content)
    {
        try{

            OAuthClient client = new OAuthClient(new URLConnectionClient());

            OAuthClientRequest request = OAuthClientRequest.tokenLocation(tokenUrl)
                    .setGrantType(GrantType.REFRESH_TOKEN)
                    .setClientId(client_id)
                    .setClientSecret(client_secret_key)
                    .setRefreshToken(refresh_token)
                    .buildBodyMessage();

            OAuthJSONAccessTokenResponse jsonResponse = client.accessToken(request, OAuth.HttpMethod.POST, OAuthJSONAccessTokenResponse.class);
            token = jsonResponse.getAccessToken();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        session = Session.getInstance(properties);
        session.setDebug(true);
        try{
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(applicationUser.getEmail()));

            message.setSubject(subject);
            message.setText(content);

            //Transport t = new SMTPSSLTransport(session, new URLName("SMTP", "smtp.gmail.com", 465, null, applicationUser.getEmail(), token));
            //t.sendMessage(message, new Address[]{ new InternetAddress(applicationUser.getEmail()) });
            Transport.send(message, senderEmail, token);
        } catch (MessagingException me)
        {
            me.printStackTrace();
        }
    }
}
