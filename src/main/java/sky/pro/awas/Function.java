package sky.pro.awas;

public enum Function {

    MORE_THAN("более чем"),
    LESS_THAN("менее чем"),
    EQUAL("равный");

    private final String description;

    Function(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
