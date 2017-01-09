/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.converters;

import it.panzers.scene.io.structures.Vertex8f;
import it.panzers.scene.main.entity.INDI;
import it.panzers.scene.main.entity.MESH;
import it.panzers.scene.main.entity.UnitVERT;
import it.panzers.scene.main.exceptions.SceneDataException;
import org.scijava.java3d.Appearance;
import org.scijava.java3d.Geometry;
import org.scijava.java3d.Group;
import org.scijava.java3d.Material;
import org.scijava.java3d.Shape3D;
import org.scijava.java3d.TransformGroup;
import org.scijava.java3d.utils.geometry.GeometryInfo;
import org.scijava.vecmath.Point3f;
import org.scijava.vecmath.Vector3f;

/**
 *
 * @author Luigi
 */
public class MeshConverter extends AbstractConverter<MESH>{

    public Group convert(MESH mesh) throws SceneDataException{

        if(mesh == null) {
            throw new SceneDataException();
        }

        UnitVERT vertexes = mesh.getVertexes();
        INDI indexes = mesh.getIndexes();
        boolean stripe = mesh.isTriangleStripe();

        TransformGroup tg = getTransformGroup(mesh.getTransformMatrix());

        //BoundingBox boundingBox = new BoundingBox(); //TODO: caricare BBOX

        int primitive;
        if(stripe) {
            primitive = GeometryInfo.TRIANGLE_STRIP_ARRAY;
        }else{
            primitive = GeometryInfo.TRIANGLE_ARRAY;
        }
        
        GeometryInfo geometryInfo = new GeometryInfo(primitive);

        convertVertexesInfo(vertexes, geometryInfo);
        convertIndexesInfo(indexes, geometryInfo);

        Geometry geometry = geometryInfo.getIndexedGeometryArray();

        Appearance appearance = new Appearance();        
        //TODO: da rimuovere dopo l'applicazione delle texture
        Material material = new Material();
        material.setAmbientColor(0.3f, 0.3f, 0.3f);
        material.setDiffuseColor(0.6f, 0.6f, 0.6f);
        appearance.setMaterial(material);

        Shape3D shape = new Shape3D(geometry, appearance);

        tg.addChild(shape);

        return tg;
    }

    private void convertVertexesInfo(UnitVERT vert, GeometryInfo geometryInfo) {
        int vertexesQuantity = vert.getVertexesQuantity();
        Vertex8f [] vertexes = vert.getVertexes();

        Point3f [] coordinates = new Point3f[vertexesQuantity];
        Vector3f [] normals = new Vector3f[vertexesQuantity];
        //TODO: textures

        for(int i = 0; i < vertexesQuantity; i++){
            coordinates[i] = new Point3f(vertexes[i].getCoord());
            normals[i] = new Vector3f(vertexes[i].getNormal());
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
}
