/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.converters;

import it.panzers.scene.main.entity.DUMY;
import it.panzers.scene.main.exceptions.SceneDataException;
import org.scijava.java3d.Appearance;
import org.scijava.java3d.Group;
import org.scijava.java3d.PointArray;
import org.scijava.java3d.PointAttributes;
import org.scijava.java3d.Shape3D;
import org.scijava.vecmath.Color3f;

/**
 *
 * @author Luigi
 */
public class DumyConverter extends AbstractConverter<DUMY>{

    private static float [] origin = {0,0,0};
    @Override
    public Group convert(DUMY dumy) throws SceneDataException {
        Group group = getTransformGroup(dumy.getTransformMatrix());

        Appearance a = new Appearance();
        a.setPointAttributes(new PointAttributes(3.5f, true));

        PointArray pa = new PointArray(1, PointArray.COORDINATES | PointArray.COLOR_3);
        pa.setColor(0, new Color3f(1, 0, 1));

        
        pa.setCoordinate(0, origin);

        Shape3D point = new Shape3D(pa, a);

        group.addChild(point);
        return group;
    }

}
