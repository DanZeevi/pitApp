package com.example.dan.pitappsf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Dan on 25/01/2018.
 */

class Pit extends ViewGroup {

    private Context context;
    private Paint paint;
    // hold sorted list of all points on screen
    private List<PointView> pointList;
    private Button btnAdd;

    public Pit(Context context) {
        super(context);
        // get context
        this.context = context;

        pointList = new ArrayList<>();
        // set BG color
        setBackgroundColor(Color.WHITE);
        // initialize paint params
        initPaint();
        // add initial 5 points
        addInitialPoints();
    }

    private void addInitialPoints() {
        for (int i = 0; i < 5; i += 1) {
            addPoint((float) (1000 * Math.random()), (float) (1000 * Math.random()));
        }
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setDither(true);
    }

    private void addPoint(float x, float y) {
        // initialize point
        PointView pointView = new PointView(context);
        // set on touch listener
        pointView.setOnTouchListener(mTouchListener);
        // set point x,y
        pointView.init(x, y);
        // add point to list
        pointList.add(pointView);
        // sort list
        Collections.sort(pointList);
        // add point to screen
        addView(pointView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        // add "ADD"  button to screen
        addButtonAdd();
        btnAdd.layout(getWidth() - 300, 16, getWidth() - 16, 16 + 120);

        // initialize points on screen
        for (int counter = 0; counter < pointList.size(); counter += 1) {
            onLayoutPointView(pointList.get(counter));
        }
    }

    private void addButtonAdd() {
        // button "ADD"
        if (btnAdd == null) {
            btnAdd = new Button(getContext());
            btnAdd.setText("ADD");
            btnAdd.setTextColor(Color.BLACK);
            btnAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    // add new point at (0,0)
                    addPoint(getWidth() / 2, getHeight() / 2);
                }
            });
            addView(btnAdd);
        }
    }

    private void onLayoutPointView(PointView pointView) {
        // get point view coordinates for layout
        final int left = (int) pointView.myLeft;
        final int top = (int) pointView.myTop;
        final int right = (int) pointView.myRight;
        final int bottom = (int) (pointView.myBottom);
        // layout point on screen
        pointView.layout(left, top, right, bottom); //right / 2, top, right, bottom / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw x and y axis
        drawAxis(canvas);
        // draw edges between points
        drawEdges(canvas);
        super.onDraw(canvas);
    }

    private void drawEdges(Canvas canvas) {
        // set color
        paint.setColor(Color.BLUE);
        float x1, y1, x2, y2;
        if (pointList.size() > 0) {
            // get edge starting point
            x1 = pointList.get(0).myX;
            y1 = pointList.get(0).myY;
            for (int i = 1; i < pointList.size(); i += 1) {
                // get edge end point
                x2 = pointList.get(i).myX;
                y2 = pointList.get(i).myY;
                // draw line
                canvas.drawLine(x1, y1, x2, y2, paint);
                // get edge starting point
                x1 = x2;
                y1 = y2;
            }
        }
    }

    private void drawAxis(Canvas canvas) {
        // set color
        paint.setColor(Color.BLACK);
        // set line width
        paint.setStrokeWidth(5);
        // draw x-axis
        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, paint);
        // draw y-axis
        canvas.drawLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight(), paint);
    }

    class PointView extends View implements Comparable<PointView> {

        private ShapeDrawable mCircle;
        private final int RADIUS = 20;

        // parent view need to init values:
        // save both view corners (left top right bottom) and circle center (x, y)
        private float myLeft, myTop, myRight, myBottom, myX, myY;

        public PointView(Context context) {
            super(context);
        }

        public void moveTo(float x, float y) {
            // move view on screen
            setLeft((int) x - RADIUS);
            setTop((int) y - RADIUS);
            setRight(getLeft() + 2 * RADIUS);
            setBottom(getTop() + 2 * RADIUS);
            // save new view coordinates
            setMyXY(x, y);
        }

        protected void changeColor() {
            mCircle.getPaint().setColor(
                    mCircle.getPaint().getColor() == Color.RED ? Color.BLUE : Color.RED);
            invalidate();
        }

        public void init(float x, float y) {
            // initialize view params: left and top point in view group
            // and initialize circle shape

            myX = x;
            myY = y;
            myLeft = x - RADIUS;
            myTop = y - RADIUS;
            myRight = x + RADIUS;
            myBottom = y + RADIUS;
            // init circle
            mCircle = new ShapeDrawable(new OvalShape());
            // set circle color
            mCircle.getPaint().setColor(Color.BLUE);
            // set bounds of circle relative to the point view itself
            mCircle.setBounds(0, 0, 0 + 2 * RADIUS, 0 + 2 * RADIUS);
        }

        protected void onDraw(Canvas canvas) {
            // draw single point
            mCircle.draw(canvas);
        }

        private void setMyXY(float x, float y) {
            // set center x,y
            this.myX = x;
            this.myY = y;
            // set view corners
            this.myLeft = x - RADIUS;
            this.myRight = x + RADIUS;
            this.myTop = y - RADIUS;
            this.myBottom = y + RADIUS;
        }

        @Override
        public boolean performClick() {
            return super.performClick();
        }

        @Override
        public int compareTo(@NonNull PointView pointView) {
            // compare x1 =? x2
            float result = this.myX - pointView.myX;
            return ((int) result);
        }

        @Override
        public boolean equals(Object obj) {
            // two points equal if x1=x2
            if (obj instanceof PointView) {
                return (this.myX == ((PointView) obj).myX);
            }
            return super.equals(obj);
        }
    }

        private final OnTouchListener mTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // get touch x,y and point
                final float x = motionEvent.getRawX();
                final float y = motionEvent.getRawY();
                final PointView pointView = (PointView) view;

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // point touch- change color for indication
                        pointView.changeColor();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        // move point to new x,y
                        pointView.moveTo(x, y);
                        // sort point list by x location
                        Collections.sort(pointList);
                        // render on screen
                        postInvalidate();
                        return true;
                    case MotionEvent.ACTION_UP:
                        // point touch stop- change color back for indication
                        pointView.changeColor();
                        return true;
                }
                return false;
            }
        };
    }
