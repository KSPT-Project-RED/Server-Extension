package com.trpo.dominion.handlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.trpo.dominion.DominionExtension;
import com.trpo.dominion.utils.LastGameEndResponse;

public class BuyHandler extends BaseClientRequestHandler {

    @Override
    public void handleClientRequest(User user, ISFSObject isfsObject) {
        trace("BUY");
        DominionExtension gameExt = (DominionExtension) getParentExtension();
        if(gameExt.checkBuy(user, isfsObject)) {
            trace("AAAAAAAAA2222222");
        }
    }


    }
