/*
 * purpose is to allow users to save their games, played games, wish to play games 
 * in a database called my_games
 * in addition each game or news will have a unique id / button that 
 * will allow the user to save the game to their profile
 */

package com.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Repository.UserSavedGamesRepository;
import com.Repository.UserSavedWishListRepository;
import com.models.NewsModel.NewsResults;
import com.models.UserModel.User;
import com.models.UserSavedGames.MyGames;
import com.models.UserSavedGames.MyWishList;

@Service
public class SaveGamesService {
    @Autowired
    private UserSavedGamesRepository userSavedGamesRepository;
    @Autowired
    private UserSavedWishListRepository userSavedWishListRepository;
    // save the game to the user's profile 
    public void saveGame(User userId, NewsResults game){
        // save the game to the user's profile
        MyGames myGames = new MyGames();
        myGames.setUser(userId);
        myGames.setGameId(game.getId());
        userSavedGamesRepository.save(myGames);
    }

    // save the add to wishlist
    public void saveToWishlist(User userId, NewsResults game){
        // save the game to the user's profile
        MyWishList wishListGames = new MyWishList();
        wishListGames.setUser(userId);
        wishListGames.setGameId(game.getId());
        userSavedWishListRepository.save(wishListGames);
    }

    // get all the games saved by the user
    public List<Long> getSavedGameIds(Long userId) {
        return userSavedGamesRepository.findGameIdsByUserId(userId);
    }

    // get all the wishlist saved by the user
    public List<Long> getWishlistGameIds(Long userId) {
        return userSavedWishListRepository.findWishlistGameIdsByUserId(userId);
    }

    // delete the game from the user's profile
    public void deleteGame(Long userId, Long gameid){
        // delete the game from the user's profile
        MyGames myGames = userSavedGamesRepository.findGameIdsByUserId(userId, gameid);
        if (myGames != null) {
            userSavedGamesRepository.delete(myGames);
        }
    }
}