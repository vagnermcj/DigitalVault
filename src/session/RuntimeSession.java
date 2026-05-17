package session;

import database.entity.Usuario;

public class RuntimeSession {

    private static String adminSecretPhrase;

    private static Usuario currentUser;

    public static void setAdminSecretPhrase(String phrase) {
        adminSecretPhrase = phrase;
    }

    public static String getAdminSecretPhrase() {
        return adminSecretPhrase;
    }

    public static void setCurrentUser(Usuario usuario) {
        currentUser = usuario;
    }

    public static Usuario getCurrentUser() {
        return currentUser;
    }

    public static void clearUserSession() {
        currentUser = null;
    }

    public static void clearAll() {

        currentUser = null;

        adminSecretPhrase = null;
    }
}