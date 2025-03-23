package resources;

public record Players(String player1, String player2) {
    public boolean validType(String type) {
        return (player1.equals(type) || player2.equals(type));
    }

    public String opposite(String player) {
        if (player.equals(player1))
            return player2;
        else if (player.equals(player2))
            return player1;
        else
            throw new RuntimeException("invalid player type");
    }

    public static final Players TIC_TAC_TOE = new Players("X", "O");
    public static final Players CONNECT_FOUR = new Players("R", "B");
    public static final Players CHECKERS = new Players("B", "R");
}
