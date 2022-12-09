package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tictactoe.databinding.ActivityGameScreenBinding;

public class GameScreen extends AppCompatActivity implements View.OnClickListener {

    private ActivityGameScreenBinding binding;
    GameScreenViewModel viewModel;
    Intent intent = getIntent();
    Bundle settings;


    private final Button[][] buttons = new Button[3][3];
    private boolean playerTurn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1, textViewPlayer2,txtViewTurnHint;

    private Adversary adversary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        bindButtons();
        initAdversary();

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
        viewModel = new ViewModelProvider(this).get(GameScreenViewModel.class);

        final Observer<Boolean> adversaryObserver = new Observer<Boolean>(){
            @Override
            public void onChanged(@Nullable final Boolean turn) {
                if (!playerTurn && settings.getBoolean("enabled")) {
                    Log.d("Reached A.I. move","Moving!");
                    adversaryMove();
                    if(checkWin()){
                        winLogic();
                        return;
                    } else roundCount++;
                    Log.d("RoundCount@OnClick","RoundCount:" + roundCount);
                    if(roundCount == 9){
                        winLogic();
                        return;
                    }

                    playerTurn = !playerTurn;
                    updateHintText();
                }
            }
        };

        viewModel.getPlayerTurn().observe(this, adversaryObserver);
    }
    private void bindButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonId = "btn" + i + j;
                int resID = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }
    @Override
    public void onClick(View view) {


        if (!((Button) view).getText().toString().equals("")) {
            return;
        }

        if(!playerTurn&&settings.getBoolean("enabled")){
            return;
        }

        if (playerTurn) {
            ((Button) view).setText("X");
        } else
            ((Button) view).setText("O");

        if(checkWin()){
            winLogic();
            return;
        } else roundCount++;
        Log.d("RoundCount@OnClick","RoundCount:" + roundCount);
        if(roundCount == 9){
            winLogic();
            return;
        }


        playerTurn = !playerTurn;
        viewModel.getPlayerTurn().setValue(playerTurn);
        updateHintText();
    }





    private void initAdversary() {
        settings = getIntent().getExtras();
        adversary = new Adversary(settings.getBoolean("difficulty"));
    }
    private void initViews() {
        textViewPlayer1 = findViewById(R.id.txtViewPlayer1);
        textViewPlayer2 = findViewById(R.id.txtViewPlayer2);
        txtViewTurnHint = findViewById(R.id.txtViewTurnHint);
        txtViewTurnHint.setText("Turn: Player 1!");
    }

    private void adversaryMove(){
        if(settings.getBoolean("enabled") && !playerTurn){
            int adversaryMove = adversary.getMove(buttons).getId();
            Button move = findViewById(adversaryMove);
            move.setText("O");
        }
    }
    private void playerWins(String winner) {

        switch (winner) {

            case "Player1":
                player1Points++;
                Toast.makeText(this, "Player 1 Wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();
                resetBoard();
                break;

            case "Player2":
                player2Points++;
                Toast.makeText(this, "Player 2 Wins!", Toast.LENGTH_SHORT).show();
                updatePointsText();
                resetBoard();
                break;

            case "Draw":
                Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();
                resetBoard();
                break;

            default:
                break;
        }


    }

    private void winLogic() {
        //roundCount++;
        Log.d("RoundCount","Count:" + roundCount);
        if (checkWin()) {
            if (playerTurn) {
                playerWins("Player1");
            } else playerWins("Player2");
        } else if (roundCount == 9) playerWins("Draw");
    }
    private boolean checkWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        return field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("");
    }
    private void updateHintText() {
        if(playerTurn)
            txtViewTurnHint.setText("Turn: Player 1!");
        else
            txtViewTurnHint.setText("Turn: Player 2!");
    }
    @SuppressLint("SetTextI18n")
    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: " + player2Points);
    }


    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        this.playerTurn = true;
        updateHintText();
    }
    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCounter", roundCount);
        outState.putInt("Player1Points", player1Points);
        outState.putInt("Player2Points", player2Points);
        outState.putBoolean("playerTurn", playerTurn);
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount = savedInstanceState.getInt("roundCounter");
        player1Points = savedInstanceState.getInt("Player1Points");
        player2Points = savedInstanceState.getInt("Player2Points");
        playerTurn = savedInstanceState.getBoolean("PlayerTurn");
    }

}