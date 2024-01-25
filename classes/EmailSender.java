import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    protected static final String MIRROR_EMAIL = "mirror@hdrezka.org";
    protected static final String MY_EMAIL = "sender4mail@mail.ru";
    protected static final String MY_PASSWORD = "NzJH8kjeUiJ2KVUcF6Fy";

    public void sendEmail() {
        // Ваши данные для отправки почты
        String fromEmail = MY_EMAIL;
        String[] toEmails = {MY_EMAIL, MIRROR_EMAIL};

        // Установка свойств
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.mail.ru"); // Укажите свой SMTP-сервер
        properties.put("mail.smtp.port", "465"); // Укажите порт SMTP-сервера
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");

        // Аутентификация пользователя
        System.out.println("Аутентификация пользователя...");

        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, MY_PASSWORD);
                }
            });

            // Создание объекта MimeMessage
            Message message = new MimeMessage(session);

            // Установка параметров письма
            message.setFrom(new InternetAddress(fromEmail));
            InternetAddress[] recipientAddresses = new InternetAddress[toEmails.length];

            for (int i = 0; i < toEmails.length; i++) {
                recipientAddresses[i] = new InternetAddress(toEmails[i]);
            }

            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            message.setSubject(generateRandomSubject()); // Генерация случайной темы
            message.setText("");

            try {
                System.out.println("Отправка письма...");
                // Отправка письма
                Transport.send(message);
                System.out.println("Письмо успешно отправлено!");
            } finally {
                // Закрытие ресурса (Message)
                if (message != null && message.getFolder() != null) {
                    try {
                        message.setFlag(Flags.Flag.DELETED, true); // Установите флаг DELETED, если это необходимо
                        message.getFolder().close(false);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateRandomSubject() {
        // Генерация случайной темы
        String[] subjects = {"Important Information", "Hello", "Meeting Agenda", "Update", "Urgent Request"};
        int randomIndex = (int) (Math.random() * subjects.length);
        return subjects[randomIndex];
    }
}