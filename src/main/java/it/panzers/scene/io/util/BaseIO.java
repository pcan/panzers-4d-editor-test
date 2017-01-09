/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.panzers.scene.io.util;

import it.panzers.scene.io.annotations.ReadsType;
import it.panzers.scene.io.annotations.WritesType;
import it.panzers.scene.io.exceptions.EditorIOException;
import it.panzers.scene.io.exceptions.ReadIOException;
import it.panzers.scene.io.exceptions.WriteIOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import static it.panzers.scene.io.util.FileConstants.*;
import java.io.InputStream;

/**
 *
 * @author Pierantonio
 */
public class BaseIO {

    protected static final Map<Class, Method> readersMap;
    protected static final Map<Class, Method> writersMap;

    static {
        // <editor-fold defaultstate="collapsed" desc="Static initialization">
        readersMap = new HashMap<Class, Method>(BaseIO.class.getDeclaredMethods().length / 2);
        writersMap = new HashMap<Class, Method>(BaseIO.class.getDeclaredMethods().length / 2);
        for (Method m : BaseIO.class.getDeclaredMethods()) {
            boolean validReader
                    = Modifier.isStatic(m.getModifiers())
                    && m.isAnnotationPresent(ReadsType.class)
                    && m.getReturnType() == m.getAnnotation(ReadsType.class).value()
                    && m.getParameterTypes().length == 1
                    && m.getParameterTypes()[0] == ByteArrayInputStream.class;

            boolean validWriter
                    = Modifier.isStatic(m.getModifiers())
                    && m.isAnnotationPresent(WritesType.class)
                    && m.getReturnType() == void.class
                    && m.getParameterTypes().length == 2
                    && m.getParameterTypes()[0] == ByteArrayOutputStream.class
                    && m.getParameterTypes()[1] == m.getAnnotation(WritesType.class).value();

            if (validReader) {
                readersMap.put(m.getAnnotation(ReadsType.class).value(), m);
            }
            if (validWriter) {
                writersMap.put(m.getAnnotation(WritesType.class).value(), m);
            }
        }// </editor-fold>
    }

    /**
     * Metodo usato per leggere un valore di un tipo di dati primitivo (numerico) da uno stream di
     * byte.
     *
     * @param <T>
     * @param inputStream
     * @param dataClass tipo di dati da leggere
     * @return
     * @throws ReadIOException
     */
    public static <T> T readData(ByteArrayInputStream inputStream, Class<T> dataClass) throws EditorIOException {
        Method m = readersMap.get(dataClass);
        if (m == null) {
            throw new ReadIOException(new NoSuchMethodException());
        }
        Object[] parameters = {inputStream};

        checkInputStream(inputStream, sizeOf(dataClass));

        try {
            return (T) m.invoke(null, parameters);
        } catch (Exception ex) {
            throw new ReadIOException(ex);
        }
    }

//    public static <T> T readData(ByteArrayInputStream inputStream, Class<T> dataClass) throws EditorIOException {
//
//
//        //checkInputStream(inputStream, sizeOf(dataClass));
//
//
//        if(dataClass == float.class) {
//            return (T) (Float)readFloat(inputStream);
//        }
//        if(dataClass == int.class) {
//            return (T) (Integer)readInt(inputStream);
//        }
//        if(dataClass == short.class) {
//            return (T) (Short)readShort(inputStream);
//        }
//        if(dataClass == long.class) {
//            return (T) (Long)readLong(inputStream);
//        }
//
//        throw new ReadIOException(new NoSuchMethodException());
//
//    }
    /**
     * Metodo usato per scrivere un valore di un tipo di dati primitivo (numerico) su uno stream di
     * byte.
     *
     * @param <T>
     * @param outputStream
     * @param value il dato da scrivere
     * @return
     * @throws ReadIOException
     */
    public static void writeData(ByteArrayOutputStream outputStream, Number value) throws WriteIOException {
        try {
            Class dataClass = (Class) value.getClass().getField("TYPE").get(null);
            Method m = writersMap.get(dataClass);
            if (m == null) {
                throw new WriteIOException(new NoSuchMethodException());
            }
            Object[] parameters = {outputStream, value};
            m.invoke(null, parameters);
        } catch (Exception ex) {
            throw new WriteIOException(ex);
        }
    }

//    public static void writeData(ByteArrayOutputStream outputStream, Number value) throws WriteIOException {
//
//        Class dataClass;
//        try {
//            dataClass = (Class) value.getClass().getField("TYPE").get(null);
//        } catch (Exception ex) {
//            throw new WriteIOException(ex);
//        }
//
//        if(dataClass == float.class) {
//            writeFloat(outputStream, (Float)value);
//            return;
//        }
//        if(dataClass == int.class) {
//            writeInt(outputStream, (Integer)value);
//            return;
//        }
//        if(dataClass == short.class) {
//            writeShort(outputStream, (Short)value);
//            return;
//        }
//        if(dataClass == long.class) {
//            writeLong(outputStream, (Long)value);
//            return;
//        }
//
//        throw new WriteIOException();
//    }
    @ReadsType(byte.class)
    public static byte readByte(ByteArrayInputStream inputStream) {
        int ch1 = inputStream.read() & 0xFF;
        return (byte) (ch1);
    }

    @WritesType(byte.class)
    public static void writeInt(ByteArrayOutputStream outputStream, byte val) {
        outputStream.write(((int) (val)) & 0xFF);
    }

    @ReadsType(int.class)
    public static int readInt(ByteArrayInputStream inputStream) {
        int ch1 = inputStream.read() & 0xFF;
        int ch2 = inputStream.read() & 0xFF;
        int ch3 = inputStream.read() & 0xFF;
        int ch4 = inputStream.read() & 0xFF;
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1));
    }

    @WritesType(int.class)
    public static void writeInt(ByteArrayOutputStream outputStream, int val) {
        outputStream.write((val) & 0xFF);
        outputStream.write((val >>> 8) & 0xFF);
        outputStream.write((val >>> 16) & 0xFF);
        outputStream.write((val >>> 24) & 0xFF);
    }

    @ReadsType(float.class)
    public static float readFloat(ByteArrayInputStream inputStream) {
        return Float.intBitsToFloat(readInt(inputStream));
    }

    @WritesType(float.class)
    public static void writeFloat(ByteArrayOutputStream outputStream, float val) {
        writeInt(outputStream, Float.floatToRawIntBits(val));
    }

    @ReadsType(short.class)
    public static short readShort(ByteArrayInputStream inputStream) {
        int ch1 = inputStream.read() & 0xFF;
        int ch2 = inputStream.read() & 0xFF;
        return (short) ((ch2 << 8) + (ch1));
    }

    @WritesType(short.class)
    public static void writeShort(ByteArrayOutputStream outputStream, short val) {
        outputStream.write((val) & 0xFF);
        outputStream.write((val >>> 8) & 0xFF);
    }

    @ReadsType(long.class)
    public static long readLong(ByteArrayInputStream inputStream) {
        int ch1 = readInt(inputStream);
        int ch2 = readInt(inputStream);
        return ((long) (ch2) << 32) + (ch1 & 0xFFFFFFFFL);
    }

    @WritesType(long.class)
    public static void writeLong(ByteArrayOutputStream outputStream, long val) {
        outputStream.write((int) ((val) & 0xFF));
        outputStream.write((int) ((val >>> 8) & 0xFF));
        outputStream.write((int) ((val >>> 16) & 0xFF));
        outputStream.write((int) ((val >>> 24) & 0xFF));
        outputStream.write((int) ((val >>> 32) & 0xFF));
        outputStream.write((int) ((val >>> 40) & 0xFF));
        outputStream.write((int) ((val >>> 48) & 0xFF));
        outputStream.write((int) ((val >>> 56) & 0xFF));
    }

    public static String readFixedString(ByteArrayInputStream inputStream, int len) throws ReadIOException {
        if (len < 0 || inputStream.available() < len) {
            throw new ReadIOException(new StreamCorruptedException());
        }
        byte[] byteStr = new byte[len];
        try {
            inputStream.read(byteStr);
            return new String(byteStr, Charset.forName("CP1252"));
        } catch (IOException ex) {
            throw new ReadIOException(ex);
        } finally {
            byteStr = null;
        }
    }

    public static String readVariableString(ByteArrayInputStream inputStream) throws ReadIOException {
        if (inputStream.available() < 2) {
            throw new ReadIOException(new StreamCorruptedException());
        }
        int len = readShort(inputStream) & 0xFFFF;
        if (len < 0 || len > MAX_STRING_LENGTH) {
            throw new ReadIOException(new StreamCorruptedException());
        }

        return readFixedString(inputStream, len);
    }

    public static void writeString(ByteArrayOutputStream outputStream, String string, boolean writeStringLength) throws WriteIOException {
        if (string.length() > MAX_STRING_LENGTH) {
            throw new WriteIOException("String size exceed limit.");
        }
        byte[] byteStr = string.getBytes(Charset.forName("CP1252"));
        if (writeStringLength) {
            writeShort(outputStream, (short) (string.length() & 0x7FFF));
        }
        try {
            outputStream.write(byteStr);
        } catch (IOException ex) {
            throw new WriteIOException();
        }
    }

    public static int sizeOf(Class numberClass) {
        if (numberClass == float.class) {
            return 4;
        }
        if (numberClass == int.class) {
            return 4;
        }
        if (numberClass == short.class) {
            return 2;
        }
        if (numberClass == long.class) {
            return 8;
        }
        if (numberClass == double.class) {
            return 8;
        }
        if (numberClass == byte.class) {
            return 1;
        }
        return 0; //todo: non deve mai accadere, si deve fare un controllo col preprocessore: tutte le classi devono avere campi primitivi *numerici*
    }

    protected static void checkInputStream(ByteArrayInputStream inputStream, int sizeOf) throws ReadIOException {
        if (inputStream == null) {
            throw new ReadIOException(new NullPointerException());
        }
        if (sizeOf < 0 || inputStream.available() < sizeOf) {
            throw new ReadIOException(new StreamCorruptedException());
        }
    }

    public static byte[] readFully(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

}
