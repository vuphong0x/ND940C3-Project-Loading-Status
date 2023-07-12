package com.udacity

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var backgroundLoadingColor = 0
    private var backgroundCircleLoadingColor = 0
    private var textColor = 0
    private val startValue = 0f
    private val targetValue = 100f
    private var progress = 0f
    private var circleRadius = resources.getDimension(R.dimen._16dp)
    private var textContentButton = resources.getString(R.string.button_download)
    private var durationAnimation = 2500L
    val textBounds = Rect()
    private var valueAnimator = ValueAnimator()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
    }

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {

            }

            ButtonState.Loading -> {
                textContentButton = resources.getString(R.string.button_loading)
                startLoadingAnim()
                invalidate()
            }

            ButtonState.Completed -> {
                textContentButton = resources.getString(R.string.button_download)
                stopLoadingAnim()
                invalidate()
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            backgroundLoadingColor = getColor(
                R.styleable.LoadingButton_backgroundLoadingColor,
                context.getColor(R.color.colorPrimaryDark)
            )
            backgroundCircleLoadingColor = getColor(
                R.styleable.LoadingButton_backgroundCircleLoadingColor,
                context.getColor(R.color.colorAccent)
            )
            textColor = getColor(R.styleable.LoadingButton_textColor, Color.WHITE)
        }
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Draw loading background
        paint.color = backgroundLoadingColor
        canvas?.drawRect(
            0f,
            0f,
            widthSize.toFloat() * progress / targetValue,
            heightSize.toFloat(),
            paint
        )

        // Draw circle progress
        val circleXPos = (width + textBounds.width()) / 2f + 8f
        val circleYPos = heightSize / 2f - circleRadius
        val rectF = RectF(
            circleXPos,
            circleYPos,
            circleXPos + circleRadius * 2,
            circleYPos + circleRadius * 2
        )
        paint.color = backgroundCircleLoadingColor
        canvas?.drawArc(rectF, 0f, 3.6f * progress, true, paint)

        // Draw text
        paint.getTextBounds(textContentButton, 0, textContentButton.length, textBounds)
        val textXPos = width / 2f
        val textYPos = height / 2f - textBounds.centerY()
        paint.color = textColor
        canvas?.drawText(textContentButton, textXPos, textYPos, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w), heightMeasureSpec, 0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    private fun startLoadingAnim() {
        valueAnimator = ValueAnimator.ofFloat(startValue, targetValue).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = durationAnimation
            addUpdateListener { animated ->
                progress = animated.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    private fun stopLoadingAnim() {
        valueAnimator.cancel()
    }
}
