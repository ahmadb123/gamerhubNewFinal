package com.example.demo.UtilityTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import com.utility.CurrentDate;

public class CurrentDateTest {

    @Test
    public void testGetCurrentDate() {
        LocalDate expected = LocalDate.now();
        LocalDate actual = CurrentDate.getCurrentDate();
        assertEquals(expected, actual, "Current date should match today's date");
    }
    
    @Test
    public void testGetDateTwoMonthsAgo() {
        LocalDate expected = LocalDate.now().minusMonths(2);
        LocalDate actual = CurrentDate.getDateTwoMonthsAgo();
        assertEquals(expected, actual, "Date two months ago should be calculated correctly");
    }
}
