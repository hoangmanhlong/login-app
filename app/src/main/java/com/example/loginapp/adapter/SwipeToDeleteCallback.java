package com.example.loginapp.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

abstract public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {

    private final Paint mClearPaint;

    protected SwipeToDeleteCallback() {
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.LEFT);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        float translationX = dX;
        float viewSwipeWidth = Math.min(-dX, itemView.getWidth() * (1.0f / 5.0f));

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX <= 0) {
            Paint paint = new Paint();
            int deleteBackgroundColor = Color.RED;
            int similarBackgroundColor = Color.parseColor("#FFA500");
            RectF similarBackground = new RectF(
                    (float) itemView.getRight() - (2 * viewSwipeWidth),
                    (float) itemView.getTop(),
                    (float) itemView.getRight() - viewSwipeWidth,
                    (float) itemView.getBottom()
            );

            RectF deleteBackground = new RectF(
                    (float) itemView.getRight() - viewSwipeWidth,
                    (float) itemView.getTop(),
                    (float) itemView.getRight(),
                    (float) itemView.getBottom()
            );

            paint.setColor(similarBackgroundColor);
            c.drawRect(similarBackground, paint);

            paint.setColor(deleteBackgroundColor);
            c.drawRect(deleteBackground, paint);

            // Vẽ chữ "Similar" và "Delete" ở giữa vùng bấm
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(40); // Kích thước chữ
            textPaint.setTextAlign(Paint.Align.CENTER);

            // Tính toán vị trí và vẽ chữ "Similar"
            float similarTextX = similarBackground.centerX();
            float textY = similarBackground.centerY() - (textPaint.descent() + textPaint.ascent()) / 2;
            c.drawText("Similar", similarTextX, textY, textPaint);

            // Tính toán vị trí và vẽ chữ "Delete"
            float deleteTextX = deleteBackground.centerX();
            c.drawText("Delete", deleteTextX, textY, textPaint);
            translationX = -(2* viewSwipeWidth);

        }

        if (translationX == -(2 * viewSwipeWidth)) translationX = -(2 * viewSwipeWidth);
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);
    }
}
