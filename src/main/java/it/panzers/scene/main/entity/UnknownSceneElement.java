/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;

import it.panzers.scene.main.exceptions.ElementInstantiationException;
import it.panzers.scene.main.exceptions.SceneDataException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Pierantonio
 */

public class UnknownSceneElement extends AbstractSceneElement{

    private String elementName;
    private byte[] rawData;

    public UnknownSceneElement(SceneContext ctx) throws ElementInstantiationException {
        super(ctx);
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setRawData(byte[] rawData) {
        this.rawData = rawData;
    }

    @Override
    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public int size(){
        return rawData.length + ELEMENT_HEADER_SIZE;
    }

    public void deserializeRawData(ByteArrayInputStream stream, String elementName, int size) throws SceneDataException {
        rawData = new byte[size];
        this.elementName = elementName;
        try {
            stream.read(rawData);
        } catch (IOException ex) {
            throw new SceneDataException(ex);
        }
    }

    public void serializeRawData(ByteArrayOutputStream stream) throws SceneDataException{
        try {
            stream.write(rawData);
        } catch (IOException ex) {
            throw new SceneDataException(ex);
        }
    }

    @Override
    public String toString(){
        return "UNK: " + elementName + " " + size() + " bytes";
    }
}
