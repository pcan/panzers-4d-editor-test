/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.converters;

import it.panzers.scene.main.entity.AbstractPlaceableEntity;
import it.panzers.scene.main.entity.SCEN;
import it.panzers.scene.main.entity.SceneContext;
import it.panzers.scene.main.exceptions.SceneDataException;
import java.util.List;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.Group;

/**
 *
 * @author Luigi
 */
public class SceneConverter extends AbstractConverter<SCEN>{

    @Override
    public Group convert(SCEN scen) throws SceneDataException {
        BranchGroup bg = new BranchGroup();
        recursivelyConvertElements(bg, -1, scen.getContext());
        return bg;
    }

    private void recursivelyConvertElements(Group parentGroup, int parentEntityId, SceneContext ctx) throws SceneDataException {
        List<AbstractPlaceableEntity> children = ctx.getPlaceableEntityTree().getChildren(parentEntityId);

        for(AbstractPlaceableEntity entity : children) {
            AbstractConverter converter = AbstractConverter.getConverter(entity.getClass());
            if(converter != null) {
                Group childGroup = converter.convert(entity);
                parentGroup.addChild(childGroup);
                recursivelyConvertElements(childGroup, ctx.getPlaceableEntityTree().getEntityId(entity) , ctx);
            }
        }

    }

}
