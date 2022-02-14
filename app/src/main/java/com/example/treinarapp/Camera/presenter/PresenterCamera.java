package com.example.treinarapp.Camera.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.treinarapp.FaceDetection.FaceDetectionResultGlRenderer;
import com.example.treinarapp.FaceMash.FaceMeshResultGlRenderer;
import com.google.mediapipe.formats.proto.LocationDataProto;
import com.google.mediapipe.solutioncore.CameraInput;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.facedetection.FaceDetection;
import com.google.mediapipe.solutions.facedetection.FaceDetectionOptions;
import com.google.mediapipe.solutions.facedetection.FaceDetectionResult;
import com.google.mediapipe.solutions.facedetection.FaceKeypoint;
import com.google.mediapipe.solutions.facemesh.FaceMesh;
import com.google.mediapipe.solutions.facemesh.FaceMeshOptions;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;

import static android.content.ContentValues.TAG;

public class PresenterCamera implements InterfaceCamera.presenter {
    private InterfaceCamera.view view;
    private Context context;

    public PresenterCamera(Context context,InterfaceCamera.view view) {
        this.context = context;
        this.view = view;
    }


    @Override
    public void iniciarFaceDetection() {
        Activity activity = (Activity) context;
        FaceDetectionOptions faceDetectionOptions =
                FaceDetectionOptions.builder()
                        .setStaticImageMode(false)
                        .setModelSelection(0).build();
        FaceDetection faceDetection = new FaceDetection(context, faceDetectionOptions);
        faceDetection.setErrorListener(
                (message, e) -> Log.e(TAG, "MediaPipe Face Detection error:" + message));

        CameraInput camera = new CameraInput(activity);
        camera.setNewFrameListener(
                textureFrame -> faceDetection.send(textureFrame));

        SolutionGlSurfaceView<FaceDetectionResult> glSurfaceView =
                new SolutionGlSurfaceView<>(
                        context, faceDetection.getGlContext(), faceDetection.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new FaceDetectionResultGlRenderer());
        glSurfaceView.setRenderInputImage(true);
        faceDetection.setResultListener(
                faceDetectionResult -> {
                    if (faceDetectionResult.multiFaceDetections().isEmpty()) {
                        return;
                    }
                    LocationDataProto.LocationData.RelativeKeypoint noseTip =
                            faceDetectionResult
                                    .multiFaceDetections()
                                    .get(0)
                                    .getLocationData()
                                    .getRelativeKeypoints(FaceKeypoint.NOSE_TIP);
                    Log.i(
                            TAG,
                            String.format(
                                    "MediaPipe Face Detection nose tip normalized coordinates (value range: [0, 1]): x=%f, y=%f",
                                    noseTip.getX(), noseTip.getY()));
                    // Request GL rendering.
                    glSurfaceView.setRenderData(faceDetectionResult);
                    glSurfaceView.requestRender();
                });

        glSurfaceView.post(() ->
                camera.start(activity,
                        faceDetection.getGlContext(),
                        CameraInput.CameraFacing.FRONT,
                        glSurfaceView.getWidth(),
                        glSurfaceView.getHeight()));

        view.setarFaceDetection(glSurfaceView);
    }

    @Override
    public void iniciarFacemash() {
        Activity activity = (Activity) context;

        FaceMeshOptions faceMeshOptions =
                FaceMeshOptions.builder()
                        .setStaticImageMode(false)
                        .setRefineLandmarks(true)
                        .setMaxNumFaces(1)
                        .setRunOnGpu(true).build();
        FaceMesh faceMesh = new FaceMesh(context, faceMeshOptions);
        faceMesh.setErrorListener(
                (message, e) -> Log.e(TAG, "MediaPipe Face Mesh error:" + message));

        CameraInput cameraInput = new CameraInput(activity);
        cameraInput.setNewFrameListener(
                textureFrame -> faceMesh.send(textureFrame));

        SolutionGlSurfaceView<FaceMeshResult> glSurfaceView =
                new SolutionGlSurfaceView<>(
                        context, faceMesh.getGlContext(), faceMesh.getGlMajorVersion());
        glSurfaceView.setSolutionResultRenderer(new FaceMeshResultGlRenderer());
        glSurfaceView.setRenderInputImage(true);


        faceMesh.setResultListener(
                faceMeshResult -> {
                    glSurfaceView.setRenderData(faceMeshResult);
                    glSurfaceView.requestRender();
                });

        glSurfaceView.post(
                () ->
                        cameraInput.start(
                                activity,
                                faceMesh.getGlContext(),
                                CameraInput.CameraFacing.FRONT,
                                glSurfaceView.getWidth(),
                                glSurfaceView.getHeight()));

        view.setarFacemash(glSurfaceView);
    }
}
