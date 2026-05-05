# MythMatch 🔍
## Life Hack or Urban Myth?Android Flashcard Quiz App
The following link contains a video of a full demonstration of how the app runs on the emulator and explanation of the code on: 
https://youtu.be/ci4Pakuv9zw

<img width="1919" height="1070" alt="Screenshot 2026-05-02 123412" src="https://github.com/user-attachments/assets/652cd0b8-9c50-4cc6-b90e-091effd6f944" />

# What is MythMatch❔
MythMatch is a Native android app designed to dertemine if a given statement is a Life Hack or just an Urban Myth, It does this using the flashcards that contains differents statements that will either be a life hack or a Myth

# How the App Works
The app has 4 Screens That the user will move throuh as they use the App

### SCREEN 1 - WELCOME SCREEN

<img width="200" height="315" alt="Media Player 5_3_2026 5_20_24 PM" src="https://github.com/user-attachments/assets/7ea2d050-5888-4478-bdc9-cea3d55c0cc4" />


: The first screen the user will see as soon as they open the app

:It has a sub title and a short description and a start box showing how many questions there are

: Also has a START QUIZ  button that will direct the user to the next sreen which is the FLASHCARD QUESTION SCREEN

### SCREEN 2 - FLASHCARD QUESTION SCREEN

<img width="200" height="300" alt="Media Player 5_3_2026 4_51_37 PM" src="https://github.com/user-attachments/assets/5f7b7aa8-9779-42f3-8ca1-b4db8f2b3639" />


:Here is the screen were by different kinds of statements will be provided in which the user will have to dertermine if they are just a Myth or Life hack

:It has the flashcard were by the statements will appear and keep changing as the user click the next button

:Has 2 buttons that the user click to answer if the statement is a HACK ✅ or Myth❌

:After answering, a feedback box appears telling the user if they were correct and explaining why.

:A NEXT QUESTION button to proceed to the next screen

### SCREEN 3 - SCORE SCREEN

<img width="200" height="300" alt="Media Player 5_3_2026 5_21_26 PM" src="https://github.com/user-attachments/assets/c3a4eae3-1cde-4fc7-8d03-b625d331c9c1" />


:This screen will appear automatically after the 10th question has been answered

: It will show how many answers were correct and how many were wrong

:Displays a personalised badge based on the score (Master hacker - Hack Detective - Getting there - Stay Safe)

:Has a Review Answers button that will direct the user to the next sreen and a Play Again button that will go the welcome screen

### SCREEN 4 - REVIEW SCREEN

<img width="200" height="300" alt="Media Player 5_3_2026 5_21_48 PM" src="https://github.com/user-attachments/assets/b4f71656-cab6-4db2-bd74-9824e32bee4d" />


:Shows all 10 questions with the correct answers and a short explanation for each

:The BACK TO SCORE button that  will go back to the score screen

# Project Structure

### I have used funtions to make my code reusable and to make the code more easy to use ,for example, instead of writing the code of showScreen function again and again if I needed to show a certain screen, I just call that fuction rather than wiring that code several times.


  1. #### Tracking Variable
     var currentQuestions = 0
     
     var score = 0
     
     var userAnswers = arrayOfNulls<Booleans>(10)

  2.  #### connectViews()
      links the xml with the kt file
     
      screenWelcome = findViewById(R.id,welcomeScreen)

  3.  #### showScreen()
      A function that hides all 4 screens and then makes only the requested one visible.

  4. #### loadQuestion()
     
     Runs once per question,Works like a loop as it keeps loading questions as long as the condition is false in this case as long as questions are less that 10

  6. #### checkAnswer()
     
     This function compares the user's response while they clicked the 2 buttons, the Hyth and Hack button with the correct answer,If ever the thier answer is correct,the score increases by one hence the use of       the  increment score++ which is the same as score = score + 1, From there the feedbackbox will appear with either the green background (correct answer) or the red background(wrong answer)

  6.showScoreScreen()
  
   This calculates the percentage score and picks the correct answer badge and subtitle using the IF/ELSE. It updates the correct and wrong stat boxes tthen switches to the score screen

  7.showReviewScreen()

  Loops through all 10 questions using a for loop and builds a Textview card for each one dynamically in Kotlin  code.Each of the card will show the question and the correct answer and whether the user was         correct or Wrong and the explanation.



   


   
   
