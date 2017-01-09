/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering;

import it.panzers.scene.main.entity.AbstractPlaceableEntity;
import it.panzers.scene.main.entity.SceneContext;
import java.util.List;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.Group;

/**
 *
 * @author Luigi
 */
public class SceneWrapper {

    private final SceneContext ctx;
    private final Group bg = new BranchGroup();
    

    public SceneWrapper(SceneContext ctx) {
        this.ctx = ctx;
        updateBranchGroup();
    }

    public final SceneContext getContext() {
        return ctx;
    }

    private void updateBranchGroup() {
        List<AbstractPlaceableEntity> rootLevel = getContext().getPlaceableEntityTree().getChildren(-1);
        convertEntitiesRecursively(bg, rootLevel);
    }

    private void convertEntitiesRecursively(Group parentGroup, List<AbstractPlaceableEntity> entities) {
        for(AbstractPlaceableEntity entity : entities) {
            Group childGroup = convertEntity(entity);
            parentGroup.addChild(childGroup);
            List<AbstractPlaceableEntity> children = getContext().getPlaceableEntityTree().getChildren(entity);
            convertEntitiesRecursively(childGroup, children);
        }
    }
    
    private Group convertEntity(AbstractPlaceableEntity entity) {
        return null;
    }


}
