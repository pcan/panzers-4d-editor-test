/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.exceptions;

/**
 *
 * @author Pierantonio
 */
public class WriteIOException extends EditorIOException {

    public WriteIOException(String msg, Throwable target) {
        super(msg, target);
    }

    public WriteIOException(Throwable target) {
        super(target);
    }

    public WriteIOException(String msg) {
        super(msg);
    }

    public WriteIOException() {
    }

}
