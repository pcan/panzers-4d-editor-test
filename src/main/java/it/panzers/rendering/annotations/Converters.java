/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.annotations;
import it.panzers.rendering.converters.AbstractConverter;
import it.panzers.scene.main.entity.AbstractSceneElement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Pierantonio
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Converters {
    Class<? extends AbstractConverter<? extends AbstractSceneElement>> [] value();
}
