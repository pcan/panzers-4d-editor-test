/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.exceptions;

/**
 *
 * @author Pierantonio
 */
public class InvalidElementName extends SceneDataException{

    public InvalidElementName(){
        
    }

    public InvalidElementName(String message) {
        super(message);
    }
}
