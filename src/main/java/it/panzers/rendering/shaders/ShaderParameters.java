/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.shaders;

/**
 *
 * @author Pierantonio
 */
public class ShaderParameters {

    private String[] variableNames;

    private int [] variableSizes;



    public ShaderParameters() {
    }

    public ShaderParameters(String[] variableNames, int[] variableSizes) {
        this.variableNames = variableNames;
        this.variableSizes = variableSizes;
    }

    public String[] getVariableNames() {
        return variableNames;
    }

    public void setVariableNames(String[] variableNames) {
        this.variableNames = variableNames;
    }

    public int[] getVariableSizes() {
        return variableSizes;
    }

    public void setVariableSizes(int[] variableSizes) {
        this.variableSizes = variableSizes;
    }


}
