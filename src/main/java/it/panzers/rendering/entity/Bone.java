/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.entity;

import java.util.ArrayList;
import java.util.List;
import org.scijava.java3d.Transform3D;

/**
 *
 * @author Pierantonio
 */
public class Bone {


    private final Bone parent; // the parent of this bone, if any.

    private List<Bone> children = new ArrayList<Bone>(); // the children of this bone.

    private final Transform3D skinTransform = new Transform3D();   // the matrix that gets uploaded to the GPU and used by the vertex shader to skin the vertices that are affected by this bone.

    private final Transform3D worldTransform = new Transform3D();;  // world transform matrix that represents the transform of bone in the world.

    private final Transform3D localTransform = new Transform3D();; // local transform that represents the transform relative to self.

    private final Transform3D restRelative = new Transform3D();;  // this bone rest transform, relative to parent bone (FIXED)

    private final Transform3D restAbsoluteInverted = new Transform3D();;  // inverse of absolute rest transform (relative to model). (FIXED)

    private final String description;

    public Bone(Transform3D restRelative, Transform3D restAbsoluteInverted, Bone parent, String description) {
        this.parent = parent;
        this.restRelative.set(restRelative);
        this.restAbsoluteInverted.set(restAbsoluteInverted);
        this.description = description;
    }

    public List<Bone> getChildren() {
        return children;
    }

    public void setChildren(List<Bone> children) {
        this.children = children;
    }

    public void onTransform(Transform3D modelWorldTransform) {

        if(parent != null) {
            // W_t = parent.W_t * R_r * L_t
            worldTransform.set(parent.worldTransform);
        }else{
            // W_t = M_t * R_r * L_t
            worldTransform.set(modelWorldTransform);
        }

        worldTransform.mul(restRelative);
        worldTransform.mul(localTransform);

        // S_t = W_t * R_ai
        skinTransform.set(worldTransform);
        skinTransform.mul(restAbsoluteInverted);

        for(Bone child : children) {
            child.onTransform(worldTransform);
        }
    }

    public Transform3D getSkinTransform() {
        return skinTransform;
    }

    public Transform3D getWorldTransform() {
        return worldTransform;
    }

    public Transform3D getRestRelative() {
        return restRelative;
    }

    public void setLocalTransform(Transform3D localTransform) {
        this.localTransform.set(localTransform);
    }

    @Override
    public String toString() {
        return description;
    }

}
