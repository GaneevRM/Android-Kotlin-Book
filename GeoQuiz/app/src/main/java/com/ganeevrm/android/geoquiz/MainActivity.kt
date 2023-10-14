package com.ganeevrm.android.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.ganeevrm.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex


        binding.trueButton.setOnClickListener {
            checkAnswer(true)
            updateQuestion()
        }

        binding.falseButton.setOnClickListener {
            checkAnswer(false)
            updateQuestion()
        }

        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            checkResult()
        }

        binding.prevButton.setOnClickListener{
            quizViewModel.moveToPrevious()
            updateQuestion()
            checkResult()
        }

        val resultCheatActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                quizViewModel.currentQuestion.cheatAnswer = it.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
                quizViewModel.cheatsAttempts -= 1
            }
        }

        binding.cheatButton.setOnClickListener { view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this,answerIsTrue)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val options = ActivityOptionsCompat.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                resultCheatActivity.launch(intent, options)
            } else {
                resultCheatActivity.launch(intent)
            }
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, quizViewModel.currentIndex)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        with(binding){
            questionTextView.setText(questionTextResId)
            if(quizViewModel.currentQuestionUserAnswer!=null){
                trueButton.isEnabled = false
                falseButton.isEnabled = false
            } else {
                trueButton.isEnabled = true
                falseButton.isEnabled = true
            }
            if(quizViewModel.cheatsAttempts==0){
                cheatButton.isEnabled = false
            }
        }
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = when {
            quizViewModel.currentQuestionCheatAnswer -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }
        quizViewModel.currentQuestion.userAnswer = userAnswer
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        if(quizViewModel.currentQuestionCheatAnswer){
            val hintsValue = getString(R.string.hints_info) + quizViewModel.cheatsAttempts
            Toast.makeText(this, hintsValue, Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkResult(){
        val result = quizViewModel.checkResult()
        if(result!=null){
            Toast.makeText(this,getString(R.string.result_toast) + result.toInt() + "%", Toast.LENGTH_LONG).show()
        }
    }
}