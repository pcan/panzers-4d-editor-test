/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.scene.main.entity;

import it.panzers.scene.main.enumerations.SceneTypeEnum;
import it.panzers.scene.main.enumerations.SceneVersionEnum;
import it.panzers.scene.main.util.PlaceableEntityTree;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Pierantonio
 */
public class SceneContext {

    private SceneVersionEnum version = SceneVersionEnum.DEFAULT_VERSION;
    private SceneTypeEnum sceneType = SceneTypeEnum.DEFAULT_SCENE_TYPE;
    private SCEN scen = null;

    private boolean versionHasBeenSet = false;
    private boolean typeHasBeenSet = false;

    public SceneContext(SCEN scen) {
        this.scen = scen;
    }

    public SceneVersionEnum getVersion() {
        return version;
    }

    public void setVersion(SceneVersionEnum version) {
        if(!versionHasBeenSet) {
            this.version = version;
            versionHasBeenSet=true;
        }
    }

    public SceneTypeEnum getSceneType() {
        return sceneType;
    }

    public void setSceneType(SceneTypeEnum sceneType) {
        if(!typeHasBeenSet) {
            this.sceneType = sceneType;
            typeHasBeenSet=true;
        }
    }

    public SCEN getScene() {
        return scen;
    }

    public PlaceableEntityTree getPlaceableEntityTree() {
        return PlaceableEntityTree.getInstance(this);
    }

}
