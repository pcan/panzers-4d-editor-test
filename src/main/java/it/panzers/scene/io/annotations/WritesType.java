/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.annotations;
import java.lang.annotation.*;
/**
 *
 * @author Pierantonio
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WritesType {
    Class value();
}
