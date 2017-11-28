package com.codegemz.elfi.coreapp.helper.face_detection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.codegemz.elfi.coreapp.BrainActivity;
import com.codegemz.elfi.coreapp.BrainApp;
import com.codegemz.elfi.coreapp.R;
import com.codegemz.elfi.coreapp.api.behavior_processor.movement.BodyConnectionHelper.Connector;
import com.codegemz.elfi.coreapp.helper.voice_recognition.TalkStateHelper;
import com.codegemz.elfi.coreapp.helper.voice_recognition.VoiceRecoHelper;
import com.codegemz.elfi.model.TalkStateType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by adrobnych on 5/20/15.
 */
public class CamPreviewView extends SurfaceView implements SurfaceHolder.Callback
        , Camera.PreviewCallback {

    private static final String LIFEPIXEL = "com.elfirobotics.beehive.lifepixel";
    private static final String EVENT = "com.elfirobotics.beehive.lifepixel.sense_intents.EVENT";
    private static final String RAW_VISITOR_ACTION = "com.elfirobotics.beehive.sense_intents.VISITOR.RAW";

    private static final String TAG = "Preview";
    private static final String YODA_ADJUST_INTENT = "io.elfix.sense_intents.BOTTOM_MOTTORS.ADJUST";
    private static final String YODA_ADJUST_INTENT_EXTRA = "io.elfix.sense_intents.extras.BOTTOM_MOTTORS.ADJUST_DIR";
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Bitmap mWorkBitmap;
    private Bitmap mMonkeyImage;

    private static final int NUM_FACES = 6; //32; // max is 64
    private static final boolean DEBUG = true;

    private FaceDetector mFaceDetector;
    private FaceDetector.Face[] mFaces = new FaceDetector.Face[NUM_FACES];
    private FaceDetector.Face face = null;      // refactor this to the callback

    private PointF eyesMidPts[] = new PointF[NUM_FACES];
    private float  eyesDistance[] = new float[NUM_FACES];

    private Paint tmpPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint pOuterBullsEye = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint pInnerBullsEye = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int picWidth, picHeight;
    private float ratio, xRatio, yRatio;

    private TalkStateHelper talkStateHelper;
    private Context context;

    public CamPreviewView(Context context) {
        super(context);
        this.context = context;
        Log.d(TAG, "Preview");
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setFormat(ImageFormat.NV21);


        tmpPaint.setStyle(Paint.Style.STROKE);

        mMonkeyImage = BitmapFactory.decodeResource(getResources(), R.drawable.smile);

        picWidth = mMonkeyImage.getWidth();
        picHeight = mMonkeyImage.getHeight();

        talkStateHelper = new TalkStateHelper(context);

    }

    public void obtainCamera() {
        //mCamera = Camera.open();
        if (Camera.getNumberOfCameras() >= 2)
            //if you want to open front facing camera use this line
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        else
            //if you want to use the back facing camera
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);


    }



    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated Surface is: " + mHolder.getSurface().getClass().getName());

        // The Surface has been created, acquire the camera and tell it where
        // to draw.
        if(mCamera != null){
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException exception) {
                mCamera.release();
                mCamera = null;
                // TODO: add more exception handling logic here
            }
        }
        setWillNotDraw(false);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");

    }

    public void releaseCamera(){
        if(mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallbackWithBuffer(null);
            mCamera.release();
            mCamera = null;

        }
        setWillNotDraw(true);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        Log.d(TAG, String.format("surfaceChanged: format=%d, w=%d, h=%d", format, w, h));

        if(mCamera != null){
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            // TODO: the obtained size is dependant on the current orientation, and that's bad
            Camera.Parameters parameters = mCamera.getParameters();

            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(sizes, w, h);
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);

            mCamera.setParameters(parameters);
            //TODO: test if front camera is enabled
            mCamera.startPreview();

            // Setup the objects for the face detection
            mWorkBitmap = Bitmap.createBitmap(optimalSize.width, optimalSize.height, Bitmap.Config.RGB_565);
            mFaceDetector = new FaceDetector(optimalSize.width, optimalSize.height, NUM_FACES);

            int bufSize = optimalSize.width * optimalSize.height *
                    ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8;
            byte[] cbBuffer = new byte[bufSize];
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.addCallbackBuffer(cbBuffer);
        }
    }

    /* Camera.PreviewCallback implementation */
    public void onPreviewFrame(byte[] data, Camera camera) {
        //Log.d(TAG, "onPreviewFrame");

        // face detection: first convert the image from NV21 to RGB_565
        YuvImage yuv = new YuvImage(data, ImageFormat.NV21,
                mWorkBitmap.getWidth(), mWorkBitmap.getHeight(), null);
        Rect rect = new Rect(0, 0, mWorkBitmap.getWidth(),
                mWorkBitmap.getHeight());	// TODO: make rect a member and use it for width and height values above

        // TODO: use a threaded option or a circular buffer for converting streams?  see http://ostermiller.org/convert_java_outputstream_inputstream.html
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        if (!yuv.compressToJpeg(rect, 100, baout)) {
            Log.e(TAG, "compressToJpeg failed");
        }
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inPreferredConfig = Bitmap.Config.RGB_565;
        mWorkBitmap = BitmapFactory.decodeStream(
                new ByteArrayInputStream(baout.toByteArray()), null, bfo);

        Arrays.fill(mFaces, null);
        Arrays.fill(eyesMidPts, null);

        mFaceDetector.findFaces(mWorkBitmap, mFaces);

        float maxEyesDistance = -1;
        float maxEyesPosX = -1;

        for (int i = 0; i < mFaces.length; i++)
        {
            face = mFaces[i];
            try {
                PointF eyesMP = new PointF();
                face.getMidPoint(eyesMP);
                eyesDistance[i] = face.eyesDistance();
                eyesMidPts[i] = eyesMP;

                if(maxEyesDistance < eyesDistance[i]) {
                    maxEyesDistance = eyesDistance[i];
                    maxEyesPosX = eyesMP.x;
                }

                if (DEBUG)
                {
                    Log.i("Face",
                            i +  " " + face.confidence() + " " + face.eyesDistance() + " "
                                    + "Pose: ("+ face.pose(FaceDetector.Face.EULER_X) + ","
                                    + face.pose(FaceDetector.Face.EULER_Y) + ","
                                    + face.pose(FaceDetector.Face.EULER_Z) + ")"
                                    + "Eyes Midpoint: ("+eyesMidPts[i].x + "," + eyesMidPts[i].y +")"
                    );
                }
            }
            catch (Exception e){
                Log.d(TAG, "face detection exception: " + e);
            }
        }

        if(maxEyesDistance != -1) {  // found closest face
            verifyAndSendRawVisitorIntent();
            Log.e(TAG, "eyes mpx: " + maxEyesPosX);
            adjustYodaToFace(maxEyesPosX);
        }

        invalidate(); // use a dirty Rect?

        // Requeue the buffer so we get called again
        mCamera.addCallbackBuffer(data);
    }

    private void adjustYodaToFace(float maxEyesPosX) {
        int w = mWorkBitmap.getWidth();

        Intent adjustIntent = new Intent(YODA_ADJUST_INTENT);

        if(maxEyesPosX > (w/2 + forwardSector) && maxEyesPosX < w) {
            adjustIntent.putExtra(YODA_ADJUST_INTENT_EXTRA, "RIGHT");
            context.sendBroadcast(adjustIntent);
            Log.e(TAG, "adjust right intent thrown");
            return;
        }

        if(maxEyesPosX > 0 && maxEyesPosX < (w/2 - forwardSector)) {
            adjustIntent.putExtra(YODA_ADJUST_INTENT_EXTRA, "LEFT");
            context.sendBroadcast(adjustIntent);
            Log.e(TAG, "adjust left intent thrown");
            return;
        }

        Log.e(TAG, "no adjust needed");
        return;
    }

    private long lastTimeFaceDetectedMillis = 0;

    private void verifyAndSendRawVisitorIntent() {
        long currentMillis = System.currentTimeMillis();
        if(currentMillis - lastTimeFaceDetectedMillis > 5000){
            Intent rawVisitorIntent = new Intent(LIFEPIXEL);
            rawVisitorIntent.putExtra(EVENT, RAW_VISITOR_ACTION);
            context.sendBroadcast(rawVisitorIntent);
            Log.e(TAG, "intent was thrown to " + RAW_VISITOR_ACTION);

            lastTimeFaceDetectedMillis = currentMillis;
        }
    }

    private int[] pixels;

    //target
    int targetR = 140;
    int targetG = 40;
    int targetB = 20;

    int precission = 20;
    int forwardSector = 20;


    String last_command = "pesik_right_step";
    boolean isNewTarget = true;
//    private void findTheBall(Bitmap mWorkBitmap) {
//
//        //Log.e("bmp center: ", "" + mWorkBitmap.getWidth() + ":" + mWorkBitmap.getHeight() );
//
//        pixels = new int[mWorkBitmap.getWidth() * mWorkBitmap.getHeight()];
//        mWorkBitmap.getPixels(pixels, 0, mWorkBitmap.getWidth(), 0, 0, mWorkBitmap.getWidth(), mWorkBitmap.getHeight());
//
//        int pixel = pixels[mWorkBitmap.getWidth()/2 + mWorkBitmap.getWidth() * mWorkBitmap.getHeight()/2];
//        int R1 = (pixel >> 16) & 0xff;
//        int G1 = (pixel >> 8) & 0xff;
//        int B1 = (pixel & 0xff);
//        if(((BrainActivity)context).isTargetFollowing() && isNewTarget) {
//            Log.e("center: ", "(" + R1 + ":" + G1 + ":" + B1 + ")");
//            targetR = R1;
//            targetG = G1;
//            targetB = B1;
//
//            isNewTarget = false;
//        }
//
//        forwardSector = mWorkBitmap.getWidth()/4;
//
//
//
//        String command = null;
//        if(targetFoundInCentralSector()) {
//            command = "pesik_go";
//            if(last_command != command)
//                send_bt(command);
//            last_command = command;
//            return;
//        }
//        if(targetFoundInLeftSector()) {
//            command = "pesik_left_step";
//            if(last_command != command)
//                send_bt(command);
//            last_command = command;
//            return;
//        }
//
//        if(last_command == "pesik_left_step")
//            command = "pesik_left_step";
//        else
//            command = "pesik_right_step";
//        Log.e("find the ball: ", command);
//        send_bt(command);
//        last_command = command;
//    }

//    private void send_bt(String command) {
//        Connector connector = ((BrainApp) context.getApplicationContext()).getConnector();
//        if(connector.connect())
//            connector.writeSingleMessage(command);
//        else{
//            Log.d(connector.getTag(), "BT connection failed");
//        }
//    }
//
//
//    private boolean targetFoundInRightSector() {
//        int w = mWorkBitmap.getWidth();
//        for(int x = w/2 + forwardSector; x < w; x+=5)
//            for(int y = 0; y<mWorkBitmap.getHeight(); y+=10) {
//                int pixel = pixels[x + y * w];
//                int R1 = (pixel >> 16) & 0xff;
//                int G1 = (pixel >> 8) & 0xff;
//                int B1 = (pixel & 0xff);
//
//                if ((R1 > (targetR - precission)) && (R1 < (targetR + precission)) &&
//                        (G1 > (targetG - precission)) && (G1 < (targetG + precission)) &&
//                        (B1 > (targetB - precission)) && (B1 < (targetB + precission))) {
//
//                    Log.e("find the ball: ", "right");
//                    return true;
//                }
//            }
//        return false;
//    }
//
//    private boolean targetFoundInLeftSector() {
//        int w = mWorkBitmap.getWidth();
//        for(int x = 0; x < w/2 - forwardSector; x+=5)
//            for(int y = 0; y<mWorkBitmap.getHeight(); y+=10) {
//                int pixel = pixels[x + y * w];
//                int R1 = (pixel >> 16) & 0xff;
//                int G1 = (pixel >> 8) & 0xff;
//                int B1 = (pixel & 0xff);
//
//                if ((R1 > (targetR - precission)) && (R1 < (targetR + precission)) &&
//                        (G1 > (targetG - precission)) && (G1 < (targetG + precission)) &&
//                        (B1 > (targetB - precission)) && (B1 < (targetB + precission))) {
//
//                    Log.e("find the ball: ", "left");
//                    return true;
//                }
//            }
//        return false;
//    }
//
//    private boolean targetFoundInCentralSector() {
//        int w = mWorkBitmap.getWidth();
//        for(int x = w/2 - forwardSector; x < w/2 + forwardSector; x+=5)
//            for(int y = 0; y<mWorkBitmap.getHeight(); y+=10) {
//                int pixel = pixels[x + y * w];
//                int R1 = (pixel >> 16) & 0xff;
//                int G1 = (pixel >> 8) & 0xff;
//                int B1 = (pixel & 0xff);
//
//                if ((R1 > (targetR - precission)) && (R1 < (targetR + precission)) &&
//                        (G1 > (targetG - precission)) && (G1 < (targetG + precission)) &&
//                        (B1 > (targetB - precission)) && (B1 < (targetB + precission))) {
//
//                    Log.e("find the ball: ", "forward");
//                    return true;
//                }
//            }
//        return false;
//    }

    public void startTalk() {
        if(talkStateHelper.getTalkState() == TalkStateType.Inactive) {
            talkStateHelper.setTalkSate(TalkStateType.Active);
            //Intent startRecoIntent = new Intent(VoiceRecoHelper.START_RECO_NOTIFICATION);
            //getContext().sendBroadcast(startRecoIntent);
        }
        if(talkStateHelper.getTalkState() == TalkStateType.Active) {
            talkStateHelper.setTalkSate(TalkStateType.Active); // touch last update time
        }
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mWorkBitmap != null){
            xRatio = getWidth() * 1.0f / mWorkBitmap.getWidth();
            yRatio = getHeight() * 1.0f / mWorkBitmap.getHeight();

            for (int i = 0; i < eyesMidPts.length; i++){
                if (eyesMidPts[i] != null){
                    ratio = eyesDistance[i] * 4.0f / picWidth;
                    RectF scaledRect =
                            new RectF((mWorkBitmap.getWidth()/2 + (mWorkBitmap.getWidth()/2-eyesMidPts[i].x - picWidth * ratio / 2.0f)) * xRatio,
                            (eyesMidPts[i].y - picHeight * ratio / 2.0f) * yRatio,
                            (mWorkBitmap.getWidth()/2 + (mWorkBitmap.getWidth()/2-eyesMidPts[i].x + picWidth * ratio / 2.0f)) * xRatio,
                            (eyesMidPts[i].y + picHeight * ratio / 2.0f) * yRatio);
                    canvas.drawBitmap(mMonkeyImage, null , scaledRect, tmpPaint);
                }
            }
        }
    }

}
