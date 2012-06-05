package au.paper.and;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Bounce;

public class AndPaperService extends  WallpaperService {

	private final Handler mHandler = new Handler();
	
    @Override
    public void onCreate() {
        super.onCreate();
    }
    	
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
	@Override
	public Engine onCreateEngine() {
		// TODO Auto-generated method stub
		return new CubeEngine();
	}

	class CubeEngine extends Engine {

		private final Paint mPaint = new Paint();
		private float mOffset;
        private float mTouchX = -1;
        private float mTouchY = -1;
		private long mStartTime;
        private float mCenterX;
        private float mCenterY;
        private final Bitmap bitmap;
        private final Matrix matrix = new Matrix();    
        private Particle particle1 = new Particle();
        private Particle particle2 = new Particle();
		
        private final Runnable mDrawCube = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;
		private TweenManager manager;
        
        CubeEngine() {
            // Create a Paint to draw the lines for our cube
            final Paint paint = mPaint;
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            bitmap =  BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            mStartTime = SystemClock.elapsedRealtime();
            mOffset = 0;
            Tween.registerAccessor(Particle.class, new ParticleAccessor());
         // We need a manager to handle every tween.

            manager = new TweenManager();

            // We can now create as many interpolations as we need !

            Tween.to(particle1, ParticleAccessor.POSITION_XY, 1.0f)
                .target(100, 200)
                .start(manager);
                
            Tween.to(particle2, ParticleAccessor.POSITION_XY, 0.5f)
                .target(0, 0)
                .ease(Bounce.OUT)
                .delay(1.0f)
                .repeatYoyo(2, 0.5f)
                .start(manager);
        }
        
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			mHandler.removeCallbacks(mDrawCube);
		}
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			// TODO Auto-generated method stub
			super.onCreate(surfaceHolder);
			// By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
		}
		
		



		@Override
		public void onVisibilityChanged(boolean visible) {
			// TODO Auto-generated method stub
			mVisible = visible;
		    if (visible) {
		        drawFrame();
		    } else {
		        mHandler.removeCallbacks(mDrawCube);
		    }
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			// TODO Auto-generated method stub
			super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            drawFrame();
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			super.onSurfaceCreated(holder);
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawCube);
		}

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            drawFrame();
        }
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mTouchX = event.getX();
				mTouchY = event.getY();
			} else {
				mTouchX = -1;
				mTouchY = -1;
			}
			// TODO Auto-generated method stub
			super.onTouchEvent(event);
		}
        
		/*
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    // draw something
                    //drawCube(c);
                	//drawRobot(c);
                    mOffset += 5 ;
                    c.drawColor(0xff000000);  
                	//drawRobot2(c);
                	//drawRobot3(c);
                	//drawRobot4(c);
                	drawRobot5(c);
                    drawTouchPoint(c);
                    
                    if (mOffset > 25 * 5 * (mCenterX * 2 / 5) ) {
                    	mOffset = 0;
                    }
                    long now = SystemClock.elapsedRealtime();
                    manager.update((float)(now - mStartTime));
                }
            } finally {
                if (c != null) holder.unlockCanvasAndPost(c);
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawCube);
            if (mVisible) {
                mHandler.postDelayed(mDrawCube, 1000 / 25);
            }
        }
        
        void drawRobot(Canvas c) {
        	  c.save();
              c.drawColor(0xff000000);
              long now = SystemClock.elapsedRealtime();
              
              c.rotate(10000/360 * ((float)(now - mStartTime)) / 1000, mCenterX  + mOffset, mCenterY);
              mPaint.setFilterBitmap(true);
              c.drawBitmap(bitmap, mCenterX - bitmap.getWidth()/2 + mOffset, mCenterY - bitmap.getHeight()/2, mPaint);              
              c.restore();              
        }
        
        void drawRobot2(Canvas c) {
      	              
            long now = SystemClock.elapsedRealtime();
            mPaint.setFilterBitmap(true);    
            matrix.reset();
            matrix.setRotate(10000/360 * ((float)(now - mStartTime)) / 1000, bitmap.getWidth()/2, bitmap.getHeight()/2);
            matrix.postTranslate((float) (500 * Math.sin(mOffset / 500)), (float) (Math.sin(mOffset / 20) * 10) + 150);
            c.drawBitmap(bitmap, matrix, mPaint);
            
        }
        
        void drawRobot3(Canvas c) {
              
            long now = SystemClock.elapsedRealtime();
            mPaint.setFilterBitmap(true);           
            matrix.reset();
            matrix.setRotate(45, bitmap.getWidth()/2, bitmap.getHeight()/2);
            matrix.postTranslate((float) (500 * Math.sin(mOffset / 500)), mCenterY * 2 - mOffset);
            c.drawBitmap(bitmap, matrix, mPaint);
        }
        
        void drawRobot4(Canvas c) {
            
            long now = SystemClock.elapsedRealtime();
            mPaint.setFilterBitmap(true);           
            matrix.reset();
            matrix.postTranslate((float) (500 * Math.sin(mOffset / 500)),  -300 + (float) (mCenterY * 2 + 40 * Math.abs(Math.sin(mOffset / 5))));
            c.drawBitmap(bitmap, matrix, mPaint);
        }
        
        void drawRobot5(Canvas c) {
            
            long now = SystemClock.elapsedRealtime();
            mPaint.setFilterBitmap(true);           
            matrix.reset();
            matrix.postTranslate(particle1.getX(),particle1.getY());
            c.drawBitmap(bitmap, matrix, mPaint);
        }
        
        /*
         * Draw a wireframe cube by drawing 12 3 dimensional lines between
         * adjacent corners of the cube
         */
        void drawCube(Canvas c) {
            c.save();
            c.translate(mCenterX, mCenterY);
            c.drawColor(0xff000000);
            drawLine(c, -400, -400, -400,  400, -400, -400);
            drawLine(c,  400, -400, -400,  400,  400, -400);
            drawLine(c,  400,  400, -400, -400,  400, -400);
            drawLine(c, -400,  400, -400, -400, -400, -400);

            drawLine(c, -400, -400,  400,  400, -400,  400);
            drawLine(c,  400, -400,  400,  400,  400,  400);
            drawLine(c,  400,  400,  400, -400,  400,  400);
            drawLine(c, -400,  400,  400, -400, -400,  400);

            drawLine(c, -400, -400,  400, -400, -400, -400);
            drawLine(c,  400, -400,  400,  400, -400, -400);
            drawLine(c,  400,  400,  400,  400,  400, -400);
            drawLine(c, -400,  400,  400, -400,  400, -400);
            c.restore();
        }

        /*
         * Draw a 3 dimensional line on to the screen
         */
        void drawLine(Canvas c, int x1, int y1, int z1, int x2, int y2, int z2) {
            long now = SystemClock.elapsedRealtime();
            float xrot = ((float)(now - mStartTime)) / 1000;
            float yrot = (0.5f - mOffset) * 2.0f;
            float zrot = 0;

            // 3D transformations

            // rotation around X-axis
            float newy1 = (float)(Math.sin(xrot) * z1 + Math.cos(xrot) * y1);
            float newy2 = (float)(Math.sin(xrot) * z2 + Math.cos(xrot) * y2);
            float newz1 = (float)(Math.cos(xrot) * z1 - Math.sin(xrot) * y1);
            float newz2 = (float)(Math.cos(xrot) * z2 - Math.sin(xrot) * y2);

            // rotation around Y-axis
            float newx1 = (float)(Math.sin(yrot) * newz1 + Math.cos(yrot) * x1);
            float newx2 = (float)(Math.sin(yrot) * newz2 + Math.cos(yrot) * x2);
            newz1 = (float)(Math.cos(yrot) * newz1 - Math.sin(yrot) * x1);
            newz2 = (float)(Math.cos(yrot) * newz2 - Math.sin(yrot) * x2);

            // 3D-to-2D projection
            float startX = newx1 / (4 - newz1 / 400);
            float startY = newy1 / (4 - newz1 / 400);
            float stopX =  newx2 / (4 - newz2 / 400);
            float stopY =  newy2 / (4 - newz2 / 400);

            c.drawLine(startX, startY, stopX, stopY, mPaint);
        }
        
        /*
         * Draw a circle around the current touch point, if any.
         */
        void drawTouchPoint(Canvas c) {
            if (mTouchX >=0 && mTouchY >= 0) {
                c.drawCircle(mTouchX, mTouchY, 60, mPaint);
            }         
        }
	}
}