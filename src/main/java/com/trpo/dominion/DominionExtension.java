package com.trpo.dominion;

import com.smartfoxserver.v2.components.signup.SignUpAssistantComponent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;

import javax.swing.text.html.HTMLDocument;

public class DominionExtension extends SFSExtension {

    private boolean gameStarted;
    private User whoseTurn;
    private LastGameEndResponse lastGameEndResponse;
    private SignUpAssistantComponent suac;
    public static final String DATABASE_ID = "users";

    @Override
    public void init() {
        TestInfo testInfo = new TestInfo();
        trace("Dominion game Extension for SFS2X started! "+testInfo.getText());

        suac = new SignUpAssistantComponent();
        addRequestHandler(SignUpAssistantComponent.COMMAND_PREFIX, suac);
        addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);

        //считаем кол-во готовых игроков
        addRequestHandler("ready", ReadyHandler.class);
    }

    boolean isGameStarted()
    {
        return gameStarted;
    }

    void startGame()
    {
        if (gameStarted)
            throw new IllegalStateException("Game is already started!");

        lastGameEndResponse = null;
        gameStarted = true;

        User player1 = getParentRoom().getUserByPlayerId(1);
        User player2 = getParentRoom().getUserByPlayerId(2);

        //первым ходит игрок с id=1
        if (whoseTurn == null)
            whoseTurn = player1;

        //сообщаем клиенту о старте игры
        ISFSObject resObj = new SFSObject();
        resObj.putInt("t", whoseTurn.getPlayerId());
        resObj.putUtfString("p1name", player1.getName());
        resObj.putInt("p1id", player1.getId());
        resObj.putUtfString("p2name", player2.getName());
        resObj.putInt("p2id", player2.getId());

        //отправляем карты (нужно инфу брать из бд)
        ISFSObject card1 = new SFSObject();
        card1.putUtfString("Name", "Погреба");
        card1.putUtfString("Description", "Сбросьте любое число карт, возьмите столько карт, сколько сбросили.");
        card1.putUtfString("Type", "Действие");
        card1.putInt("Actions", 1);
        card1.putInt("Money", 0);
        card1.putInt("Buy", 0);
        card1.putInt("Cards", 0);
        card1.putInt("Cost", 2);

        ISFSObject card2 = new SFSObject();
        card2.putUtfString("Name", "Рынок");
        card2.putUtfString("Description", "");
        card2.putUtfString("Type", "Действие");
        card2.putInt("Actions", 1);
        card2.putInt("Money", 1);
        card2.putInt("Buy", 1);
        card2.putInt("Cards", 1);
        card2.putInt("Cost", 5);

        ISFSArray cardArray = new SFSArray();
        cardArray.addSFSObject(card1);
        cardArray.addSFSObject(card2);

        trace("SIZE CARD ARRAY: "+cardArray.size());
        resObj.putSFSArray("cards", cardArray);

        send("start", resObj, getParentRoom().getUserList());
    }

    Room getGameRoom()
    {
        return this.getParentRoom();
    }

    void updateSpectator(User user)
    {
        ISFSObject resObj = new SFSObject();

        User player1 = getParentRoom().getUserByPlayerId(1);
        User player2 = getParentRoom().getUserByPlayerId(2);

        resObj.putInt("t", whoseTurn == null ? 0 : whoseTurn.getPlayerId());
        resObj.putBool("status", gameStarted);
        //resObj.putSFSArray("board", gameBoard.toSFSArray());

        if (player1 == null)
            resObj.putInt("p1id", 0); // <--- indicates no P1
        else
        {
            resObj.putInt("p1id", player1.getId());
            resObj.putUtfString("p1name", player1.getName());
        }

        if (player2 == null)
            resObj.putInt("p2id", 0); // <--- indicates no P2
        else
        {
            resObj.putInt("p2id", player2.getId());
            resObj.putUtfString("p2name", player2.getName());

        }

        send("specStatus", resObj, user);
    }

    LastGameEndResponse getLastGameEndResponse()
    {
        return lastGameEndResponse;
    }

    void setLastGameEndResponse(LastGameEndResponse lastGameEndResponse)
    {
        this.lastGameEndResponse = lastGameEndResponse;
    }
}
