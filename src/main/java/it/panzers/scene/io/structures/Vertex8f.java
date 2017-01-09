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
public class Vertex8f extends AbstractStructure {

    public Vertex8f() throws EditorIOException {
        super();
    }

    @FieldIndex(0)
    @ArraySize(3)
    private float [] coord;

    @FieldIndex(1)
    @ArraySize(3)
    private float [] normal;

    @FieldIndex(2)
    @ArraySize(2)
    private float [] textureCoord;

    public float[] getCoord() {
        return coord;
    }

    public float[] getNormal() {
        return normal;
    }

    public float[] getTextureCoord() {
        return textureCoord;
    }

    
}
