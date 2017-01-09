/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.panzers.scene.main;

import it.panzers.scene.io.exceptions.EditorIOException;
import it.panzers.scene.io.structures.AbstractStructure;
import it.panzers.scene.io.util.FileConstants;
import static it.panzers.scene.io.util.BaseIO.*;
import it.panzers.scene.main.annotations.FixedLength;
import it.panzers.scene.main.entity.AbstractSceneElement;
import it.panzers.scene.main.entity.UnknownSceneElement;
import it.panzers.scene.main.exceptions.SceneDataException;
import it.panzers.scene.main.exceptions.SceneElementNotAllowed;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 *
 * @author Pierantonio
 */
public class SceneSerializer {

    private final ByteArrayOutputStream stream;

    public SceneSerializer(ByteArrayOutputStream stream, boolean writeHeader)  {
        this.stream = stream;
        if(writeHeader) {
            writeInt(stream, FileConstants.HEADER_INT_1);
            writeInt(stream, FileConstants.HEADER_INT_2);
        }
    }

    public void serialize(AbstractSceneElement element) throws SceneDataException, EditorIOException {
        if(element == null) {
            throw new EditorIOException(new NullPointerException());
        }
        String elementName = element.getElementName();
        int elementSize = element.size() - AbstractSceneElement.ELEMENT_HEADER_SIZE;

        writeString(stream,elementName,false);
        writeInt(stream, elementSize);

        Class<? extends AbstractSceneElement> elementClass = element.getClass();

        if(elementClass == UnknownSceneElement.class) {
            ((UnknownSceneElement)element).serializeRawData(stream);
        }else{
            serializeElement(element);
        }

    }

    private void serializeElement(AbstractSceneElement element) throws SceneDataException {
        TreeMap<Integer, Field> elementFieldsMap = element.getElementFields();
        try {
            for (Entry<Integer, Field> entry : elementFieldsMap.entrySet()) {
                Field f = entry.getValue();
                Class fieldClass = f.getType();
                Object fieldValue = element.getFieldValue(f);

                if(fieldValue == null) {
                    throw new SceneDataException(new NullPointerException());
                }

                if (fieldClass.isPrimitive()) {
                    writeData(stream, (Number)fieldValue);
                }

                if (fieldClass == String.class) {
                    boolean writeStringLength = !f.isAnnotationPresent(FixedLength.class);
                    writeString(stream, (String)fieldValue, writeStringLength);
                }

                if (AbstractStructure.class.isAssignableFrom(fieldClass)) {
                    AbstractStructure structure = (AbstractStructure) fieldValue;
                    structure.serializeStructure(stream);
                }

                if (element.isValidStructureArrayField(f)) {
                    int quantity = Array.getLength(fieldValue);
                    boolean primitive = f.getType().getComponentType().isPrimitive();
                    for(int i = 0; i < quantity; i++) {
                        if(primitive){
                            writeData(stream, (Number)Array.get(fieldValue, i));
                        }else{
                            AbstractStructure structure = (AbstractStructure) Array.get(fieldValue, i);
                            structure.serializeStructure(stream);
                        }
                    }
                }
            }

            for(AbstractSceneElement child : element.getChildren()) {
                if(!child.checkParent(element.getClass())) {
                    throw new SceneElementNotAllowed();
                }
                serialize(child);
            }
        } catch (SceneDataException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SceneDataException(ex);
        }


    }

}
