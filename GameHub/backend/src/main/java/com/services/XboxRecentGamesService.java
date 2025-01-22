package com.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.Repository.XboxProfileRepository;
import com.Repository.XboxRecentGamesRepository;
import com.models.DataModelAccountLinks.XboxRecentGame;
import com.models.XboxModel.GameLastPlayed;
import com.models.XboxModel.RecentGamesXbox;
import com.models.XboxModel.TitleId;
import com.models.XboxModel.XboxProfile;

public class XboxRecentGamesService {
    @Autowired
    private XboxRecentGamesRepository xboxRecentGamesRepository;
    @Autowired
    private XboxProfileRepository xboxProfileRepository; // if needed to fetch the profile

    public void saveRecentGames(RecentGamesXbox recentGamesXbox, XboxProfile profile){
        // clear old data - 
        xboxRecentGamesRepository.deleteByTitleId(profile.getId());

        List<TitleId> titles = recentGamesXbox.getTitles();
        if (titles == null || titles.isEmpty()) {
            return;
        }

        for (TitleId title : titles) {
            XboxRecentGame recentGame = new XboxRecentGame();
            recentGame.setXboxProfile(profile); // Link to the user’s profile
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
}
