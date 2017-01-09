/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;
import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.FixedLength;
import it.panzers.scene.main.annotations.HasChildren;
import it.panzers.scene.main.enumerations.SceneVersionEnum;
import it.panzers.scene.main.exceptions.ElementInstantiationException;
import it.panzers.scene.main.exceptions.InvalidSceneVersion;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Pierantonio
 */

@ChildOf({})
@HasChildren
public class SCEN extends AbstractSceneElement {

    public SCEN(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @FieldIndex(0)
    @FixedLength(4)
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) throws InvalidSceneVersion {
        try{
            getContext().setVersion(SceneVersionEnum.valueOf(version));
        } catch (IllegalArgumentException ex) {
            throw new InvalidSceneVersion();
        }
        this.version = version;
    }

}
