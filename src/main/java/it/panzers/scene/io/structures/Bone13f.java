/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.structures;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.exceptions.EditorIOException;

/**
 *
 * @author Luigi
 */
public class Bone13f extends TransformMatrix {

    public Bone13f() throws EditorIOException {
        super();
    }

    @FieldIndex(0)
    private int dumyIndex;

    public int getDumyIndex() {
        return dumyIndex;
    }

    public void setDumyIndex(int dumyIndex) {
        this.dumyIndex = dumyIndex;
    }


    public float[] getPosition() {
        float [] retValue = new float[3];
        System.arraycopy(getMatrix4x4f(), 9, retValue, 0, 3);
        return retValue;
    }

}
