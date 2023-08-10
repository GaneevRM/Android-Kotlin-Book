package com.ganeevrm.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider

const val EXTRA_ANSWER_SHOWN = "com.ganeevrm.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TURE = "com.ganeevrm.android.geoquiz.answer_is_true"
private const val TAG = "CheatActivity"
private const val KEY_IS_CHEATER = "isCheater"
class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private val cheatViewModel: CheatViewModel by lazy {
        ViewModelProvider(this).get(CheatViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        setContentView(R.layout.activity_cheat)

        cheatViewModel.answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TURE, false)
        cheatViewModel.isCheater = savedInstanceState?.getBoolean(KEY_IS_CHEATER, false) ?: false

        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        showAnswerButton.setOnClickListener {
            cheatViewModel.isCheater = true
            updateInterface()
        }

        updateInterface()
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
        outState.putBoolean(KEY_IS_CHEATER, cheatViewModel.isCheater)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    private fun updateInterface(){
        if(cheatViewModel.isCheater){
            showAnswerButton.visibility = View.GONE
            val answerText = when {
                cheatViewModel.answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult()
        } else {
            showAnswerButton.visibility = View.VISIBLE
        }
    }

    private fun setAnswerShownResult() {
        Log.d(TAG, "setAnswerShownResult")
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, true)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply { putExtra(EXTRA_ANSWER_IS_TURE, answerIsTrue) }
        }
    }
}