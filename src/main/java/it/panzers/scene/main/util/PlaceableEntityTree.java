/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.util;

import it.panzers.scene.main.entity.AbstractPlaceableEntity;
import it.panzers.scene.main.entity.AbstractSceneElement;
import it.panzers.scene.main.entity.SCEN;
import it.panzers.scene.main.entity.SceneContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Luigi
 */
public class PlaceableEntityTree {

    private final static Map<SceneContext, PlaceableEntityTree> instances = new HashMap<SceneContext, PlaceableEntityTree>();

    private final SCEN scen;
    private final List<AbstractPlaceableEntity> placeableEntities = new LinkedList<AbstractPlaceableEntity>();


    private PlaceableEntityTree(SceneContext ctx) {
        this.scen = ctx.getScene();
        this.loadFromScene();
    }

    public static PlaceableEntityTree getInstance(SceneContext ctx) {
        PlaceableEntityTree instance = instances.get(ctx);
        if(instance == null) {
            instance = new PlaceableEntityTree(ctx);
            instances.put(ctx, instance);
        }
        return instance;
    }

    public List<AbstractPlaceableEntity> getChildren(int parentEntityId) {
        List<AbstractPlaceableEntity> children = new ArrayList<AbstractPlaceableEntity>();
        for(AbstractPlaceableEntity entity : placeableEntities) {
            if(entity.getParentEntityId() == parentEntityId) {
                children.add(entity);
            }
        }
        return children;
    }

    public List<AbstractPlaceableEntity> getChildren(AbstractPlaceableEntity parent) {
        return getChildren(getEntityId(parent));
    }

    public int getEntityId(AbstractPlaceableEntity entity){
        return placeableEntities.indexOf(entity);
    }

    public AbstractPlaceableEntity get(int entityId) {
        if(entityId<0 || entityId >= placeableEntities.size()) {
            throw new IndexOutOfBoundsException();
        }
        return placeableEntities.get(entityId);
    }

    private void loadFromScene() {
        for(AbstractSceneElement element : scen.getChildren()) {
            if(AbstractPlaceableEntity.class.isAssignableFrom(element.getClass())) {
                AbstractPlaceableEntity placeableEntity = (AbstractPlaceableEntity)element;
                placeableEntities.add(placeableEntity);
            }
        }
    }


}
