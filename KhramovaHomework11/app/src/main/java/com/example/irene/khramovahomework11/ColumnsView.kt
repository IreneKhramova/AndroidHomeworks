package com.example.irene.khramovahomework11

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Rect
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import java.util.*

class ColumnsView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var textColor = ContextCompat.getColor(context, R.color.warm_grey_six)
    private var columnColor = ContextCompat.getColor(context, R.color.light_gold)
    private var columnsNumber = 1
    private var columnsData: LinkedHashMap<String, Int> = LinkedHashMap()
    private var columnPaint = Paint(ANTI_ALIAS_FLAG)
    private var datePaint = Paint(ANTI_ALIAS_FLAG)
    private var columnValuePaint = Paint(ANTI_ALIAS_FLAG)
    private var dateTextBounds = Rect()
    private var columnValueTextBounds = Rect()
    private var columnHeightCoef = 1f

    companion object {
        const val DATE_TEXT_SIZE = 10f
        const val COLUMN_VALUE_TEXT_SIZE = 11f
        const val CORNER_RADIUS = 100f
        const val COLUMN_MAX_HEIGHT = 85f
        const val COLUMN_WIDTH = 4f
        const val SPACE_BETWEEN_COLUMN_DATA = 10f
        const val MIN_SPACE_BETWEEN_COLUMNS_DATES = 12f
    }

    //TODO: конструкторы
    init {
        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ColumnsView,
                0,
                0).apply {
            try {
                //TODO: правильно ли при объявлении переменной присвоить ей цвет, а потом делать так?
                textColor = getColor(R.styleable.ColumnsView_textColor, textColor)
                columnColor = getColor(R.styleable.ColumnsView_columnColor, columnColor)
            } finally {
                recycle()
            }
        }

        columnPaint.color = columnColor
        columnValuePaint.color = columnColor
        datePaint.color = textColor

        val dateSizeInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                DATE_TEXT_SIZE,
                resources.displayMetrics)
        datePaint.textSize = dateSizeInPixels

        val columnValueSizeInPixels = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                COLUMN_VALUE_TEXT_SIZE,
                resources.displayMetrics)
        columnValuePaint.textSize = columnValueSizeInPixels

        columnPaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //TODO: что здесь нужно?
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = 0
        var height = 0
        var desiredHeight = 0
        var desiredWidth = 0


        if(columnsData.isNotEmpty()) {
            val dateText = columnsData.keys.elementAt(0)
            datePaint.getTextBounds(dateText, 0, dateText.length, dateTextBounds)

            val columnValueText = columnsData.values.elementAt(0).toString()
            columnValuePaint.getTextBounds(columnValueText, 0, columnValueText.length, columnValueTextBounds)

            val minh = (paddingBottom + paddingTop + dateTextBounds.height() + columnValueTextBounds.height()
                    + dpToPx(4 * SPACE_BETWEEN_COLUMN_DATA + COLUMN_MAX_HEIGHT).toInt())
            desiredHeight = resolveSizeAndState(minh, heightMeasureSpec, 0)

            val minw = (paddingLeft + paddingRight + columnsData.size * dateTextBounds.width()
                    + (columnsData.size - 1) * dpToPx(MIN_SPACE_BETWEEN_COLUMNS_DATES).toInt())
            desiredWidth = resolveSizeAndState(minw, widthMeasureSpec, 0)
        }

        when (widthMode) {
            MeasureSpec.EXACTLY -> //Must be this size
                width = widthSize
            MeasureSpec.AT_MOST -> //Can't be bigger than...
                width = Math.min(desiredWidth, widthSize)
            MeasureSpec.UNSPECIFIED -> //Be whatever you want
                width = desiredWidth
        }

        when (heightMode) {
            MeasureSpec.EXACTLY -> //Must be this size
                height = heightSize
            MeasureSpec.AT_MOST -> //Can't be bigger than...
                height = Math.min(desiredHeight, heightSize)
            MeasureSpec.UNSPECIFIED -> //Be whatever you want
                height = desiredHeight
        }

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //TODO: высота самого большого столбика всегда 85dp? Или рассчитывать от высоты вью?
        //TODO: высота столбиков не как в макете. У меня 30 - это половина от 60, в макете - примерно треть. По какому правилу тогда вычислять?

        Log.d("onDraw", canvas.height.toString() + "   " + canvas.width.toString())

        if(columnsData.isNotEmpty()) {
            val itemWidth = canvas.width.toFloat() / (columnsData.size + 1)

            val maxColumnValue = Collections.max(columnsData.values)

            for (i in 0 until columnsData.size) {
                val dateText = columnsData.keys.elementAt(i)
                datePaint.getTextBounds(dateText, 0, dateText.length, dateTextBounds) // правильно без обнуления bounds?

                val columnValueText = columnsData.values.elementAt(i).toString()
                columnValuePaint.getTextBounds(columnValueText, 0, columnValueText.length, columnValueTextBounds)

                val itemHorizontalCenter = itemWidth * (i + 1)

                /* отступ + текст + отступ */
                val topOfMaxColumn = columnValueTextBounds.height() + dpToPx(2 * SPACE_BETWEEN_COLUMN_DATA)

                val topOfColumn = (topOfMaxColumn + (maxColumnValue - columnHeightCoef * columnValueText.toFloat())
                        / maxColumnValue * dpToPx(COLUMN_MAX_HEIGHT))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    canvas.drawRoundRect(itemHorizontalCenter - dpToPx(COLUMN_WIDTH) / 2,
                            topOfColumn,
                            itemHorizontalCenter + dpToPx(COLUMN_WIDTH) / 2,
                            topOfMaxColumn + dpToPx(COLUMN_MAX_HEIGHT),
                            dpToPx(CORNER_RADIUS),
                            dpToPx(CORNER_RADIUS),
                            columnPaint)
                } else {
                    canvas.drawRect(itemHorizontalCenter - dpToPx(COLUMN_WIDTH) / 2,
                            topOfColumn,
                            itemHorizontalCenter + dpToPx(COLUMN_WIDTH) / 2,
                            topOfMaxColumn - dpToPx(COLUMN_MAX_HEIGHT),
                            columnPaint)
                }

                canvas.drawText(columnValueText, itemHorizontalCenter - columnValueTextBounds.width().toFloat() / 2,
                        topOfColumn - dpToPx(SPACE_BETWEEN_COLUMN_DATA), columnValuePaint)

                canvas.drawText(dateText, itemHorizontalCenter - dateTextBounds.width().toFloat() / 2,
                        topOfMaxColumn + dpToPx(COLUMN_MAX_HEIGHT + SPACE_BETWEEN_COLUMN_DATA) + dateTextBounds.height(),
                        datePaint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when {
            event.action == MotionEvent.ACTION_DOWN -> true
            event.action == MotionEvent.ACTION_UP -> {
                animation()
                true
            }
            else -> false
        }
    }

    fun setData(data: LinkedHashMap<String, Int>) {
        if(data.size in 1..9) {
            columnsNumber = data.size
        } else {
            //TODO: так норм?
            throw ExceptionInInitializerError("Number of columns must be in 1..9")
        }
        columnsData = data

        invalidate()
        requestLayout()
    }

    private fun animation() {
        //TODO: в начале должна быть задержка?
        ValueAnimator.ofFloat(0f,1f).apply {
            duration = 500

            addUpdateListener { updatedAnimation ->
                columnHeightCoef = updatedAnimation.animatedValue as Float
                invalidate()
            }

            start()
        }
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}