/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.entity;

import it.panzers.scene.io.structures.Bone13f;
import it.panzers.scene.main.entity.AbstractPlaceableEntity;
import it.panzers.scene.main.entity.BONS;
import it.panzers.scene.main.entity.SKVS;
import it.panzers.scene.main.util.PlaceableEntityTree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.scijava.java3d.Transform3D;
import org.scijava.vecmath.Matrix4f;

/**
 *
 * @author Pierantonio
 */
public class Skeleton {

    private Map<Integer,Bone> bonesMap = new HashMap<Integer, Bone>();
    private Bone13f [] bone13fArray;

    public Skeleton(SKVS skvs) {
        PlaceableEntityTree entityTree = skvs.getContext().getPlaceableEntityTree();
        BONS bons = skvs.getBons();
        bone13fArray = bons.getBones();
        Map<Integer,Bone13f> bones13fMap = skvs.getDumyBonesMap();
        findRootBones(-1, entityTree, bones13fMap);
        bones13fMap.clear();
        bones13fMap = null;
    }

    public void transformBone(int boneId, Transform3D localBoneTransform, Transform3D modelWorldTransform) {
        Bone selectedBone = bonesMap.get(boneId);
        if(selectedBone != null) {
            selectedBone.setLocalTransform(localBoneTransform);
            selectedBone.onTransform(modelWorldTransform);
        }
    }

    private void findRootBones(int parentEntityId, PlaceableEntityTree entityTree, Map<Integer,Bone13f> bones13fMap) {

        List<AbstractPlaceableEntity> children = entityTree.getChildren(parentEntityId);
        for(AbstractPlaceableEntity child : children) {
            int childId = entityTree.getEntityId(child);
            if(bones13fMap.containsKey(childId)){
                loadSkeleton(null, parentEntityId, entityTree, bones13fMap);
                break;
            }else{
                findRootBones(childId, entityTree, bones13fMap);
            }
        }
    }
    
    private List<Bone> loadSkeleton(Bone parent, int parentEntityId, PlaceableEntityTree entityTree, Map<Integer,Bone13f> bones13fMap) {
        List<Bone> bones = new ArrayList<Bone>();

        for(Entry<Integer,Bone13f> e : bones13fMap.entrySet()) {
            AbstractPlaceableEntity entity = entityTree.get(e.getKey());
            if(entity.getParentEntityId() == parentEntityId) {
                Transform3D restRelative = new Transform3D(entity.getTransformMatrix().getMatrix4x4f());
                Transform3D restAbsoluteInverted = new Transform3D(e.getValue().getMatrix4x4f());
                Bone bone = new Bone(restRelative, restAbsoluteInverted, parent, entity.getEntityName());
                bonesMap.put(e.getValue().getDumyIndex(), bone);
                bone.setChildren(loadSkeleton(bone, e.getValue().getDumyIndex(), entityTree, bones13fMap));
                bones.add(bone);
            }
        }
        return bones;
    }

    private Transform3D getSkinTransform(int boneId) {
        return bonesMap.get(boneId).getSkinTransform();
    }

    public Bone getBone(int boneId) {
        return bonesMap.get(bone13fArray[boneId].getDumyIndex());
    }

    public int getBonesCount(){
        return bonesMap.size();
    }

    public Matrix4f getSkinMatrix(int boneId) {
        Matrix4f matrix4f = new Matrix4f();
        getBone(boneId).getSkinTransform().get(matrix4f);
        //getSkinTransform(boneId).get(matrix4f);
        return matrix4f;
    }

}
