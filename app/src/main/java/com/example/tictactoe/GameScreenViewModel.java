package com.example.tictactoe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameScreenViewModel extends ViewModel {
    private MutableLiveData<Boolean> playerTurn;

    public MutableLiveData<Boolean> getPlayerTurn(){

        if (playerTurn == null) {
            playerTurn = new MutableLiveData<Boolean>();
        }
        return playerTurn;
    }




}
