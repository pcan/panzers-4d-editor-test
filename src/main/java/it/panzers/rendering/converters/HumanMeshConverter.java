/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.converters;

import it.panzers.rendering.entity.Skeleton;
import it.panzers.rendering.shaders.ShaderLoader;
import it.panzers.rendering.shaders.ShaderParameters;
import it.panzers.scene.io.structures.Bone13f;
import it.panzers.scene.io.structures.SkinnedVertex;
import it.panzers.scene.main.entity.BONS;
import it.panzers.scene.main.entity.HumanVERT;
import it.panzers.scene.main.entity.INDI;
import it.panzers.scene.main.entity.SKVS;
import it.panzers.scene.main.exceptions.SceneDataException;
import org.scijava.java3d.Appearance;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.GLSLShaderProgram;
import org.scijava.java3d.GeometryArray;
import org.scijava.java3d.Group;
import org.scijava.java3d.IndexedGeometryArray;
import org.scijava.java3d.IndexedTriangleArray;
import org.scijava.java3d.IndexedTriangleStripArray;
import org.scijava.java3d.Material;
import org.scijava.java3d.Node;
import org.scijava.java3d.PointArray;
import org.scijava.java3d.PointAttributes;
import org.scijava.java3d.Shader;
import org.scijava.java3d.ShaderAppearance;
import org.scijava.java3d.ShaderAttribute;
import org.scijava.java3d.ShaderAttributeArray;
import org.scijava.java3d.ShaderAttributeSet;
import org.scijava.java3d.ShaderProgram;
import org.scijava.java3d.Shape3D;
import org.scijava.java3d.SourceCodeShader;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;
import org.scijava.java3d.utils.geometry.GeometryInfo;
import org.scijava.vecmath.Color3f;
import org.scijava.vecmath.Matrix4f;
import org.scijava.vecmath.Point3f;
import org.scijava.vecmath.Point4f;
import org.scijava.vecmath.Vector3f;

/**
 *
 * @author Pierantonio
 */
public class HumanMeshConverter extends AbstractConverter<SKVS>{

    @Override
    public Group convert(SKVS skvs) throws SceneDataException {
        BranchGroup root = new BranchGroup();
        
        if(skvs == null) {
            throw new SceneDataException();
        }

        HumanVERT vertexes = skvs.getVertexes();
        INDI indexes = skvs.getIndexes();
        boolean stripe = skvs.isTriangleStripe();

        //test code: -----------------
        Transform3D modelWorldTransform = new Transform3D();

        Skeleton skeleton = new Skeleton(skvs);
        skeleton.transformBone(5, new Transform3D(), modelWorldTransform);
        skeleton.transformBone(3, new Transform3D(), modelWorldTransform);
        Transform3D rot = new Transform3D();
        rot.rotY(-0.5f);
        skeleton.transformBone(9, rot, null);

        //-------------------

        root.addChild(getHumanModel(vertexes, indexes, stripe, skeleton));

        //root.addChild(getHumanModelOld(vertexes, indexes, skvs.getBons(), GeometryInfo.TRIANGLE_STRIP_ARRAY, skeleton, getShader(getShaderParameters(), skeleton)));

        root.compile();
        return root;
    }

    private Group getHumanModel(HumanVERT vert, INDI indi, boolean isStripe, Skeleton skeleton){
        TransformGroup mainGroup = new TransformGroup();
        int vertexesQuantity = vert.getVertexesQuantity();
        int indexesQuantity = indi.getIndexesQuantity();

        ShaderParameters shaderParams = getShaderParameters();

        IndexedGeometryArray geometry = getEmptyPrimitive(vertexesQuantity, indexesQuantity, isStripe, shaderParams);

        Point3f[] coordinates = new Point3f[vertexesQuantity];
        Vector3f[] normals = new Vector3f[vertexesQuantity];
        Point4f[] boneIndexes = new Point4f[vertexesQuantity];
        Point4f[] boneWeights = new Point4f[vertexesQuantity];

        SkinnedVertex[] skinnedVertexsArray = vert.getSkinnedVertexArray();

        for (int i = 0; i < vertexesQuantity; i++) {
            coordinates[i] = new Point3f(skinnedVertexsArray[i].getCoord());
            normals[i] = new Vector3f(skinnedVertexsArray[i].getNormal());
            boneIndexes[i] = new Point4f(skinnedVertexsArray[i].getBoneIndexesFloat());
            boneWeights[i] = new Point4f(skinnedVertexsArray[i].getBonesWeights());
        }

        geometry.setCoordinates(0, coordinates);
        geometry.setNormals(0,normals);
        

        //geometry.setVertexAttrs(vertexesQuantity, indexesQuantity, vertexAttrs)
        geometry.setCoordinateIndices(0,indi.getIntegerIndexes());

        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setAmbientColor(0.3f, 0.3f, 0.3f);
        material.setDiffuseColor(0.6f, 0.6f, 0.6f);
        appearance.setMaterial(material);

        geometry.setVertexAttrs(0, 0, boneIndexes);
        geometry.setVertexAttrs(1, 0, boneWeights);
        
        //for (int i = 0; i < 644; i++)
//        geometry.setVertexAttrs(0, 0, (float[])shaderData.getVariableValues()[0]);
//        geometry.setVertexAttrs(1, 0, (float[])shaderData.getVariableValues()[1]);

        
        //Shape3D shape = new Shape3D(geometry,appearance);
        Shape3D shape = new Shape3D(geometry,getShader(shaderParams, skeleton));

        mainGroup.addChild(shape);

        return mainGroup;
    }


    private IndexedGeometryArray getEmptyPrimitive(int vertexesQuantity , int indexesQuantity, boolean isStripe, ShaderParameters shaderParams) {
        IndexedGeometryArray geometry;
        int vertexFormat = IndexedGeometryArray.COORDINATES |
                IndexedGeometryArray.NORMALS |
                IndexedGeometryArray.VERTEX_ATTRIBUTES |
                IndexedGeometryArray.USE_COORD_INDEX_ONLY;

        int [] vertexAttrSizes = shaderParams.getVariableSizes();
        int vertexAttrCount = vertexAttrSizes.length;

        if(isStripe) {
            geometry = new IndexedTriangleStripArray(
                    vertexesQuantity,
                    vertexFormat,
                    0,
                    null,
                    vertexAttrCount,
                    vertexAttrSizes,
                    indexesQuantity,
                    new int[]{indexesQuantity});
        }else{
            geometry = new IndexedTriangleArray(vertexesQuantity,
                    vertexFormat,
                    0,
                    null,
                    vertexAttrCount,
                    vertexAttrSizes,
                    indexesQuantity);
        }
        

        return geometry;
    }


    private ShaderAppearance getShader(ShaderParameters shaderData, Skeleton skeleton){
        ShaderAppearance app = null;
        try{
            String vertexProgram = ShaderLoader.loadShader("/shaders/skinning_vp.glsl");
            Shader[] shaders = new Shader[1];
            shaders[0] = new SourceCodeShader(Shader.SHADING_LANGUAGE_GLSL, Shader.SHADER_TYPE_VERTEX, vertexProgram);
            ShaderProgram shaderProgram = new GLSLShaderProgram();
            shaderProgram.setShaders(shaders);
            shaderProgram.setVertexAttrNames(shaderData.getVariableNames());
            shaderProgram.setShaderAttrNames(new String[]{"skinMatrices"});
            app = new ShaderAppearance();

            Matrix4f [] skinMatrixArray = new Matrix4f[skeleton.getBonesCount()];
            for(int i = 0; i<skeleton.getBonesCount(); i++) {
                skinMatrixArray[i] = skeleton.getSkinMatrix(i);
            }


            ShaderAttribute attribute = new ShaderAttributeArray("skinMatrices",skinMatrixArray);
            attribute.setCapability(ShaderAttributeArray.ALLOW_VALUE_WRITE);
            ShaderAttributeSet set = new ShaderAttributeSet();
            set.put(attribute);

            

            app.setShaderAttributeSet(set);
            app.setShaderProgram(shaderProgram);

            
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return app;
    }


    private ShaderParameters getShaderParameters(){
        String[] variableNames = {"boneIndexes","boneWeights"};
        int[] variableSizes = { 4, 4 };
        return new ShaderParameters(variableNames, variableSizes);
    }


    // <editor-fold defaultstate="collapsed" desc="getHumanModel OLD">
    private Node getHumanModelOld(HumanVERT vert, INDI indexes, BONS bons, int primitive, Skeleton modelSkeleton, Appearance app) {
        TransformGroup mainGroup = new TransformGroup();
        GeometryInfo geometryInfo = new GeometryInfo(primitive);

        int vertexesQuantity = vert.getVertexesQuantity();
        SkinnedVertex[] vertexes = vert.getSkinnedVertexArray();

        Point3f[] coordinates = new Point3f[vertexesQuantity];
        Vector3f[] normals = new Vector3f[vertexesQuantity];
        //TODO: textures
        Transform3D t1 = new Transform3D(),
                t2 = new Transform3D(),
                t3 = new Transform3D(),
                t4 = new Transform3D();

        Bone13f[] bones = bons.getBones();
        for (int i = 0; i < vertexesQuantity; i++) {

            SkinnedVertex vertex = vertexes[i];
            // <editor-fold defaultstate="collapsed" desc="cpu skinning">
            int bone1 = vertex.getBonesIndexes()[0];
            int bone2 = vertex.getBonesIndexes()[1];
            int bone3 = vertex.getBonesIndexes()[2];
            int bone4 = vertex.getBonesIndexes()[3];


            t1.set(modelSkeleton.getSkinMatrix(bone1));
            t2.set(modelSkeleton.getSkinMatrix(bone2));
            t3.set(modelSkeleton.getSkinMatrix(bone3));
            t4.set(modelSkeleton.getSkinMatrix(bone4));


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
            // </editor-fold>

            coordinates[i] =coord;// new Point3f(vertexes[i].getCoord()); //  coord;
            normals[i] =normal;// new Vector3f(vertexes[i].getNormal()); // normal;

            //TODO: textures

        }

        geometryInfo.setCoordinates(coordinates);
        geometryInfo.setNormals(normals);

        convertIndexesInfo(indexes, geometryInfo);


        GeometryArray geometry = geometryInfo.getIndexedGeometryArray();
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setAmbientColor(0.3f, 0.3f, 0.3f);
        material.setDiffuseColor(0.6f, 0.6f, 0.6f);
        app.setMaterial(material);

        Shape3D shape = new Shape3D(geometry, appearance);

        mainGroup.addChild(shape);

        return mainGroup;
    }

    private void convertIndexesInfo(INDI indi, GeometryInfo geometryInfo) {
        int indexesQuantity = indi.getIndexesQuantity();
        geometryInfo.setStripCounts(new int[]{indexesQuantity});
        geometryInfo.setCoordinateIndices(indi.getIntegerIndexes());
        geometryInfo.setUseCoordIndexOnly(true);
    }
    // </editor-fold>


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
        Shape3D point = new Shape3D(pa, a);
        point.setCapability(Shape3D.ALLOW_GEOMETRY_READ | Shape3D.ALLOW_GEOMETRY_WRITE);
        return point;
    }
}
