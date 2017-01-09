/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.converters;

import it.panzers.rendering.entity.Skeleton;
import it.panzers.scene.io.structures.Bone13f;
import it.panzers.scene.io.structures.TransformMatrix;
import it.panzers.scene.io.structures.SkinnedVertex;
import it.panzers.scene.main.entity.AbstractPlaceableEntity;
import it.panzers.scene.main.entity.BONS;
import it.panzers.scene.main.entity.DUMY;
import it.panzers.scene.main.entity.HumanVERT;
import it.panzers.scene.main.entity.INDI;
import it.panzers.scene.main.entity.SKVS;
import it.panzers.scene.main.entity.SceneContext;
import it.panzers.scene.main.exceptions.SceneDataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scijava.java3d.Appearance;
import org.scijava.java3d.BoundingBox;
import org.scijava.java3d.Geometry;
import org.scijava.java3d.Group;
import org.scijava.java3d.LineArray;
import org.scijava.java3d.LineAttributes;
import org.scijava.java3d.Material;
import org.scijava.java3d.PointArray;
import org.scijava.java3d.PointAttributes;
import org.scijava.java3d.Shader;
import org.scijava.java3d.ShaderAppearance;
import org.scijava.java3d.ShaderProgram;
import org.scijava.java3d.Shape3D;
import org.scijava.java3d.SourceCodeShader;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;
import org.scijava.java3d.utils.geometry.GeometryInfo;
import org.scijava.vecmath.Color3f;
import org.scijava.vecmath.Point3f;
import org.scijava.vecmath.Vector3f;

/**
 *
 * @author Luigi
 */
public class HumanMeshConverterOld extends AbstractConverter<SKVS>{

    public Shape3D point = null;
    public HumanVERT modelVertexes  = null;

    Map<Integer, Transform3D> transformMap = new HashMap<Integer, Transform3D>();
//    private int max_bone_index = 0;

    TransformGroup rootnode;

    public Group convert(SKVS skvs) throws SceneDataException {

        if(skvs == null) {
            throw new SceneDataException();
        }

        modelVertexes = skvs.getVertexes();
        INDI indexes = skvs.getIndexes();
        boolean stripe = skvs.isTriangleStripe();

//        float [] meshMatrix = skvs.getTransformMatrix().getMatrix4x4f();
//        Transform3D transformMatrix = new Transform3D(meshMatrix);
        TransformGroup skvsTg = new TransformGroup();

        skvsTg.addChild(desiredPoint(new float[]{0,0,0} , new Color3f(1f, 1f, 1),6));

        BoundingBox boundingBox = new BoundingBox(); //TODO: caricare BBOX

        int primitive;
        if(stripe) {
            primitive = GeometryInfo.TRIANGLE_STRIP_ARRAY;
        }else{
            primitive = GeometryInfo.TRIANGLE_ARRAY;
        }


        TransformGroup tg = new TransformGroup();
        rootnode = tg;
        tg.addChild(skvsTg);
        skvsTg.addChild(getBonesShape(tg,skvs,indexes,primitive,skvs.getBons() ));

        Skeleton s = new Skeleton(skvs);
        
        return tg;
    }

    private Shape3D getBonesShape(TransformGroup tg, SKVS skvs, INDI indexes, int primitive, BONS bons) throws SceneDataException {
      
        
        Map<Bone13f,TransformGroup> boneGroupsMap = new HashMap<Bone13f, TransformGroup>();


        Transform3D testMatrix = new Transform3D();

        PointArray pa = new PointArray(bons.getBonesQuantity(), PointArray.COORDINATES | PointArray.COLOR_3);

        ArrayList<Point3f> points = new ArrayList<Point3f>();
        recursivelyConvertElements(tg, -1, skvs.getContext(), skvs.getDumyBonesMap(),boneGroupsMap, testMatrix);

//        for(int i = 0; i<points.size(); i++) {
//            Point3f p = points.get(i);
//            pa.setCoordinate(i, p);
//            pa.setColor(i, new float[] {0,1,0.3f});
//        }

        //start test code ---------------------------------------
//        VertexBone18f vertex = modelVertexes.getVertexBoneArray()[150];
//
//
//
//        Appearance app = new Appearance();
//        app.setPointAttributes(new PointAttributes(7, true));
        Shape3D s = new Shape3D();
        //return s;
        //end test code ---------------------------------------
        
        return getHumanShape(indexes, primitive, bons, boneGroupsMap);
        //return tg;
    }


    private Shape3D getHumanShape(INDI indexes, int primitive, BONS bons, Map<Bone13f,TransformGroup> boneGroupsMap) {
        GeometryInfo geometryInfo = new GeometryInfo(primitive);

        convertVertexesInfo(modelVertexes, geometryInfo,bons, boneGroupsMap);

        convertIndexesInfo(indexes, geometryInfo);

        Geometry geometry = geometryInfo.getIndexedGeometryArray();
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setAmbientColor(0.3f, 0.3f, 0.3f);
        material.setDiffuseColor(0.6f, 0.6f, 0.6f);
        appearance.setMaterial(material);

        Shape3D shape = new Shape3D(geometry, appearance);

        return shape;
    }

    private void recursivelyConvertElements(
            Group parentGroup,
            int parentEntityId,
            SceneContext ctx,
            Map<Integer,Bone13f> bonesMap,
            Map<Bone13f,TransformGroup> boneGroupsMap,
            Transform3D testMatrix) throws SceneDataException
    {
        List<AbstractPlaceableEntity> children = ctx.getPlaceableEntityTree().getChildren(parentEntityId);

        Transform3D backup = new Transform3D(testMatrix);

        for(AbstractPlaceableEntity entity : children) {
            AbstractConverter converter = AbstractConverter.getConverter(entity.getClass());
            if(converter != null && entity.getClass() == DUMY.class) {
                Group childGroup = converter.convert(entity);

                testMatrix.mul(new Transform3D(entity.getTransformMatrix().getMatrix4x4f()));
                childGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ | TransformGroup.ALLOW_TRANSFORM_WRITE);
                int entityId = ctx.getPlaceableEntityTree().getEntityId(entity);

                if(entityId == 6) { //gamba dx
                     Transform3D rot = new Transform3D();

                     rot.rotZ(Math.PI/12);
                     testMatrix.mul(rot);
                     rot.setIdentity();
                     rot.rotY(-Math.PI /4);
                     testMatrix.mul(rot);
                     rot.setIdentity();
                }



                if(entityId == 9) {
                    Transform3D rot = new Transform3D();
                    rot.rotZ(Math.PI /12);
                    testMatrix.mul(rot);
                }


                if(entityId == 22) {
                    Transform3D rot = new Transform3D();
                    rot.rotY(Math.PI /3);
                    testMatrix.mul(rot);
                    rot.setIdentity();
                    rot.rotX(-Math.PI /10);
                    testMatrix.mul(rot);
                }

                if(entityId == 23) {
                    Transform3D rot = new Transform3D();
                    rot.rotY(-Math.PI /5);
                    testMatrix.mul(rot);
                }

                if(entityId == 24) {
                    Transform3D rot = new Transform3D();
                    rot.rotZ(-Math.PI /3);
                    testMatrix.mul(rot);
                }

                
                if(entityId == 18) {
                    Transform3D rot = new Transform3D();
                    rot.rotZ(-Math.PI /3);
                    testMatrix.mul(rot);
                }

                Bone13f associatedBone = bonesMap.get(entityId);
                if(associatedBone != null) {
                    Transform3D transform3D = new Transform3D(associatedBone.getMatrix4x4f());
                    Transform3D testTransform = new Transform3D(transform3D);
                    Transform3D tempTransform = new Transform3D(testMatrix);
                    tempTransform.mul(testTransform);
                    transformMap.put(entityId, tempTransform);
                    float [] buf = new float[16];
                    testTransform.get(buf);
                    System.out.println("Index: "+ entityId);
                    System.out.println(TransformMatrix.getString(buf,2));

                    Point3f p = new Point3f(0, 0, 0);
                    testTransform.transform(p);
                    float [] pointBuf = new float[3];
                    p.get(pointBuf);

                    TransformGroup boneGroup = new TransformGroup(transform3D);
                    //boneGroup.addChild(getLineFromOrigin(new float[]{0,50,0}));
                    //childGroup.addChild(getLineFromOrigin(pointBuf));
                    childGroup.addChild(boneGroup);
                    boneGroupsMap.put(associatedBone, boneGroup);
                    
                }
                parentGroup.addChild(childGroup);

                recursivelyConvertElements(childGroup, ctx.getPlaceableEntityTree().getEntityId(entity) , ctx, bonesMap, boneGroupsMap, testMatrix);
            }
            testMatrix.set(backup);
        }
    }


    private ShaderAppearance getShaderAppearance() {
        SourceCodeShader shader = new SourceCodeShader(Shader.SHADING_LANGUAGE_CG, Shader.SHADER_TYPE_VERTEX, "");
        //ShaderProgram program = new CgShaderProgram();
        //program.s
        ShaderAppearance shaderAppearance = new ShaderAppearance();
        shaderAppearance.setShaderProgram(null);
        return null;
    }



    private void convertVertexesInfo(HumanVERT vert, GeometryInfo geometryInfo, BONS bons, Map<Bone13f,TransformGroup> boneGroupsMap) {
        int vertexesQuantity = vert.getVertexesQuantity();
        SkinnedVertex [] vertexes = vert.getSkinnedVertexArray();

        Point3f [] coordinates = new Point3f[vertexesQuantity];
        Vector3f [] normals = new Vector3f[vertexesQuantity];
        //TODO: textures
        Transform3D
                t1 = new Transform3D(),
                t2 = new Transform3D(),
                t3 = new Transform3D(),
                t4 = new Transform3D(),
                tr = new Transform3D();
        
        Bone13f[] bones = bons.getBones();
        for(int i = 0; i < vertexesQuantity; i++){
            t1.setIdentity();
            t2.setIdentity();
            t3.setIdentity();
            t4.setIdentity();
            tr.setZero();

            SkinnedVertex vertex = vertexes[i];

            int bone1 = vertex.getBonesIndexes()[0];
            int bone2 = vertex.getBonesIndexes()[1];
            int bone3 = vertex.getBonesIndexes()[2];
            int bone4 = vertex.getBonesIndexes()[3];

            try{
                t1.set(transformMap.get(bones[bone1].getDumyIndex()));
            //boneGroupsMap.get(bones[bone1]).getTransform(t1);
            } catch (Exception ex) {
                System.out.println(ex.getClass().getSimpleName() + ": bone " + bone1 + " index: " + i);
            }
            try{
            //boneGroupsMap.get(bones[bone2]).getTransform(t2);
                t2.set(transformMap.get(bones[bone2].getDumyIndex()));
            } catch (Exception ex) {
                System.out.println(ex.getClass().getSimpleName() + ": bone " + bone2 + " index: " + i);
            }
            try{
            //boneGroupsMap.get(bones[bone3]).getTransform(t3);
                t3.set(transformMap.get(bones[bone3].getDumyIndex()));
            } catch (Exception ex) {
                System.out.println(ex.getClass().getSimpleName() + ": bone " + bone3 + " index: " + i);
            }
            try{
            //boneGroupsMap.get(bones[bone4]).getTransform(t4);
                t4.set(transformMap.get(bones[bone4].getDumyIndex()));
            } catch (Exception ex) {
                System.out.println(ex.getClass().getSimpleName() + ": bone " + bone4 + " index: " + i);
            }
            
            Point3f p1 = new Point3f(vertexes[i].getCoord());
            Point3f p2 = new Point3f(vertexes[i].getCoord());
            Point3f p3 = new Point3f(vertexes[i].getCoord());
            Point3f p4 = new Point3f(vertexes[i].getCoord());

            t1.transform(p1);
            t2.transform(p2);
            t3.transform(p3);
            t4.transform(p4);

            p1.scale(vertex.getBonesWeights()[0]);
            p2.scale(vertex.getBonesWeights()[1]);
            p3.scale(vertex.getBonesWeights()[2]);
            p4.scale(vertex.getBonesWeights()[3]);

            Point3f coord = new Point3f(p1);
            coord.add(p2);
            coord.add(p3);
            coord.add(p4);
            
            //Point3f coord = new Point3f(vertexes[i].getCoord());
            //Vector3f normal = new Vector3f(vertexes[i].getNormal());

            Vector3f n1 = new Vector3f(vertexes[i].getNormal());
            Vector3f n2 = new Vector3f(vertexes[i].getNormal());
            Vector3f n3 = new Vector3f(vertexes[i].getNormal());
            Vector3f n4 = new Vector3f(vertexes[i].getNormal());

            n1.scale(vertex.getBonesWeights()[0]);
            n2.scale(vertex.getBonesWeights()[1]);
            n3.scale(vertex.getBonesWeights()[2]);
            n4.scale(vertex.getBonesWeights()[3]);

            Vector3f normal = new Vector3f(n1);
            normal.add(n2);
            normal.add(n3);
            normal.add(n4);

            normal.normalize();

            coordinates[i] = coord;
            normals[i] = normal;
            
            //TODO: textures

        }

        geometryInfo.setCoordinates(coordinates);
        geometryInfo.setNormals(normals);
    }

    private void convertIndexesInfo(INDI indi, GeometryInfo geometryInfo) {
        int indexesQuantity = indi.getIndexesQuantity();

        geometryInfo.setStripCounts(new int[]{indexesQuantity});
        geometryInfo.setCoordinateIndices(indi.getIntegerIndexes());
        geometryInfo.setUseCoordIndexOnly(true);

    }

    private Shape3D getLineFromOrigin(float[] endPoint) {
        LineArray la = new LineArray(2, LineArray.COORDINATES | LineArray.COLOR_3);
        la.setCoordinate(0, new float[]{0,0,0});
        la.setCoordinate(1, endPoint);
        la.setColor(0, new float[]{.5f,.2f,0});
        la.setColor(1, new float[]{1,1,0});
        Appearance a = new Appearance();
        a.setLineAttributes(new LineAttributes(1.0f, LineAttributes.PATTERN_SOLID, true));
        Shape3D shape = new Shape3D(la, a);
        Point3f p = new Point3f(endPoint);

        System.out.println(p.distance(new Point3f(new float[]{0,0,0})));
        return  shape;
    }

    private Shape3D desiredPoint(float [] coord, Color3f color, float size) {

        Appearance a = new Appearance();
        a.setPointAttributes(new PointAttributes(size, true));

        PointArray pa = new PointArray(1, PointArray.COORDINATES | PointArray.COLOR_3);
        pa.setColor(0, color);
        pa.setCoordinates(0, coord);

//        System.out.println(String.format("Bones for vertex %d: %d, %d, %d, %d",
//                index, vertex.getBonesIndexes()[0], vertex.getBonesIndexes()[1], vertex.getBonesIndexes()[2], vertex.getBonesIndexes()[3]));
//
//        System.out.println(String.format("Weights: %.2f   %.2f   %.2f   %.2f",
//                 vertex.getBonesWeights()[0], vertex.getBonesWeights()[1], vertex.getBonesWeights()[2], vertex.getBonesWeights()[3]));


        pa.setCapability(PointArray.ALLOW_COORDINATE_WRITE);
        point = new Shape3D(pa, a);
        point.setCapability(Shape3D.ALLOW_GEOMETRY_READ | Shape3D.ALLOW_GEOMETRY_WRITE);
        return point;
    }
}
