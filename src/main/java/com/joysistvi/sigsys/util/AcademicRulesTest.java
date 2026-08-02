package com.joysistvi.sigsys.util;

public final class AcademicRulesTest {
    private AcademicRulesTest() { }

    public static void main(String[] args) {
        require(ScheduleRules.daysOverlap("MWF", "WF"), "shared class days should conflict");
        require(!ScheduleRules.daysOverlap("MWF", "TTHS"), "different class days should not conflict");
        require(ScheduleRules.timesOverlap("0800H-1000H", "0930H-1100H"), "overlapping times should conflict");
        require(!ScheduleRules.timesOverlap("0800H-1000H", "1000H-1200H"), "adjacent times should not conflict");
        System.out.println("AcademicRulesTest passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
