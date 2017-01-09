/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.exceptions;

import it.panzers.scene.main.exceptions.SceneException;

/**
 *
 * @author Pierantonio
 */
public class EditorIOException extends SceneException {

    public EditorIOException(String msg, Throwable target) {
        super(msg, target);
    }

    public EditorIOException(Throwable target) {
        super(target);
    }

    public EditorIOException(String msg) {
        super(msg);
    }

    public EditorIOException() {
    }

}
