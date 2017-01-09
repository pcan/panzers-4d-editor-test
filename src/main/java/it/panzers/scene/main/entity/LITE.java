/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;
import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.structures.Struct3f;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.FixedLength;
import it.panzers.scene.main.annotations.HasChildren;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Pierantonio
 */
@ChildOf({SCEN.class})
@HasChildren
public class LITE extends AbstractPlaceableEntity {

    public LITE(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }


    @FieldIndex(6)
    @FixedLength(4)
    private String liteVersion;

    @FieldIndex(7)
    private Struct3f lightColor;

    @FieldIndex(8)
    private float lightEnergy;

    public Struct3f getLightColor() {
        return lightColor;
    }

    public float getLightEnergy() {
        return lightEnergy;
    }

    public String getLiteVersion() {
        return liteVersion;
    }

    public void setLightEnergy(float lightEnergy) {
        this.lightEnergy = lightEnergy;
    }

    
}
