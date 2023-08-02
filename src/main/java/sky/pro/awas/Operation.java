package sky.pro.awas;

public enum Operation {

    MORE_THAN("больше чем"),
    LESS_THAN("меньше чем"),
    EQUAL("равное");

    private final String description;

    Operation(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
