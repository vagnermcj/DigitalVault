package session;

public class RuntimeSession {

    private static String adminSecretPhrase;

    public static void setAdminSecretPhrase(String phrase) {
        adminSecretPhrase = phrase;
    }

    public static String getAdminSecretPhrase() {
        return adminSecretPhrase;
    }

    public static void clear() {
        adminSecretPhrase = null;
    }
}