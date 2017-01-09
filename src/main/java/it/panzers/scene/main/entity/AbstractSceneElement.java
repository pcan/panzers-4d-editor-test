/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.panzers.scene.main.entity;

import it.panzers.scene.common.annotations.FieldIndex;
import it.panzers.scene.io.structures.AbstractStructure;
import it.panzers.scene.io.util.BaseIO;
import it.panzers.scene.main.annotations.ChildOf;
import it.panzers.scene.main.annotations.FixedLength;
import it.panzers.scene.main.annotations.HasChildren;
import it.panzers.scene.main.annotations.IndexOfQuantityField;
import it.panzers.scene.main.annotations.NotPresentInVersion;
import it.panzers.scene.main.exceptions.ElementInstantiationException;
import it.panzers.scene.main.exceptions.SceneDataException;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pierantonio
 */
public abstract class AbstractSceneElement {

    public final static int ELEMENT_HEADER_SIZE = 8;
    private final SceneContext ctx;
    private final ArrayList<AbstractSceneElement> children = new ArrayList<AbstractSceneElement>();
    private static final Map<Class, TreeMap<Integer, Field>> fieldsMap = new HashMap<Class, TreeMap<Integer, Field>>();

    public AbstractSceneElement(SceneContext ctx) throws ElementInstantiationException {
        if (ctx == null) {
            if (this.getClass() == SCEN.class) {
                ctx = new SceneContext((SCEN)this);
            } else {
                throw new ElementInstantiationException();
            }
        }
        this.ctx = ctx;
    }

    public List<AbstractSceneElement> getChildren() {
        return children;
    }

    public int getChildrenCount() {
        return children.size();
    }

    public SceneContext getContext() {
        return ctx;
    }

    public int size() throws SceneDataException {
        int size = ELEMENT_HEADER_SIZE;

        TreeMap<Integer, Field> elementFields = getElementFields();
        try {
            for (Entry<Integer, Field> entry : elementFields.entrySet()) {

                Field f = entry.getValue();
                Class fieldClass = f.getType();
                f.setAccessible(true);
                Object fieldValue = f.get(this);

                if (fieldClass.isPrimitive()) {
                    size += BaseIO.sizeOf(fieldClass);
                    continue;
                }

                if (AbstractStructure.class.isAssignableFrom(fieldClass) && fieldValue != null) {
                    AbstractStructure structure = (AbstractStructure) fieldValue;
                    size += structure != null ? structure.size() : 0;
                    continue;
                }

                if (fieldClass == String.class) {
                    if (f.isAnnotationPresent(FixedLength.class)) {
                        size += f.getAnnotation(FixedLength.class).value();
                    } else if (fieldValue != null) {
                        size += 2 + ((String) fieldValue).length();
                    }
                    continue;
                }

                if (isValidStructureArrayField(f)) {
                    Field quantityField = elementFields.get(f.getAnnotation(IndexOfQuantityField.class).value());
                    boolean primitive = f.getType().getComponentType().isPrimitive();
                    if (quantityField.getType() == int.class && fieldValue != null && Array.getLength(fieldValue) > 0) {
                        int quantity = (Integer) quantityField.get(this);
                        int structureSize = primitive ? BaseIO.sizeOf(f.getType().getComponentType()) : ((AbstractStructure) Array.get(fieldValue, 0)).size();
                        size += quantity * structureSize;
                    }
                    continue;
                }
            }

            if (this.getClass().isAnnotationPresent(HasChildren.class) && children != null && !children.isEmpty()) {
                for (AbstractSceneElement element : children) {
                    size += element.size();
                }
            }

        } catch (Exception ex) {
            throw new SceneDataException(ex);
        }
        return size;
    }


    public final TreeMap<Integer, Field> getElementFields(){
        TreeMap<Integer, Field> fields = fieldsMap.get(getClass());
        if(fields == null) {
            fields = recursivelyGetFields(getClass());
            fieldsMap.put(getClass(), fields);
        }
        return fields;
    }


    public boolean isValidStructureArrayField(Field f) {
        return f.getType().isArray()
                && (AbstractStructure.class.isAssignableFrom(f.getType().getComponentType())
                || f.getType().getComponentType().isPrimitive())
                && f.isAnnotationPresent(IndexOfQuantityField.class);
    }

    public boolean checkParent(Class<? extends AbstractSceneElement> parentClass) {
        if(this.getClass() == UnknownSceneElement.class) {
            return true;
        }
        if(parentClass != null && this.getClass() != null &&  this.getClass().isAnnotationPresent(ChildOf.class)) {
            Class<? extends AbstractSceneElement> [] classArray = this.getClass().getAnnotation(ChildOf.class).value();
            for(Class c : classArray) {
                if(c == parentClass) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * returns the FIRST child of the specified class.
     * @param <T>
     * @param childClass
     * @return
     */
    public <T extends AbstractSceneElement> T getChildOfClass(Class<T> childClass) {
        for(AbstractSceneElement element : children) {
            if(element.getClass() == childClass) {
                return (T)element;
            }
        }
        throw new NullPointerException();
    }

    public <T extends AbstractSceneElement> boolean hasChildOfClass(Class<T> childClass) {
        return getChildOfClass(childClass) != null;
    }

    @Override
    public String toString(){
        try {
            return this.getClass().getSimpleName() + " " + size() + " bytes";
        } catch (SceneDataException ex) {
            Logger.getLogger(AbstractSceneElement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this.getClass().getSimpleName();
    }


    public String getElementName() {
        return getClass().getSimpleName();
    }

    public void setFieldValue(Field f, Object value) throws SceneDataException {
        try {
            try {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), f.getDeclaringClass());
                Method setter = pd.getWriteMethod();
                setter.invoke(this, new Object[]{value});
            } catch (IntrospectionException ex) {
                f.setAccessible(true);
                f.set(this, value);
            }
        } catch (InvocationTargetException ex) {
            //ex.getTargetException().printStackTrace();
            throw new SceneDataException(ex);
        } catch (Exception ex) {
            throw new SceneDataException(ex);
        }
    }

    public Object getFieldValue(Field f) throws SceneDataException {
        try {
            Object returnValue;
            try {
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), f.getDeclaringClass());
                Method getter = pd.getReadMethod();
                returnValue = getter.invoke(this);
            } catch (IntrospectionException ex) {
                f.setAccessible(true);
                returnValue = f.get(this);
            }
            return returnValue;
        } catch (Exception ex) {
            throw new SceneDataException(ex);
        }
    }



    private TreeMap<Integer, Field> recursivelyGetFields(Class<?> elementClass) {
        TreeMap<Integer, Field> orderedFields = new TreeMap<Integer, Field>();
        for (Field f : elementClass.getDeclaredFields()) {
            Class fieldClass = f.getType();
            boolean validField =
                    (fieldClass == String.class
                    || fieldClass.isPrimitive()
                    || AbstractStructure.class.isAssignableFrom(fieldClass)
                    || isValidStructureArrayField(f)
                    ) && f.isAnnotationPresent(FieldIndex.class);

            if(f.isAnnotationPresent(NotPresentInVersion.class)){
                validField &= f.getAnnotation(NotPresentInVersion.class).value() != ctx.getVersion();
            }

            if (validField) {
                orderedFields.put(f.getAnnotation(FieldIndex.class).value(), f);
            }
        }
        if(elementClass.getSuperclass() != AbstractSceneElement.class) {
            orderedFields.putAll(recursivelyGetFields(elementClass.getSuperclass()));
        }
        return orderedFields;
    }


}
