/* 
 * Maintains a collection of walls
 */

function WALLS(ctx) {

    var walls = [];

    function addWall(left, top, width, height) {
        walls.push(new Wall(left, top, width, height));
    }

    function drawWalls() {
        for (var i = 0; i < walls.length; i++) {
            walls[i].draw();
        }
    }

    function Wall(left, top, width, height) {
        this.draw = function () {
            ctx.fillStyle = 'DimGray';
            ctx.fillRect(left, top, width, height);
        };
        this.contains = function (point) {
            if (point.x > left && point.x < left + width) {
                if (point.y > top && point.y < top + height) {
                    return true;
                }
            }
            return false;
        };
    }

    function hasCollision(points) {
        for (var p = 0; p < points.length; p++) {
            for (var i = 0; i < walls.length; i++) {
                if (walls[i].contains(points[p])) {
                    return true;
                }
            }
        }
        return false;
    }
    
    return {
        addWall: addWall,
        drawWalls: drawWalls,
        hasCollision: hasCollision
    };
}


