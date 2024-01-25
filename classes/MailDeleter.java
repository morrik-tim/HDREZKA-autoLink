import javax.mail.*;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.Properties;

public class MailDeleter {
    public void mailDeleter() {
        String host = "imap.mail.ru";
        String username = EmailSender.MY_EMAIL;
        String password = EmailSender.MY_PASSWORD;

        Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(properties);

            try (Store store = session.getStore("imap")) {
                store.connect(username, password);

                try (Folder inbox = store.getFolder("INBOX")) {
                    inbox.open(Folder.READ_WRITE);

                    Message[] messages = inbox.getMessages();
                    for (Message message : messages) {
                        // Display email details
                        System.out.println("Subject: " + message.getSubject());
                        System.out.println("From: " + message.getFrom()[0]);
                        System.out.println("Sent Date: " + message.getSentDate());
                        System.out.println("Text: " + message.getContent().toString());
                        System.out.println("---------------------------------------------");

                        // Delete the message
                        if (isUsernameSender(message, username)) {
                            System.out.println("Письмо не удалено");
                        } else {
                            // Удаление сообщения для других отправителей
                            message.setFlag(Flags.Flag.DELETED, true);
                            System.out.println("Письмо удалено.");
                        }
                        System.out.println("Deleted");
                    }

                    // Expunge and close the folder
                    inbox.expunge();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private boolean isUsernameSender(Message message, String targetUsername) throws MessagingException {
        String[] fromAddresses = InternetAddress.toString(message.getFrom()).split(",");
        for (String fromAddress : fromAddresses) {
            if (fromAddress.trim().equals(targetUsername)) {
                return true; // Найден нужный отправитель
            }
        }
        return false; // Не найден нужный отправитель
    }
}
