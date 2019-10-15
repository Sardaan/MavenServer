package laba.mail;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender {

    static String host = "smtp.gmail.com";
    static String myMail = "laba.sard@gmail.com"; // логин
    static String pwd = "laba7laba"; // пароль

    static MimeMessage msg;
    static Session mailSession;


    public static void sendPassword(String password, String userMail) {
        try {
            String to = userMail;
            String from = myMail;

            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "25");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(myMail, pwd);
                            }
                    });

            msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));

            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject("Password");
            msg.setText("Your password is: "+password);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host, myMail, pwd);
            Transport.send(msg);
            transport.close();

        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Couldn't connect to the mail");
        }
    }
}