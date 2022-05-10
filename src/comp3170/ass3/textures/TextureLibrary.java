package comp3170.ass3.textures;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class TextureLibrary {
	
	final private static File DIRECTORY = new File("src/comp3170/ass3/textures"); 

	public final static Map<String, Integer> loadedTextures = new HashMap<String, Integer>();
	
	/**
	 * Load a texture from this directory.
	 * 
	 * @param filename
	 * @return The GL texture ID
	 * @throws IOException If the file can't be read
	 */
	
	public static int loadTexture(String filename) throws IOException {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		if (loadedTextures.containsKey(filename)) {
			return loadedTextures.get(filename);
		}
		
		File textureFile = new File(DIRECTORY, filename);
		
		Texture tex = TextureIO.newTexture(textureFile, true);
		int textureID = tex.getTextureObject();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
		
		// TODO: Add your code to configure the texture here
				
		loadedTextures.put(filename, textureID);
		return textureID;
	}

}
