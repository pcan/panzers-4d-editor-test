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
 * @author Luigi
 */
public class SkinnedVertex extends Vertex8f{

    public SkinnedVertex() throws EditorIOException {
        super();
    }

    @FieldIndex(3)
    @ArraySize(4)
    private byte [] bonesIndexes;

    @FieldIndex(4)
    @ArraySize(4)
    private float [] bonesWeights;

    @FieldIndex(5)
    @ArraySize(4)
    private byte [] mtblBonesIndexes;

    @FieldIndex(6)
    @ArraySize(4)
    private float [] mtblBonesWeights;


    public byte[] getBonesIndexes() {
        return bonesIndexes;
    }

    public float[] getBoneIndexesFloat(){
        float [] array=new float[4];
        for(int i = 0; i<4; i++) {
            array[i] = (bonesIndexes[i] & 0xFF);
        }
        return array;
    }

    public float[] getBonesWeights() {
        return bonesWeights;
    }


    public byte[] getMtblBonesIndexes() {
        return mtblBonesIndexes;
    }


    public float[] getMtblBonesWeights() {
        return mtblBonesWeights;
    }

    

}
