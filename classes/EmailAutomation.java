import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EmailAutomation {
	private EmailChecker emailChecker;
	public void emailAutomation() throws Exception {
		System.out.println("Пошел движ миж");

		runEmailSender();
		runEmailParser();
		runMailDeleter();
	}

	protected void runEmailSender() throws Exception {
		EmailSender emailSender = new EmailSender();
		emailSender.sendEmail();
	}

	protected void runEmailParser() {
		int sec = 120;
		consoleCount(sec);
	}

	protected void runMailDeleter() {
		System.out.println("Распарсили, далее удаляем письма");
		MailDeleter mailDeleter = new MailDeleter();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
		int sec = 15;

		scheduler.scheduleAtFixedRate(mailDeleter::mailDeleter, sec, sec, TimeUnit.SECONDS);
		consoleCount(scheduler, sec);
		System.out.println("готово");
	}

	private void consoleCount(int seconds) {
		EmailParser emailParser = new EmailParser();
		int countdown = seconds; // Время в секундах
		while (countdown > 0) {
			System.out.println("Осталось времени: " + countdown + " сек.");
			countdown--;
			if (EmailChecker.emailChecker()) {
				System.out.println("Получено письмо от " + EmailChecker.TARGET_SENDER + " Прекращаем выполнение.");
				emailParser.emailParser();
				break;
			} else if (!EmailChecker.emailChecker()) {
				System.out.println("Не получено письмо от " + EmailChecker.TARGET_SENDER + " Продолжаем выполнение.");
                continue;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private void consoleCount(ScheduledExecutorService scheduler, int seconds) {
		int countdown = seconds; // Время в секундах
		while (countdown > 0) {
			System.out.println("Осталось времени: " + countdown + " сек.");
			countdown--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		scheduler.shutdown(); // Останавливаем планировщик
		try {
			scheduler.awaitTermination(60, TimeUnit.SECONDS); // Ждем завершения всех задач
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}