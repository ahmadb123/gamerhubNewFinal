package com.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.XboxRecentGamesRepository;
import com.dto.XboxRecentGameDTO;
import com.models.DataModelAccountLinks.XboxRecentGame;
import com.models.XboxModel.GameLastPlayed;
import com.models.XboxModel.RecentGamesXbox;
import com.models.XboxModel.TitleId;
import com.models.XboxModel.XboxProfile;

@Service
public class XboxRecentGamesService {
    @Autowired
    private XboxRecentGamesRepository xboxRecentGamesRepository;

    public void saveRecentGames(RecentGamesXbox recentGamesXbox, XboxProfile profile){
        // clear old data - 

        xboxRecentGamesRepository.deleteByTitleId(String.valueOf(profile.getId()));

        List<TitleId> titles = recentGamesXbox.getTitles();
        if (titles == null || titles.isEmpty()) {
            return;
        }

        for (TitleId title : titles) {
            XboxRecentGame recentGame = new XboxRecentGame();
            recentGame.setXboxProfile(profile); // Link to the user’s Xbox_profile
            recentGame.setTitleId(title.getTitleId());
            recentGame.setGameName(title.getName());
            recentGame.setDisplayImage(title.getDisplayImage());
            GameLastPlayed titleHistory = title.getTitleHistory();
            if(titleHistory != null){
                recentGame.setDescription(titleHistory.getLastTimePlayedFormatted());
            }
            xboxRecentGamesRepository.save(recentGame);
        }
    }

    public List<XboxRecentGameDTO> getRecentGamesByUsername(String username) {
        List<XboxRecentGame> recentGames = xboxRecentGamesRepository.findRecentGamesByUsername(username);

        return recentGames.stream()
            .map(game -> new XboxRecentGameDTO(
                game.getId(),
                game.getGameName(),
                game.getTitleId(),
                game.getDisplayImage(),
                game.getLastTimePlayedFormatted()
            ))
            .collect(Collectors.toList());
        }


    // return all recent games played from db - 
    public List<XboxRecentGame> getRecentGames(String username) {
        List<XboxRecentGame> recentGames = xboxRecentGamesRepository.findRecentGamesByUsername(username);
        System.out.println("Fetched recent games: " + recentGames); // Add this to debug
        return recentGames;
    }
}    
