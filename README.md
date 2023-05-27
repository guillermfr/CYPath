# Java Project: CY Path

## Presentation

This project is a reproduction of the game Quoridor.
This game is a 2 or 4 players game where you need to reach the other side of the board with your pawn.
To do this, you can either move your pawn one square, or place a barrier to slow the progression of your opponent.
This project is developed in Java.

## Run the project

To run this project, all you need to do is:
- Download (or clone) this repository
- Open the project folder on your favourite Java IDE (Intellij, Eclipse, ...)
- Go to the following file: "src/main/java/graphicInterface/Home.java"
- Run the file with the green arrow (in the top right corner on Intellij, in the top left corner on Eclipse)
- Enjoy the game!

## Play the game

### Home

When you run the game, the first scene that you see is the _Home scene_.
From this scene, you can either start a new game, continue a saved game, or quit the game.

### New game

By clicking the _New game button_,
you will land on a scene on which you will be able to choose if you want to play a game with 2 or 4 players.
Clicking on of those two buttons will start a game with the number of players chosen.

### Game

#### Game display

By clicking on of the two buttons from the _New game_ scene, you will land on the Game scene.

In the centre is the board. You can see the different players that are represented with colorful circles.

At the top of the screen, there is a label showing whose turn it is.

On the right, there is multiple information.
- First, there is the turns' counter.
- Then, there is the number of barriers placed on the board. This number is limited to 20. When every barrier has been placed, the button becomes gray, and it is no longer possible to click on it.
- Just below is the _action button_. By default, the player can move his pawn. By clicking this button, the player can place a barrier. He can click this button one more time if he changed his mind.
- Finally, there are _two buttons_. The first one is to save the game, and the second one is to load a game.

#### Move your pawn

To move your pawn, all you need to do is click on the case you want to go to during your turn.
However, you will only be able to do that if the move is valid.
To know if a move is valid, the game indicates with lighter circles where you can go.
You can then click on one of the cases.
If you try to click on a case you are not able to go to, nothing will happen.

#### Place a barrier

First, you need to click the _action button_ on the right.
Then, when you move your mouse on the screen, you will see a bar on the board that follows your mouse.
It represents the barrier that you can set.
If you want to change the direction of the barrier, you can right-click to change it.

Then, you can left-click to place the barrier.
If the barrier is invalid, it will flash red.
This can happen in the cases:
- If the barrier overlaps another barrier
- If the barrier blocks another player's path to the other side of the board

#### Save and Load

When you click on the _Save button_ for the first time,
a dialog box will appear where you will be able to enter the name of the save file.
Then everytime you will click on the _Save button_ in a game that has already been saved previously,
it will automatically save it in the same file.

When you click on the _Load button_, it will redirect you to the Continue scene.

#### End of the game

The game ends when one of the players reaches the other side of the board.
It is no longer possible to move your pawn neither place a barrier.
The save button also disappears, and the two buttons left are the _Load button_ and the _Back button_.

### Continue

By clicking the _Continue button_,
you will land on a scene on which you will see a list of every game that has been saved.
From this scene, you are able to either resume a saved game, or delete it.

### Back

At any point, if you want to go back to the _Home scene_, you can click on the top left arrow.

### Quit

By clicking the _Quit button_, the window will simply close and the program will stop.

## Authors

- [@Amandine CHANTOME](https://github.com/amandine-ch)
- [@Line FOURCHER](https://github.com/LineFourcher)
- [@François GUILLERM](https://github.com/guillermfr)
- [@Valentin SING](https://github.com/ValentinChanter)
