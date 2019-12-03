package com.trpo.dominion.game;

import com.smartfoxserver.v2.entities.User;
import com.trpo.dominion.dao.FieldCash;
import com.trpo.dominion.dao.GameState;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GameLogic {
    private FieldCash fieldCash;
    private List<Player> players = new ArrayList<Player>();//массив игроков

    @Setter
    GameState gameState = GameState.WAITING;

    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getPlayerInfo().getPlayerId() == id) {
                return player;
            }
        }
        return null;
    }

    //создание новой игровой сессии
    public GameLogic(int numOfPlayer, User player1, User player2) {
        //восстановление колод монет и поместий
        //fieldCash.resetCash();

        players.add(new Player(player1));
        players.add(new Player(player2));


        //создание стартовых рук игроков
        for (Player player : players) {
            player.createHand();
        }

        gameState = GameState.FIRST_BUY;


    }

    private void changeState(GameState state) {
        if (state == GameState.FIRST_ACTION && gameState == GameState.FIRST_BUY) {
            gameState = GameState.FIRST_ACTION;
        } else if (state == GameState.SECOND_ACTION && gameState == GameState.SECOND_BUY) {
            gameState = GameState.SECOND_ACTION;
        }
        else if (state == GameState.END) {
            if (gameState == GameState.FIRST_BUY || gameState == GameState.FIRST_ACTION) {
                players.get(0).createHand();
                gameState = GameState.SECOND_BUY;
            } else if (gameState == GameState.SECOND_BUY || gameState == GameState.SECOND_ACTION) {
                players.get(1).createHand();
                gameState = GameState.FIRST_BUY;
            }
        }
    }
}
