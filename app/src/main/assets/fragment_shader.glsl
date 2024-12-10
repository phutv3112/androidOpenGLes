precision mediump float;
uniform sampler2D u_Texture;
varying vec2 v_TexCoord;

void main() {
    vec4 color = texture2D(u_Texture, v_TexCoord);
    float gray = 0.299 * color.r + 0.587 * color.g + 0.114 * color.b;
    gl_FragColor = vec4(gray, gray, gray, 1.0);
}
