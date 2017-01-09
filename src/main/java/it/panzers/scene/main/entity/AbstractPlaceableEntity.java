/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.structures.TransformMatrix;
import it.panzers.scene.main.annotations.NotPresentInVersion;
import it.panzers.scene.main.enumerations.SceneVersionEnum;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Pierantonio
 */
public abstract class AbstractPlaceableEntity extends AbstractSceneElement{

    public AbstractPlaceableEntity(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    private String entityName;

    @FieldIndex(1)
    private int parentEntityId;

    @FieldIndex(2)
    @NotPresentInVersion(SceneVersionEnum.v100)
    private int unknownInt1;

    @FieldIndex(3)
    @NotPresentInVersion(SceneVersionEnum.v100)
    private int unknownInt2;

//    @FieldIndex(4)
//    private Struct3x3f matrix3x3f;
//
//    @FieldIndex(5)
//    private Struct3f position;

    @FieldIndex(4)
    private TransformMatrix transformMatrix;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(int parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public TransformMatrix getTransformMatrix() {
        return transformMatrix;
    }

    public int getUnknownInt1() {
        return unknownInt1;
    }

    public void setUnknownInt1(int unknownInt1) {
        this.unknownInt1 = unknownInt1;
    }

    public int getUnknownInt2() {
        return unknownInt2;
    }

    public void setUnknownInt2(int unknownInt2) {
        this.unknownInt2 = unknownInt2;
    }



}
