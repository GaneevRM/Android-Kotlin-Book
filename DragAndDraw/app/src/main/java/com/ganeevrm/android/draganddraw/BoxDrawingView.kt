package com.ganeevrm.android.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2

private const val TAG = "BoxDrawingView"
private const val BOXES_STATE_KEY = "BoxesState"

class BoxDrawingView(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var currentBox: Box? = null
    private val boxes = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
        style = Paint.Style.FILL
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }
    private var isRotating = false
    private var drawingPointerId: Int? = null
    private var rotatingPointerId: Int? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val maskedAction = event.actionMasked
        val actionIndex = event.actionIndex

        val current = PointF(event.x, event.y)
        var action = ""
        when (maskedAction) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val pointerId = event.getPointerId(actionIndex)
                val point = PointF(event.getX(actionIndex), event.getY(actionIndex))

                if (currentBox == null) {
                    action =
                        if (maskedAction == MotionEvent.ACTION_DOWN) "ACTION_DOWN" else "ACTION_POINTER_DOWN"
                    currentBox = Box(point).also { boxes.add(it) }
                    drawingPointerId = pointerId
                    isRotating = false
                    Log.i(
                        TAG,
                        "$action (рисование) указатель ID: $pointerId, индекс: $actionIndex, в x=${point.x}, y=${point.y}"
                    )

                } else if (!isRotating && event.pointerCount > 1) {
                    action =
                        if (maskedAction == MotionEvent.ACTION_POINTER_DOWN) "ACTION_POINTER_DOWN" else "ACTION_DOWN"
                    rotatingPointerId = pointerId
                    isRotating = true
                    Log.i(
                        TAG,
                        "$action (вращение) указатель ID: $pointerId, индекс: $actionIndex, в x=${point.x}, y=${point.y}"
                    )
                }

            }

            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                if (currentBox != null) {
                    if (!isRotating) {
                        val drawingPointerIndex = event.findPointerIndex(drawingPointerId ?: 0)
                        if (drawingPointerIndex != -1) {
                            val current = PointF(
                                event.getX(drawingPointerIndex),
                                event.getY(drawingPointerIndex)
                            )
                            updateCurrentBox(current)
                            Log.i(
                                TAG,
                                "$action (рисование) указатель ID: $drawingPointerId, индекс: $drawingPointerIndex, в x=${current.x}, y=${current.y}"
                            )
                        }
                    } else {
                        val drawingPointerIndex = event.findPointerIndex(drawingPointerId ?: 0)
                        val rotatingPointerIndex = event.findPointerIndex(rotatingPointerId ?: 0)

                        if (drawingPointerIndex != -1 && rotatingPointerIndex != -1) {
                            val drawingPoint = PointF(
                                event.getX(drawingPointerIndex),
                                event.getY(drawingPointerIndex)
                            )
                            val rotatingPoint = PointF(
                                event.getX(rotatingPointerIndex),
                                event.getY(rotatingPointerIndex)
                            )
                            updateCurrentBoxWithRotation(drawingPoint, rotatingPoint)
                            Log.i(
                                TAG,
                                "$action (вращение) указ. ID рисования: $drawingPointerId, индекс: $drawingPointerIndex, указ. ID вращения: $rotatingPointerId, индекс: $rotatingPointerIndex"
                            )
                        }
                    }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val pointerId = event.getPointerId(actionIndex)

                if (pointerId == drawingPointerId) {
                    action =
                        if (maskedAction == MotionEvent.ACTION_UP) "ACTION_UP" else "ACTION_POINTER_UP"
                    updateCurrentBoxEndPoint(
                        PointF(
                            event.getX(actionIndex),
                            event.getY(actionIndex)
                        )
                    )
                    currentBox = null
                    drawingPointerId = null
                    isRotating = false
                    Log.i(
                        TAG,
                        "$action (завершение рисования) указатель ID: $pointerId, индекс: $actionIndex"
                    )

                } else if (pointerId == rotatingPointerId) {
                    action =
                        if (maskedAction == MotionEvent.ACTION_POINTER_UP) "ACTION_POINTER_UP" else "ACTION_UP"
                    rotatingPointerId = null
                    isRotating = false
                    Log.i(
                        TAG,
                        "$action (завершение вращения) указатель ID: $pointerId, индекс: $actionIndex"
                    )
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null
                drawingPointerId = null
                rotatingPointerId = null
                isRotating = false
                Log.i(TAG, "$action")
            }
        }
        Log.i(TAG, "$action at x=${current.x}, y=${current.y}")
        return true
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    private fun updateCurrentBoxEndPoint(current: PointF) {
        currentBox?.let {
            it.end = current
        }
    }

    private var previousRotationAngle = 0f
    private fun updateCurrentBoxWithRotation(drawingPoint: PointF, rotatingPoint: PointF) {
        currentBox?.let { box ->
            box.end = drawingPoint

            val boxCenterX = (box.left + box.right) / 2f
            val boxCenterY = (box.top + box.bottom) / 2f

            val rotationAngle = calculateRotationAngle(boxCenterX, boxCenterY, rotatingPoint)

            box.rotationAngle += rotationAngle - previousRotationAngle
            previousRotationAngle = rotationAngle

            invalidate()
        }
    }

    private fun calculateRotationAngle(centerX: Float, centerY: Float, point: PointF): Float {
        return Math.toDegrees(atan2(point.y - centerY, point.x - centerX).toDouble()).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(backgroundPaint)

        boxes.forEach { box ->
            canvas.save()
            val boxCenterX = (box.left + box.right) / 2f
            val boxCenterY = (box.top + box.bottom) / 2f
            canvas.translate(boxCenterX, boxCenterY)
            canvas.rotate(box.rotationAngle)
            canvas.translate(-boxCenterX, -boxCenterY)

            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)

            canvas.restore()
        }

        currentBox?.let { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelableArrayList(BOXES_STATE_KEY, ArrayList(boxes))
        bundle.putParcelable("superState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable("superState"))
            val savedBoxes = state.getParcelableArrayList<Box>(BOXES_STATE_KEY)
            boxes.clear()
            if (savedBoxes != null) {
                boxes.addAll(savedBoxes)
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}