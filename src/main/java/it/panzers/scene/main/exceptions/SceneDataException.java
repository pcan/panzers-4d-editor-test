/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.exceptions;

import it.panzers.scene.main.exceptions.SceneException;

/**
 *
 * @author Pierantonio
 */
public class SceneDataException extends SceneException{

    public SceneDataException(String msg, Throwable target) {
        super(msg, target);
    }

    public SceneDataException(Throwable target) {
        super(target);
    }

    public SceneDataException(String msg) {
        super(msg);
    }

    public SceneDataException() {
    }

}
