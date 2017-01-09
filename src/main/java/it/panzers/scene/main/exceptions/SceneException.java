/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.exceptions;

/**
 *
 * @author Pierantonio
 */
public class SceneException extends Exception {

    private Throwable target;

    /**
     * Creates a new instance of <code>EditorException</code> without detail message.
     */
    public SceneException() {
    }


    /**
     * Constructs an instance of <code>EditorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public SceneException(String msg) {
        super(msg);
    }

    public SceneException(Throwable target) {
        this.target = target;
    }

    public SceneException(String msg, Throwable target) {
        super(msg);
        this.target = target;
    }

    public Throwable getTarget() {
        return target;
    }

    
}
