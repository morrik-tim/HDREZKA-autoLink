/*


import java.util.Timer;
import java.util.TimerTask;

public class EmailAutomation {
    public static EmailSender emailSender = new EmailSender();
    public static EmailParser emailParser = new EmailParser();
    public static MailDeleter mailDeleter = new MailDeleter();
    public static String mirrorEmail = "mirror@hdrezka.org";
    public static String myEmail = "sender4mail@mail.ru";
    public static String myPassword = "NzJH8kjeUiJ2KVUcF6Fy";

    public static void main(String[] args) {
        // Запуск первого кода (EmailSender)
        System.out.println("движ миж");
        runEmailSender();

        // Установка таймера для ожидания 3 минут (180000 миллисекунд)
        Timer timer = new Timer();


        System.out.println("Ждёмс таймера");

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runEmailParser();// Запуск второго кода (EmailParser)
                timer.cancel(); // Остановка таймера после выполнения второго кода
            }
        }, 120000, 1000); // 0 - зажержка перед первым запуском, 1000 - период в миллисекундах
        consoleCount();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runMailDeleter();// Запуск третьего кода (MailDeleter)
                timer.cancel(); // Остановка таймера после выполнения третьего кода
            }
        }, 60000, 1000); // 0 - зажержка перед первым запуском, 1000 - период в миллисекундах
        consoleCount();
    }

    // Запуск отправки письма
    private static void runEmailSender() {
        emailSender.sendEmail();
    }

    // Запуск парсера
    private static void runEmailParser() {
        emailParser.emailParser();
    }

    // Запуск удаления письма
    private static void runMailDeleter() {
        mailDeleter.mailDeleter();
    }

    private static void consoleCount() {
        int countdown = 120; // Время в секундах
        while (countdown > 0) {
            System.out.println("Осталось времени: " + countdown + " сек.");
            countdown--;
            try {
                Thread.sleep(1000); // задержка на 1 миллисекунду
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}


*/
