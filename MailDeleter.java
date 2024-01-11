import javax.mail.*;
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
            Store store = session.getStore("imap");
            store.connect(username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE); // Открываем для чтения/записи

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Sent Date: " + message.getSentDate());
                System.out.println("Text: " + message.getContent().toString());
                System.out.println("---------------------------------------------");
                // Удаление сообщения
                message.setFlag(Flags.Flag.DELETED, true);
                System.out.println("Deleted");
            }
            inbox.expunge();
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
