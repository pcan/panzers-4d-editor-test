/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.util;

import it.panzers.scene.io.structures.Struct3f;
import it.panzers.scene.io.structures.Struct3x3f;

/**
 *
 * @author Luigi
 */
public class StructureTransform {

        /**
     *
     * @return 4x4 transform matrix
     */
    public static float[] getTransformMatrix(Struct3x3f matrix3x3f, Struct3f position) {
        float matr[] = new float[16];
        for (int i = 0; i < 3; i++) {
            float[] vector = matrix3x3f.getVector(i);
            matr[i] = vector[0];
            matr[4 + i] = vector[1];
            matr[8 + i] = vector[2];
        }
        matr[3] = position.getVector()[0];
        matr[7] = position.getVector()[1];
        matr[11] = position.getVector()[2];
        matr[15] = 1;
        return matr;
    }



    public static void convertMatrix12fToMatrix16f(float [] matrix12f, float [] matrix16f) {
        if(matrix12f.length != 12 || matrix16f.length != 16) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for(int i = 0; i<3; i++) {
            matrix16f[i*4    ] = matrix12f[i];
            matrix16f[i*4 + 1] = matrix12f[i + 3];
            matrix16f[i*4 + 2] = matrix12f[i + 6];
            matrix16f[i*4 + 3] = matrix12f[i + 9];
        }
        matrix16f[12] = 0;
        matrix16f[13] = 0;
        matrix16f[14] = 0;
        matrix16f[15] = 1;

    }

    public static void extractRotationMatrixFromMatrix12f(float [] matrix9f, float [] matrix12f) {
        if(matrix12f.length != 12 || matrix9f.length != 9) {
            throw new ArrayIndexOutOfBoundsException();
        }
        for(int i = 0; i<3; i++) {
            matrix9f[i*3    ] = matrix12f[i];
            matrix9f[i*3 + 1] = matrix12f[i + 3];
            matrix9f[i*3 + 2] = matrix12f[i + 6];
        }

    }
}
