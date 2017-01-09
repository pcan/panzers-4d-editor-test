/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;
import it.panzers.scene.main.annotations.*;
import it.panzers.scene.main.exceptions.ElementInstantiationException;
/**
 *
 * @author Pierantonio
 */
@ChildOf({SCEN.class})
public class DUMY extends AbstractPlaceableEntity {

    public DUMY(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

}
