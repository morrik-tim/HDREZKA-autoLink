import javax.mail.*;
import java.util.Properties;

public class EmailChecker {

    private static final String HOST = "imap.mail.ru"; // Укажите адрес вашего IMAP-сервера
    private static final String EMAIL = EmailSender.MY_EMAIL; // Ваш адрес электронной почты
    private static final String PASSWORD = EmailSender.MY_PASSWORD; // Пароль от вашей почты
    protected static final String TARGET_SENDER = "mirror@hdrezka.org"; // Целевой отправитель

    public static boolean emailChecker() {
        Properties properties = new Properties();
        properties.put("mail.imap.host", HOST);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getInstance(properties);
            Store store = session.getStore("imaps");
            store.connect(HOST, EMAIL, PASSWORD);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            while (true) {
                Message[] messages = inbox.getMessages();

                for (Message message : messages) {
                    if (message.getFrom()[0].toString().contains(TARGET_SENDER)) {
                        // Если получено письмо от целевого отправителя, прекращаем проверку
                        System.out.println("Получено письмо от " + TARGET_SENDER + ". Прекращаем проверку.");
                        inbox.close(false);
                        store.close();
                        return true;
                    }
                }
                //Thread.sleep(2000); // Подождать 2 секунды перед следующей проверкой
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}