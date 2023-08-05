package sky.pro.awas;

public enum Function {

    MORE_THAN("moreThan"),
    LESS_THAN("lessThan"),
    EQUAL("equal");

    private final String description;

    Function(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
