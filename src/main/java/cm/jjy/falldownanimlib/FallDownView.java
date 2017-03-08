package cm.jjy.falldownanimlib;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by JJY on 2017/3/8.
 */

public class FallDownView extends View{
    private ValueAnimator valueAnimator;
    private List<ViewItem> viewItems;
    private int itemnumber=20;
    private int imageId;
    private boolean cycle;
    private int itemHeight=-1;
    private int itemWidth=-1;
    Random random = new Random();

    public FallDownView(Context context) {
        super(context);
        //init();
    }

    public FallDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.falldown);
        imageId = a.getResourceId(R.styleable.falldown_falldownimage, R.drawable.star);
        itemnumber = a.getInteger(R.styleable.falldown_number,1);
        cycle = a.getBoolean(R.styleable.falldown_cycle,false);
        itemHeight = a.getInteger(R.styleable.falldown_itemheight,-1);
        itemWidth = a.getInteger(R.styleable.falldown_itemwidth,-1);

        a.recycle();
    }

    public FallDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    public int getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(int itemnumber) {
        this.itemnumber = itemnumber;
    }

    public List<ViewItem> getViewItems() {
        return viewItems;
    }

    public void setViewItems(List<ViewItem> viewItems) {
        this.viewItems = viewItems;
    }

    public boolean isCycle() {
        return cycle;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public void start(){

        for(int i=0;i<itemnumber;i++) {
            ViewItem viewItem = new ViewItem();
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(),imageId);
            if(itemHeight!=-1 && itemWidth!=-1) {
                Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap,
                        (int) itemWidth, (int) itemHeight, true);
                viewItem.setBitmap(bitmap);
            }else {
                viewItem.setBitmap(originalBitmap);
            }
            int initx = random.nextInt(getWidth());
            int speedx = random.nextInt(10)-5;
            int speedy = random.nextInt(15)+5;
            viewItem.setX(initx);
            viewItem.setY(0);
            viewItem.setSpeedy(speedy);
            viewItem.setSpeedx(speedx);
            viewItems.add(viewItem);
        }
        valueAnimator.start();
    }

    public void init(){
        viewItems = new ArrayList<ViewItem>();
        valueAnimator = new ValueAnimator().ofFloat(0,1);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                for(ViewItem viewItem : viewItems){
                    viewItem.setY(viewItem.getY() + viewItem.getSpeedy());
                    viewItem.setX(viewItem.getX() + viewItem.getSpeedx());
                    viewItem.setSpeedy((float)(viewItem.getSpeedy()+0.25));
                    if (viewItem.getY() >= getBottom()) {
                        if(cycle) {
                            int initx = random.nextInt(getWidth());
                            viewItem.setY(0);
                            viewItem.setX(initx);
                        }else{
                            //valueAnimator.cancel();
                        }
                    }

                }
                invalidate();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(viewItems!=null)
        {
            for(ViewItem viewItem : viewItems){
                canvas.drawBitmap(viewItem.getBitmap(),viewItem.getX(),viewItem.getY(),null);
            }
        }

    }
}
