/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.Implementations;
import it.panzers.scene.main.exceptions.ElementInstantiationException;

/**
 *
 * @author Pierantonio
 */
@ChildOf({MESH.class, SKVS.class})
@Implementations({UnitVERT.class, HumanVERT.class})
public abstract class VERT extends AbstractSceneElement{

    public VERT(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    @Override
    public String getElementName(){
        return VERT.class.getSimpleName();
    }
}
