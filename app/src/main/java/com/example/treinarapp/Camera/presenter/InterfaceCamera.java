package com.example.treinarapp.Camera.presenter;

import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.facedetection.FaceDetectionResult;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;

public interface InterfaceCamera {
    interface presenter {
        void iniciarFaceDetection();
        void iniciarFacemash();
    }
    interface view {
        void setarFaceDetection(SolutionGlSurfaceView<FaceDetectionResult> renderiza);
        void setarFacemash(SolutionGlSurfaceView<FaceMeshResult> glSurfaceView);
    }
}
