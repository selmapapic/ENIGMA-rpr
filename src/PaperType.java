public enum PaperType {
    BACHELORS_THESIS("Bachelor's Thesis"), DOCTORATE("Doctorate"), MASTERS_THESIS("Master's Thesis"), SCIENTIFIC_ARTICLE("Scientific Article"), SEMINARY_PAPER("Seminary Paper"), OTHER("Other");
    private final String name;

    PaperType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
