<div align="center">
		<img src="https://i.imgur.com/YnWDRCO.png" alt="Logo CYPath" />
</div>

## Table of Contents
- [About](#about)
- [Installation](#installation)
    * [Recommended environment setup](#recommended-environment-setup)
    * [Run without generating a JAR file](#run-without-generating-a-jar-file)
    * [Setup to run through a JAR file](#setup-to-run-through-a-jar-file)
    * [Generate JavaDoc](#generate-javadoc)
- [How to play](#how-to-play)
    * [Rules](#rules)
    * [Controls](#controls)
- [Documentation](#documentation)
- [Authors](#authors)

## About
This project is a reproduction of the game Quoridor.
This game is a 2 or 4 players game where you need to reach the other side of the board with your pawn. <br />
To do this, you can either move your pawn one square, or place a barrier to slow the progression of your opponent. <br />
This project is developed in Java.

## Installation
### Recommended environment setup
* [JDK](https://www.oracle.com/fr/java/technologies/downloads/) 20 or newer
* [JavaFX](https://gluonhq.com/products/javafx/) 20 or newer

Before anything else, start by cloning the repository
```bash
git clone https://github.com/guillermfr/CYPath
cd CYPath
```

### Run without generating a JAR file
If you don't need to generate a JAR file and just want to execute the application, you can do the following:

1. Run the application using the Maven Wrapper

   1.1. Under Windows
    ```bash
    .\mvnw.cmd javafx:run
    ```

   1.2. Under Linux (or any other Unix-based OS)
    ```bash
    ./mvnw javafx:run
    ```

### Setup to run through a JAR file
If you do need to generate a JAR file to export it afterward, you can do the following:

1. Generate the JAR archive from the source code using the Maven Wrapper

    1.1. Under Windows
    ```bash
    .\mvnw.cmd package
    ```

   1.2. Under Linux (or any other Unix-based OS)
    ```bash
    ./mvnw package
    ```

2. Move to the generated folder
```bash
cd target
```

3. Now, you can launch the application using the JAR file
```bash
java --module-path "path/to/javafx-sdk-x.y.z/lib" --add-modules javafx.controls,javafx.fxml -jar ./CYPath-x.y.jar
```
Where `path/to/javafx-sdk-x.y.z/lib` is the path to your javafx lib directory (e.g. `C:/javafx-sdk-20.0.1/lib`) and `./CYPath-x.y.jar` is the name of the generated file with the correct version (e.g. `./CYPath-1.0.jar`). <br />
Note that if you're using Windows, you might need to replace all `/` with `\ `.

### Generate JavaDoc
1. Generate it using the Maven Wrapper

   1.1. Under Windows
    ```bash
    .\mvnw.cmd javadoc:javadoc
    ```

   1.2. Under Linux (or any other Unix-based OS)
    ```bash
    ./mvnw javadoc:javadoc
    ```

## How to play

Upon launching the application, you will choose between starting a new game, load one from a save file, or quit the application. If you choose to start a new game, you will be prompted to choose between 2 or 4 players.

![Home](https://i.imgur.com/dOTouXj.png)

### Rules
Each player starts in the middle of an edge of the board. To win, a player has to reach any cell on the opposite edge first. <br />
On their turn, a player can either move or place a barrier.

* If they choose to move, they can only move by one cell, unless there is a player next to them. In this case, they can jump over them. However, if there is a barrier or another player behind their opponent, they can choose to move diagonally (next to the opponent next to them).

<div align="center">
   <img src="https://i.imgur.com/tRI59M9.gif" width="400" height="400" alt="Jumping Move">
   <img src="https://i.imgur.com/8HrrspE.gif" width="400" height="400" alt="Diagonal Move">
</div>

* If they choose to place a barrier, they can place it horizontally or vertically. <br />
They can't place it if it overlaps another barrier or completely blocks a player from winning.

![Barrier](https://i.imgur.com/tLEgtSn.gif)

### Controls

* To move a player, click on one of the *ghosts* on the board to move there.
* To place a barrier, click `Place a barrier` and then click between two cells to place it. <br />
  By default, it will be placed horizontally, and it can be switched vertically by right-clicking. <br />
  If you changed your mind and want to move your player, you can click on `Move player`

## Documentation
JavaDoc is generated automatically and available in `Doc/apidocs`. To view the documentation, open index.html in your browser.

## Authors

- [@Amandine CHANTOME](https://github.com/amandine-ch)
- [@Line FOURCHER](https://github.com/LineFourcher)
- [@François GUILLERM](https://github.com/guillermfr)
- [@Valentin SING](https://github.com/ValentinChanter)
