/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.structures.SkinnedVertex;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.IndexOfQuantityField;
import it.panzers.scene.main.annotations.MatchingSceneType;
import it.panzers.scene.main.enumerations.SceneTypeEnum;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Luigi
 */
@ChildOf({SKVS.class})
@MatchingSceneType(SceneTypeEnum.HUMAN_MODEL_SCENE)
public class HumanVERT extends VERT{

    public HumanVERT(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    private int vertexesQuantity;

    @FieldIndex(1)
    private int skinnedMeshType;

    @FieldIndex(2)
    private int unknownInt1;

    @FieldIndex(3)
    @IndexOfQuantityField(0)
    private SkinnedVertex [] skinnedVertexArray; // todo: rinominare coerentemente con l'altra implementazione di vert.

    public int getVertexesQuantity() {
        if(skinnedVertexArray==null) {
            vertexesQuantity = 0;
        } else {
            vertexesQuantity = skinnedVertexArray.length;
        }
        return vertexesQuantity;
    }

    public int getSkinnedMeshType() {
        return skinnedMeshType;
    }

    public void setSkinnedMeshType(int skinnedMeshType) {
        this.skinnedMeshType = skinnedMeshType;
    }

    public int getUnknownInt1() {
        return unknownInt1;
    }

    public void setUnknownInt1(int unknownInt1) {
        this.unknownInt1 = unknownInt1;
    }

    public SkinnedVertex[] getSkinnedVertexArray() {
        return skinnedVertexArray;
    }

    public void setSkinnedVertexArray(SkinnedVertex[] skinnedVertexArray) {
        this.skinnedVertexArray = skinnedVertexArray;
    }



}
