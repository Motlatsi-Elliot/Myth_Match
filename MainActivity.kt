package com.example.mythmatch

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

//Questions: Stored 10 elements/questions in the array with a variable name questions
    val questions = arrayOf(
        "Drinking cold water after a meal speeds up your metabolism.",
        "You only use 10% of your brain at a time.",
        "Reading in dim light permanently damages your eyesight.",
        "Chewing gum boosts focus and short-term memory.",
        "Swallowed gum stays in your stomach for 7 years.",
        "Eating breakfast kickstarts your metabolism for the day.",
        "Lightning never strikes the same place twice.",
        "Sleeping with wet hair causes you to catch a cold.",
        "Listening to music while studying improves retention.",
        "Cracking your knuckles leads to arthritis."
    )

    //Answers: Stored 10 elements/answers in the array with a variable name answers,
    // the array contains boolean values which corresponds to the questions.
    val answers = arrayOf(true, false, false, true, false, true, false, false, true, false)

    //explanations: Stored 10 elements/explanations in the array with a variable name explanations,
    //the array also contains string values which corresponds to the questions.
    val explanations = arrayOf(
        "Cold water causes a small but real metabolic boost.",
        "Myth — virtually all brain regions are active daily.",
        "It causes strain but no lasting damage.",
        "Studies confirm improved concentration while chewing.",
        "Myth — it passes through your system normally.",
        "A morning meal activates thermogenesis.",
        "Myth — tall structures are struck repeatedly.",
        "Colds are caused by viruses, not wet hair.",
        "Instrumental music can improve focus and memory.",
        "No proven link between knuckle-cracking and arthritis."
    )

    var currentQuestion = 0 // We set the current question to 0 since we start from the first question(index 1)
    var score = 0 // We also set the score to 0 since we start with zero score
    var userAnswers = arrayOfNulls<Boolean>(10) // An array that will store all the user's answers

    // here we declare the variables for the screens
    //lateinit is used to declare variables that will be initialized later
    lateinit var screenWelcome:View
    lateinit var screenQuestion:View
    lateinit var screenScore:View
    lateinit var screenReview:View
    lateinit var tvScoreLabel:TextView
    lateinit var tvStatement:TextView
    lateinit var btnHack:View
    lateinit var btnMyth:View
    lateinit var feedbackBox:View
    lateinit var tvFeedbackText:TextView
    lateinit var btnNext:View
    lateinit var tvBadge:TextView
    lateinit var tvScoreSub:TextView
    lateinit var tvCorrectCount:TextView
    lateinit var tvWrongCount:TextView
    lateinit var reviewList:LinearLayout

//onCreate is run first when the app is opened
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectViews()//Calling the connectViews function to link variables to XML views
        setupWelcomeScreen() //Calling the setupWelcomeScreen function to set up the welcome screen
    }

    // connectView is functions that links variables to XML views
    fun connectViews() {
        screenWelcome  = findViewById(R.id.welcomeScreen)
        screenQuestion = findViewById(R.id.questionScreen)
        screenScore= findViewById(R.id.screenScore)
        screenReview= findViewById(R.id.screenReview)
        tvScoreLabel= findViewById(R.id.tvScoreLabel)
        tvStatement= findViewById(R.id.tvStatement)
        btnHack= findViewById(R.id.btnHack)
        btnMyth= findViewById(R.id.btnMyth)
        feedbackBox= findViewById(R.id.feedbackBox)
        tvFeedbackText= findViewById(R.id.tvFeedbackText)
        btnNext= findViewById(R.id.btnNext)
        tvBadge= findViewById(R.id.tvBadge)
        tvScoreSub= findViewById(R.id.tvScoreSub)
        tvCorrectCount= findViewById(R.id.tvCorrectCount)
        tvWrongCount= findViewById(R.id.tvWrongCount)
        reviewList= findViewById(R.id.reviewList)
    }

    // SCREEN 1 — Welcome
    fun setupWelcomeScreen() {
        // Show the welcome screen when the app opens
        showScreen(screenWelcome)
        val btnStart = findViewById<Button>(R.id.btnStart)
        btnStart.setOnClickListener {
            currentQuestion= 0
            score= 0
            userAnswers= arrayOfNulls(10)
            showScreen(screenQuestion)//calling the showScreen function to show the question screen
                                                    //All other screens are hidden(visbility set to gone)
            loadQuestion()//calling the loadQuestion function to load the first question
        }
    }

    // SCREEN 2 — Question
    fun loadQuestion() {
        // Hide feedback and next button at the start of each question
        feedbackBox.visibility = View.GONE //Hide the feedback box when that screen is shown
        btnNext.visibility = View.GONE //Hide the next button when that screen is shown

        // Re-enable both answer buttons
        btnHack.isClickable = true // makes the buttons clickable again,then we set it to false later to prevent multiple clicks
        btnMyth.isClickable = true

        // Update score label
        tvScoreLabel.text = "Score: $score" // Update the score label with the current score

        // Show the current question
        tvStatement.text = questions[currentQuestion] // Update the statement text with the current question

        // When user taps HACK
        btnHack.setOnClickListener {
            checkAnswer(true) //checkAnswer function is called to check the answer, with a parameter of true
        }

        // When user taps MYTH
        btnMyth.setOnClickListener {
            checkAnswer(false) //checkAnswer function is called to check the answer, with a parameter of false
        }

        // When user taps NEXT
        btnNext.setOnClickListener {
            currentQuestion++
            if (currentQuestion >= questions.size) {
                showScoreScreen()
            } else {
                loadQuestion()
            }
        }
    }

    // checkAnswer — runs when the user taps Hack or Myth
    fun checkAnswer(userChoseHack: Boolean) {
        userAnswers[currentQuestion] = userChoseHack

        val isCorrect = userChoseHack == answers[currentQuestion]

        if (isCorrect) {
            score++
        }

        tvScoreLabel.text = "Score: $score"

        if (isCorrect) {
            tvFeedbackText.setTextColor(Color.parseColor("#3DE4A4"))
            tvFeedbackText.text = "✅ Correct! " + explanations[currentQuestion]
            feedbackBox.setBackgroundResource(R.drawable.bg_feedback)
        } else {
            tvFeedbackText.setTextColor(Color.parseColor("#FF5E6C"))
            tvFeedbackText.text = "❌ Wrong! " + explanations[currentQuestion]
            feedbackBox.setBackgroundResource(R.drawable.bg_feedback_wrong)
        }

        feedbackBox.visibility = View.VISIBLE
        btnNext.visibility = View.VISIBLE
        btnHack.isClickable = false
        btnMyth.isClickable = false
    }

    // SCREEN 3 — Score
    fun showScoreScreen() {
        val wrong = questions.size - score//record the number of wrong answers by subtracting the score from the total number of questions
        val percentage = (score * 100) / questions.size

        if (percentage >= 90) {
            tvBadge.text = "Master Hacker! 🏆"
            tvScoreSub.text = "You can spot an urban myth from a mile away."
        } else if (percentage >= 70) {
            tvBadge.text = "Hack Detective! 🔍"
            tvScoreSub.text = "Pretty sharp - Just a few steps to being  a Hacker"
        } else if (percentage >= 50) {
            tvBadge.text = "Getting There! 💪"
            tvScoreSub.text = "You are getting there"
        } else {
            tvBadge.text = "Stay Safe Online! 🛡️"
            tvScoreSub.text = "Don't worry — now you know what's real!"
        }

        tvCorrectCount.text = score.toString() // Set the number of correct answers
        tvWrongCount.text = wrong.toString() //set the number of wrong answers

        showScreen(screenScore)//show the score screen

        val btnReview = findViewById<Button>(R.id.btnReview)
            btnReview.setOnClickListener {
            showReviewScreen()
        }


        val btnPlayAgain = findViewById<Button>(R.id.btnPlayAgain)
            btnPlayAgain.setOnClickListener {
            currentQuestion = 0
            score = 0
            userAnswers = arrayOfNulls(10)
            showScreen(screenWelcome)
        }
    }

    // SCREEN 4 — Review
    fun showReviewScreen() {
        // Clear old review cards (keep title + back button = 2 children)
        while (reviewList.childCount > 2) {//while the reviewList has more than 2 children, remove the last child
            reviewList.removeViewAt(1)
        }

        for (i in questions.indices) {//indices used to get the index of the array
            val isCorrect= userAnswers[i] == answers[i]
            //using if statement as an expression to assign a value to a variable
            val correctAnswer = if (answers[i]) "✅ Hack" else "❌ Myth"
            val resultText= if (isCorrect) "Your answer was correct." else "Your answer was wrong."

            // We create a new TextView for each review item
            val reviewItem = TextView(this)
            reviewItem.text = "${i + 1}. ${questions[i]}\n$correctAnswer — $resultText\n${explanations[i]}"
            reviewItem.textSize = 15f
            reviewItem.setTextColor(Color.parseColor("#EEF0F6"))
            reviewItem.setBackgroundResource(R.drawable.bg_review_item)
            reviewItem.setPadding(32, 28, 32, 28)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 20
            reviewItem.layoutParams = params

            // Insert before the Back button (last child)
            reviewList.addView(reviewItem, reviewList.childCount - 1)
        }

        showScreen(screenReview)

        val btnBackToScore = findViewById<Button>(R.id.btnBackToScore)
            btnBackToScore.setOnClickListener {
            showScoreScreen()
        }
    }

    // showScreen — hides all screens then shows the one we want
    fun showScreen(screenToShow: View) {
        screenWelcome.visibility= View.GONE
        screenQuestion.visibility = View.GONE
        screenScore.visibility= View.GONE
        screenReview.visibility= View.GONE
        screenToShow.visibility= View.VISIBLE
    }
}
