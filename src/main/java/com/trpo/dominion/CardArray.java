package com.trpo.dominion;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CardArray {
    List<CardInfo> cards = new ArrayList<CardInfo>();

    CardArray(){
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

    public CardInfo getCardByName(String name){
        for(int i=0;i<cards.size();i++){
            if(cards.get(i).getName().equals(name)){
                return cards.get(i);
            }
        }
        return null;
    }

    public Integer getSize(){
        return cards.size();
    }
}
