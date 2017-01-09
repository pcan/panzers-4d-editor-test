/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.panzers;

import it.panzers.scene.main.SceneDeserializer;
import it.panzers.scene.main.SceneSerializer;
import it.panzers.scene.main.entity.AbstractSceneElement;
import it.panzers.scene.main.entity.UnknownSceneElement;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.RandomAccessFile;

//charset: CP1252  -> String s = new String(bytes,Charset.forName("CP1252"));
/**
 *
 * @author Pierantonio
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        //testDeserialization();

    }

    private static void testDeserialization() throws Exception {

        String inputFile = "italy_dario_soldier.4d";

        RandomAccessFile file = new RandomAccessFile(inputFile, "r");

        RandomAccessFile file2 = new RandomAccessFile("tiger2.4d", "rw");

        file2.setLength(0);

        int fileLen = (int) (file.length() & 0x7FFFFFFF);

        byte[] buffer = new byte[fileLen];

        file.readFully(buffer);

        long startTime = System.currentTimeMillis();

        ByteArrayInputStream stream = new ByteArrayInputStream(buffer);

        SceneDeserializer deserializer = new SceneDeserializer(stream, true);

        AbstractSceneElement scene = deserializer.deserialize();

//        List<AbstractSceneElement> coll = scene.getChildren();
//        for(AbstractSceneElement x : coll) {
//            if(AbstractPlaceableEntity.class.isAssignableFrom(x.getClass())) {
//                AbstractPlaceableEntity entity = (AbstractPlaceableEntity)x;
//                System.out.println(coll.indexOf(x) + ") " + entity.getElementName() + ": " + entity.getEntityName() + " - parent: "+entity.getParentEntityId());
//            }
//        }

        System.out.println("Elapsed time for deserialization: " + (System.currentTimeMillis() - startTime) + "ms");

        int sceneSize = scene.size();

        System.out.println("Scene size: " + sceneSize); //71259 x tiger.4d

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(sceneSize);

        long midTime = System.currentTimeMillis();

        SceneSerializer sceneSerializer = new SceneSerializer(outputStream, true);

        sceneSerializer.serialize(scene);

        System.out.println("Elapsed time for serialization: " + (System.currentTimeMillis() - midTime) + "ms");

        file2.write(outputStream.toByteArray());

        file.close();
        file2.close();
    }

    private static void test1() {
        //        byte[] bytes = {0x01, 0x00, 0x0, 0x01};
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//        DataIO.writeData(outputStream, 1254065465570651015L);
//
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//
//        System.out.println(DataIO.readData(inputStream, long.class));



        String packageName = AbstractSceneElement.class.getPackage().getName();
        Class<? extends AbstractSceneElement> elementClass;
        try {
            elementClass = (Class<? extends AbstractSceneElement>) Class.forName(packageName + "." + "hhj");
        } catch (ClassNotFoundException ex) {
            elementClass = UnknownSceneElement.class;
        }
        System.out.println(elementClass.getName());
    }
}
