package com.trpo.dominion.handlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.trpo.dominion.DominionExtension;
import com.trpo.dominion.utils.LastGameEndResponse;

public class ReadyHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        trace("READY");
        DominionExtension gameExt = (DominionExtension) getParentExtension();

        if (user.isPlayer())
        {
            // Checks if two players are available and start game
            if (gameExt.getGameRoom().getSize().getUserCount() == 2)
                gameExt.startGame(2);
        }

        else
        {
            //gameExt.updateSpectator(user);

            LastGameEndResponse endResponse = gameExt.getLastGameEndResponse();

            // If game has ended send the outcome
            if (endResponse != null)
                send(endResponse.getCmd(), endResponse.getParams(), user);
        }
    }
}
