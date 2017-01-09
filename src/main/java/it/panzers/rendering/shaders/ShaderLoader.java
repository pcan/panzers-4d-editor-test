/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.panzers.rendering.shaders;

import java.io.IOException;
import java.net.URL;
import org.scijava.java3d.utils.shader.StringIO;

/**
 *
 * @author Pierantonio
 */
public class ShaderLoader {

    public static String loadShader(String name) throws IOException {
        URL url = getResource(name);
        return StringIO.readFully(url);
    }


    private static URL getResource(String filename) {
         URL url = ShaderLoader.class.getResource(filename);
         return url;
    }

}
