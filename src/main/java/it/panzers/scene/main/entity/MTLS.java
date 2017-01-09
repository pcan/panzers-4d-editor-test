/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.HasChildren;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Luigi
 */
@ChildOf({MESH.class, SKVS.class})
@HasChildren
public class MTLS extends AbstractSceneElement {

    public MTLS(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    private int unknownInt;

    public int getUnknownInt() {
        return unknownInt;
    }

    public boolean isTriangleStripe() {
        return this.hasChildOfClass(STRP.class);
    }
}
