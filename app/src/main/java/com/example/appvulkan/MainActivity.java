package com.example.appvulkan;

import android.content.Context;
import android.os.SystemClock;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private TextView tvTime;
    private Button btnRender, btnReset;
    private GLRenderer glRenderer;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize GLSurfaceView and UI components
        glSurfaceView = findViewById(R.id.glSurfaceView);
        tvTime = findViewById(R.id.tvTime);
        btnRender = findViewById(R.id.btnRender);
        btnReset = findViewById(R.id.btnReset);

        // Set OpenGL ES 2.0 context
        glSurfaceView.setEGLContextClientVersion(2);

        // Create GLRenderer instance
        glRenderer = new GLRenderer(this);
        glSurfaceView.setRenderer(glRenderer);

        // Initialize start time for render time calculation
        startTime = 0;

        // Set up listener for the "Render" button
        btnRender.setOnClickListener(v -> {
            //startTime = SystemClock.elapsedRealtime(); // Start time when render button is pressed
            glRenderer.requestRender(); // Request a render
        });

        // Set up listener for the "Reset" button
        btnReset.setOnClickListener(v -> {
            glRenderer.reset(); // Đặt lại trạng thái renderer
            glRenderer.requestRender(); // Yêu cầu render lại
            tvTime.setText("Render Time: 0 ms"); // Reset thời gian hiển thị
        });
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

    // Method to update the render time on UI thread
    public void updateRenderTime(long renderTime) {
        String timeText = "Render Time: " + renderTime + " ms";
        tvTime.setText(timeText);
    }
}
