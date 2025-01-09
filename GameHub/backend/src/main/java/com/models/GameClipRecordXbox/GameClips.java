package com.models.GameClipRecordXbox;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class GameClips {
   private List<GameClip> gameClips; // array of game clips objects

   public List<GameClip> getGameClips() {
       return gameClips;
   }

   public void setGameClips(List<GameClip> gameClips) {
       this.gameClips = gameClips;
   }
}
