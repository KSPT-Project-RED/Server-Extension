package com.trpo.dominion.dao;

import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CardArray {
    List<CardInfo> cards = new ArrayList<CardInfo>();
    List<CardInfo> moneyCards = new ArrayList<CardInfo>();
    List<CardInfo> winCards = new ArrayList<CardInfo>();


    //по-хорошему надо брать карты из бд, ну или хотя бы завести список компбинаций карт (сейчас используктся первая)
    public ISFSArray getCardArray() {
        ISFSArray cardArray = new SFSArray();
        for (int i = 0; i < cards.size(); i++) {
            cardArray.addSFSObject(getObject(cards.get(i)));
        }

        return cardArray;
    }

//    public ISFSArray getCardArrayByListName(List<String> names){
//        for(String name:names){
//
//        }
//    }

    public ISFSObject getObject(CardInfo card){
        ISFSObject cardObj = new SFSObject();
        cardObj.putUtfString("Name", card.getName());
        cardObj.putUtfString("Description", card.getDescription());
        cardObj.putInt("Action", card.getAction());
        cardObj.putInt("Money", card.getMoney());
        cardObj.putInt("Buy", card.getBuy());
        cardObj.putInt("Cards", card.getCards());
        cardObj.putInt("Cost", card.getCost());
        cardObj.putUtfString("ImageId", card.getImageId());
        return cardObj;
    }

    public CardArray() {
        winCards.add(new CardInfo(
                "Поместье",
                "",
                "очки",
                1,
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "army-2026972_1280"));

        moneyCards.add(new CardInfo(
                "Медь",
                "",
                "деньги",
                0,
                0,
                0,
                1,
                0,
                0,
                0,
                false,
                0,
                "army-2026972_1280"));

        moneyCards.add(new CardInfo(
                "Серебро",
                "",
                "деньги",
                3,
                0,
                0,
                3,
                0,
                0,
                0,
                false,
                0,
                "army-2026972_1280"));

        moneyCards.add(new CardInfo(
                "Золото",
                "",
                "деньги",
                6,
                0,
                0,
                6,
                0,
                0,
                0,
                false,
                0,
                "army-2026972_1280"));

        cards.add(new CardInfo(
                "ополчение",
                "Все остальные игроки сбрасывают карты из руки, пока не останется не больше трех.",
                "действие",
                4,
                0,
                0,
                2,
                0,
                0,
                0,
                false,
                0,
                "army-2026972_1280"));

        cards.add(new CardInfo(
                "кузница",
                "",
                "действие",
                4,
                0,
                0,
                0,
                3,
                0,
                0,
                false,
                0,
                "blacksmith-2672326_960_720"));

        cards.add(new CardInfo(
                "погреба",
                "Сбросьте любое количество карт.+1 карта за каждую сброшенную.",
                "действие",
                2,
                1,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "cellar-148262_960_720"));

        cards.add(new CardInfo(
                "ров",
                "Когда другой игрок применяет карту Атаки, вы можете раскрыть эту карту из руки. В этом случае Атака на вас не влияет.",
                "действие",
                2,
                0,
                0,
                0,
                2,
                0,
                0,
                true,
                0,
                "draw-bridge-148103_960_720"));

        cards.add(new CardInfo(
                "рынок",
                "",
                "действие",
                5,
                1,
                1,
                1,
                1,
                0,
                0,
                false,
                0,
                "fruit-seller-4117003_960_720"));

        cards.add(new CardInfo(
                "мастерская",
                "Получите карту не дороже $4",
                "действие",
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "tools-1801946_640"));

        cards.add(new CardInfo(
                "мастерская",
                "Получите карту не дороже $4",
                "действие",
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "tools-1801946_640"));

        cards.add(new CardInfo(
                "реконструкция",
                "Выкиньте на Свалку карту из руки. Получите карту: по стоимости она может быть дороже выкинутой не больше, чем на $2.",
                "действие",
                4,
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "tools-3411589_1920"));

        cards.add(new CardInfo(
                "деревня",
                "",
                "действие",
                3,
                2,
                0,
                0,
                1,
                0,
                0,
                false,
                0,
                "windmill-3626846_960_720"));

        cards.add(new CardInfo(
                "торговец",
                "",
                "действие",
                3,
                1,
                0,
                0,
                1,
                0,
                0,
                false,
                0,
                "money-307192_960_720"));
    }

    public CardInfo getCardByName(String name) {
        //поиск в картах действия
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getName().equals(name)) {
                return cards.get(i);
            }
        }

        //поиск в картах денег
        for (int i = 0; i < moneyCards.size(); i++) {
            if (moneyCards.get(i).getName().equals(name)) {
                return moneyCards.get(i);
            }
        }

        //поиск в картах очков
        for (int i = 0; i < winCards.size(); i++) {
            if (winCards.get(i).getName().equals(name)) {
                return winCards.get(i);
            }
        }
        return null;
    }

    public Integer getSize() {
        return cards.size();
    }
}
