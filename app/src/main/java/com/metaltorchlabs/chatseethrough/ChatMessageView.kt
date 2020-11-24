package com.metaltorchlabs.chatseethrough

import android.content.Context
import android.graphics.*
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View

class ChatMessageView(context: Context) : View(context) {

    // These constants were included here for demonstration purposes. They could be changed into
    // properties on each message if desired.
    companion object {

        // Represented in pixels. Pads up the message bubble size.
        const val PADDING_VERTICAL = 7f
        const val PADDING_HORIZONTAL = 28f

        // Represented in pixels. Margins applied to the outside of the message bubble.
        const val MARGIN_RIGHT = 56f
        const val MARGIN_BOTTOM = 56f

        // Attributes to be applied to the paint for the text and the background.
        const val TEXT_SIZE = 84f // in pixels
        const val TEXT_COLOR = Color.WHITE
        const val BACKGROUND_COLOR = Color.WHITE

        // Represented in pixels. The text is allowed to reach this width before it wraps to a new
        // line.
        const val TEXT_MAX_WIDTH = 1200

        // Represented in pixels. The radius of all four corners of the message bubble. Can be
        // separated out into each individual corner if required.
        const val CORNER_RADIUS = 14f

    }

    // This fancy StaticLayout class allows text to be pre-rendered in a given width with
    // text wrapping. It even separates out each line and allows measurements to be taken on each.
    private var messageLayout: StaticLayout? = null

    // Where to position the message within it's parent. These are pixel values.
    private var messageX: Float = -1f
    private var messageY: Float = -1f

    // The measured message dimensions in pixels. These will be taken directly from the
    // messageLayout lines in order to get the most accurate values.
    private var messageWidth: Float = -1f
    private var messageHeight: Float = -1f

    // Paint used to render the chat message text.
    private var messagePaint: TextPaint = TextPaint()

    // It's called "background" paint, but this is really the portion of the chat message that will
    // be drawn since the message bubble itself will be left un-drawn and therefore transparent.
    private var backgroundPaint: Paint = Paint()

    // The path that will make up the shape of the message background that will be rendered.
    private var backgroundPath: Path = Path()

    // Builds this ChatMessageView. This can be called on an existing ChatMessageView with a new
    // message string in order to reconfigure. Useful for the recycler view.
    fun build(message: String) {

        buildMessage(message)
        buildBackground()

    }

    // Sets up all properties relating to the message including it's pre-render layout
    // (StaticLayout).
    private fun buildMessage(message: String) {

        messagePaint.textSize = TEXT_SIZE
        messagePaint.color = TEXT_COLOR
        messageLayout = StaticLayout.Builder.obtain(message, 0, message.length, messagePaint,
                TEXT_MAX_WIDTH).build()
        messageWidth = getLongestMessageLineWidth()
        messageHeight = messageLayout!!.height.toFloat()

        if (width > 0) {
            buildMessagePosition()
        }

    }

    // Positions the text for the chat message where it needs to be according to the view size,
    // message padding, and any margins.
    private fun buildMessagePosition() {

        messageX = width - messageWidth - PADDING_HORIZONTAL - MARGIN_RIGHT
        messageY = PADDING_VERTICAL

    }

    // Finds the longest line in the wrapped message layout and returns it. Used to set the total
    // width of the message text.
    private fun getLongestMessageLineWidth() : Float {

        var longestLineWidth = 0f
        var potentialWidth: Float
        var lineNumber = 0

        while (lineNumber < messageLayout!!.lineCount) {
            potentialWidth = messageLayout!!.getLineWidth(lineNumber)
            if (potentialWidth > longestLineWidth) {
                longestLineWidth = potentialWidth
            }
            lineNumber++
        }

        return longestLineWidth

    }

    // Sets all properties related to the background (the portion that will be drawn).
    private fun buildBackground() {

        backgroundPaint.color = BACKGROUND_COLOR

        // The background path relies on the width of this entire component. The component width
        // may not yet be set as it's set by the `onSizeChanged()` function on first layout. Since
        // these will be used in a recycler view, `onSizeChanged()` may only be called once.
        // Therefore, the background path must be built from here whenever width is available as
        // well as from `onSizeChanged()`.
        if (width > 0) {
            buildBackgroundPath()
        }

    }

    // Builds the shape of the background to be used when it comes time to draw.
    private fun buildBackgroundPath() {

        // Set a few common calculations.
        val totalWidth = width.toFloat()
        val totalHeight = height.toFloat()
        val bubbleWidth = messageWidth + 2 * PADDING_HORIZONTAL
        val bubbleHeight = messageHeight + 2 * PADDING_VERTICAL
        val bubbleWidthWithMargin = bubbleWidth + MARGIN_RIGHT
        val cornerDiameter = 2 * CORNER_RADIUS

        // Calculate all the node points. A few of these are commented out because they weren't
        // needed in the end. This is because these points were the end of the corner arcs around
        // the message bubble, and the point is not required to be defined. They may come in handy
        // for modifications though so they haven't been removed completely.
        val topLeft = PointF(0f, 0f)
        val topRight = PointF(totalWidth, 0f)
        val bottomLeft = PointF(0f, totalHeight)
        val bottomRight = PointF(totalWidth, totalHeight)
        val bubbleTopLeft = PointF(totalWidth - bubbleWidthWithMargin + CORNER_RADIUS, 0f)
        // val bubbleLeftTop = PointF(totalWidth - bubbleWidthWithMargin, CORNER_RADIUS)
        // val bubbleTopRight = PointF(totalWidth - MARGIN_RIGHT - CORNER_RADIUS, 0f)
        val bubbleRightTop = PointF(totalWidth - MARGIN_RIGHT, CORNER_RADIUS)
        // val bubbleBottomLeft = PointF(totalWidth - bubbleWidthWithMargin + CORNER_RADIUS, bubbleHeight)
        val bubbleLeftBottom = PointF(totalWidth - bubbleWidthWithMargin, bubbleHeight - CORNER_RADIUS)
        val bubbleBottomRight = PointF(totalWidth - MARGIN_RIGHT - CORNER_RADIUS, bubbleHeight)
        // val bubbleRightBottom = PointF(totalWidth - MARGIN_RIGHT, bubbleHeight - CORNER_RADIUS)

        // Define the circle bounding boxes to assist in creating the arcs on the corners of the
        // chat bubble. Each arc is just a portion of the circle bound by these boxes.
        val arcReferenceBubbleTopRight = RectF(
                bubbleRightTop.x - cornerDiameter,
                bubbleRightTop.y - CORNER_RADIUS,
                bubbleRightTop.x,
                bubbleRightTop.y + CORNER_RADIUS)

        val arcReferenceBubbleTopLeft = RectF(
            bubbleTopLeft.x - CORNER_RADIUS,
            bubbleTopLeft.y,
            bubbleTopLeft.x + CORNER_RADIUS,
            bubbleTopLeft.y + cornerDiameter
        )

        val arcReferenceBubbleBottomLeft = RectF(
            bubbleLeftBottom.x,
            bubbleLeftBottom.y - CORNER_RADIUS,
            bubbleLeftBottom.x + cornerDiameter,
            bubbleLeftBottom.y + CORNER_RADIUS
        )

        val arcReferenceBubbleBottomRight = RectF(
            bubbleBottomRight.x - CORNER_RADIUS,
            bubbleBottomRight.y - cornerDiameter,
            bubbleBottomRight.x + CORNER_RADIUS,
            bubbleBottomRight.y
        )

        // Create the background path in a clockwise fashion starting at the upper left corner of
        // the view.
        // NOTE: The `arcTo` function works with angles in reverse from standard mathematics.
        // Positive angles push clockwise around the circle, while negative angles push counter-
        // clockwise around the circle.
        backgroundPath = Path()
        backgroundPath.apply{
            moveTo(topLeft.x, topLeft.y)
            lineTo(topRight.x, topRight.y)
            lineTo(topRight.x, bubbleRightTop.y)
            lineTo(bubbleRightTop.x, bubbleRightTop.y)
            arcTo(arcReferenceBubbleTopRight, 0f, -90f, false)
            lineTo(bubbleTopLeft.x, bubbleTopLeft.y)
            arcTo(arcReferenceBubbleTopLeft, -90f, -90f, false)
            lineTo(bubbleLeftBottom.x, bubbleLeftBottom.y)
            arcTo(arcReferenceBubbleBottomLeft, -180f, -90f, false)
            lineTo(bubbleBottomRight.x, bubbleBottomRight.y)
            arcTo(arcReferenceBubbleBottomRight, -270f, -90f, false)
            lineTo(bubbleRightTop.x, bubbleRightTop.y)
            lineTo(topRight.x, bubbleRightTop.y)
            lineTo(bottomRight.x, bottomRight.y)
            lineTo(bottomLeft.x, bottomLeft.y)
            lineTo(topLeft.x, topLeft.y)
            close()
        }

    }

    // Called any time the size of this view changes. We're forcing a size change through a call
    // to `requestLayout()` from within the recycler view adapter since our height might be modified
    // in cases where this view is recycled.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {

        super.onSizeChanged(w, h, oldw, oldh)

        // Since this component will be used within a recycler view, this `onSizeChanged()` function
        // may only be called once when the layout is first loaded. The background path relies on
        // the width (which won't change) so it can be built from here for the first time, and from
        // the `buildBackgroundPath()` function every other time.
        buildBackgroundPath()
        buildMessagePosition()

    }

    // Specifies the dimensions this view needs to be.
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val componentHeight = (messageHeight + (2 * PADDING_VERTICAL) + MARGIN_BOTTOM).toInt()

        setMeasuredDimension(parentWidth, componentHeight)

    }

    // Get this chat message view drawn to the screen!
    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
        canvas?.drawPath(backgroundPath, backgroundPaint)
        canvas?.translate(messageX, messageY)
        messageLayout?.draw(canvas)
        canvas?.translate(-messageX, -messageY)

    }

}
