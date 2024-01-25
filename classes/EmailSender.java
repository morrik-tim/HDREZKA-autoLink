import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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


        try {
            // Аутентификация пользователя
            System.out.println("Аутентификация пользователя...");
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, MY_PASSWORD);
                }
            });

            if (openUrlFromJson()) {
                // Если файл существует, открываем ссылку из файла
                System.out.println("Проверка файла");
                openUrlFromJson();
                System.exit(0);
            } else {
                System.out.println("Нет JSON файла с сохраненными ссылками.");
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
                    //Transport.send(message);
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

    protected static boolean openUrlFromJson() {
        try {
            String expectedFileName = "./список_ссылок.json";

            File jsonFile = new File(expectedFileName);

            if (jsonFile.exists()) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<UrlData> urlList = objectMapper.readValue(jsonFile, new TypeReference<>() {
                });

                if (!urlList.isEmpty()) {
                    // Берем последнюю ссылку
                    UrlData latestUrlData = urlList.get(urlList.size() - 1);
                    // Проверяем, что дата последней ссылки соответствует текущей дате
                    String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
                    if (latestUrlData.getDate().equals(currentDate)) {
                        System.out.println("Открываем ссылку из JSON файла: " + latestUrlData.getUrl());
                        OpenBrowser.openBrowser(latestUrlData.getUrl());
                        return true;
                    } else {
                        System.out.println("Последняя ссылка не соответствует текущей дате.");
                        return false;
                    }
                } else {
                    System.out.println("JSON файл пуст.");
                    return false;
                }
            } else {
                System.out.println("Нет JSON файла с текущей датой.");
                return false;
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Ошибка при чтении JSON файла.");
            e.printStackTrace();
        }
        return false;
    }
}