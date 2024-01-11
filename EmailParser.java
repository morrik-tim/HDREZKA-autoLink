import javax.mail.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailParser {

    public void emailParser() {
        // Ваши данные для получения почты
        String host = "imap.mail.ru"; // Укажите адрес вашего IMAP-сервера
        String email = EmailSender.MY_EMAIL; // ��дрес получателя
        String password = EmailSender.MY_PASSWORD; // Пароль от вашей почты
        String mirrorEmail = EmailSender.MIRROR_EMAIL; // Адрес получателя

        Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        Session session = Session.getInstance(properties);

        try {
            // Получение писем
            Store store = session.getStore("imaps");
            store.connect(host, email, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Отбор писем от определенного отправителя
            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                if (message.getFrom()[0].toString().contains(mirrorEmail)) {
                    // Парсинг текста письма
                    String content = getTextFromMessage(message);
                    if (content == null){return;}

                    // Извлечение ссылки с использованием регулярного выражения
                    String url = "http://" + extractUrl(content);
                    System.out.println("Извлеченная ссылка: " + url);

                    // Открытие ссылки в браузере по умолчанию
                    System.out.println("Открываем сайт!");
                    openBrowser(url);
                    break;

                }
            }

            inbox.close();
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTextFromMessage(Message message) throws Exception {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) message.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    return bodyPart.getContent().toString();
                }
            }
        }
        return "";
    }

    private static String extractUrl(String text) {
        // Регулярное выражение для извлечения ссылки из текста
        String regex = "[a-zA-Z0-9./?=&-_]+.org";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // Поиск первого вхождения ссылки
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    private static void openBrowser(String url) throws URISyntaxException, IOException
    {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            throw new UnsupportedOperationException("Desktop browsing is not supported on this platform.");
        }
    }
}

