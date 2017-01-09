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
 * @author Pierantonio
 */
@ChildOf({LITE.class})
@HasChildren
public class SPOT extends AbstractSceneElement { //TODO: estenterà PONT, ha dati in più rispetto a quest'ultima.

    public SPOT(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    private float maxDistance;

    @FieldIndex(1)
    private float constantAttenuation;

    @FieldIndex(2)
    private float linearAttenuation;

    @FieldIndex(3)
    private float quadraticAttenuation;

    @FieldIndex(4)
    private float spotExponent;

    @FieldIndex(5)
    private float spotCutoff; //cone angle
    
    public float getConstantAttenuation() {
        return constantAttenuation;
    }

    public void setConstantAttenuation(float constantAttenuation) {
        this.constantAttenuation = constantAttenuation;
    }

    public float getLinearAttenuation() {
        return linearAttenuation;
    }

    public void setLinearAttenuation(float linearAttenuation) {
        this.linearAttenuation = linearAttenuation;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public float getQuadraticAttenuation() {
        return quadraticAttenuation;
    }

    public void setQuadraticAttenuation(float quadraticAttenuation) {
        this.quadraticAttenuation = quadraticAttenuation;
    }

    public float getSpotCutoff() {
        return spotCutoff;
    }

    public void setSpotCutoff(float spotCutoff) {
        this.spotCutoff = spotCutoff;
    }

    public float getSpotExponent() {
        return spotExponent;
    }

    public void setSpotExponent(float spotExponent) {
        this.spotExponent = spotExponent;
    }

    
}
