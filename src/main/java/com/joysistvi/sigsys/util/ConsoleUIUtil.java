package com.joysistvi.sigsys.util;

public final class ConsoleUIUtil {
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";

    public static final String WELCOME_BANNER = """
            / ___| | |_  _   _   __| |  ___  _ __  | |_                                    \s
            \\___ \\ | __|| | | | / _` | / _ \\| '_ \\ | __|                                   \s
             ___) || |_ | |_| || (_| ||  __/| | | || |_                                    \s
            |____/  \\__| \\__,_| \\__,_| \\___||_| |_| \\__|     _    _                  ___   \s
            |_ _| _ __   / _|  ___   _ __  _ __ ___    __ _ | |_ (_)  ___   _ __    ( _ )  \s
             | | | '_ \\ | |_  / _ \\ | '__|| '_ ` _ \\  / _` || __|| | / _ \\ | '_ \\   / _ \\/\\\s
             | | | | | ||  _|| (_) || |   | | | | | || (_| || |_ | || (_) || | | | | (_>  <\s
            |___||_| |_||_|   \\___/ |_|_  |_| |_| |_| \\__,_| \\__||_| \\___/ |_| |_|  \\___/\\/\s
             / ___| _ __  __ _   __| |(_) _ __    __ _                                     \s
            | |  _ | '__|/ _` | / _` || || '_ \\  / _` |                                    \s
            | |_| || |  | (_| || (_| || || | | || (_| |                                    \s
             \\____||_|   \\__,_| \\__,_||_||_| |_| \\__, |                                    \s
            / ___|  _   _  ___ | |_  ___  _ __ __|___/                                     \s
            \\___ \\ | | | |/ __|| __|/ _ \\| '_ ` _ \\                                        \s
             ___) || |_| |\\__ \\| |_|  __/| | | | | |                                       \s
            |____/  \\__, ||___/ \\__|\\___||_| |_| |_|                                       \s
                    |___/                                                                  \s
            """;

    private static final int WIDTH = 90;

    private ConsoleUIUtil() {
    }

    public static void printBigTitle(String title) {
        System.out.println(CYAN + repeat("=", WIDTH) + RESET);
        System.out.println(CYAN + BOLD + center(title, WIDTH) + RESET);

    }

    public static void printBannerTitle() {
        System.out.println(CYAN + repeat("=", WIDTH) + RESET);

        int artWidth = WIDTH;

        int padding = Math.max(0, (100 - artWidth) / 2);
        String leftMargin = repeat(" ", padding);

        WELCOME_BANNER.lines().forEach(line -> {
            System.out.println(GREEN + BOLD + leftMargin + line.stripTrailing() + RESET);
        });

        System.out.println(CYAN + repeat("=", WIDTH) + RESET);
    }

    public static void printBoxedSectionHeader(String title) {
        String border = "+" + repeat("-", WIDTH - 2) + "+";
        String content = "|"
                + center(" " + title.trim().toUpperCase() + " ", WIDTH - 2)
                + "|";
        System.out.println(CYAN + BOLD + border + RESET);
        System.out.println(CYAN + BOLD + content + RESET);
        System.out.println(CYAN + BOLD + border + RESET);
    }

    public static void printCenteredMenuOption(int option, String label) {
        String color = option <= 0 ? YELLOW : GREEN;
        String text = "[" + option + "] " + label.trim();

        int menuBlockWidth = 20;

        int leftPadding = Math.max(0, (WIDTH - menuBlockWidth) / 2);
        String leftMargin = repeat(" ", leftPadding);

        System.out.println(color + leftMargin + text + RESET);
    }

    public static void printDivider(String character) {
        System.out.println(CYAN + repeat(character, WIDTH) + RESET);
    }

    public static void printError(String message) {
        System.out.println(RED + BOLD + message + RESET);
    }

    private static String center(String value, int width) {
        String text = value;
        if (text.length() >= width) {
            return text;
        }
        int padding = width - text.length();
        int left = padding / 2;
        int right = padding - left;
        return repeat(" ", left) + text + repeat(" ", right);
    }

    private static String repeat(String character, int count) {
        return character.repeat(Math.max(0, count));
    }

}
