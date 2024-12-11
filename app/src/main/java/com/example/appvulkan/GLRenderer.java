package com.example.appvulkan;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    private final MainActivity mainActivity;
    private int program;
    private int texture;
    private FloatBuffer vertexBuffer, texCoordBuffer;

    private static final float[] VERTICES = {
            -1.0f,  1.0f, -1.0f, -1.0f,  1.0f, -1.0f,  1.0f,  1.0f
    };

    private static final float[] TEX_COORDS = {
            0.0f,  0.0f,  0.0f,  1.0f,  1.0f,  1.0f,  1.0f,  0.0f
    };

    private boolean isRendering = false; // Flag to control render state

    public GLRenderer(Context context) {
        this.context = context;
        this.mainActivity = (MainActivity) context;

        vertexBuffer = ByteBuffer.allocateDirect(VERTICES.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(VERTICES);
        vertexBuffer.position(0);

        texCoordBuffer = ByteBuffer.allocateDirect(TEX_COORDS.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_COORDS);
        texCoordBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 1);

        int vertexShader = ShaderUtils.compileShader(GLES20.GL_VERTEX_SHADER, ShaderUtils.readShaderFile(context, "vertex_shader.glsl"));
        int fragmentShader = ShaderUtils.compileShader(GLES20.GL_FRAGMENT_SHADER, ShaderUtils.readShaderFile(context, "fragment_shader.glsl"));

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        GLES20.glUseProgram(program);

        texture = TextureUtils.loadTexture(context, R.drawable.pexels2);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (!isRendering) {
            return; // Do nothing if not rendering
        }

        long startTime = SystemClock.elapsedRealtime(); // Start the timer

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(program);

        int positionHandle = GLES20.glGetAttribLocation(program, "a_Position");
        int texCoordHandle = GLES20.glGetAttribLocation(program, "a_TexCoord");
        int textureHandle = GLES20.glGetUniformLocation(program, "u_Texture");

        GLES20.glEnableVertexAttribArray(positionHandle);
        GLES20.glEnableVertexAttribArray(texCoordHandle);

        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(textureHandle, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        GLES20.glDisableVertexAttribArray(positionHandle);
        GLES20.glDisableVertexAttribArray(texCoordHandle);

        long endTime = SystemClock.elapsedRealtime(); // End the timer
        long renderTime = endTime - startTime; // Calculate render time

        // Update render time on UI thread
        mainActivity.runOnUiThread(() -> mainActivity.updateRenderTime(renderTime));

        isRendering = false; // Reset the rendering flag
    }

    // Request to start rendering
    public void requestRender() {
        isRendering = true;
    }

    // Method to reset the renderer (reset image and time)
    public void reset() {
        isRendering = false; // Dừng mọi quá trình render đang diễn ra
        texture = TextureUtils.loadTexture(context, R.drawable.pexels2); // Tải lại texture gốc
        isRendering = true; // Kích hoạt render lại
    }

}
