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
        String toEmail = MIRROR_EMAIL;
        String fromEmail = MY_EMAIL;
        String password = MY_PASSWORD ; // Пароль от вашей почты

        // Установка свойств
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.mail.ru"); // Укажите свой SMTP-сервер
        properties.put("mail.smtp.port", "465"); // Укажите порт SMTP-сервера
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");

        // Аутентификация пользователя
        System.out.println("Аутентификация пользователя...");
        Session session = Session.getInstance(properties, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
           // Создание объекта MimeMessage
            Message message = new MimeMessage(session);

            // Установка параметров письма
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(generateRandomSubject()); // Генерация случайной темы
            message.setText(""); // Пустой текст*/

            System.out.println("Отправка письма...");
            // Отправка письма
            Transport.send(message);

            System.out.println("Письмо успешно отправлено!");

        } catch (MessagingException e) {
            System.out.println("Произошла ошибка при отправке письма.");
            // TODO: добавьте более ростокую логику для обработки ошибок
            e.printStackTrace();
        }
    }

    private static String generateRandomSubject() {
        // Генерация случайной темы
        String[] subjects = {"Important Information", "Hello", "Meeting Agenda", "Update", "Urgent Request"};
        int randomIndex = (int) (Math.random() * subjects.length);
        return subjects[randomIndex];
    }
}