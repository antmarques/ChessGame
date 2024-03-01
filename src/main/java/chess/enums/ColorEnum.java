package chess.enums;

public enum ColorEnum {

    BLACK (1, "Black"),
    WHITHE(2, "White");

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
