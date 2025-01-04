package com.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;   
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.models.NewsModel.News;
import com.services.NewsService;                           
import com.utility.CurrentDate;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping("/recent-news")
    public ResponseEntity<?> getRecentNews(
        @RequestParam(value = "start_date", required = false) LocalDate startDate,
        @RequestParam(value = "end_date", required = false) LocalDate endDate,
        @RequestParam(value = "platform", required = false) String platform
    ) {
        try {
            // Use provided dates or default to the past month
            if (startDate == null) {
                startDate = CurrentDate.getLastMonth();
            }
            if (endDate == null) {
                endDate = CurrentDate.getCurrentDate();
            }

            // Default platform IDs if not provided
            if (platform == null || platform.isEmpty()) {
                platform = "187,186, 2"; // PlayStation 5 and Xbox Series S/X
            }

            // Format dates
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedStartDate = startDate.format(formatter);
            String formattedEndDate = endDate.format(formatter);

            // Call your service class
            List<News> newsList = newsService.getRecentNews(
                formattedStartDate,
                formattedEndDate,
                platform
            );

            // Return the list of news as JSON
            return ResponseEntity.ok(newsList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                .status(500)
                .body("Failed to fetch news: " + e.getMessage());
        }
    }
}
