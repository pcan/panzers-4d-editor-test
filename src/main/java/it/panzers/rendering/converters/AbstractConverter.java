/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.converters;
import it.panzers.rendering.annotations.Converters;
import it.panzers.scene.io.structures.TransformMatrix;
import it.panzers.scene.main.entity.AbstractSceneElement;
import it.panzers.scene.main.exceptions.SceneDataException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.scijava.java3d.Group;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;

/**
 *
 * @author Luigi
 */
@Converters({MeshConverter.class, HumanMeshConverterOld.class, DumyConverter.class, SceneConverter.class})
public abstract class AbstractConverter<T extends AbstractSceneElement> {

    private final static Map<Class,AbstractConverter> convertersMap = new HashMap<Class, AbstractConverter>();

    static {
        Class<? extends AbstractConverter<?>>[] converterClasses = AbstractConverter.class.getAnnotation(Converters.class).value();
        
        for(Class<? extends AbstractConverter<?>> c : converterClasses) {
            try {
                ParameterizedType t = (ParameterizedType)c.getGenericSuperclass();
                Class targetElementClass = (Class)t.getActualTypeArguments()[0];
                convertersMap.put(targetElementClass, c.newInstance());
            } catch (Exception ex) {
                Logger.getLogger(AbstractConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public abstract Group convert(T element) throws SceneDataException;

    public static <T extends AbstractSceneElement> AbstractConverter<T> getConverter(Class<T> elementClass) {
        return convertersMap.get(elementClass);
    }


    protected TransformGroup getTransformGroup(TransformMatrix transformMatrix) {
        float [] meshMatrix = transformMatrix.getMatrix4x4f();
        Transform3D transform3d = new Transform3D(meshMatrix);
        TransformGroup tg = new TransformGroup(transform3d);
        return tg;
    }

}
