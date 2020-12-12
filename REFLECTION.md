# Reflection

Add to this file to satisfy the "Reflection Updates" non-functional requirement
for this project. Please keep this document organized using Markdown. If you
click on this file in your team's GitHub repository website, then you will see
that the Markdown is transformed into nice looking HTML.


## MON 2019-11-30 @ 2:26 PM EST

1. **DONE:** Downloaded the starter code and decided which game to implement. Idea is to create
    a version of Reversi by using a tilepane to hold image views that will represent each square
    of the board. Update the images based on the user's move.

2. **TODO:** Begin setting up the scene that I will use to represent the game and play around with
    the shapes and keyboard features in Java.

3. **BRANCHING:** Will create branches for each new addition I make for the game. For example, I will
    create a branch while creating the layout of the scene. I will also create a new branch while creating
    the score feature, or keyboard commands for the users to make moves.

## THR 2019-12-3 @ 4:13 PM EST

1. **DONE:** Created basic outline of the gameboard. Designed my images for the game pieces and retrieved
    the url for the image after posting them on facebook. The title scene is complete, however I will
    likely make more modifications to it to make it look cleaner.

2. **TODO:** Begin working on the mouse events and create the actual game loop. Need to figure out how I want
    to implement the mouse events. I do not think I will use keyboard events to implement this game.

3. **BRANCHING:** Created the braches titlewindow and gamewindow that I used when I was designing the title
    and game windows. I merged titlewindow back to master after I was finished with the basic desgin for
    the title window of the game. Merged gamewindow back to the master branch when I completed the design for
    the game window.

## TUES 2019-12-8 @ 11:24 AM EST

1. **DONE:** Worked on the methods that are involved in flipping the tiles for each player's turn and placing
    the tiles on the board by implementing mouse events. Fixed bugs throughout the process of creating methods that
    were successful. The game is almost fully functional, except I have not created the criteria for winning or
    the actual game loop yet.

2. **TODO:** Create the actual game loop and create the criteria for winning. Figure out how I want to display
    a wine message and end the game. Work out the final kinks in the code and continue testing and looking
    for minor bugs that may be present.

3. **BRANCHING:** Created the branch called tileflipping where I worked on creating mouse events and the logic
    behind the flipping of the tiles. Added three methods to my code to implement these processes and then
    merged it back to the master branch.

4. **ISSUES** I encountered some difficulties while trying to implement the mouse events that I used for the
    players to add their peieces to the board. It took me a while to look through all of the API Documentation
    to figure out exactly how the mouse events functioned and which code I should be using. The logic behind
    the actual game loop is still a bit confusing to me because I have never use a game loop within a GUI
    application before. I am still trying to figure out which methods I need to put inside the loop and which I
    can leave out.

## SAT 2019-12-12 @ 4:50 PM EST

1. **DONE:** Wrote a method to determine when the game should end. Created the alerts to give the user the instructions
    for the game. Edited the code the flip the tiles to fix a minor bug that I found. Added the win messages when the game
    ends. Made sure the code passed checkstyle. Tested the game in a variety of different cases.

2. **ISSUE FIXES** Discovered that I did not actually need a game loop to end the game. Instead I created a method that
    checked if the player has any valid moves at any location on the board, and if they did not, I switched to the win
    scene and printed the win message for whichever player's score was higher. None of my methods needed to be inside
    a game loop in order for it to work, so that solved my issue that I was previously having in the reflection entry above.
    I encountered a couple of other minor bugs while I was testing my game, but I was able to work them out.

3. **BRANCHING:** Created a branch called userInstructions that I used when I was creating the alert that I would present to
    the user to explain the directions of the game. I then merged this branch back to master and made a couple of final
    changes to my code and commited the code on the master branch.

4. **EXTRA CREDIT** I did not include any of the extra credit assignments in my project.