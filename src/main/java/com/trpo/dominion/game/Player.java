package com.trpo.dominion.game;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.trpo.dominion.dao.CardArray;
import com.trpo.dominion.dao.CardInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Player {

    //отложенные карты, которые рубашкой вверх
    private List<String> hideCards = new ArrayList<String>();

    //текущая рука игрока
    private List<String> hand = new ArrayList<String>();

    //карты сброса - карты, которые были разыграны, но еще не вернулись в колоду
    private List<String> dropCards = new ArrayList<String>();

    //карты игрока, которые на поле
    private List<String> fieldCards = new ArrayList<String>();

    //инофрмация о всех игровых картах
    private CardArray cardArray = new CardArray();

    //кол-во доступных действий у игрока
    private Integer actions = 0;

    //кол-во монет у игрока
    private Integer money = 0;

    //кол-во доступных покупок
    private Integer buy = 0;

    //кол-во очков победы
    private Integer coins = 0;

    //техническая информация о игроке
    private User playerInfo;

    public ISFSArray convertToSFSArray(List<String> names){
        return cardArray.getCardArrayByListName(names);
    }

    //инициализация игрока
    Player(User playerInfo) {

        //предоставление обязательных 7 меди
        for(int i=0;i<7;i++) {
            hideCards.add("Медь");
        }

        //предоставление обязательных 3х карт поместья
        for(int i=0;i<3;i++) {
            hideCards.add("Поместье");
        }

        this.playerInfo = playerInfo;
    }

    //покупка карты игроком
    public boolean buyNewCard(String card) {
        CardInfo cardInfo = cardArray.getCardByName(card);

        //проверяем, что у нас достаточно средств на покупку карты
        //и у нас есть доступное кол-во покупок
        if (cardInfo.getCost() <= money && buy > 0) {
            money -= cardInfo.getCost();
            buy--;

            //кладем купленную карту в руку
            hand.add(card);

            return true;
        }
        return false;
    }

    //перетаскиваем карты из руки в поле
    public void playToField(String card, int playerId) {
        CardInfo cardInfo = cardArray.getCardByName(card);

        fieldCards.add(card);

        //карта уходит только из руки игрока, чей ход
        if(playerInfo.getPlayerId() == playerId){
            hand.remove(card);

            if (!cardInfo.getType().equals("деньги") && hand.contains(card)) {
                actions--;
            }
        }
    }

    //выкидываем карту из руки
    public void deleteCardFromHand(String card) {
        hand.remove(card);
    }

    private void resetState() {
        money = 0;
        actions = 1;
        buy = 1;
        coins = 0;
    }

    //пересчет очков, денег, кол-ва действия и т.д. в зависимости от того
    //текущий ход - ход игрока или нет
    public void updateCurrentPlayerState() {
        resetState();

        for (String card : hand) {
            CardInfo cardInfo = cardArray.getCardByName(card);

            if (cardInfo.getType().equals("очки")) {
                coins += cardInfo.getCost();
            }
        }

        for (String card : fieldCards) {
            CardInfo cardInfo = cardArray.getCardByName(card);
            actions += cardInfo.getAction();
            buy += cardInfo.getBuy();

            if (cardInfo.getType().equals("деньги")) {
                money += cardInfo.getMoney();
            }
        }

        for (String card : dropCards) {
            CardInfo cardInfo = cardArray.getCardByName(card);

            if (cardInfo.getType().equals("очки")) {
                coins += cardInfo.getCost();
            }
        }

        for (String card : hideCards) {
            CardInfo cardInfo = cardArray.getCardByName(card);

            if (cardInfo.getType().equals("очки")) {
                coins += cardInfo.getCost();
            }
        }
    }

    //после хода карты с поля идут в сброс
    public void fieldToDrop(int playerId){
        if(playerInfo.getPlayerId() == playerId){

            for(String fieldCard: fieldCards){
                dropCards.add(fieldCard);
            }

            fieldCards = new ArrayList<String>();
        }else{
            fieldCards = new ArrayList<String>();
        }
    }

    //создание игровой руки при старте игровой сессии и на каждом шаге
    public void createHand() {

        //если в руке есть карты, то надо карты все эти карты сбросить
        if(hand.size()!=0){
            for(String handCard:hand){
                dropCards.add(handCard);
            }
        }

        hand = new ArrayList<String>();

        addNumCards(5);
    }

    //тасуем калоду и раздаем в руку
    private void shuffleAndToHand(int numCards, int cost) {
        for (int i = 0; i < numCards; i++) {
            int randomNum = 0 + (int) (Math.random() * hideCards.size());

            CardInfo card = cardArray.getCardByName(hideCards.get(randomNum));
            if(card.getCost() > cost){
                i--;
            }else{
                hand.add(hideCards.get(randomNum));
                hideCards.remove(randomNum);
            }
        }
    }

    public void addNumCards(int numCards, int cost) {
        //если в колоде меньше 5 карт, то оставшиеся карты переходя в руку, а сброс превращается в колоду
        if (hideCards.size() < numCards) {
            for (String hideCard : hideCards) {
                hand.add(hideCard);
            }
            hideCards.clear();

            for (String dropCard : dropCards) {
                hideCards.add(dropCard);
            }
            dropCards.clear();

            shuffleAndToHand(5 - hand.size(), cost);

        } else {
            shuffleAndToHand(numCards, cost);
        }
    }

    public void addNumCards(int numCards){
        addNumCards(numCards, 10000000);
    }

    public ISFSObject getAllCards(){
        ISFSObject cards = new SFSObject();
        cards.putSFSArray("hand", convertToSFSArray(getHand()));
        cards.putSFSArray("hide", convertToSFSArray(getHideCards()));
        cards.putSFSArray("drop", convertToSFSArray(getDropCards()));
        cards.putSFSArray("field", convertToSFSArray(getFieldCards()));

        return cards;
    }

    public ISFSObject getState(){
        ISFSObject state = new SFSObject();
        state.putInt("Money", getMoney());
        state.putInt("Buy", getBuy());
        state.putInt("Action", getActions());
        state.putInt("Coin", getCoins());

        return state;
    }

    public void removeAllMoneyFromField(int playerId){
        List<String> tmp = new ArrayList<String>();

        if (fieldCards.size()==0) return;

        for(int i=0; i<fieldCards.size(); i++){
            if(fieldCards.get(i).equals("Медь")
                    ||fieldCards.get(i).equals("Серебро")
                    ||fieldCards.get(i).equals("Золото")){

                if(playerInfo.getPlayerId() == playerId) {
                    dropCards.add(fieldCards.get(i));
                }
            }else{
                tmp.add(fieldCards.get(i));
            }
        }
        fieldCards = tmp;
    }
}
