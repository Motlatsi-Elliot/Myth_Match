/**package com.example.mythmatch

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {
        v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left,
            systemBars. Top,
            systemBars. Right,
            systemBars. Bottom)
            insets
        }
    }
}*/

package com.example.mythmatch

import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.*
/*import android.widget.TextView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.LinearLayout
import android.widget.Toast*/
import androidx.appcompat.app.AppCompatActivity

// ─────────────────────────────────────────────────────────────────
// This is the main file that controls everything the app does.
// Think of it as the "brain" of the app.
// ─────────────────────────────────────────────────────────────────

class MainActivity : AppCompatActivity() {

    // ── QUESTIONS ─────────────────────────────────────────────────
    // Each question has 3 parts: the statement, the answer (true/false), and the explanation.
    // true  = it IS a real hack
    // false = it IS a myth

    val questions    = arrayOf(
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

    val answers = arrayOf(true, false, false, true, false, true, false, false, true, false)
    // This matches each question above — true = Hack, false = Myth.

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

    // ── TRACKING VARIABLES ────────────────────────────────────────
    // These keep track of what is happening while the user plays.

    var currentQuestion = 0   // Which question we are on (starts at 0)
    var score = 0   // How many correct answers the user got
    var userAnswers = arrayOfNulls<Boolean>(10) // What the user answered (null = not answered yet)//Null is the absence of a value


    // ── SCREENS ───────────────────────────────────────────────────
    // These are the 4 screens from activity_main.xml

    lateinit var screenWelcome:  View
    lateinit var screenQuestion: View
    lateinit var screenScore:    View
    lateinit var screenReview:   View

    // ── QUESTION SCREEN ELEMENTS ──────────────────────────────────
    lateinit var tvQuestionCount: TextView
    lateinit var progressBar:     ProgressBar
    lateinit var tvScoreLabel:    TextView
    lateinit var tvStatement:     TextView
    lateinit var btnHack:         View
    lateinit var btnMyth:         View
    lateinit var feedbackBox:     View
    lateinit var tvFeedbackIcon:  TextView
    lateinit var tvFeedbackText:  TextView
    lateinit var btnNext:         View

    // ── SCORE SCREEN ELEMENTS ─────────────────────────────────────
    //late-init var scoreRing:      ScoreRingView
    lateinit var tvBadge:        TextView
    lateinit var tvScoreSub:     TextView
    lateinit var tvCorrectCount: TextView
    lateinit var tvWrongCount:   TextView
    lateinit var tvAccuracy:     TextView

    // ── REVIEW SCREEN ELEMENTS ────────────────────────────────────
    lateinit var reviewList: LinearLayout


    // ─────────────────────────────────────────────────────────────────
    // onCreate — this runs FIRST when the app opens
    // ─────────────────────────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the top and bottom bars dark
        window.statusBarColor     = Color.parseColor("#0D0F14")
        window.navigationBarColor = Color.parseColor("#0D0F14")

        // Load the layout from activity_main.xml
        setContentView(R.layout.activity_main)

        // Connect all the variables above to the views in the XML
        ////////////////////
        connectViews()

        // Set up the welcome screen buttons
        setupWelcomeScreen()
    }


    // ─────────────────────────────────────────────────────────────────
    // connectViews — links each variable to its view in the XML


    // Sort it out like the previous assignment
    fun connectViews() {

        // Screens
        screenWelcome  = findViewById(R.id.screenWelcome)
        screenQuestion = findViewById(R.id.screenQuestion)
        screenScore    = findViewById(R.id.screenScore)
        screenReview   = findViewById(R.id.screenReview)

        // Question screen
        tvQuestionCount = findViewById(R.id.tvQuestionCount)
        progressBar     = findViewById(R.id.progressBar)
        tvScoreLabel    = findViewById(R.id.tvScoreLabel)
        tvStatement     = findViewById(R.id.tvStatement)
        btnHack         = findViewById(R.id.btnHack)
        btnMyth         = findViewById(R.id.btnMyth)
        feedbackBox     = findViewById(R.id.feedbackBox)
        tvFeedbackIcon  = findViewById(R.id.tvFeedbackIcon)
        tvFeedbackText  = findViewById(R.id.tvFeedbackText)
        btnNext         = findViewById(R.id.btnNext)

        // Score screen
        //scoreRing      = findViewById(R.id.scoreRing)
        tvBadge        = findViewById(R.id.tvBadge)
        tvScoreSub     = findViewById(R.id.tvScoreSub)
        tvCorrectCount = findViewById(R.id.tvCorrectCount)
        tvWrongCount   = findViewById(R.id.tvWrongCount)
        tvAccuracy     = findViewById(R.id.tvAccuracy)

        // Review screen
        reviewList = findViewById(R.id.reviewList)
    }


    // ─────────────────────────────────────────────────────────────────
    // SCREEN 1 — Welcome
    // ─────────────────────────────────────────────────────────────────
    fun setupWelcomeScreen() {
      val btnStart = findViewById<Button>(R.id.btnStart)

        // When the user taps START
        btnStart.setOnClickListener {
            currentQuestion = 0      // Go back to the first question
            score = 0      // Reset the score
            userAnswers = arrayOfNulls(10) // Clear all previous answers

            showScreen(screenQuestion) // Switch to the question screen
            loadQuestion()             // Load the first question
        }

        // When the user taps HOW TO PLAY
       /* findViewById<Button>(R.id.btnHowToPlay).setOnClickListener {
            Toast.makeText(this, "Read each statement. Is it a real Hack ✅ or an Urban Myth ❌?", Toast.LENGTH_LONG).show()
        }*/
    }


    // ─────────────────────────────────────────────────────────────────
    // SCREEN 2 — Question
    // ─────────────────────────────────────────────────────────────────
    fun loadQuestion() {

        // Hide the feedback and next button at the start of each question
        feedbackBox.visibility = View.GONE
        btnNext.visibility = View.GONE

        // Make both answer buttons clickable again
        btnHack.isClickable = true
        btnMyth.isClickable = true

        // Update the counter text e.g. "3 / 10"
        tvQuestionCount.text = "${currentQuestion + 1} / ${questions.size}"

        // Move the progress bar forward
       // progressBar.progress = currentQuestion + 1

        // Update the score label
        tvScoreLabel.text = "Score: $score"

        // Show the current question on the flashcard
        tvStatement.text = questions[currentQuestion]

        // ── When the user taps HACK ──
        btnHack.setOnClickListener {
            checkAnswer(true) // User chose Hack (true)
        }

        // ── When the user taps MYTH ──
        btnMyth.setOnClickListener {
            checkAnswer(false) // User chose Myth (false)
        }

        // ── When the user taps NEXT ──
        btnNext.setOnClickListener {
            currentQuestion++ // Move to the next question

            // If we have gone past the last question, show the score screen
            if (currentQuestion >= questions.size) {
                showScoreScreen()
            } else {
                loadQuestion() // Otherwise load the next question
            }
        }
    }


    // ─────────────────────────────────────────────────────────────────
    // checkAnswer — runs when the user taps Hack or Myth
    // ─────────────────────────────────────────────────────────────────
    fun checkAnswer(userChoseHack: Boolean) {

        // Save what the user answered for this question
        userAnswers[currentQuestion] = userChoseHack

        // Check if the answer is correct
        val isCorrect = userChoseHack == answers[currentQuestion]

        // If correct, add 1 to the score
        if (isCorrect) {
            score++
        }

        // Update the score label immediately
        tvScoreLabel.text = "Score: $score"

        // Show the correct or wrong feedback
        if (isCorrect) {
            //tvFeedbackIcon.text = "✅"
            tvFeedbackText.setTextColor(Color.parseColor("#3DE4A4")) // Green
            tvFeedbackText.text = "Correct! " + explanations[currentQuestion]
            feedbackBox.setBackgroundResource(R.drawable.bg_feedback)
        } else {
            tvFeedbackIcon.text = "❌"
            tvFeedbackText.setTextColor(Color.parseColor("#FF5E6C")) // Red
            tvFeedbackText.text = "Wrong! " + explanations[currentQuestion]
            feedbackBox.setBackgroundResource(R.drawable.bg_feedback_wrong)
        }

        // Show the feedback box and the Next button
        feedbackBox.visibility = View.VISIBLE
        btnNext.visibility     = View.VISIBLE

        // Disable both buttons so the user cannot change their answer
        btnHack.isClickable = false
        btnMyth.isClickable = false
    }


    // ─────────────────────────────────────────────────────────────────
    // SCREEN 3 — Score
    // ─────────────────────────────────────────────────────────────────
    fun showScoreScreen() {

        val wrong      = questions.size - score           // Number of wrong answers
        val percentage = (score * 100) / questions.size   // Score as a percentage

        // Pick a badge title based on the percentage
        if (percentage >= 90) {
            tvBadge.text    = "Master Hacker! 🏆"
            tvScoreSub.text = "You can spot an urban myth from a mile away."
        } else if (percentage >= 70) {
            tvBadge.text    = "Hack Detective! 🔍"
            tvScoreSub.text = "Pretty sharp — a few myths slipped past you."
        } else if (percentage >= 50) {
            tvBadge.text    = "Getting There! 💪"
            tvScoreSub.text = "Good effort! Keep practising your instincts."
        } else {
            tvBadge.text    = "Stay Safe Online! 🛡️"
            tvScoreSub.text = "Don't worry — now you know what's real!"
        }

        // Fill in the stat boxes
        tvCorrectCount.text = score.toString()
        tvWrongCount.text   = wrong.toString()
        tvAccuracy.text     = "$percentage%"

        // Tell the score ring what score to draw
        //scoreRing.setScore(score, questions.size)

        // Switch to the score screen
        showScreen(screenScore)

        // When the user taps REVIEW
        findViewById<Button>(R.id.btnReview).setOnClickListener {
            showReviewScreen()
        }

        // When the user taps PLAY AGAIN
        findViewById<Button>(R.id.btnPlayAgain).setOnClickListener {
            currentQuestion = 0
            score           = 0
            userAnswers     = arrayOfNulls(10)
            showScreen(screenWelcome)
        }
    }


    // ─────────────────────────────────────────────────────────────────
    // SCREEN 4 — Review
    // ─────────────────────────────────────────────────────────────────
    fun showReviewScreen() {

        // Remove any review cards from a previous game (keep the title + back button)
        while (reviewList.childCount > 2) {
            reviewList.removeViewAt(1)
        }

        // Loop through all 10 questions and build a card for each one
        for (i in questions.indices) {

            val isCorrect = userAnswers[i] == answers[i]
            val correctAnswer = if (answers[i]) "✅ Hack" else "❌ Myth"

            // Build the text to show in the review item
            val resultText = if (isCorrect) "Your answer was correct." else "Your answer was wrong."

            // Create a simple TextView that shows all the info
            val reviewItem = TextView(this)
            reviewItem.text      = "${i + 1}. ${questions[i]}\n$correctAnswer — $resultText\n${explanations[i]}"
            reviewItem.textSize  = 12f
            reviewItem.setTextColor(Color.parseColor("#EEF0F6"))
            reviewItem.setBackgroundResource(R.drawable.bg_review_item)
            reviewItem.setPadding(32, 28, 32, 28)
            /*reviewItem.lineSpacingMultiplier = 1.5f*/

            // Space between cards
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.bottomMargin = 20
            reviewItem.layoutParams = params

            // Add the card just before the Back button
            reviewList.addView(reviewItem, reviewList.childCount - 1)
        }

        // Switch to the review screen
        showScreen(screenReview)

        // When the user taps BACK TO SCORE
        findViewById<Button>(R.id.btnBackToScore).setOnClickListener {
            showScoreScreen()
        }
    }


    // ─────────────────────────────────────────────────────────────────
    // showScreen — hides all screens and shows only the one we want
    // ─────────────────────────────────────────────────────────────────
    fun showScreen(screenToShow: View) {
        screenWelcome.visibility  = View.GONE
        screenQuestion.visibility = View.GONE
        screenScore.visibility    = View.GONE
        screenReview.visibility   = View.GONE

        // Now show only the one we want
        screenToShow.visibility = View.VISIBLE
    }
}


// ─────────────────────────────────────────────────────────────────
// ScoreRingView — draws the circular score ring on the score screen
// ─────────────────────────────────────────────────────────────────
/*class ScoreRingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    var score = 0
    var total = 10

  // The grey background ring
 val ringBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      style       = Paint.Style.STROKE
      strokeWidth = 20f
      color       = Color.parseColor("#1C2130")
  }

 // The yellow filled arc (score progress)
  val ringProgress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      style       = Paint.Style.STROKE
      strokeWidth = 20f
      strokeCap   = Paint.Cap.ROUND
      color       = Color.parseColor("#F0C040")
  }

 /* // The dark inner circle (creates the donut hole)
  val innerCircle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      style = Paint.Style.FILL
      color = Color.parseColor("#0D0F14")
  }*/

  // The score text in the middle
  val scoreText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
      textAlign       = Paint.Align.CENTER
      textSize        = 52f
      isFakeBoldText  = true
      color           = Color.parseColor("#EEF0F6")
  }

  // Called from MainActivity to update the score and redraw
  fun setScore(s: Int, t: Int) {
      score = s
      total = t
      invalidate() // Tells Android to redraw this view
  }

  // Android calls this automatically to draw the view
  /*override fun onDraw(canvas: Canvas) {

      val centerX = width / 2f
      val centerY = height / 2f
      val radius  = minOf(centerX, centerY) - 24f

      // The bounding box for the arc
      val oval = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

      // Draw the grey background ring
      canvas.drawCircle(centerX, centerY, radius, ringBackground)

      // Calculate how much of the ring to fill based on the score
      val sweepAngle = (score.toFloat() / total.toFloat()) * 360f

      // Draw the yellow progress arc (starts from the top = -90 degrees)
      canvas.drawArc(oval, -90f, sweepAngle, false, ringProgress)

      // Draw the dark inner circle to create the donut hole effect
      canvas.drawCircle(centerX, centerY, radius - 24f, innerCircle)

      // Draw the score text in the center
      val textY = centerY - (scoreText.descent() + scoreText.ascent()) / 2f
      canvas.drawText("$score/$total", centerX, textY, scoreText)
  }*/
}*/