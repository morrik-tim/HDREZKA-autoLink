public class UrlData {
    private String url;
    private String date;

    // Пустой конструктор для Jackson
    public UrlData() {
    }

    public UrlData(String url, String date) {
        this.url = url;
        this.date = date;
    }

    // Геттеры и сеттеры
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
