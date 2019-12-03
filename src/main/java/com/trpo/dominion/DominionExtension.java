package com.trpo.dominion;

import com.smartfoxserver.v2.components.signup.SignUpAssistantComponent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.trpo.dominion.dao.GameState;
import com.trpo.dominion.handlers.LoginEventHandler;
import com.trpo.dominion.handlers.StepHandler;
import com.trpo.dominion.handlers.ZoneJoinEventHandler;
import com.trpo.dominion.dao.CardArray;
import com.trpo.dominion.game.GameLogic;
import com.trpo.dominion.game.Player;
import com.trpo.dominion.handlers.ReadyHandler;
import com.trpo.dominion.utils.LastGameEndResponse;

public class DominionExtension extends SFSExtension {

    private boolean gameStarted;
    private User whoseTurn;
    private LastGameEndResponse lastGameEndResponse;
    private SignUpAssistantComponent suac;
    public static final String DATABASE_ID = "users";
    private GameLogic gameLogic;

    @Override
    public void init() {
        trace("Dominion game Extension for SFS2X started! ");

        suac = new SignUpAssistantComponent();
        addRequestHandler(SignUpAssistantComponent.COMMAND_PREFIX, suac);
        addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);

        //считаем кол-во готовых игроков
        addRequestHandler("ready", ReadyHandler.class);
        //addRequestHandler("buy", ReadyHandler.class);
        addRequestHandler("endTurn", StepHandler.class);
        //addRequestHandler("action", ReadyHandler.class);

    }

    boolean isGameStarted()
    {
        return gameStarted;
    }

    public void startGame(int numOfPlayers)
    {
        if (gameStarted)
            throw new IllegalStateException("Game is already started! AAAAAAAA");

        lastGameEndResponse = null;
        gameStarted = true;

        ISFSObject resObj = getInitGameInfo();

        //формируем колоду (далее сделать несколько вариаций колод)
        CardArray cardArray = new CardArray();
        resObj.putSFSArray("cards", cardArray.getCardArray());

        //отправляем игрокам информацию о всех игроках и очередности
        send("start", resObj, getParentRoom().getUserList());

        //создаем игровую сессию (инициализируем руки игроков и т.д.)
        gameLogic = new GameLogic(numOfPlayers, getParentRoom().getUserByPlayerId(1), getParentRoom().getUserByPlayerId(2));

        //отправляем игрокам инфу о их начальной руке
        for(Player player: gameLogic.getPlayers()){
            sendNewCards(player);
        }
    }

    public void endTurn(User user){
        gameLogic.setGameState(GameState.END);
        sendNewCards(gameLogic.getPlayerById(user.getPlayerId()));
        send("step", getInitGameInfo(), getParentRoom().getUserList());
    }

    //отправка текущей руки, колоды и сброса
    public void sendNewCards(Player player){
        ISFSObject cards = new SFSObject();
        cards.putUtfStringArray("hand", player.getHand());
        cards.putUtfStringArray("hide", player.getHideCards());
        cards.putUtfStringArray("drop", player.getDropCards());

        send("cards", cards, player.getPlayerInfo());

    }

    private ISFSObject getInitGameInfo(){

        User player1 = getParentRoom().getUserByPlayerId(1);
        User player2 = getParentRoom().getUserByPlayerId(2);

        //первым ходит игрок с id=1
        if (whoseTurn == null)
            whoseTurn = player1;
        else if(whoseTurn == player1){
            whoseTurn = player2;
        } else if(whoseTurn == player2){
            whoseTurn = player1;
        }

        //сообщаем клиенту о старте игры
        ISFSObject resObj = new SFSObject();
        resObj.putInt("t", whoseTurn.getPlayerId());
        resObj.putUtfString("p1name", player1.getName());
        resObj.putInt("p1id", player1.getId());
        resObj.putUtfString("p2name", player2.getName());
        resObj.putInt("p2id", player2.getId());

        return resObj;
    }

    public Room getGameRoom()
    {
        return this.getParentRoom();
    }

    public LastGameEndResponse getLastGameEndResponse()
    {
        return lastGameEndResponse;
    }

    public void setLastGameEndResponse(LastGameEndResponse lastGameEndResponse)
    {
        this.lastGameEndResponse = lastGameEndResponse;
    }
}
