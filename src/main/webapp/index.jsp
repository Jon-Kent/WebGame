<!DOCTYPE html>
<html>
    <head>
        <title>Web Game</title>
        <link rel="stylesheet" href="<c:url value="/resource/stylesheet/game.css" />" />
    </head>
    <body>
        <img src="<c:url value="/resource/images/angryBall.png" />" alt="Shmetterling Inc." 
             style="width:277px;height:262px;">
        <div id="login">
            <form action="<c:url value="/game"/>" method="post">
                <table>
                    <tbody>
                        <tr>
                            <td>Name:</td>
                            <td><input type="text" name="name" value="guest" /></td>
                        </tr>
                        <tr>
                            <td>Colour:</td>
                            <td><input type="text" name="color" value="red" /></td>
                        </tr>
                        <tr>
                            <td>Beast:</td>
                            <td><input type="checkbox" name="beast" value="true" /></td>             
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="submit" value="join" /></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    </body>
</html>
