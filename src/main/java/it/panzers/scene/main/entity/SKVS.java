/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;
import it.panzers.scene.io.structures.Bone13f;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.HasChildren;
import it.panzers.scene.main.enumerations.SceneTypeEnum;
import it.panzers.scene.main.exceptions.ElementInstantiationException;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Pierantonio
 */
@ChildOf({SCEN.class})
@HasChildren
public class SKVS extends AbstractPlaceableEntity {

    public SKVS(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
        ctx.setSceneType(SceneTypeEnum.HUMAN_MODEL_SCENE);
    }

    public HumanVERT getVertexes() {
        return getChildOfClass(HumanVERT.class);
    }

    public INDI getIndexes() {
        return getChildOfClass(INDI.class);
    }

    public MTLS getMtls() {
        return getChildOfClass(MTLS.class);
    }

    public BONS getBons() {
        return getChildOfClass(BONS.class);
    }

    public boolean isTriangleStripe() {
        return getMtls().isTriangleStripe();
    }

    /**
     *
     * @param bons
     * @return mapping DUMY index -> Bone
     */
    public Map<Integer,Bone13f> getDumyBonesMap() {
        Map<Integer,Bone13f> bonesMap = new HashMap<Integer, Bone13f>();
        BONS bons =  getBons();
        for(Bone13f bone : bons.getBones()) {
            bonesMap.put(bone.getDumyIndex(), bone);
        }
        return bonesMap;
    }
}
