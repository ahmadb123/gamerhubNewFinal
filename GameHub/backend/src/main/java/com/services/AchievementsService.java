package com.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.models.XboxProfileAchievements.Achievements;
import com.models.XboxProfileAchievements.MediaAssets;
import com.models.XboxProfileAchievements.Progression;
import com.models.XboxProfileAchievements.TitleAssociations;

@Service
public class AchievementsService {

    public List<Achievements> getAchievements(String xuid, String url) {
        List<Achievements> achievements = new ArrayList<>();
        try {
            if (xuid == null || xuid.isEmpty()) {
                // If xuid is empty, return an empty list
                return achievements;
            }

            // Simulate reading JSON from the string `url`.
            // If `url` is actually a URL endpoint, you’d do an HTTP GET instead,
            // then use mapper.readTree(responseBody).
            ObjectMapper mapper = new ObjectMapper();
            JsonNode response = mapper.readTree(url);

            if (response != null && response.has("achievements")) {
                for (int i = 0; i < response.get("achievements").size(); i++) {
                    JsonNode achievement = response.get("achievements").get(i);

                    Achievements newAchievement = new Achievements();
                    newAchievement.setId(achievement.get("id").asText());
                    newAchievement.setDescription(achievement.get("description").asText());

                    Progression progression = null; 
                    if (achievement.has("progression")) {
                        JsonNode progNode = achievement.get("progression");
                        progression = new Progression();
                        progression.setTimeUnlocked(progNode.get("timeUnlocked").asText());
                        
                        // If you want to parse "requirements" too, 
                        // you'll do it here and set them on progression.
                    }
                    // Set the single progression object on the Achievements
                    newAchievement.setProgression(progression);

                    // Handle media assets
                    List<MediaAssets> mediaAssets = new ArrayList<>();
                    if (achievement.has("mediaAssets")) {
                        for (int j = 0; j < achievement.get("mediaAssets").size(); j++) {
                            JsonNode assetNode = achievement.get("mediaAssets").get(j);

                            MediaAssets mediaAsset = new MediaAssets();
                            mediaAsset.setName(assetNode.get("name").asText());
                            mediaAsset.setType(assetNode.get("type").asText());
                            mediaAsset.setUrl(assetNode.get("url").asText());

                            mediaAssets.add(mediaAsset);
                        }
                    }

                    // Handle title associations
                    List<TitleAssociations> titleAssociations = new ArrayList<>();
                    if (achievement.has("titleAssociations")) {
                        for (int j = 0; j < achievement.get("titleAssociations").size(); j++) {
                            JsonNode titleNode = achievement.get("titleAssociations").get(j);

                            TitleAssociations titleAssociation = new TitleAssociations();
                            titleAssociation.setName(titleNode.get("name").asText());
                            titleAssociation.setId(titleNode.get("id").asText());

                            titleAssociations.add(titleAssociation);
                        }
                    }

                    newAchievement.setProgression(progression);
                    newAchievement.setMediaAssets(mediaAssets);
                    newAchievement.setTitleAssociations(titleAssociations);

                    achievements.add(newAchievement);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return achievements;
    }
}
