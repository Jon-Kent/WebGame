<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Web Game</title>
        <link rel="stylesheet" href="<c:url value="/resource/stylesheet/game.css" />" />
        <script src="<c:url value="/resource/js/player.js" />"></script>
        <script src="<c:url value="/resource/js/walls.js" />"></script>
        <script src="<c:url value="/resource/js/connection.js" />"></script>
    </head>
    <body>
        <canvas id="gameCanvas" width="800" height="500"></canvas>
        <script>
            
            var name = "<c:out value="${name}"/>";
            var color = "<c:out value="${color}"/>";
            var beast = <c:out value="${beast}"/>;
            
            var context = document.getElementById("gameCanvas").getContext("2d");      
            var playerCreator = new PLAYER(context, "<c:url value="/resource/images/beast.png" />");
            var walls = new WALLS(context);
            
            var connection;
            var player;
            var others = {};
                               
            function startGame(){             
                if(beast){
                    player = playerCreator.createBeast();
                    player.x = 50;
                    player.y = 450;
                }
                else{
                    player = playerCreator.createPlayer(color);
                    player.x = 15;
                    player.y = 15;
                }
                others[name] = player;            
                setWalls();
                gameArea.start(); 
            }
            
            function setWalls(){
                
                //boundary
                var boundaryThickness = 4;
                walls.addWall(0, 0, gameArea.width, boundaryThickness);
                walls.addWall(0, 0, boundaryThickness, gameArea.height);
                walls.addWall(0, gameArea.height - boundaryThickness, 
                                        gameArea.width, boundaryThickness);
                walls.addWall(gameArea.width - boundaryThickness, 0, 
                                        boundaryThickness, gameArea.height);
          
                //safety zone
                walls.addWall(0, 30, 50, 10);
                
                //internal walls
                walls.addWall(100, 100, 10, 100);
                walls.addWall(100, 400, 100, 10);
                walls.addWall(300, 100, 100, 10);
                walls.addWall(600, 300, 100, 100);
            }

            var gameArea = {
                width: document.getElementById("gameCanvas").width,
                height: document.getElementById("gameCanvas").height,
                start: function(){
                    this.interval = setInterval(updateGameArea, 20);
                    gameArea.keys = [];
                    window.addEventListener('keydown', function (e) {
                        gameArea.keys[e.keyCode] = (e.type === "keydown");
                    });
                    window.addEventListener('keyup', function (e) {
                        gameArea.keys[e.keyCode] = false;
                    });
                },
                clear: function () {
                    context.clearRect(0, 0, this.width, this.height);
                },
                stop: function(){
                    clearInterval(this.interval);
                }
            };
        
            function updateGameArea() {
                gameArea.clear();
                player.velX = 0;
                player.velY = 0;
                checkInput();
                player.newPos();  
                if(!walls.hasCollision(player.getPoints())){              
                    connection.sendPosition(player);
                }
                else{
                    player.resetPos();
                }
               // player.draw();
                for (var other in others){
                    others[other].draw();
                }
                walls.drawWalls();
            }
            
            function checkInput(){
                var amount = player.speed;
                if (gameArea.keys[37]) {
                    player.velX = -amount;
                }
                if (gameArea.keys[39]) {
                    player.velX = amount;
                }
                if (gameArea.keys[38]) {
                    player.velY = -amount;
                }
                if (gameArea.keys[40]) {
                    player.velY = amount;          
                }
            }
            
            window.onload = function(){
                connection = new CONNECTION({
                    name:name, 
                    gameArea:gameArea,
                    others:others,
                    playerCreator:playerCreator
                });
                startGame();
            };
            
            window.onbeforeunload = function(){
                connection.closeConnection();
            };
            
        </script>
    </body>
</html>
