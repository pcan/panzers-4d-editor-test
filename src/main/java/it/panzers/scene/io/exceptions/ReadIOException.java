/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.io.exceptions;

/**
 *
 * @author Pierantonio
 */
public class ReadIOException extends EditorIOException {

    public ReadIOException(String msg, Throwable target) {
        super(msg, target);
    }

    public ReadIOException(Throwable target) {
        super(target);
    }

    public ReadIOException(String msg) {
        super(msg);
    }

    public ReadIOException() {
    }

}
