package com.joysistvi.sigsys.util;

import com.joysistvi.sigsys.model.CourseSection;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ScheduleRules {
    private static final Pattern TIME_PATTERN = Pattern.compile("(?<!\\d)(\\d{1,4})(?::(\\d{2}))?");

    private ScheduleRules() { }

    public static boolean conflicts(CourseSection requested, List<CourseSection> enrolledSections) {
        for (CourseSection enrolled : enrolledSections) {
            if (enrolled.getPeriodId() == requested.getPeriodId()
                    && daysOverlap(enrolled.getScheduleDays(), requested.getScheduleDays())
                    && timesOverlap(enrolled.getScheduleTime(), requested.getScheduleTime())) return true;
        }
        return false;
    }

    public static boolean daysOverlap(String first, String second) {
        String firstDays = normalizeDays(first);
        String secondDays = normalizeDays(second);
        for (char day : firstDays.toCharArray()) if (secondDays.indexOf(day) >= 0) return true;
        return false;
    }

    public static boolean timesOverlap(String first, String second) {
        int[] firstRange = timeRange(first);
        int[] secondRange = timeRange(second);
        return firstRange != null && secondRange != null
                && firstRange[0] < secondRange[1] && secondRange[0] < firstRange[1];
    }

    private static String normalizeDays(String value) {
        return value == null ? "" : value.toUpperCase().replaceAll("[^A-Z]", "");
    }

    private static int[] timeRange(String value) {
        if (value == null) return null;
        Matcher matcher = TIME_PATTERN.matcher(value);
        List<Integer> values = new java.util.ArrayList<>();
        while (matcher.find()) {
            String hourText = matcher.group(1);
            int hour;
            int minute;
            if (matcher.group(2) != null) {
                hour = Integer.parseInt(hourText);
                minute = Integer.parseInt(matcher.group(2));
            } else if (hourText.length() > 2) {
                hour = Integer.parseInt(hourText.substring(0, hourText.length() - 2));
                minute = Integer.parseInt(hourText.substring(hourText.length() - 2));
            } else {
                hour = Integer.parseInt(hourText);
                minute = 0;
            }
            values.add(hour * 60 + minute);
        }
        if (values.isEmpty()) return null;
        int start = values.get(0);
        int end = values.size() > 1 ? values.get(1) : start + 1;
        return new int[]{start, end};
    }
}
