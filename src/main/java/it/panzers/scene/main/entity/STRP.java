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
@ChildOf({MTLS.class}) //mate
@HasChildren
public class STRP extends AbstractSceneElement {

    public STRP(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    private int indexesCount;

    @FieldIndex(1)
    private int startVertex;

    @FieldIndex(2)
    private int endVertex;

    public int getEndVertex() {
        return endVertex;
    }

    public int getIndexesCount() {
        return indexesCount;
    }

    public int getStartVertex() {
        return startVertex;
    }

}
