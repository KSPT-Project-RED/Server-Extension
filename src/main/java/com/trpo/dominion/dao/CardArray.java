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
    private final List<CardInfo> cards = new ArrayList<>();
    private final List<CardInfo> moneyCards = new ArrayList<>();
    private final List<CardInfo> winCards = new ArrayList<>();

    //по-хорошему надо брать карты из бд, ну или хотя бы завести список компбинаций карт (сейчас используктся первая)
    public ISFSArray getCardArray() {
        ISFSArray cardArray = new SFSArray();
        for (CardInfo card : cards) {
            cardArray.addSFSObject(getObject(card));
        }
        return cardArray;
    }

    public ISFSArray getCardArrayByListName(List<String> names) {
        ISFSArray cardArray = new SFSArray();
        for (String name : names) {
            cardArray.addSFSObject(getObject(getCardByName(name)));
        }
        return cardArray;
    }

    public ISFSObject getObject(CardInfo card) {
        ISFSObject cardObj = new SFSObject();
        cardObj.putUtfString("Name", card.getName());
        cardObj.putUtfString("Description", card.getDescription());
        cardObj.putInt("Action", card.getAction());
        cardObj.putInt("Money", card.getMoney());
        cardObj.putInt("Buy", card.getBuy());
        cardObj.putInt("Cards", card.getCards());
        cardObj.putInt("Cost", card.getCost());
        cardObj.putUtfString("ImageId", card.getImageId());
        cardObj.putUtfString("Type", card.getType());
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
                "windmill-3626846_960_720"));

        winCards.add(new CardInfo(
                "Дом",
                "",
                "очки",
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "castle-3971786_960_720"));

        winCards.add(new CardInfo(
                "Провинция",
                "",
                "очки",
                3,
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "church-31328_960_720"));

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
                "money-3468157_960_720"));

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
                "game-3468135_960_720"));

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
                "coin-3468134_960_720"));

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
                "+3 карты",
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
                "рудник",
                "Скидывайте сокровище из Вашей руки и получите скоровище на 3 дороже",
                "действие",
                5,
                0,
                0,
                0,
                0,
                0,
                0,
                false,
                0,
                "mine-145631_960_720"));

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
                "+1 карта",
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
                "+1 карта",
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
        for (CardInfo card : cards) {
            if (card.getName().equals(name)) {
                return card;
            }
        }

        //поиск в картах денег
        for (CardInfo moneyCard : moneyCards) {
            if (moneyCard.getName().equals(name)) {
                return moneyCard;
            }
        }

        //поиск в картах очков
        for (CardInfo winCard : winCards) {
            if (winCard.getName().equals(name)) {
                return winCard;
            }
        }
        return null;
    }
}
