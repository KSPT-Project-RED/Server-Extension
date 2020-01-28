package com.trpo.dominion.game;

import java.util.Arrays;
import java.util.List;

import com.smartfoxserver.v2.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    static GameLogic gameLogic;
    static Player player1;
    static Player player2;
    
    @BeforeAll
    static void beforeAll() {
        List<User> users = Arrays.asList(user(1), user(2));
        gameLogic = new GameLogic(users);
        player1 = gameLogic.getPlayerById(1);
        player2 = gameLogic.getPlayerById(2);
    }

    @Test
    void testGetPlayerById() {
        assertNotNull(gameLogic.getPlayerById(1));
        assertEquals("1", gameLogic.getPlayerById(1).getPlayerInfo().getName());
        assertNull(gameLogic.getPlayerById(3));
    }
    
    private static User user(int id) {
        return new UserStub(id);
    }
}
