uniform mat4 skinMatrices[32];
attribute vec4 boneIndexes;
attribute vec4 boneWeights;

varying vec3 normal;
varying vec3 viewvec;

void main(void)
{
    gl_Position = vec4 (0, 0, 0, 0);
    ivec4 ibonesidx = ivec4(boneIndexes);
    for (int i = 0; i < 4; i++)
    {
        gl_Position += skinMatrices[ibonesidx[i]] * gl_Vertex * boneWeights[i];
    }

    gl_Position = gl_ModelViewProjectionMatrix * gl_Position;

    gl_FrontColor = vec4(1,1,0, 1.0);

    normal = gl_NormalMatrix * gl_Normal;
    viewvec = gl_NormalMatrix * vec3 (gl_Vertex);
}