package chess.enums;

public enum ColorEnum {

    YELLOW(1, "Yellow"),
    BLUE(2, "Blue");

    private final Integer id;

    private final String desc;

    ColorEnum(Integer id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
