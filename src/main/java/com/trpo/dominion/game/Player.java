package com.trpo.dominion.game;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.trpo.dominion.dao.CardArray;
import com.trpo.dominion.dao.CardInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Player {

    private List<String> hideCards = new ArrayList<String>();
    private List<String> hand = new ArrayList<String>();
    private List<String> dropCards = new ArrayList<String>();
    private List<String> fieldCards = new ArrayList<String>();

    private CardArray cardArray = new CardArray();
    private Integer actions = 0;
    private Integer money = 0;
    private Integer buy = 0;
    private User playerInfo;

    public ISFSArray convertToSFSArray(List<String> names){
        return cardArray.getCardArrayByListName(names);
    }

    Player(User playerInfo) {
        for(int i=0;i<7;i++){
            hideCards.add("Медь");
        }
        for(int i=0;i<3;i++){
            hideCards.add("Поместье");
        }
        this.playerInfo = playerInfo;
    }

    public boolean buyNewCard(String card) {
        //trace("AAAAAAAA66666666  "+card);
        CardInfo cardInfo = cardArray.getCardByName(card);
        //trace("AAAAAAAA77777777");
        System.out.println(cardInfo.getCost()+" ;;; "+buy);
        if (cardInfo.getCost() <= money && buy > 0) {
            money -= cardInfo.getCost();
            hand.add(card);
            buy--;
            return true;
        }
        return false;
    }

    //перетаскиваем карты из руки в поле
    public boolean playToField(String card) {
        CardInfo cardInfo = cardArray.getCardByName(card);
        if (!cardInfo.getType().equals("деньги") && hand.contains(card) && actions > 0) {
            fieldCards.add(card);
            hand.remove(card);
            actions--;
            return true;
        }
        return false;
    }

    public void deleteCardFromHand(String card) {
        hand.remove(card);
    }

    private void resetState() {
        money = 0;
        actions = 1;
        buy = 1;
    }

    public void updateCurrentPlayerState() {
        resetState();
        for (String card : hand) {
            CardInfo cardInfo = cardArray.getCardByName(card);
            if (cardInfo.getType().equals("деньги")) {
                money += cardInfo.getMoney();
            }
        }

        for (String card : fieldCards) {
            CardInfo cardInfo = cardArray.getCardByName(card);
            actions += cardInfo.getAction();
            buy += cardInfo.getBuy();
        }
    }

    public void createHand() {
        if(hand.size()!=0){
            for(String handCard:hand){
                dropCards.add(handCard);
            }
        }
        hand = new ArrayList<String>();

        //если в колоде меньше 5 карт, то оставшиеся карты переходя в руку, а сброс превращается в колоду
        if (hideCards.size() < 5) {
            for (String hideCard : hideCards) {
                hand.add(hideCard);
            }
            hideCards.clear();

            for (String dropCard : dropCards) {
                hideCards.add(dropCard);
            }
            dropCards.clear();

            shuffleAndToHand(5 - hand.size());

        } else {
            shuffleAndToHand(5);
        }
    }

    private void shuffleAndToHand(int numCards) {
        for (int i = 0; i < numCards; i++) {
            int randomNum = 0 + (int) (Math.random() * hideCards.size());
            hand.add(hideCards.get(randomNum));
            hideCards.remove(randomNum);
        }
    }
}
