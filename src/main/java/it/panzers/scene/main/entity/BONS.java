/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.structures.Bone13f;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.IndexOfQuantityField;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Luigi
 */
@ChildOf({SKVS.class})
public class BONS extends AbstractSceneElement{

    public BONS(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    private int bonesQuantity;

    @FieldIndex(1)
    @IndexOfQuantityField(0)
    private Bone13f [] bones;

    public Bone13f[] getBones() {
        return bones;
    }

    public void setBones(Bone13f[] bones) {
        this.bones = bones;
    }

    public int getBonesQuantity() {
        if(bones==null) {
            bonesQuantity = 0;
        } else {
            bonesQuantity = bones.length;
        }
        return bonesQuantity;
    }


}
