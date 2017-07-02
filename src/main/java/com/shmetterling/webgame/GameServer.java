package com.shmetterling.webgame;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Jon Kent
 */
@ServerEndpoint("/gameServer/{username}")
public class GameServer {
    
    public static List<Session> sessions = new ArrayList<>();
    public static Map<String, Player> players = new HashMap<>();
    public static Map<String, Player> pendingPlayers = new HashMap<>();
    public static Session broadcaster;
    private static ObjectMapper mapper = new ObjectMapper();
    
    public enum Action{
        JOIN,
        UPDATE,
        QUIT;
    }
    
    @OnOpen
    public void onOpen(Session session, 
            @PathParam("username") String username) throws IOException{
        
        if(broadcaster == null){
            broadcaster = session;
        }
        Player p = pendingPlayers.remove(username);
        players.put(username, p);
        sessions.add(session);
        
        //tell everyone you've joined
        sendMessageToAllExcept(session, new SinglePlayerMessage(Action.JOIN.name(), p));
        
        //get details of everyone else
        for(Player player : players.values()){
            if(!player.equals(p)){
                sendMessage(new SinglePlayerMessage(Action.JOIN.name(), player), session);
            }
        }     
    }
    
    @OnClose
    public void onClose(Session session,
            @PathParam("username") String username) throws IOException{
        Player p = players.remove(username);
        sessions.remove(session);
        
        //if you are the broadcaster ensure someone else is now assigned to be.
        if(session.equals(broadcaster)){
            broadcaster = sessions.get(0);
        }
        //tell everyone you've quit
        sendMessageToAllExcept(session, new SinglePlayerMessage(Action.QUIT.name(), p));
    }
    
    @OnMessage
    public void onMessage(Session session, String recieved,
            @PathParam("username") String username) throws IOException{
        
        SinglePlayerMessage message = mapper.readValue(recieved, SinglePlayerMessage.class);
        Player player = players.get(username);
        
        //update the player on the server
        if(player != null){
            player.setPosition(message.getPlayer().getX(), message.getPlayer().getY());
        }

        //only the broadcaster sends update to everyone
        if(session.equals(broadcaster)){
            Capture.checkCapture(players);
            sendMessageToAll(new MultiplePlayerMessage(Action.UPDATE.name(), players));
        }
    }
    
    private void sendMessage(Message message, Session session) throws IOException{ 
        session.getBasicRemote().sendText(mapper.writeValueAsString(message));
    }
    
    private void sendMessageToAll(Message message) throws IOException{ 
        for (Session s : sessions){
            s.getBasicRemote().sendText(mapper.writeValueAsString(message));
        }     
    }
    
    private void sendMessageToAllExcept(Session session, Message message) throws IOException{ 
        for (Session s : sessions){
            if(!s.equals(session)){
                s.getBasicRemote().sendText(mapper.writeValueAsString(message));
            }
        }     
    }
     
    @OnError
    public void onError(Throwable t) {
    }
    
    public static class Message{
        
        private String action;
        
        public Message(){
        }
        
        public Message(String action){
            this.action = action;
        }
        
        public void setAction(String action){
            this.action = action;
        }
        
        public String getAction(){
            return action;
        }
    }

    public static class SinglePlayerMessage extends Message{
        
        private Player player;
    
        public SinglePlayerMessage(){ 
        }
        
        public SinglePlayerMessage(String action, Player player){
            super(action);
            this.player = player;
        }
        
        public void setPlayer(Player player){
            this.player = player;
        }

        public Player getPlayer() {
            return player;
        }
    }
    
    public static class MultiplePlayerMessage extends Message{
        
        private Map<String,Player> players;
    
        public MultiplePlayerMessage(String action, Map<String,Player> players){
            super(action);
            this.players = players;
        }
        
        public Map<String,Player> getPlayers() {
            return players;
        }
    }
}
