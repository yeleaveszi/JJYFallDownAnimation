/*
 * Copyright (C) 2012 www.apkdv.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cm.jjy.falldownanimlib.flake;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import cm.jjy.falldownanimlib.R;


/**
 * This class is the custom view where all of the Droidflakes are drawn. This class has
 * all of the logic for adding, subtracting, and rendering Droidflakes.
 */
public class FlakeView extends View {

    Bitmap droid;       // The bitmap that all flakes use
    int numFlakes = 0;  // Current number of flakes
    ArrayList<Flake> flakes = new ArrayList<Flake>(); // List of current flakes

    // Animator used to drive all separate flake animations. Rather than have potentially
    // hundreds of separate animators, we just use one and then update all flakes for each
    // frame of that single animation.
    public ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    //long startTime, prevTime; // Used to track elapsed time for animations and fps
    //int frames = 0;     // Used to track frames per second
    //Paint textPaint;    // Used for rendering fps text
    float fps = 0;      // frames per second
    Matrix m = new Matrix(); // Matrix used to translate/rotate each flake during rendering
    String fpsString = "";
    String numFlakesString = "";


    public FlakeView(Context context) {
        super(context);
        droid = BitmapFactory.decodeResource(getResources(), R.drawable.b);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                for (int i = 0; i < flakes.size(); ++i) {
                    Flake flake = flakes.get(i);
                    flake.y += 5;
                    if (flake.y > getHeight()) {
                        // If a flake falls off the bottom, send it back to the top
                        flake.y = 0 - flake.height;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed);
                }
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
    }

    public FlakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
        numFlakesString = "numFlakes: " + numFlakes;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        flakes.clear();
        numFlakes = 0;
       for (int i = 0; i < 25; ++i) {
            Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);
            Bitmap bitmap= Bitmap.createScaledBitmap(originalBitmap,
                    (int) 350, (int) 350, true);
            flakes.add(Flake.createFlake(getWidth(), bitmap,getContext()));
        }
        setNumFlakes(numFlakes + 25);
        animator.cancel();
        //frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            canvas.drawBitmap(flake.bitmap, flake.x,flake.y, null);
        }
    }

    public void pause() {
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

}
