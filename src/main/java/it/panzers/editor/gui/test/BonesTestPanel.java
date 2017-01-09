/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.editor.gui.test;

import static it.panzers.scene.io.util.BaseIO.readFully;
import it.panzers.scene.main.SceneDeserializer;
import it.panzers.scene.main.entity.AbstractSceneElement;
import it.panzers.scene.main.entity.BONS;
import it.panzers.scene.main.entity.SCEN;
import it.panzers.scene.main.entity.SKVS;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import javax.swing.JPanel;
import org.scijava.java3d.Appearance;
import org.scijava.java3d.BoundingSphere;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.Canvas3D;
import org.scijava.java3d.Group;
import org.scijava.java3d.LineArray;
import org.scijava.java3d.PointArray;
import org.scijava.java3d.PointAttributes;
import org.scijava.java3d.Shape3D;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;
import org.scijava.java3d.utils.behaviors.mouse.MouseRotate;
import org.scijava.java3d.utils.universe.SimpleUniverse;
import org.scijava.vecmath.Color3f;
import org.scijava.vecmath.Vector3d;

/**
 *
 * @author Luigi
 */
public class BonesTestPanel extends JPanel{
    private SCEN scen;

    public BonesTestPanel(){
        try {

            scen = readFile();
            SKVS skvs = (SKVS)scen.getChildren().get(2);
            Group group = convertBones(skvs.getChildOfClass(BONS.class));

            init(group);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private Group convertBones(BONS bons) {
        TransformGroup tg = new TransformGroup();

        int bonesQuantity = bons.getBonesQuantity();

        PointArray pointArray = new PointArray(bonesQuantity, PointArray.COORDINATES | PointArray.COLOR_3);
        for(int i = 0; i<bonesQuantity; i++){
            pointArray.setCoordinate(i, bons.getBones()[i].getPosition());
            pointArray.setColor(i, new Color3f(1, 1, 1));

        }
        
        pointArray.setColor(18, new Color3f(1, 0, 0));

        pointArray.setColor(19, new Color3f(0, 1, 0));

        pointArray.setColor(17, new Color3f(0, 0, 1));


        Appearance a = new Appearance();
        a.setPointAttributes(new PointAttributes(3, true));

        Shape3D shape = new Shape3D(pointArray, a);

        tg.addChild(shape);
        return tg;
    }

    private void init(Group group) {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        BranchGroup scene = createSceneGraph(group);

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);


	// This will move the ViewPlatform back a bit so the
	// objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();

        TransformGroup vp = simpleU.getViewingPlatform().getViewPlatformTransform();
        Transform3D t3d = new Transform3D();
        double viewDistance = 20.0d;
        t3d.set(new Vector3d(0.0, 0.0, viewDistance));
        vp.setTransform(t3d);


        simpleU.addBranchGraph(scene);
    }

    private BranchGroup createSceneGraph(Group group) {
        BranchGroup objBranchRoot = new BranchGroup();
        TransformGroup objRoot = new TransformGroup();
        objBranchRoot.addChild(objRoot);
        
        Transform3D rotate = new Transform3D();
	Transform3D tempRotate = new Transform3D();

        rotate.rotX(Math.PI/4.0d);
	tempRotate.rotY(Math.PI/5.0d);
        rotate.mul(tempRotate);
        rotate.setScale(0.031);
	TransformGroup objRotate = new TransformGroup(rotate);

	objRoot.addChild(objRotate);
	//objRotate.addChild(new ColorCube(0.4));

        objRotate.addChild(createAxis());


	objRotate.addChild(group);


        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(objRotate);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);


        // Let Java 3D perform optimizations on this scene graph.
        objBranchRoot.compile();
        
        return objBranchRoot;
    }

    private SCEN readFile() throws Exception {
        InputStream resourceAsStream = getClass().getResourceAsStream("/models/italy_dario_soldier.4d");
        ByteArrayInputStream stream = new ByteArrayInputStream(readFully(resourceAsStream));
        SceneDeserializer deserializer = new SceneDeserializer(stream, true);
        AbstractSceneElement scene = deserializer.deserialize();
        return (SCEN)scene;
    }


    private Shape3D createAxis () {
        LineArray la = new LineArray(6, LineArray.COORDINATES | LineArray.COLOR_3);
        float[] axis = {0,0,0, 5000,0,0, 0,0,0, 0,5000,0, 0,0,0, 0,0,5000};
        float[] red={1,0,0};
        float[] green={0,1,0};
        float[] blue={0,0,1};

        la.setCoordinates(0, axis, 0, 6);
        la.setColor(0, red); la.setColor(1, red);
        la.setColor(2, green); la.setColor(3, green);
        la.setColor(4, blue); la.setColor(5, blue);

        Shape3D axisShape = new Shape3D(la);
        return axisShape;
    }
}
