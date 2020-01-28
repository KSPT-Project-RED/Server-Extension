package com.trpo.dominion.game;

import com.smartfoxserver.v2.entities.User;
import com.trpo.dominion.dao.CardArray;
import com.trpo.dominion.dao.CardInfo;
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

    //инофрмация о всех игровых картах
    private CardArray cardArray = new CardArray();


    //номер игрока, чей ход
    @Setter
    Integer turnNumber = 0;

    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getPlayerInfo().getPlayerId() == id) {
                return player;
            }
        }
        return null;
    }

    //создание новой игровой сессии
    public GameLogic(List<User> users) {

        for(int i=0;i<users.size();i++) {
            players.add(new Player(users.get(i)));
        }

        //создание стартовых рук игроков
        for (Player player : players) {
            player.createHand();
        }

        gameState = GameState.BUY;
    }

    //обработка карты
    public String processCard(String cardName, User user){

        //добавляем карту на поле (у основного игрока эта карта уходит из руки)
        for(Player player: getPlayers()){
            player.playToField(cardName, user.getPlayerId());
        }

        //обновляем состояние игрока
        Player player = getPlayerById(user.getPlayerId());
        player.updateCurrentPlayerState();

        //формируем ответ для остальных игроков (если карта действия влияет на них)
        //и для себя (чтобы в клиенте игрок сделал нужное действие)
        CardInfo card = cardArray.getCardByName(cardName);
        String message = "";

        if (card.getName().equals("ополчение")){

            //все остальные игроки сбрасывают карты, пока не останется не более 3
            message = "DropToMin3";

        }else if (card.getName().equals("погреба")){

            //сбрасываем любое количество карт и получаем карту за каждую сброшенную
            message = "MainPlayerDropAnyCards";

        }else if (card.getName().equals("мастерская")){

            //получаем любую карту не дороже 4
            player.addNumCards(1,4);
            message = "NoAction";

        }else if (card.getName().equals("реконструкция")){

            //скидываем одну карту и получаем одну не дороже 2
            message = "DropOneGetCard2";

        }else if (card.getName().equals("рудник")){

            //Скидывайте сокровище из Вашей руки и получите скоровище на 3 дороже
            message = "DropGoldGetGoldPlus3";

        } else if (card.getName().equals("кузница")){

            //+3 карты
            player.addNumCards(3);
            message = "NoAction";

        }else if (card.getName().equals("деревня")){

            //+1 карта
            player.addNumCards(1);
            message = "NoAction";

        }else if (card.getName().equals("торговец")){

            //+1 карта
            player.addNumCards(1);
            message = "NoAction";

        }else {
            message = "NoAction";
        }

        return message;
    }

    private void changeState() {
        if(gameState == GameState.ACTION) {
            gameState = GameState.BUY;
        }
        else if (gameState == GameState.BUY) {
            gameState = GameState.ACTION;
            turnNumber++;
            turnNumber = turnNumber % players.size();
        }
    }

    public void endTurn(User user) {
        endTurn(user, "");
    }

    public void endTurn(User user, String type){

        if(type.equals("BUY")){
            gameState = GameState.BUY;
        }else {
            for (Player player : getPlayers()) {
                player.fieldToDrop(player.getPlayerInfo().getPlayerId());
            }
            getPlayerById(user.getPlayerId()).createHand();

            if (gameState == GameState.ACTION) {
                changeState();
                changeState();
            }else{
                changeState();
            }
        }



    }
}
