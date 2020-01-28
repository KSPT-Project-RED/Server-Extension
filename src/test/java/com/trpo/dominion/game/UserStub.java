package com.trpo.dominion.game;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import com.smartfoxserver.bitswarm.sessions.ISession;
import com.smartfoxserver.v2.buddylist.BuddyProperties;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSVariableException;
import com.smartfoxserver.v2.mmo.BaseMMOItem;
import com.smartfoxserver.v2.mmo.MMORoom;
import com.smartfoxserver.v2.util.Country;
import com.smartfoxserver.v2.util.IDisconnectionReason;

public class UserStub implements User {
    private final int playerId;
    private final String name;

    public UserStub(int playerId) {
        this.playerId = playerId;
        this.name = String.valueOf(playerId);
    }

    @Override
    public int getId() {
        return 0;
    }
    
    @Override
    public ISession getSession() {
        return null;
    }

    @Override
    public String getIpAddress() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public BuddyProperties getBuddyProperties() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public boolean isLocal() {
        return false;
    }

    @Override
    public boolean isNpc() {
        return false;
    }

    @Override
    public long getLoginTime() {
        return 0;
    }

    @Override
    public void setLastLoginTime(long l) {

    }

    @Override
    public Room getLastJoinedRoom() {
        return null;
    }

    @Override
    public List<Room> getJoinedRooms() {
        return null;
    }

    @Override
    public void addJoinedRoom(Room room) {

    }

    @Override
    public void removeJoinedRoom(Room room) {

    }

    @Override
    public boolean isJoinedInRoom(Room room) {
        return false;
    }

    @Override
    public void addCreatedRoom(Room room) {

    }

    @Override
    public void removeCreatedRoom(Room room) {

    }

    @Override
    public List<Room> getCreatedRooms() {
        return null;
    }

    @Override
    public void subscribeGroup(String s) {

    }

    @Override
    public void unsubscribeGroup(String s) {

    }

    @Override
    public List<String> getSubscribedGroups() {
        return null;
    }

    @Override
    public boolean isSubscribedToGroup(String s) {
        return false;
    }

    @Override
    public Zone getZone() {
        return null;
    }

    @Override
    public void setZone(Zone zone) {

    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public int getPlayerId(Room room) {
        return 0;
    }

    @Override
    public void setPlayerId(int i, Room room) {

    }

    @Override
    public Map<Room, Integer> getPlayerIds() {
        return null;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isPlayer(Room room) {
        return false;
    }

    @Override
    public boolean isSpectator(Room room) {
        return false;
    }

    @Override
    public boolean isJoining() {
        return false;
    }

    @Override
    public void setJoining(boolean b) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void setConnected(boolean b) {

    }

    @Override
    public boolean isSuperUser() {
        return false;
    }

    @Override
    public int getMaxAllowedVariables() {
        return 0;
    }

    @Override
    public void setMaxAllowedVariables(int i) {

    }

    @Override
    public Object getProperty(Object o) {
        return null;
    }

    @Override
    public ConcurrentMap<Object, Object> getProperties() {
        return null;
    }

    @Override
    public void setProperty(Object o, Object o1) {

    }

    @Override
    public boolean containsProperty(Object o) {
        return false;
    }

    @Override
    public void removeProperty(Object o) {

    }

    @Override
    public int getOwnedRoomsCount() {
        return 0;
    }

    @Override
    public boolean isBeingKicked() {
        return false;
    }

    @Override
    public void setBeingKicked(boolean b) {

    }

    @Override
    public short getPrivilegeId() {
        return 0;
    }

    @Override
    public void setPrivilegeId(short i) {

    }

    @Override
    public UserVariable getVariable(String s) {
        return null;
    }

    @Override
    public List<UserVariable> getVariables() {
        return null;
    }

    @Override
    public void setVariable(UserVariable userVariable) throws SFSVariableException {

    }

    @Override
    public void setVariables(List<UserVariable> list) throws SFSVariableException {

    }

    @Override
    public void removeVariable(String s) {

    }

    @Override
    public boolean containsVariable(String s) {
        return false;
    }

    @Override
    public int getVariablesCount() {
        return 0;
    }

    @Override
    public int getBadWordsWarnings() {
        return 0;
    }

    @Override
    public void setBadWordsWarnings(int i) {

    }

    @Override
    public int getFloodWarnings() {
        return 0;
    }

    @Override
    public void setFloodWarnings(int i) {

    }

    @Override
    public long getLastRequestTime() {
        return 0;
    }

    @Override
    public void setLastRequestTime(long l) {

    }

    @Override
    public void updateLastRequestTime() {

    }

    @Override
    public ISFSArray getUserVariablesData() {
        return null;
    }

    @Override
    public ISFSArray toSFSArray(Room room) {
        return null;
    }

    @Override
    public ISFSArray toSFSArray() {
        return null;
    }

    @Override
    public void disconnect(IDisconnectionReason iDisconnectionReason) {

    }

    @Override
    public String getDump() {
        return null;
    }

    @Override
    public int getReconnectionSeconds() {
        return 0;
    }

    @Override
    public void setReconnectionSeconds(int i) {

    }

    @Override
    public List<User> getLastProxyList() {
        return null;
    }

    @Override
    public void setLastProxyList(List<User> list) {

    }

    @Override
    public List<BaseMMOItem> getLastMMOItemsList() {
        return null;
    }

    @Override
    public void setLastMMOItemsList(List<BaseMMOItem> list) {

    }

    @Override
    public MMORoom getCurrentMMORoom() {
        return null;
    }

    @Override
    public Country getCountry() {
        return null;
    }

    @Override
    public void addPersistentRoomVarReference(Room room) {

    }

    @Override
    public Set<Integer> getPersistentRoomVarReferences() {
        return null;
    }
}
