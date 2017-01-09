/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.structures;

import it.panzers.scene.common.annotations.ArraySize;
import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.exceptions.EditorIOException;
import it.panzers.scene.io.util.StructureTransform;
import it.panzers.scene.main.util.PlaceableEntityTree;

/**
 *
 * @author Luigi
 */
public class TransformMatrix extends AbstractStructure {

    public TransformMatrix() throws EditorIOException {
        super();
    }

    @FieldIndex(1)
    @ArraySize(12)
    private float [] values;


    private float [] matrix = null;

    private float [] rotationMatrix = null;


    public float [] getMatrix4x4f() {
        updateMatrixFromValues();
        return matrix;
    }

    public float [] getRotationMatrix3f3f() {
        if(rotationMatrix == null) {
            rotationMatrix = new float[9];
        }
        StructureTransform.extractRotationMatrixFromMatrix12f(rotationMatrix, values);
        return rotationMatrix;
    }

    public float [] getPosition() {

        return new float[] {values[9], values[10], values[11]};
    }
    private void updateMatrixFromValues(){
        if(matrix == null) {
            matrix = new float[16];
        }
        StructureTransform.convertMatrix12fToMatrix16f(values,matrix);
    }


    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder("Values:\r\n");
        for(int i = 0; i<4; i++) {
            retVal.append(String.format("% .1f  ", values[i*3]));
            retVal.append(String.format("% .1f  ", values[i*3 + 1]));
            retVal.append(String.format("% .1f  ", values[i*3 + 2]));
            retVal.append("\r\n");
        }
        retVal.append("\r\nTransform Matrix:\r\n");
        updateMatrixFromValues();
        for(int i = 0; i<4; i++) {
            retVal.append(String.format("% .1f  ", matrix[i*4]));
            retVal.append(String.format("% .1f  ", matrix[i*4 + 1]));
            retVal.append(String.format("% .1f  ", matrix[i*4 + 2]));
            retVal.append(String.format("% .1f  ", matrix[i*4 + 3]));
            retVal.append("\r\n");
        }

        return retVal.toString();
    }

    public static String getString(float matrix4x4[], int precision) {
        StringBuilder retVal = new StringBuilder("Values:\r\n");

        for(int i = 0; i<4; i++) {
            retVal.append(String.format("% ." + precision + "f  ", matrix4x4[i*4]));
            retVal.append(String.format("% ." + precision + "f  ", matrix4x4[i*4 + 1]));
            retVal.append(String.format("% ." + precision + "f  ", matrix4x4[i*4 + 2]));
            retVal.append(String.format("% ." + precision + "f  ", matrix4x4[i*4 + 3]));
            retVal.append("\r\n");
        }
        retVal.append("\r\n");
        return retVal.toString();
    }
}
