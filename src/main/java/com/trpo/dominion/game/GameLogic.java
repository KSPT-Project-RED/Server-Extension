package com.trpo.dominion.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.smartfoxserver.v2.entities.User;
import com.trpo.dominion.dao.CardArray;
import com.trpo.dominion.dao.CardInfo;
import com.trpo.dominion.dao.FieldCash;
import com.trpo.dominion.dao.GameState;
import lombok.Getter;
import lombok.Setter;


@Getter
public class GameLogic {
    private FieldCash fieldCash;
    private Map<Integer, Player> players = new HashMap<>(); //игроки

    @Setter
    private GameState gameState;

    //инофрмация о всех игровых картах
    private CardArray cardArray = new CardArray();

    //номер игрока, чей ход
    @Setter
    private Integer turnNumber = 0;

    //создание новой игровой сессии
    public GameLogic(List<User> users) {
        for (User user : users) {
            players.put(user.getPlayerId(), new Player(user));
        }

        //создание стартовых рук игроков
        players.forEach((id, player) -> player.createHand());

        gameState = GameState.BUY;
    }

    public Player getPlayerById(int id) {
        return players.get(id);
    }
    
    public Collection<Player> getPlayers() {
        return players.values();
    }

    //обработка карты
    public String processCard(String cardName, User user) {
        //добавляем карту на поле (у основного игрока эта карта уходит из руки)
        getPlayers().forEach(player -> player.playToField(cardName, user.getPlayerId()));

        //обновляем состояние игрока
        Player player = getPlayerById(user.getPlayerId());
        player.updateCurrentPlayerState();

        //формируем ответ для остальных игроков (если карта действия влияет на них)
        //и для себя (чтобы в клиенте игрок сделал нужное действие)
        CardInfo card = cardArray.getCardByName(cardName);
        String message = "NoAction";
        switch (card.getName()) {
            case "ополчение":
                //все остальные игроки сбрасывают карты, пока не останется не более 3
                message = "DropToMin3";
                break;
            case "погреба":
                //сбрасываем любое количество карт и получаем карту за каждую сброшенную
                message = "MainPlayerDropAnyCards";
                break;
            case "мастерская":
                //получаем любую карту не дороже 4
                player.addNumCards(1, 4);
                break;
            case "реконструкция":
                //скидываем одну карту и получаем одну не дороже 2
                message = "DropOneGetCard2";
                break;
            case "рудник":
                //Скидывайте сокровище из Вашей руки и получите скоровище на 3 дороже
                message = "DropGoldGetGoldPlus3";
                break;
            case "кузница":
                //+3 карты
                player.addNumCards(3);
                break;
            case "деревня":
            case "торговец":
                //+1 карта
                player.addNumCards(1);
                break;
            default:
                break;
        }
        return message;
    }

    private void changeState() {
        if (gameState == GameState.ACTION) {
            gameState = GameState.BUY;
        } else if (gameState == GameState.BUY) {
            gameState = GameState.ACTION;
            turnNumber++;
            turnNumber = turnNumber % players.size();
        }
    }

    public void endTurn(User user) {
        endTurn(user, "");
    }

    public void endTurn(User user, String type) {
        if (type.equals("BUY")) {
            gameState = GameState.BUY;
        } else {
            getPlayers().forEach(player -> player.fieldToDrop(player.getPlayerInfo().getPlayerId()));
            getPlayerById(user.getPlayerId()).createHand();

            if (gameState == GameState.ACTION) {
                changeState();
                changeState();
            } else {
                changeState();
            }
        }
    }
}
