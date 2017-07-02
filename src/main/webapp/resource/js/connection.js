/* 
 * Encapsulates the logic for the
 * websocket connection.
 */

function CONNECTION(game) {

    var con;

    try{
        var address = 'ws://' + window.location.host 
                        + '/WebGame/gameServer/' + game.name;  
        con = new WebSocket(address);
        alert("connected");
        con.onmessage = recieveMessage;
        con.onclose = onCloseConnection;
    } catch (error) {
        alert("sorry - failed to connect!");
        return;
    }

    function sendPosition(player) {
        con.send(JSON.stringify(
                {action: 'UPDATE', player: {x: player.x, y: player.y}}));
    }

    function recieveMessage(event) {
        var message = JSON.parse(event.data);
        var action = message.action;
        if (action === "JOIN") {
            addPlayer(message.player);
        } else if (action === "UPDATE") {
            updatePlayers(message.players);
        } else if (action === "QUIT") {
            deletePlayer(message.player);
        }
    }

    function addPlayer(player) {
        var p;
        if(player.beast){
            p = game.playerCreator.createBeast();
        }
        else{
            p = game.playerCreator.createPlayer(player.color);
        }
        p.x = player.x;
        p.y = player.y;
        game.players[player.name] = p;
    }

    function deletePlayer(player) {
        delete game.players[player.name];
    }

    function updatePlayers(players) {
        for (var key in players) {
            var player = game.players[key];
            var details = players[key];
            player.alive = details.alive;
            if (key !== game.name) {
                player.x = details.x;
                player.y = details.y;      
            }
        }
    }

    function onCloseConnection(event) {
        con.close();
        game.gameArea.stop();
        alert("The connection has closed!");
    }
    
    function closeConnection(){
        con.close();
    }
    
    return {
        sendPosition: sendPosition,
        closeConnection: closeConnection
    };
        
}
