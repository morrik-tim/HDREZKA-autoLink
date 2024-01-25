import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.mail.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailParser {

    public static void openLatestUrl() {
        try {
            File[] jsonFiles = new File(".").listFiles((dir, name) -> name.endsWith(".json"));

            if (jsonFiles != null && jsonFiles.length > 0) {
                Arrays.sort(jsonFiles, Comparator.comparingLong(File::lastModified));
                File latestJsonFile = jsonFiles[jsonFiles.length - 1];

                ObjectMapper objectMapper = new ObjectMapper();
                List<UrlData> urlList = objectMapper.readValue(latestJsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, UrlData.class));

                if (!urlList.isEmpty()) {
                    UrlData latestUrlData = urlList.get(urlList.size() - 1);

                    System.out.println("Открываем ссылку из последнего JSON файла: " + latestUrlData.getUrl());
                    OpenBrowser.openBrowser(latestUrlData.getUrl());
                } else {
                    System.out.println("JSON файл с сохраненными ссылками пуст.");
                }
            } else {
                System.out.println("Нет JSON файлов с сохраненными ссылками.");
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void emailParser() {
        // Ваши данные для получения почты
        String host = "imap.mail.ru"; // Укажите адрес вашего IMAP-сервера
        String email = EmailSender.MY_EMAIL; // адрес получателя
        String password = EmailSender.MY_PASSWORD; // Пароль от вашей почты
        String mirrorEmail = EmailSender.MIRROR_EMAIL; // Адрес получателя

        List<String> urls = new ArrayList<>();

        Properties properties = new Properties();
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        try {
            // Получение писем
            try (Store store = Session.getInstance(properties).getStore("imaps")) {
                store.connect(host, email, password);

                try (Folder inbox = store.getFolder("INBOX")) {
                    inbox.open(Folder.READ_ONLY);

                    // Отбор писем от определенного отправителя
                    Message[] messages = inbox.getMessages();
                    for (Message message : messages) {
                        if (message.getFrom()[0].toString().contains(mirrorEmail)) {
                            // Парсинг текста письма
                            String content = getTextFromMessage(message);
                            if (content == null) {
                                return;
                            }

                            // Извлечение ссылки с использованием регулярного выражения
                            String url = "https://" + extractUrl(content);
                            System.out.println("Извлеченная ссылка: " + url);

                            // Сохранение ссылки в JSON файл с текущей датой
                            urls.add(url);
                            saveUrlToJson(url);

                            // Открытие ссылки в браузере по умолчанию
                            System.out.println("Открываем сайт!");
                            openLatestUrl();
                            break;
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getTextFromMessage(Message message) throws Exception {
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

    private String extractUrl(String text) {
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

    private void saveUrlToJson(String url) throws IOException {
        // Путь к файлу с постоянным именем
        String fileName = "./список_ссылок.json";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Создаем объект списка ссылок
        List<UrlData> urlList;

        // Пытаемся прочитать существующий файл, если он существует
        try {
            urlList = objectMapper.readValue(new File(fileName), objectMapper.getTypeFactory().constructCollectionType(List.class, UrlData.class));
        } catch (IOException e) {
            // Если файл не существует или не удается прочитать, создаем новый список
            urlList = new ArrayList<>();
        }

        // Добавляем новую ссылку в список
        String currentDate = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
        urlList.add(new UrlData(url, currentDate));

        // Записываем обновленный список обратно в файл
        objectMapper.writeValue(new File(fileName), urlList);

        System.out.println("Ссылка добавлена в файл: " + fileName);
    }
}