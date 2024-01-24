public class RunClass {
    public static void main(String[] args) throws Exception {
        runEmailAutomation();
    }

    private static void runEmailAutomation() throws Exception {
        EmailAutomation automation = new EmailAutomation();
        automation.startAutomation();
    }
}
