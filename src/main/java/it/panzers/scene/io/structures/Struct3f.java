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
public class Struct3f extends AbstractStructure {

    public Struct3f() throws EditorIOException {
        super();
    }

    @FieldIndex(0)
    @ArraySize(3)
    private float [] vector;

    public float[] getVector() {
        return vector;
    }


}
