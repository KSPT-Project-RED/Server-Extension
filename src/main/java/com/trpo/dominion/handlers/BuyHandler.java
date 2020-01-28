package com.trpo.dominion.handlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.trpo.dominion.DominionExtension;

public class BuyHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        trace("BuyHandler");
        DominionExtension gameExt = (DominionExtension) getParentExtension();
        if (gameExt.checkBuy(user, isfsObject)) {
            trace("User bought", user.getPlayerId());
        }
    }
}
