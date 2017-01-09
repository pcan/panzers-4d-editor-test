/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.panzers.scene.main.entity;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.IndexOfQuantityField;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Luigi
 */
@ChildOf({MESH.class, SKVS.class})
public class INDI extends AbstractSceneElement {

    public INDI(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }
    
    @FieldIndex(0)
    private int indexesQuantity;

    @FieldIndex(1)
    @IndexOfQuantityField(0)
    private short[] indexes;

    public int getIndexesQuantity() {
        if (indexes == null) {
            indexesQuantity = 0;
        } else {
            indexesQuantity = indexes.length;
        }
        return indexesQuantity;
    }

    public short[] getIndexes() {
        return indexes;
    }

    @SuppressWarnings("unchecked")
    public int[] getIntegerIndexes(){
        int[] integerIndexes = new int[getIndexesQuantity()];
        for(int i=0;i<indexesQuantity;i++) {
            integerIndexes[i] = indexes[i] & 0xFFFF;
        }
        return integerIndexes;
    }

    public void setIndexes(short[] indexes) {
        this.indexes = indexes;
    }
}
