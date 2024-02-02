//package com.example.loginapp.adapter;
//
//public class MyItemTouchHelper extends ItemTouchHelper.SimpleCallback {
//    private MyAdapter mAdapter;
//    private Paint mPaint = new Paint ();
//    private Context mContext;
//
//    public MyItemTouchHelper (Context context, MyAdapter adapter) : base (ItemTouchHelper.ActionStateIdle, ItemTouchHelper.Left | ItemTouchHelper.Right)  {
//        this.mContext = context;
//        this.mAdapter = adapter;
//    }
//
//    public override bool OnMove (RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)  {
//        return false;
//    }
//
//    public override void OnSwiped (RecyclerView.ViewHolder viewHolder, int direction)  {
//    }
//
//    public override void OnChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, bool isCurrentlyActive)  {
//        float translationX = dX;
//        View itemView = viewHolder.ItemView;
//        float height = (float)itemView.Bottom - (float)itemView.Top;
//
//        if (actionState == ItemTouchHelper.ActionStateSwipe && dX <= 0) // Swiping Left
//        {
//            translationX = -Math.Min (-dX, height * 2);
//            mPaint.Color = Color.RED;
//            RectF background = new RectF ( (float)itemView.Right + translationX, (float)itemView.Top, (float)itemView.Right, (float)itemView.Bottom);
//            c.DrawRect (background, mPaint);
//        }
//        else if (actionState == ItemTouchHelper.ActionStateSwipe && dX > 0) // Swiping Right
//        {
//            translationX = Math.Min (dX, height * 2);
//            mPaint.Color = Color.BLUE;
//            RectF background = new RectF ( (float)itemView.Left, (float)itemView.Top, (float)itemView.Left + translationX, (float)itemView.Bottom);
//            c.DrawRect (background, mPaint);
//        }
//
//        if (Math.Abs(translationX) >= 100) {
//            translationX = -100;
//        }
//
//        base.OnChildDraw (c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
//    }
//}
