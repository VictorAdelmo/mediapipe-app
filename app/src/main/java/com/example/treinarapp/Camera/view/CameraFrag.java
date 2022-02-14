package com.example.treinarapp.Camera.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.treinarapp.Camera.presenter.InterfaceCamera;
import com.example.treinarapp.Camera.presenter.PresenterCamera;
import com.example.treinarapp.R;
import com.google.mediapipe.solutioncore.SolutionGlSurfaceView;
import com.google.mediapipe.solutions.facedetection.FaceDetectionResult;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;

public class CameraFrag extends AppCompatActivity implements InterfaceCamera.view {

    PresenterCamera presenterCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        presenterCamera = new PresenterCamera(this,this);
        presenterCamera.iniciarFacemash();

    }

    @Override
    public void setarFaceDetection(SolutionGlSurfaceView<FaceDetectionResult> renderiza) {
        FrameLayout frameLayout = findViewById(R.id.preview_display_layout);
        frameLayout.removeAllViewsInLayout();
        frameLayout.addView(renderiza);
        renderiza.setVisibility(View.VISIBLE);
        frameLayout.requestLayout();
    }

    @Override
    public void setarFacemash(SolutionGlSurfaceView<FaceMeshResult> renderiza) {
        FrameLayout frameLayout = findViewById(R.id.preview_display_layout);
        frameLayout.removeAllViewsInLayout();
        frameLayout.addView(renderiza);
        renderiza.setVisibility(View.VISIBLE);
        frameLayout.requestLayout();
    }
}
