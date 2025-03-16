package resources;

public record PlayerTypes(String type1, String type2) {
    public boolean validType(String type) {
        return (type1.equals(type) || type2.equals(type));
    }

    public String opposite(String type) {
        if (type.equals(type1))
            return type2;
        else if (type.equals(type2))
            return type1;
        else
            throw new RuntimeException("invalid player type");
    }

    public static final PlayerTypes TIC_TAC_TOE = new PlayerTypes("X", "O");
    public static final PlayerTypes CONNECT_FOUR = new PlayerTypes("R", "B");
    public static final PlayerTypes CHECKERS = new PlayerTypes("R", "B");
}
