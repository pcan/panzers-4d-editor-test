/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.HasChildren;
import it.panzers.scene.main.enumerations.SceneTypeEnum;
import it.panzers.scene.main.exceptions.ElementInstantiationException;
/**
 *
 * @author Pierantonio
 */
@ChildOf({SCEN.class})
@HasChildren
public class MESH extends AbstractPlaceableEntity {

    public MESH(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
        ctx.setSceneType(SceneTypeEnum.UNIT_SCENE);
    }

    public UnitVERT getVertexes() {
        return getChildOfClass(UnitVERT.class);
    }

    public INDI getIndexes() {
        return getChildOfClass(INDI.class);
    }
    
    public MTLS getMtls() {
        return getChildOfClass(MTLS.class);
    }

    public boolean isTriangleStripe() {
        return getMtls().isTriangleStripe();
    }

}
