package githubapi.com.example.githubapi.enums;

public enum QueryParam {
    PAGE("page"),
    PER_PAGE("per_page");

    private final String value;

    QueryParam(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
