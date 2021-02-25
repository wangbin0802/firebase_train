package com.stareme.spantext

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.text.style.ReplacementSpan
import android.util.TypedValue
import com.stareme.firebase.R


class IconTextSpan(private val mContext: Context, bgColorResId: Int, text: String) : ReplacementSpan() {
    private var mBgColorResId //Icon背景颜色
            = 0
    private var mText //Icon内文字
            : String? = null
    private var mBgHeight //Icon背景高度
            = 0f
    private var mBgWidth //Icon背景宽度
            = 0f
    private var mRadius //Icon圆角半径
            = 0f
    private var mRightMargin //右边距
            = 0f
    private var mTextSize //文字大小
            = 0f
    private var mTextColorResId //文字颜色
            = 0

    private var mBgPaint //icon背景画笔
            : Paint? = null
    private var mTextPaint //icon文字画笔
            : Paint? = null
    init {
        //初始化默认数值
        initDefaultValue(mContext, bgColorResId, text);
        //计算背景的宽度
        mBgWidth = calculateBgWidth(text);
        //初始化画笔
        initPaint();
    }

    /**
     * 计算icon背景宽度
     *
     * @param text icon内文字
     */
    private fun calculateBgWidth(text: String): Float {
        return if (text.length > 1) {
            //多字，宽度=文字宽度+padding
            val textRect = Rect()
            val paint = Paint()
            paint.textSize = mTextSize
            paint.getTextBounds(text, 0, text.length, textRect)
            val padding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                4f,
                mContext.resources.displayMetrics
            )
            textRect.width() + padding * 2
        } else {
            //单字，宽高一致为正方形
            mBgHeight
        }
    }

    /**
     * 初始化默认数值
     *
     * @param context
     */
    private fun initDefaultValue(context: Context, bgColorResId: Int, text: String?) {
        mBgColorResId = bgColorResId
        mText = text
        mBgHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            17f,
            context.resources.displayMetrics
        )
        mRightMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            2f,
            context.resources.displayMetrics
        )
        mRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            9f,
            context.resources.displayMetrics
        )
        mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            13f,
            context.resources.displayMetrics
        )
        mTextColorResId = android.R.color.black
    }

    /**
     * 初始化画笔
     */
    private fun initPaint() {
        //初始化背景画笔
        mBgPaint = Paint()
        mBgPaint?.color = mContext.resources.getColor(mBgColorResId)
        mBgPaint?.style = Paint.Style.FILL
        mBgPaint?.isAntiAlias = true

        //初始化文字画笔
        mTextPaint = TextPaint()
        mTextPaint?.color = mContext.resources.getColor(mTextColorResId)
        mTextPaint?.textSize = mTextSize
        mTextPaint?.isAntiAlias = true
        mTextPaint?.textAlign = Paint.Align.CENTER
    }

    /**
     * 设置右边距
     *
     * @param rightMarginDpValue
     */
    fun setRightMarginDpValue(rightMarginDpValue: Int) {
        mRightMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            rightMarginDpValue.toFloat(),
            mContext.resources.displayMetrics
        )
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence?,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        return (mBgWidth + mRightMargin).toInt()
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence?,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        //画背景
        val bgPaint = Paint()
        bgPaint.color = mContext.resources.getColor(mBgColorResId)
        bgPaint.style = Paint.Style.FILL
        bgPaint.isAntiAlias = true
        val metrics = paint.fontMetrics

        val textHeight = metrics.descent - metrics.ascent
        //算出背景开始画的y坐标
        val bgStartY = y + (textHeight - mBgHeight) / 2 + metrics.ascent

        //画背景
//        val bgRect = RectF(x, bgStartY, x + mBgWidth, bgStartY + mBgHeight)
//        canvas.drawRoundRect(bgRect, mRadius, mRadius, bgPaint)
        canvas.drawCircle(x + mBgWidth / 2, bgStartY + mBgHeight / 2, mRadius, bgPaint)

        //把字画在背景中间
        val textPaint = TextPaint()
        textPaint.color = mContext.resources.getColor(mTextColorResId)
        textPaint.textSize = mTextSize
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER //这个只针对x有效

        val fontMetrics = textPaint.fontMetrics
        val textRectHeight = fontMetrics.bottom - fontMetrics.top
        canvas.drawText(
            mText!!,
            x + mBgWidth / 2,
            bgStartY + (mBgHeight - textRectHeight) / 2 - fontMetrics.top,
            textPaint
        )

    }
}