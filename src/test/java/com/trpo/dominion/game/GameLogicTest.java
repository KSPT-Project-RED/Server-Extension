package com.trpo.dominion.game;

import java.util.Arrays;
import java.util.List;

import com.smartfoxserver.v2.entities.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {

    static GameLogic gameLogic;
    static User user1;
    static User user2;
    static Player player1;
    static Player player2;
    
    @BeforeAll
    static void beforeAll() {
        user1 = new UserStub(1);
        user2 = new UserStub(2);
        gameLogic = new GameLogic(Arrays.asList(user1, user2));
        player1 = gameLogic.getPlayerById(1);
        player2 = gameLogic.getPlayerById(2);
    }

    @Test
    void testGetPlayerById() {
        assertNotNull(gameLogic.getPlayerById(1));
        assertEquals("1", gameLogic.getPlayerById(1).getPlayerInfo().getName());
        assertNull(gameLogic.getPlayerById(3));
    }
    
    @Test
    void testProcessCard() {
        assertEquals(5, player1.getCardNumber());
        assertEquals("NoAction", gameLogic.processCard("кузница", user1));
        assertEquals(8, player1.getCardNumber());
        assertEquals("DropToMin3", gameLogic.processCard("ополчение", user1));
    }
    
    @Test
    void testBuyCard() {
        assertTrue(player1.buyNewCard("Медь"));
        assertFalse(player1.buyNewCard("Поместье"));
    }
}
