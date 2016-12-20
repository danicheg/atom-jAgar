package zagar.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class Reporter {

    private Reporter() {
        throw new IllegalAccessError(getClass() + " - utility class");
    }

    public static void reportFail(@NotNull String title, @NotNull String message) {
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void reportWarn(@NotNull String title, @NotNull String message) {
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.WARNING_MESSAGE);
    }

}
