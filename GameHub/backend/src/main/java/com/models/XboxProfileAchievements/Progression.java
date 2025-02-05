package com.models.XboxProfileAchievements;

import com.utility.TimeConverter;
public class Progression {
    private String timeUnlocked;
    // use utility class to convert string to date
    public String getTimeUnlocked() {
        return TimeConverter.convertToReadableFormat(timeUnlocked);
    }

    public void setTimeUnlocked(String timeUnlocked) {
        this.timeUnlocked = timeUnlocked;
    }
}
