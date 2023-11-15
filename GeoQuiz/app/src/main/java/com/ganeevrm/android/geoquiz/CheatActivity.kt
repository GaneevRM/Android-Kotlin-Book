package com.ganeevrm.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ganeevrm.android.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.ganeevrm.android.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TURE = "com.ganeevrm.android.geoquiz.answer_is_true"
private const val TAG = "CheatActivity"
private const val KEY_IS_CHEATER = "isCheater"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding
    private val cheatViewModel: CheatViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cheatViewModel.answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TURE, false)
        cheatViewModel.isCheater = savedInstanceState?.getBoolean(KEY_IS_CHEATER, false) ?: false

        binding.showAnswerButton.setOnClickListener {
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

    private fun updateInterface() {
        val apiLevelValue = getString(R.string.api_level) + Build.VERSION.SDK_INT
        binding.apiLevelTextView.text = apiLevelValue
        if (cheatViewModel.isCheater) {
            binding.showAnswerButton.visibility = View.GONE
            val answerText = when {
                cheatViewModel.answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            binding.answerTextView.setText(answerText)
            setAnswerShownResult()
        } else {
            binding.showAnswerButton.visibility = View.VISIBLE
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
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(
                    EXTRA_ANSWER_IS_TURE,
                    answerIsTrue
                )
            }
        }
    }
}