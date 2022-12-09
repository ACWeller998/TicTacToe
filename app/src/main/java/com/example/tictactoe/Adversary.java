package com.example.tictactoe;

import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class Adversary {
    //Objectives: Add two A.I. adversaries. One easy mode that picks remaining spaces at random and another that plays defensively perfectly//
    //data

    private int difficulty;
    private ArrayList<Button> remainingSpace = new ArrayList();

    public Adversary(boolean difficulty) {


        //Zero represents easy mode and 1 equals hard.
        if (difficulty) this.difficulty = 1;
        else if (!difficulty) this.difficulty = 0;
    }

    public Button getMove(Button[][] array){
        this.remainingSpace = calculateRemaining(array);
        if (difficulty == 0){
           return playEasy(this.remainingSpace);
        }
        if (difficulty == 1){
            return playHard(this.remainingSpace);
        }
        return null;
    }

    private Button playEasy(ArrayList array){
        Random random = new Random();
        if(!(array.size() <= 0)){
            int randomInt = random.nextInt(array.size())+1;
            Log.e("Play Easy","Int rolled: "+ randomInt);
            return this.remainingSpace.get(random.nextInt(randomInt));
        }
        return null;
    } //randomly returning a space within the blank spaces left.

    private Button playHard(ArrayList array){
        return null;
    }

    private ArrayList<Button> calculateRemaining (Button[][] array){
        ArrayList<Button> remainingSpace = new ArrayList<>();

        for (int i = 0; i < array.length ; i++){
            for(int j = 0; j < array.length ; j++){
                if (array[i][j].getText().toString() == ""){
                    remainingSpace.add(array[i][j]);
                    Log.e("Remaining Space and size" + remainingSpace.size(),"Adding: " + array[i][j]);
                }
            }
        }

        return remainingSpace;
    }




}
