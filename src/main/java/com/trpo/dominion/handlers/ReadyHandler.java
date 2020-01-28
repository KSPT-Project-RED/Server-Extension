package com.trpo.dominion.handlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.trpo.dominion.DominionExtension;

public class ReadyHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        trace("ReadyHandler");
        DominionExtension gameExt = (DominionExtension) getParentExtension();
        if (user.isPlayer()) {
            if (gameExt.getGameRoom().getSize().getUserCount() == gameExt.getGameRoom().getMaxUsers())
                gameExt.startGame(gameExt.getGameRoom().getMaxUsers());
        } else {
            // TODO
        }
    }
}
