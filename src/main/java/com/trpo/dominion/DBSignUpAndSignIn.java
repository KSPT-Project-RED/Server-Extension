package com.trpo.dominion;

import com.smartfoxserver.v2.components.signup.SignUpAssistantComponent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class DBSignUpAndSignIn extends SFSExtension {

    private SignUpAssistantComponent suac;
    public static final String DATABASE_ID = "users";

    @Override
    public void init()
    {
        TestInfo testInfo = new TestInfo();
        trace("Hello. It is the dominion server extension! + "+testInfo.getText());

        suac = new SignUpAssistantComponent();
        addRequestHandler(SignUpAssistantComponent.COMMAND_PREFIX, suac);
        addEventHandler(SFSEventType.USER_LOGIN, LoginEventHandler.class);
        addEventHandler(SFSEventType.USER_JOIN_ZONE, ZoneJoinEventHandler.class);
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }
}
