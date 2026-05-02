package com.example.mythmatch

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // ── QUESTIONS ─────────────────────────────────────────────────
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

    val answers = arrayOf(true, false, false, true, false, true, false, false, true, false)

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
    var currentQuestion = 0
    var score = 0
    var userAnswers = arrayOfNulls<Boolean>(10)

    // ── SCREENS ───────────────────────────────────────────────────
    lateinit var screenWelcome:  View
    lateinit var screenQuestion: View
    lateinit var screenScore:    View
    lateinit var screenReview:   View

    // ── QUESTION SCREEN ELEMENTS ──────────────────────────────────
    lateinit var tvScoreLabel:   TextView
    lateinit var tvStatement:    TextView
    lateinit var btnHack:        View
    lateinit var btnMyth:        View
    lateinit var feedbackBox:    View
    lateinit var tvFeedbackText: TextView
    lateinit var btnNext:        View

    // ── SCORE SCREEN ELEMENTS ─────────────────────────────────────
    lateinit var tvBadge:        TextView
    lateinit var tvScoreSub:     TextView
    lateinit var tvCorrectCount: TextView
    lateinit var tvWrongCount:   TextView

    // ── REVIEW SCREEN ELEMENTS ────────────────────────────────────
    lateinit var reviewList: LinearLayout


    // ─────────────────────────────────────────────────────────────────
    // onCreate — runs first when the app opens
    // ─────────────────────────────────────────────────────────────────
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectViews()
        setupWelcomeScreen()
    }


    // ─────────────────────────────────────────────────────────────────
    // connectViews — links variables to XML views
    // ─────────────────────────────────────────────────────────────────
    fun connectViews() {
        screenWelcome  = findViewById(R.id.welcomeScreen)
        screenQuestion = findViewById(R.id.questionScreen)
        screenScore    = findViewById(R.id.screenScore)
        screenReview   = findViewById(R.id.screenReview)

        tvScoreLabel    = findViewById(R.id.tvScoreLabel)
        tvStatement     = findViewById(R.id.tvStatement)
        btnHack         = findViewById(R.id.btnHack)
        btnMyth         = findViewById(R.id.btnMyth)
        feedbackBox     = findViewById(R.id.feedbackBox)
        tvFeedbackText  = findViewById(R.id.tvFeedbackText)
        btnNext         = findViewById(R.id.btnNext)

        tvBadge         = findViewById(R.id.tvBadge)
        tvScoreSub      = findViewById(R.id.tvScoreSub)
        tvCorrectCount  = findViewById(R.id.tvCorrectCount)
        tvWrongCount    = findViewById(R.id.tvWrongCount)

        reviewList      = findViewById(R.id.reviewList)
    }


    // ─────────────────────────────────────────────────────────────────
    // SCREEN 1 — Welcome
    // ─────────────────────────────────────────────────────────────────
    fun setupWelcomeScreen() {

        // Show the welcome screen when the app opens
        showScreen(screenWelcome)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            currentQuestion = 0
            score           = 0
            userAnswers     = arrayOfNulls(10)
            showScreen(screenQuestion)
            loadQuestion()
        }
    }


    // ─────────────────────────────────────────────────────────────────
    // SCREEN 2 — Question
    // ─────────────────────────────────────────────────────────────────
    fun loadQuestion() {
        // Hide feedback and next button at the start of each question
        feedbackBox.visibility = View.GONE
        btnNext.visibility     = View.GONE

        // Re-enable both answer buttons
        btnHack.isClickable = true
        btnMyth.isClickable = true

        // Update score label
        tvScoreLabel.text = "Score: $score"

        // Show the current question
        tvStatement.text = questions[currentQuestion]

        // When user taps HACK
        btnHack.setOnClickListener {
            checkAnswer(true)
        }

        // When user taps MYTH
        btnMyth.setOnClickListener {
            checkAnswer(false)
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


    // ─────────────────────────────────────────────────────────────────
    // checkAnswer — runs when the user taps Hack or Myth
    // ─────────────────────────────────────────────────────────────────
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


    // ─────────────────────────────────────────────────────────────────
    // SCREEN 3 — Score
    // ─────────────────────────────────────────────────────────────────
    fun showScoreScreen() {
        val wrong = questions.size - score
        val percentage = (score * 100) / questions.size

        if (percentage >= 90) {
            tvBadge.text = "Master Hacker! 🏆"
            tvScoreSub.text = "You can spot an urban myth from a mile away."
        } else if (percentage >= 70) {
            tvBadge.text = "Hack Detective! 🔍"
            tvScoreSub.text = "Pretty sharp — a few myths slipped past you."
        } else if (percentage >= 50) {
            tvBadge.text = "Getting There! 💪"
            tvScoreSub.text = "Good effort! Keep practising your instincts."
        } else {
            tvBadge.text = "Stay Safe Online! 🛡️"
            tvScoreSub.text = "Don't worry — now you know what's real!"
        }

        tvCorrectCount.text = score.toString()
        tvWrongCount.text = wrong.toString()

        showScreen(screenScore)

        findViewById<Button>(R.id.btnReview).setOnClickListener {
            showReviewScreen()
        }

        findViewById<Button>(R.id.btnPlayAgain).setOnClickListener {
            currentQuestion = 0
            score = 0
            userAnswers = arrayOfNulls(10)
            showScreen(screenWelcome)
        }
    }

    // SCREEN 4 — Review
    fun showReviewScreen() {
        // Clear old review cards (keep title + back button = 2 children)
        while (reviewList.childCount > 2) {
            reviewList.removeViewAt(1)
        }

        for (i in questions.indices) {
            val isCorrect     = userAnswers[i] == answers[i]
            val correctAnswer = if (answers[i]) "✅ Hack" else "❌ Myth"
            val resultText    = if (isCorrect) "Your answer was correct." else "Your answer was wrong."

            val reviewItem = TextView(this)
            reviewItem.text = "${i + 1}. ${questions[i]}\n$correctAnswer — $resultText\n${explanations[i]}"
            reviewItem.textSize = 12f
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

        findViewById<Button>(R.id.btnBackToScore).setOnClickListener {
            showScoreScreen()
        }
    }

    // showScreen — hides all screens then shows the one we want
    fun showScreen(screenToShow: View) {
        screenWelcome.visibility  = View.GONE
        screenQuestion.visibility = View.GONE
        screenScore.visibility    = View.GONE
        screenReview.visibility   = View.GONE
        screenToShow.visibility   = View.VISIBLE
    }
}