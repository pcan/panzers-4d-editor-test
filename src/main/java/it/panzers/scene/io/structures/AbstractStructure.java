/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.panzers.scene.io.structures;

import it.panzers.scene.common.annotations.ArraySize;
import it.panzers.scene.io.exceptions.EditorIOException;
import it.panzers.scene.io.util.BaseIO;
import it.panzers.scene.common.annotations.FieldIndex;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Pierantonio
 */
public abstract class AbstractStructure {

    private static final Map<Class<? extends AbstractStructure>,Collection<ArrayProperties>> arrayPropertiesMap = new HashMap<Class<? extends AbstractStructure>, Collection<ArrayProperties>>();
    private static final Map<Class, Integer> sizeMap = new HashMap<Class, Integer>();
    private static final Map<Class, Collection<Field>> fieldsMap = new HashMap<Class, Collection<Field>>();


    protected AbstractStructure() throws EditorIOException {
        initArrays();
    }

    private void initArrays() throws EditorIOException{
        Collection<ArrayProperties> properties = arrayPropertiesMap.get(getClass());

        if (properties == null) {
            properties = new ArrayList<ArrayProperties>();
            for (Field f : getStructureFields(getClass())) {
                if (f.getType().isArray()) {
                    ArrayProperties p = new ArrayProperties();
                    f.setAccessible(true);
                    p.arrayField = f;
                    p.arraySize = f.getAnnotation(ArraySize.class).value();
                    p.componentType = f.getType().getComponentType();
                    properties.add(p);
                }
            }
            arrayPropertiesMap.put(getClass(), properties);
        }

        try {
            for (ArrayProperties p : properties) {
                p.arrayField.set(this, Array.newInstance(p.componentType, p.arraySize));
            }
        } catch (Exception ex) {
            throw new EditorIOException();
        }

    }

    public int size() {
        Integer size = sizeMap.get(getClass());

        if (size == null) {
            size = 0;
            for (Field f : getStructureFields(getClass())) {
                if (!f.getType().isArray()) {
                    size += BaseIO.sizeOf(f.getType());
                } else {
                    int arraySize = f.getAnnotation(ArraySize.class).value();
                    size += arraySize * BaseIO.sizeOf(f.getType().getComponentType());
                }
            }
            sizeMap.put(getClass(), size);
        }
        
        return size;
    }

    protected final Collection<Field> getStructureFields(Class<? extends AbstractStructure> structClass) {
        Collection<Field> fields = fieldsMap.get(structClass);
        
        if(fields == null) {
            fields = recursivelyGetFields(structClass).values();
            fieldsMap.put(structClass, fields);
        }

        return fields;
    }

    private TreeMap<Integer, Field> recursivelyGetFields(Class<?> structClass) {
        TreeMap<Integer, Field> orderedFields = new TreeMap<Integer, Field>();
        if (structClass.getSuperclass() != AbstractStructure.class) {
            orderedFields.putAll(recursivelyGetFields(structClass.getSuperclass()));
        }
        for (Field f : structClass.getDeclaredFields()) {
            Class fieldClass = f.getType();
            boolean validField = f.isAnnotationPresent(FieldIndex.class)
                    && (fieldClass.isPrimitive()
                    || (fieldClass.isArray() && fieldClass.getComponentType().isPrimitive() && f.isAnnotationPresent(ArraySize.class)));
            if (validField) {
                orderedFields.put(f.getAnnotation(FieldIndex.class).value(), f);
            }
        }
        return orderedFields;
    }

    public void deserializeStructure(ByteArrayInputStream stream) throws EditorIOException {
        try {
            for (Field f : getStructureFields(getClass())) {
                f.setAccessible(true);
                if(!f.getType().isArray()) {
                    f.set(this, BaseIO.readData(stream, f.getType()));
                }else{
                    int arraySize = f.getAnnotation(ArraySize.class).value();
                    for(int i = 0; i< arraySize ; i++) {
                        Array.set(f.get(this), i, BaseIO.readData(stream, f.getType().getComponentType()));
                    }
                }
            }
        } catch (Exception ex) {
            throw new EditorIOException(ex);
        }
    }

    public void serializeStructure(ByteArrayOutputStream stream) throws EditorIOException {
        try {
            for (Field f : getStructureFields(getClass())) {
                f.setAccessible(true);
                if(!f.getType().isArray()) {
                    BaseIO.writeData(stream, (Number)f.get(this));
                }else{
                    int arraySize = f.getAnnotation(ArraySize.class).value();
                    for(int i = 0; i< arraySize ; i++) {
                        BaseIO.writeData(stream, (Number) Array.get(f.get(this), i));
                    }
                }
            }
        } catch (Exception ex) {
            throw new EditorIOException(ex);
        }
    }

    private class ArrayProperties {
        Field arrayField;
        int arraySize;
        Class componentType;
    }

}
