package com.joysistvi.sigsys.util;

import java.util.Scanner;

public class InputCheckerUtil {

    public static int readIntChoice(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    public static boolean isValueProvided(String value){
        return value != null && !value.trim().isEmpty();
    }
}
