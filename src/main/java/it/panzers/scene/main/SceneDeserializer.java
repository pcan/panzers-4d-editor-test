/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main;

import it.panzers.scene.io.exceptions.ReadIOException;
import it.panzers.scene.io.structures.AbstractStructure;
import it.panzers.scene.io.util.BaseIO;
import it.panzers.scene.io.util.FileConstants;
import static it.panzers.scene.io.util.BaseIO.*;
import it.panzers.scene.main.annotations.FixedLength;
import it.panzers.scene.main.annotations.Implementations;
import it.panzers.scene.main.annotations.IndexOfQuantityField;
import it.panzers.scene.main.annotations.MatchingSceneType;
import it.panzers.scene.main.entity.AbstractSceneElement;
import it.panzers.scene.main.entity.SCEN;
import it.panzers.scene.main.entity.SceneContext;
import it.panzers.scene.main.entity.UnknownSceneElement;
import it.panzers.scene.main.exceptions.ElementInstantiationException;
import it.panzers.scene.main.exceptions.InvalidElementName;
import it.panzers.scene.main.exceptions.InvalidElementSize;
import it.panzers.scene.main.exceptions.InvalidFileHeader;
import it.panzers.scene.main.exceptions.SceneClassException;
import it.panzers.scene.main.exceptions.SceneDataException;
import it.panzers.scene.main.exceptions.SceneElementNotAllowed;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 *
 * @author Pierantonio
 */
public class SceneDeserializer {


    private final ByteArrayInputStream stream;
    private final int streamLength;
    private SceneContext sceneContext;
    private final static Pattern elementNamePattern = Pattern.compile("[A-Z_]{4,4}");


    public SceneDeserializer(ByteArrayInputStream stream, boolean readHeader) throws ReadIOException, SceneDataException{
        this.stream = stream;
        streamLength = stream.available();
        if(readHeader) {
            validateHeader();
        }
    }


    public AbstractSceneElement deserialize() throws ReadIOException, SceneDataException{
        String elementName = readFixedString(stream, 4);

        if(!elementNamePattern.matcher(elementName).matches()) {
            throw new InvalidElementName(streamPositionString());
        }

        Class<? extends AbstractSceneElement> elementClass = classLookup(elementName);
        AbstractSceneElement element = null;
        try {
            element = elementClass.getConstructor(SceneContext.class).newInstance(sceneContext);
            if(elementClass == SCEN.class && sceneContext == null) {
                sceneContext = element.getContext();
            }
        } catch (Exception ex) {
            throw new ElementInstantiationException(streamPositionString());
        }

        int elementSize = readInt(stream);

        if(elementSize < 0 || elementSize > FileConstants.MAX_ELEMENT_SIZE) {
            throw new InvalidElementSize(streamPositionString());
        }

        if(elementClass == UnknownSceneElement.class) {
            ((UnknownSceneElement)element).deserializeRawData(stream, elementName, elementSize);
        }else{
            deserializeElement(element, elementSize);
        }

        return element;
    }

    private void deserializeElement(AbstractSceneElement element, int elementSize) throws SceneDataException {
        TreeMap<Integer, Field> elementFieldsMap = element.getElementFields();
        int consumedBytes = 0;
        try {
            for (Entry<Integer, Field> entry : elementFieldsMap.entrySet()) {
                Field f = entry.getValue();
                Class fieldClass = f.getType();
                int fieldSize = 0;


                if (fieldClass.isPrimitive()) {
                    element.setFieldValue(f, readData(stream, fieldClass));
                    fieldSize = BaseIO.sizeOf(fieldClass);
                }

                if (fieldClass == String.class) {
                    String string = "";
                    if (f.isAnnotationPresent(FixedLength.class)) {
                        string = readFixedString(stream, f.getAnnotation(FixedLength.class).value());
                        fieldSize = string.length();
                    } else {
                        string = readVariableString(stream);
                        fieldSize = string.length() + 2;
                    }
                    element.setFieldValue(f,string);
                }

                if (AbstractStructure.class.isAssignableFrom(fieldClass)) {
                    AbstractStructure structure = (AbstractStructure) fieldClass.newInstance();
                    structure.deserializeStructure(stream);
                    element.setFieldValue(f,structure);
                    fieldSize = structure.size();
                }

                if (element.isValidStructureArrayField(f)) {
                    Field quantityField = elementFieldsMap.get(f.getAnnotation(IndexOfQuantityField.class).value());
                    if (quantityField.getType() == int.class) {
                        int quantity = (Integer) quantityField.get(element);
                        if(quantity > 0) {
                            Class componentType = fieldClass.getComponentType();
                            Object array = Array.newInstance(componentType, quantity);
                            element.setFieldValue(f,array);
                            boolean primitive = componentType.isPrimitive();
                            for(int i=0; i<quantity; i++) {
                                Object obj;
                                if(primitive) {
                                    obj = readData(stream, componentType);
                                }else{
                                    AbstractStructure structure = (AbstractStructure) componentType.newInstance();
                                    structure.deserializeStructure(stream);
                                    obj = structure;
                                }
                                Array.set(array, i, obj);
                            }
                            int structureSize = primitive ? sizeOf(componentType) : ((AbstractStructure) Array.get(array, 0)).size();
                            fieldSize += quantity * structureSize;
                        }
                    }
                }
                consumedBytes += fieldSize;
            }

            while(elementSize - consumedBytes > 0) {
                AbstractSceneElement child = deserialize();
                if(!child.checkParent(element.getClass())) {
                    throw new SceneElementNotAllowed(child.getElementName() + " -> " + streamPositionString());
                }
                element.getChildren().add(child);
                consumedBytes += child.size();
            }
        } catch (SceneDataException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SceneDataException(streamPositionString(), ex);
        }
    }


    private void validateHeader() throws ReadIOException, InvalidFileHeader{
        int h1 = readInt(stream);
        int h2 = readInt(stream);
        if (h1 != FileConstants.HEADER_INT_1 || h2 != FileConstants.HEADER_INT_2)  {
            throw new InvalidFileHeader();
        }
    }

    private Class<? extends AbstractSceneElement> classLookup(String elementName) throws SceneDataException {
        String packageName = AbstractSceneElement.class.getPackage().getName();

        Class<? extends AbstractSceneElement> elementClass;
        try{
            elementClass =(Class<? extends AbstractSceneElement>)Class.forName(packageName + "." + elementName);
            if(Modifier.isAbstract(elementClass.getModifiers()) && elementClass.isAnnotationPresent(Implementations.class)){
                for(Class<? extends AbstractSceneElement> c : elementClass.getAnnotation(Implementations.class).value()) {
                    if(c.isAnnotationPresent(MatchingSceneType.class) && c.getAnnotation(MatchingSceneType.class).value() == sceneContext.getSceneType()) {
                        return c;
                    }
                }
                throw new ClassNotFoundException();
            }
        } catch (ClassNotFoundException ex) {
            elementClass = UnknownSceneElement.class;
        } catch (Exception ex) {
            throw new SceneClassException(streamPositionString(), ex);
        }

        return elementClass;

    }

    private String streamPositionString(){
        return "Stream position: " + (streamLength - stream.available());
    }

}

