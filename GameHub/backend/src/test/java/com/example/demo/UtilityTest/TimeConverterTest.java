package com.example.demo.UtilityTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import com.utility.TimeConverter;

public class TimeConverterTest {

    @Test
    public void testConvertToReadableFormat() {
        // Use a known ISO 8601 string
        String isoTime = "2021-01-01T00:00:00Z";
        String readable = TimeConverter.convertToReadableFormat(isoTime);
        assertNotNull(readable, "Readable time should not be null");
        
        // Define the formatter as in TimeConverter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parsedDateTime = LocalDateTime.parse(readable, formatter);
        
        // Convert the ISO time to local date time using the system's default zone
        LocalDateTime expectedDateTime = Instant.parse(isoTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
        
        assertEquals(expectedDateTime, parsedDateTime, "Converted time should match the expected local date time");
    }
}
