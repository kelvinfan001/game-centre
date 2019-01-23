
# Game Centre

A Game Centre that contains several simple games.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You will need to have the following software:

```
Android Studio
Android 8.1 API 27
```

### Installing

These instructions should help you get a development env running on Android Studio.

Clone from the Game Centre repository;

```
https://markus.teach.cs.toronto.edu/git/csc207-2018-09-reg/group_0601
```

Enter the correct directory;

```
cd group_0601/Phase2/
```

Open the project named *GameCentre* with Android Studio.

Run *app* to launch the Game Centre application in an emulator. Game Centre should be able to run on most Android emulators running Android 8.1.

Install Build Tools if necessary, and sync with project. 

Wait patiently for Gradle to build. This may take a while.

You may need to clean project if errors occur. 

## Features of Phase 1
We have implemented all the required field, such as:
GameCentre itself
Sign up/Sign in
Save/Load
Scoreboard
AutoSave
Undo
Multiple complexities

We also implemented the bonuses including:
Unlimited undos
Change background of tiles
Extra feature: Change the theme of the application

## Features of Phase 2
Made sliding tiles always solvable
Pong Game
Cold War Game
Unit Tests

## Code Tour of Sliding Tiles
* **Class Board:** It keeps track of Tile objects in a 2D array. The constructor is given a 1D List of Tiles,
 and these are used to populate the 2D array. This class is also Observable, which means that other classes 
 can sign up to be alerted when the contents change. This happens, as you'll see, when swapTiles is called. 
 The call to notifyAll ends up letting the GameActivity know that the user interface needs to be updated.
 
* **Class SlidingTilesManager:** SlidingTilesManager manipulates the Board, figuring out whether a tap is legal, checking 
whether the puzzle has been solved, performing a move the user has made, undoing a previous move, and 
calculating the user's score using the number of moves and time taken.

* **Class SlidingTilesComplexity Activity:** This screen allows a user to choose between the provided 3x3, 4x4, 5x5 game 
modes for the number tiles puzzle game as well as allowing the user to change the number of steps they wish 
to undo when they are playing the game(Bonus). A user may also choose to download an image and slide
images instead of sliding number tiles.

* **Class SlidingTilesGameInfo:** This class keeps track of the state of the board, the board complexity, the user's
score, as well as making sure the board is always solvable. 

* **Class GameActivity:** This is the main sliding tile puzzle. It uses a SlidingTilesInfo to keep track of the 
board, and also manages all the user interface components. In it is also an undo button that allows
the user to return to their previous position.

* **Class SlidingTilesFilerSaverModel:** This class is responsible for keeping a record of all users' game states by saving
to a file should a user decide to save their game. A user may choose to load a previous saved game at a later time.

* **Class SlidingTilesGameController:** This class contains all the logic involving processing a user's 
touch for the game.

* **Class DownloadingActivity:** This screen allows the user copy and paste an URL link to download an images as the tiles 
background when playing the sliding tiles game.(Bonus)  

* **Class StartingActivity:** This screen is where users can start a new game or load a previously saved game.

## Code Tour of Cold War
* **Class Agent** The abstract parent of all pieces in the game. Its subclasses include the 2 bases, Spy, and Diplomat.

* **Class ColdWarGameController** The controller under the MVC model for the Cold War game. It manages inputs from the user
and manipulates and reads from the model if necessary. 

* **Class ColdWarGameInfo** The model under the MVC model for the Cold War game. It contains all relevant 
information regarding the current state of the game, including the location of each piece, the number of 
remaining pieces owned by each owner, etc.

* **Class ColdWarMainActivity** The main view under the MVC model for the Cold War game. It contains what
is shown to users.

* **Class GameOverUtility** A utility class used by the controller for tasks related to game over handling.

* **Class MovementUtility** A utilities package for handling move-making.

* **Class TurnManagementUtility** A utilities package for handling tasks related to beginning, executing, and ending turns.

## Code Tour of Pong
* **Class Ball:** Model for the Ball in the Pong Game. It keeps track of the position of the ball, size of the ball,
speed of the ball and the movement of the ball (i.e how much does the ball move when the speed is X)

* **Class Racket:** Model for the Racket in the Pong Game. It keeps track of the position of the racket, size of the racket,
speed of the racket and the movement of the ball (i.e when you tap left how much does the racket move)

* **Class PongGameController:** Controller for Pong. Contains the logic behind the pong game.
i.e what happens when the ball bounces on the racket, when it hits the bottom screen, whether it's paused/playing, etc.

* **Class PongStartingActivity:** The main menu for pong. Displays the ScoreBoard, Load Game and New Game button
for the user to click.

* **Class PongSurfaceView:** The view for Pong. It extends SurfaceView and implements runnable to allow for
animations. It draws the game on the screen and also as a run function that constantly runs and updates game.

* **Class PongGameInfo:** Contains all the info for a game session. It contains their current lives, score
speed of the ball, user playing, etc.

* **Class SerializableRectF:** This is just a class that is basically a copy of the built in RectF android
class however we made it Serializable because so that we could save the position of the ball and racket when
loading the game.

* **Class PongGameActivity:** This is the activity that is called when you click new game button from the PongStartingActivity.
It sets up the View, gameInfo, Save Button, etc.

* **Class PongGameScoreBoardActivity:**  This is the activity that is called when you click the scoreboard button from
PongStartingActivity. It basically sets up the scoreboard and displays all the global/local scores using
PongGameScoreBoardManager.

* **Class PongGameScoreBoardManager:** This is the logic behind displaying the scores on PongGameScoreBoardActivity.
It sets the text values (displays the score) in the PongGameScoreBoardActivity and also the logic in obtaining the High Scores
from the list of scores that we saved.

* **Class PongFileSaverModel:** This contains all the logic on saving the score to the scoreboard when the game is over.
It also loads the scoreboard when there is an existing scoreboard.

## Code Tour of GeneralClasses

* **Class GameSelectionActivity:** This screen allows the user to pick which game they wish to play.

* **Class MenuActivity:** This screen allows the user to logout, configure their settings, or proceed 
to choosing a game to play.

* **Class LoginActivity:** This is the interface where users have to sign in into their accounts before 
getting access to the menu selection screen. New users are required to sign up first.

* **Class ScoreBoardActivity:** This screen allows the user to display the global rankings which 
shows the ranking of the top players of the game at all time with their username, top score and ranking.
The user can also choose to display local rankings which shows their personal achievements.

## UnitTests 
We ask that you edit your configurations before running our unit tests.
Please go to run, edit configurations, choose "fall2018.csc2017 in app" under Android Junit and go to 
the Code coverage tab. Then click on + to add the classes we listed below. Uncheck the "Include/Exclude"
to successfully exclude classes. Finally right-click on "fall2018.csc2017" and select "Run Tests in fall2018.csc2017 with Coverage"

## Classes Excluded from Unit Testing
For slidingtiles: CustomAdapter, DownloadingActivity, GameActivity, GestureDetectGridView,
SlidingTilesComplexityActivity, SlidingTilesFileSaverModel, SlidingTilesScoreBoardActivity, StartingActivity.

For pong: PongFileSaverModel, PongGameActivity, PongGameInfo, PongGameScoreBoards, PongScoreBoardActivity
PongStartingActivity, PongSurfaceView, SerializableRectF, PongScoreBoardManager

For coldwar: ColdWarMainActivity, ColdWarMenu, ColdWarSaverModel, ColdWarScoreBoardActivity, GuestPiecesSelectionActivity,
ImageAdapterGridView, PiecesSelectionActivity, UserPiecesSelection Activity

## Some Details for GameCenter and SlidingTiles.
Most of the things we have implemented are straight forward to test.
In order to test Sign up/Sign in, please enter a username and password, and press the signup button if this is your first time launching the app. Press login to access the starting menu once you have done so with the same username and password.

The multiple complexities can be tested by following the prompt from the app.
There is also an option of "Image" in complexity.
Paste https://developer.android.com/_static/0d76052693/images/android/touchicon-180.png?hl=ja to the URL prompt; this will enable you to use the android logo as the background to the sliding tile game. Other URLs of images from the internet will work as well (uploading local files is still under development).

During the game, you will be able to undo unlimited times and save the game.
This saved data can be loaded by selecting the save desired on the StartingActivityPage (the page after selecting complexity). The saves are named based on the date and time at which they were saved. The game is automatically saved after every move and saved under the name "autosave".

After the game is completed, please go to the score board and check if the local ranking is updated or not, then the global ranking. (Must click on either button to display).

Lastly, in order to change the theme of the app, please go to the settings menu from the main menu, and pick any theme you like from there. This changes the background of all the screens except for the actual game.

Thank you

## Warnings
In order to test these codes properly, please use the drawable file from our submission. The themes are kept there, hence we need this file instead of the general ones.

## Authors

* **Kelvin Yao Fan**
* **Yuwa Yokohama**
* **Kevin Alitirto Lie**
* **Kailong Huang**
* **Stephen Utama**

## License

This project is licensed under the MIT License.

## Acknowledgments

* Special thanks to Suguru Seo for briefly being in our Phase 1 group for about 24 hours.
** Special special thanks to Stephen Utama for coming to the rescue for Phase 2.
