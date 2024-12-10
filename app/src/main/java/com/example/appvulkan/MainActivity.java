package com.example.appvulkan;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize GLSurfaceView
        glSurfaceView = new GLSurfaceView(this);

        // Set OpenGL ES 2.0 context
        glSurfaceView.setEGLContextClientVersion(2);

        // Set renderer
        glSurfaceView.setRenderer(new GLRenderer(this));

        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}