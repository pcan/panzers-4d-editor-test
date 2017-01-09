/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.structures;

import it.panzers.scene.common.annotations.ArraySize;
import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.exceptions.EditorIOException;

/**
 *
 * @author Pierantonio
 */
public class Struct3x3f extends AbstractStructure {

    public Struct3x3f() throws EditorIOException {
        super();
    }

    @FieldIndex(0)
    @ArraySize(3)
    private float [] vectorA;

    @FieldIndex(1)
    @ArraySize(3)
    private float [] vectorB;

    @FieldIndex(2)
    @ArraySize(3)
    private float [] vectorC;

    public float[] getVectorA() {
        return vectorA;
    }

    public float[] getVectorB() {
        return vectorB;
    }

    public float[] getVectorC() {
        return vectorC;
    }


    public float[] getVector(int index) {
        if(index == 0) {
            return vectorA;
        }
        if(index == 1) {
            return vectorB;
        }
        if(index == 2) {
            return vectorC;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    

}
