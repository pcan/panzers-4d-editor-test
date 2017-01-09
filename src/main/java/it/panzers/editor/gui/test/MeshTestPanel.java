/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.editor.gui.test;

import it.panzers.rendering.converters.AbstractConverter;
import it.panzers.scene.main.SceneDeserializer;
import it.panzers.scene.main.entity.AbstractSceneElement;
import it.panzers.scene.main.entity.SCEN;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.io.ByteArrayInputStream;
import java.io.RandomAccessFile;
import javax.swing.JPanel;
import org.scijava.java3d.AmbientLight;
import org.scijava.java3d.BoundingSphere;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.Canvas3D;
import org.scijava.java3d.DirectionalLight;
import org.scijava.java3d.Group;
import org.scijava.java3d.LineArray;
import org.scijava.java3d.Shape3D;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;
import org.scijava.java3d.utils.behaviors.mouse.MouseRotate;
import org.scijava.java3d.utils.behaviors.mouse.MouseWheelZoom;
import org.scijava.java3d.utils.universe.SimpleUniverse;
import org.scijava.vecmath.Color3f;
import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Vector3d;
import org.scijava.vecmath.Vector3f;

/**
 *
 * @author Luigi
 */
public class MeshTestPanel extends JPanel {

    private SCEN scen;

    public MeshTestPanel(){
        try {
            
            scen = readFile();
            //MeshConverter converter = new MeshConverter();
            //Group group = converter.convert((MESH)scen.getChildren().get(2));

            Group group = AbstractConverter.getConverter(SCEN.class).convert(scen);

            init(group);
            
        } catch(Exception ex) {
            ex.printStackTrace();
        }
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
	float [] mainTransform = {1.0F, 0.0F, 0.0F, 0.0F,
                                  0.0F, 0.0F, 1.0F, 0.0F,
                                  0.0F, -1.0F, 0.0F, 0.0F,
                                  0.0F, 0.0F, 0.0F, 1.0F};

        // Create the root of the branch graph
	BranchGroup objBranchRoot = new BranchGroup();
        TransformGroup objRoot = new TransformGroup();//new Transform3D(mainTransform));
        objBranchRoot.addChild(objRoot);
        setupLights(objBranchRoot);


	// rotate object has composited transformation matrix
	Transform3D rotate = new Transform3D();
	Transform3D tempRotate = new Transform3D();

        rotate.rotX(Math.PI/4.0d);
	tempRotate.rotY(Math.PI/5.0d);
        rotate.mul(tempRotate);
        rotate.setScale(0.015);
	TransformGroup objRotate = new TransformGroup(rotate);

	objRoot.addChild(objRotate);
	//objRotate.addChild(new ColorCube(0.4));
        objRotate.addChild(createAxis());


	objRotate.addChild(group);


        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRotate.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        addMouseBehaviours(objRotate);

        // Let Java 3D perform optimizations on this scene graph.
        objBranchRoot.compile();

	return objBranchRoot;
    } // end of CreateSceneGraph method of HelloJava3Db

    private void addMouseBehaviours(TransformGroup targetGroup) {
        MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(targetGroup);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        targetGroup.addChild(myMouseRotate);

        MouseWheelZoom myMouseWheelZoom = new MouseWheelZoom();
        myMouseWheelZoom.setTransformGroup(targetGroup);
        myMouseWheelZoom.setSchedulingBounds(new BoundingSphere());
        targetGroup.addChild(myMouseWheelZoom);
        myMouseWheelZoom.setFactor(1.1);
    }


    private void setupLights(Group objRoot){

        TransformGroup tg = new TransformGroup();
        objRoot.addChild(tg);

        AmbientLight lightA = new AmbientLight(true, new Color3f(0.7f, 0.7f, 0.7f));
        lightA.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 500));
        tg.addChild(lightA);

        DirectionalLight lightB = new DirectionalLight(true, new Color3f(0.3f, 0.5f, 0.5f), new Vector3f(-0.4f, -0.0f, -0.5f));
        lightB.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 500));
        tg.addChild(lightB);

        DirectionalLight lightC = new DirectionalLight(true, new Color3f(0.0f, 0.2f, 0.2f), new Vector3f(0.5f, 0.0f, -0.5f));
        lightC.setInfluencingBounds(new BoundingSphere(new Point3d(0, 0, 0), 500));
        tg.addChild(lightC);

//        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        MouseRotate myMouseRotate = new MouseRotate();
//        myMouseRotate.setTransformGroup(tg);
//        myMouseRotate.setSchedulingBounds(new BoundingSphere());
//        objRoot.addChild(myMouseRotate);
    }

    private SCEN readFile() throws Exception {
        String fileName = "tiger.4d";
        RandomAccessFile file = new RandomAccessFile(fileName, "r");
        int fileLen = (int)(file.length() & 0x7FFFFFFF);
        byte [] buffer = new byte[fileLen];
        file.readFully(buffer);
        ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
        SceneDeserializer deserializer = new SceneDeserializer(stream, true);
        AbstractSceneElement scene = deserializer.deserialize();
        return (SCEN)scene;
    }


    private Shape3D createAxis () {
        TransformGroup tg = new TransformGroup();
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
