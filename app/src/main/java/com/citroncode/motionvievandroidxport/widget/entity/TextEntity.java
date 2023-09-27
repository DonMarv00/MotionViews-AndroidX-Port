package com.citroncode.motionvievandroidxport.widget.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.citroncode.motionvievandroidxport.utils.BackgroundColorSpan;
import com.citroncode.motionvievandroidxport.utils.FontProvider;
import com.citroncode.motionvievandroidxport.viewmodel.TextLayer;


public class TextEntity extends MotionEntity {

    private final TextPaint textPaint;
    private final FontProvider fontProvider;
    @Nullable
    private Bitmap bitmap;
    private Context context;

    public TextEntity(@NonNull TextLayer textLayer,
                      @IntRange(from = 1) int canvasWidth,
                      @IntRange(from = 1) int canvasHeight,
                      @NonNull FontProvider fontProvider,
                      @NonNull Context context) {
        super(textLayer, canvasWidth, canvasHeight);
        this.fontProvider = fontProvider;

        this.textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

        this.context = context;

        updateEntity(false);
    }

    private void updateEntity(boolean moveToPreviousCenter) {

        // save previous center
        PointF oldCenter = absoluteCenter();

        Bitmap newBmp = createBitmap(getLayer(), bitmap);

        // recycle previous bitmap (if not reused) as soon as possible
        if (bitmap != null && bitmap != newBmp && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        this.bitmap = newBmp;

        float width = bitmap.getWidth();
        float height = bitmap.getHeight();

        @SuppressWarnings("UnnecessaryLocalVariable")
        float widthAspect = 1.0F * canvasWidth / width;

        // for text we always match text width with parent width
        this.holyScale = widthAspect;

        // initial position of the entity
        srcPoints[0] = 0;
        srcPoints[1] = 0;
        srcPoints[2] = width;
        srcPoints[3] = 0;
        srcPoints[4] = width;
        srcPoints[5] = height;
        srcPoints[6] = 0;
        srcPoints[7] = height;
        srcPoints[8] = 0;
        srcPoints[8] = 0;

        if (moveToPreviousCenter) {
            // move to previous center
            moveCenterTo(oldCenter);
        }
    }

    /**
     * If reuseBmp is not null, and size of the new bitmap matches the size of the reuseBmp,
     * new bitmap won't be created, reuseBmp it will be reused instead
     *
     * @param textLayer text to draw
     * @param reuseBmp  the bitmap that will be reused
     * @return bitmap with the text
     */
    /*
    @NonNull
    private Bitmap createBitmap(@NonNull TextLayer textLayer, @Nullable Bitmap reuseBmp) {

        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.RED); // Set your desired background color


        int boundsWidth = canvasWidth;

        // init params - size, color, typeface
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(textLayer.getFont().getSize() * canvasWidth);
        textPaint.setColor(textLayer.getFont().getColor());
        textPaint.setTypeface(fontProvider.getTypeface(textLayer.getFont().getTypeface()));

        // drawing text guide : http://ivankocijan.xyz/android-drawing-multiline-text-on-canvas/
        // Static layout which will be drawn on canvas
        StaticLayout sl = new StaticLayout(
                textLayer.getText(), // - text which will be drawn
                textPaint,
                boundsWidth, // - width of the layout
                Layout.Alignment.ALIGN_CENTER, // - layout alignment
                1, // 1 - text spacing multiply
                1, // 1 - text spacing add
                true); // true - include padding


        // calculate height for the entity, min - Limits.MIN_BITMAP_HEIGHT
        int boundsHeight = sl.getHeight();

        // create bitmap not smaller than TextLayer.Limits.MIN_BITMAP_HEIGHT
        int bmpHeight = (int) (canvasHeight * Math.max(TextLayer.Limits.MIN_BITMAP_HEIGHT,
                1.0F * boundsHeight / canvasHeight));

        // create bitmap where text will be drawn
        Bitmap bmp;
        if (reuseBmp != null && reuseBmp.getWidth() == boundsWidth
                && reuseBmp.getHeight() == bmpHeight) {
            // if previous bitmap exists, and it's width/height is the same - reuse it
            bmp = reuseBmp;
            bmp.eraseColor(Color.TRANSPARENT); // erase color when reusing
        } else {
            bmp = Bitmap.createBitmap(boundsWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bmp);
        canvas.save();

        // move text to center if bitmap is bigger that text
        if (boundsHeight < bmpHeight) {
            //calculate Y coordinate - In this case we want to draw the text in the
            //center of the canvas so we move Y coordinate to center.
            float textYCoordinate = (bmpHeight - boundsHeight) / 2;
            canvas.translate(0, textYCoordinate);
        }


        // draw the background rectangle
        backgroundPaint.setColor(Color.YELLOW);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

// Radius für abgerundete Ecken (in Pixeln)
        float cornerRadius = 20.0f; // Ändere dies je nach gewünschter Rundung

// Zeichne das Hintergrund-Rechteck mit abgerundeten Ecken
        canvas.drawRoundRect(0, 0, boundsWidth, boundsHeight, cornerRadius, cornerRadius, backgroundPaint);

        int backgroundWidth = boundsWidth;
        int backgroundHeight = boundsHeight;


        //draws static layout on canvas
        sl.draw(canvas);
        canvas.restore();

        return bmp;
    } */

    @NonNull
    private Bitmap createBitmap(@NonNull TextLayer textLayer, @Nullable Bitmap reuseBmp) {

        int backgroundColor = Color.BLACK;  // Beispielhafte Hintergrundfarbe (rot)
        int padding = 16;  // Beispielhafte Polsterung
        int radius = 10;  // Beispielhafter Radius
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(backgroundColor, padding, radius);

        String text = "Dies ist der Text \nder angezeigt werden soll. \nHAhhahha";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(backgroundColorSpan, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int boundsWidth = canvasWidth;

        // Erstellen einer neuen TextView
        TextView textView = new TextView(context); // 'context' sollte initialisiert werden

        // Setzen des Texts, Textgröße, Textfarbe und Typface
      //  textView.setText(textLayer.getText());
        textView.setText(spannableString);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textLayer.getFont().getSize() * canvasWidth);
        textView.setTextColor(textLayer.getFont().getColor());
        textView.setTypeface(fontProvider.getTypeface(textLayer.getFont().getTypeface()));

        // Messen der TextView-Abmessungen basierend auf dem verfügbaren Platz
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(boundsWidth, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = textView.getMeasuredWidth();
        int measuredHeight = textView.getMeasuredHeight();

        // Erstellen eines neuen Bitmaps basierend auf den gemessenen Abmessungen
        Bitmap bmp;
        if (reuseBmp != null && reuseBmp.getWidth() == measuredWidth && reuseBmp.getHeight() == measuredHeight) {
            bmp = reuseBmp;
            bmp.eraseColor(Color.TRANSPARENT);
        } else {
            bmp = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        }

        // Zeichnen der TextView auf das Bitmap
        Canvas canvas = new Canvas(bmp);
        textView.layout(0, 0, measuredWidth, measuredHeight);
        textView.draw(canvas);

        return bmp;
    }
    @Override
    @NonNull
    public TextLayer getLayer() {
        return (TextLayer) layer;
    }

    @Override
    protected void drawContent(@NonNull Canvas canvas, @Nullable Paint drawingPaint) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, matrix, drawingPaint);
        }
    }

    @Override
    public int getWidth() {
        return bitmap != null ? bitmap.getWidth() : 0;
    }

    @Override
    public int getHeight() {
        return bitmap != null ? bitmap.getHeight() : 0;
    }

    public void updateEntity() {
        updateEntity(true);
    }

    @Override
    public void release() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }
}