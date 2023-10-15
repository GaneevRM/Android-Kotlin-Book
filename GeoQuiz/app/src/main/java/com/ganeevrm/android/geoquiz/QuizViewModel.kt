package com.ganeevrm.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
private const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    init {
        Log.d(TAG, "ViewModel instance created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel instance about to be destroyed")
    }

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    var cheatsAttempts = 3

    private val questionBank = listOf(
        Question(R.string.question_australia, true, null, false),
        Question(R.string.question_oceans, true, null, false),
        Question(R.string.question_mideast, false, null, false),
        Question(R.string.question_africa, false, null, false),
        Question(R.string.question_americas, true, null, false),
        Question(R.string.question_asia, true, null, false)
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionUserAnswer: Boolean?
        get() = questionBank[currentIndex].userAnswer
    val currentQuestionCheatAnswer: Boolean
        get() = questionBank[currentIndex].cheatAnswer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId
    val currentQuestion: Question
        get() = questionBank[currentIndex]

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrevious(){
        val diff = currentIndex - 1
        currentIndex = if (diff == -1) questionBank.size-1 else diff % questionBank.size
    }

    fun checkResult(): Double? {
        var valueAnswer = 0
        for(question in questionBank){
            if (question.userAnswer!=null){
                valueAnswer++
            }
        }
        return if(questionBank.size == valueAnswer){
            valueAnswer = 0
            for(question in questionBank){
                if (question.answer==question.userAnswer){
                    valueAnswer++
                }
            }
            (valueAnswer.toDouble() / questionBank.size) * 100
        } else {
            null
        }
    }
}