package com.example.demo.ServiceTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.models.GameClipRecordXbox.GameClip;
import com.models.GameClipRecordXbox.GameClips;
import com.services.XboxService.GameClipsServiceXbox;

import java.util.List;

public class GameClipsServiceXboxTest {

    @Test
    public void testGetGameClips_NullResponse() {
        GameClipsServiceXbox service = new GameClipsServiceXbox();
        List<GameClip> result = service.getGameClips(null);
        assertNull(result, "Expected null when responseBody is null");
    }

    @Test
    public void testGetGameClips_ValidResponse() {
        String jsonResponse = "{ \"gameClips\": [ { \"gameClipId\": \"clip1\", \"datePublished\": \"2021-01-01\", \"dateRecorded\": \"2021-01-01\", \"views\": \"100\", \"savedByUser\": \"true\", \"titleName\": \"Test Clip\", \"thumbnails\": [ { \"uri\": \"http://example.com/thumb.png\" } ], \"gameClipUris\": [ { \"uri\": \"http://example.com/clip.mp4\" } ] } ] }";
        GameClipsServiceXbox service = new GameClipsServiceXbox();
        List<GameClip> result = service.getGameClips(jsonResponse);
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Expected non-empty list for valid JSON response");
    }
}
