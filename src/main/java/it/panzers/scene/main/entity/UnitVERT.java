/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.structures.Vertex8f;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.IndexOfQuantityField;
import it.panzers.scene.main.annotations.MatchingSceneType;

import it.panzers.scene.main.enumerations.SceneTypeEnum;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Pierantonio
 */
@ChildOf({MESH.class})
@MatchingSceneType(SceneTypeEnum.UNIT_SCENE)
public class UnitVERT extends VERT{

    public UnitVERT(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    private int vertexesQuantity;

    @FieldIndex(1)
    private float dummyNumber;

    @FieldIndex(2)
    @IndexOfQuantityField(0)
    private Vertex8f [] vertexes;

    public float getDummyNumber() {
        return dummyNumber;
    }

    public void setDummyNumber(float dummyNumber) {
        this.dummyNumber = dummyNumber;
    }

    public int getVertexesQuantity() {
        if(vertexes==null) {
            vertexesQuantity = 0;
        } else {
            vertexesQuantity = vertexes.length;
        }
        return vertexesQuantity;
    }

    public Vertex8f[] getVertexes() {
        return vertexes;
    }

    public void setVertexes(Vertex8f[] vertexes) {
        this.vertexes = vertexes;
    }
    

}
