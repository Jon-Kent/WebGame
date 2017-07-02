/* 
 * Player class
 */

function PLAYER(ctx, beastImageSrc){ 
    
    function Player(color) {
        this.alive = true;
        this.speed = 2;
        this.velX = 0;
        this.velY = 0;
        this.x = 0;
        this.y = 0;
        this.radius = 10;
        this.draw = function () {
            if(this.alive){
                ctx.beginPath();
                ctx.arc(this.x, this.y, this.radius, 0, 2 * Math.PI);
                ctx.fillStyle = color;
                ctx.fill();
                ctx.stroke();
            }
        };
        this.newPos = function () {
            this.x += this.velX;
            this.y += this.velY;
        };
        this.resetPos = function () {
            this.x -= this.velX;
            this.y -= this.velY;
        };
    }

    Player.prototype.getPoints = function () {
        var points = [];
        var radianFractions = [0, 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75];
        for (var r = 0; r < radianFractions.length; r++) {
            var rad = radianFractions[r];
            points.push({x: this.x + this.radius * Math.cos(rad * Math.PI),
                y: this.y + this.radius * Math.sin(rad * Math.PI)});
        }
        return points;
    };

    function createPlayer(color){
        return new Player(color);
    }

    function createBeast(){
        var beast = new Player("black", ctx);
        beast.image = new Image();
        beast.image.src = beastImageSrc;
        beast.radius = 20;
        beast.speed = 1;
        beast.draw = function () {
            ctx.drawImage(
                beast.image, 
                beast.x - beast.radius - 1, 
                beast.y - beast.radius - 1,
                beast.image.width, 
                beast.image.height
            );
        };
        return beast;
    }
    
    return {
        createPlayer: createPlayer,
        createBeast: createBeast
    };
}