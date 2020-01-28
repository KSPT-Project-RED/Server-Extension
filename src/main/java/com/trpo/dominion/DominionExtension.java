package com.trpo.dominion;

import com.smartfoxserver.v2.components.signup.SignUpAssistantComponent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;
import com.trpo.dominion.dao.GameState;
import com.trpo.dominion.handlers.*;
import com.trpo.dominion.dao.CardArray;
import com.trpo.dominion.game.GameLogic;
import com.trpo.dominion.game.Player;
import com.trpo.dominion.utils.LastGameEndResponse;

public class DominionExtension extends SFSExtension {

    private boolean gameStarted;
    private User whoseTurn;
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
        addRequestHandler("buy", BuyHandler.class);
        addRequestHandler("endTurn", StepHandler.class);
        addRequestHandler("action", ActionHandler.class);
        addRequestHandler("DropCard", DropCardHandler.class);
    }

    boolean isGameStarted()
    {
        return gameStarted;
    }

    public void startGame(int numOfPlayers)
    {
        trace("QQQQQQQ1");
        if (gameStarted)
            throw new IllegalStateException("Game is already started!");

        gameStarted = true;

        ISFSObject resObj = getInitGameInfo();

        //формируем колоду (далее сделать несколько вариаций колод)
        CardArray cardArray = new CardArray();
        resObj.putSFSArray("cards", cardArray.getCardArray());

        //отправляем игрокам информацию о всех игроках и очередности
        send("start", resObj, getParentRoom().getUserList());

        //создаем игровую сессию (инициализируем руки игроков и т.д.)
        gameLogic = new GameLogic(getParentRoom().getPlayersList());

        //отправляем игрокам инфу о их начальной руке
        for(Player player: gameLogic.getPlayers()){
            sendNewCards(player);
        }

        //отправляем инфу о текущем состоянии
        for(Player player: gameLogic.getPlayers()){
            player.updateCurrentPlayerState();
            sendNewState(player);
        }
    }

    public void DropCard(User user, ISFSObject isfsObject){

        Player player = gameLogic.getPlayerById(user.getPlayerId());
        player.deleteCardFromHand(isfsObject.getUtfString("DropCard"));
        sendNewCards(player);

    }

    public boolean checkBuy(User user, ISFSObject isfsObject){

        Player player = gameLogic.getPlayerById(user.getPlayerId());

        if(player.buyNewCard(isfsObject.getUtfString("Name"))){

            for(Player player1: gameLogic.getPlayers()){
                player1.removeAllMoneyFromField(user.getPlayerId());
                sendNewCards(player1);
            }

            send("buy", isfsObject, getParentRoom().getUserList());

            gameLogic.endTurn(user, "BUY");

            endStepToAll();
            sendNewCards(player);
            sendNewState(player);

            return true;
        }

        return false;
    }

    //обрабатываем карту действия
    public void addAction(User user, ISFSObject isfsObject){
        String message = gameLogic.processCard(isfsObject.getUtfString("NameCard"), user);

        //после действия обновляем карты у всех игроков
        for(Player player: gameLogic.getPlayers()){
            sendNewCards(player);
            if(player.getPlayerInfo().getPlayerId() == user.getPlayerId()){
                sendNewState(player);
            }
        }

        //отправка всем сообщения
        ISFSObject command = new SFSObject();
        command.putUtfString("command", message);
        for(Player player: gameLogic.getPlayers()) {
            if(user.getPlayerId() != player.getPlayerInfo().getPlayerId()) {
                trace("1111Send "+message);
                send("command", command, player.getPlayerInfo());
            }
        }
    }

    public void endStepToAll(){
        //отправляем инфу о текущем состоянии
        for(Player player: gameLogic.getPlayers()){
            player.updateCurrentPlayerState();
            sendNewState(player);
        }

        ISFSObject step = new SFSObject();
        step.putUtfString("Step", gameLogic.getGameState().toString());
        step.putInt("currentTurn", gameLogic.getTurnNumber());
        send("step", step, getParentRoom().getUserList());
    }

    public void endTurn(User user){
        gameLogic.endTurn(user);

        for(Player player: gameLogic.getPlayers()){
            sendNewCards(player);
        }

        endStepToAll();
    }

    //отправляем состояние игрока (деньги, карты и т.д.)
    public void sendNewState(Player player){
        send("state", player.getState(), player.getPlayerInfo());
    }

    //отправка текущей руки, колоды и сброса
    public void sendNewCards(Player player){
        trace("AAAAAAA SEND HANDS "+ player.getPlayerInfo().getPlayerId());
        send("cards", player.getAllCards(), player.getPlayerInfo());
    }

    private ISFSObject getInitGameInfo(){

        ISFSObject resObj = new SFSObject();

        for(int i=0;i< getParentRoom().getPlayersList().size();i++){
            User player = getParentRoom().getPlayersList().get(i);
            resObj.putInt("t"+i, player.getPlayerId());
            resObj.putUtfString("name"+i, player.getName());
            resObj.putInt("id"+i, player.getId());
        }

        resObj.putInt("num", getParentRoom().getPlayersList().size());
        return resObj;
    }

    public Room getGameRoom()
    {
        return this.getParentRoom();
    }
}
