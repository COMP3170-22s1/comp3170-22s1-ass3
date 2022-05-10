package comp3170.ass3.sceneobjects;

import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.GLBuffers;
import comp3170.Shader;
import comp3170.ass3.shaders.ShaderLibrary;

public class Plane extends SceneObject {

	static final private String VERTEX_SHADER = "simpleVertex.glsl";
	static final private String FRAGMENT_SHADER = "simpleFragment.glsl";
	
	private Shader shader;
	private Vector4f[] vertices;
	private int vertexBuffer;
	private int[] indices;
	private int indexBuffer;
	private Vector4f colour = new Vector4f(1,1,1,1); // white

	public Plane(float size) {
		shader = ShaderLibrary.compileShader(VERTEX_SHADER, FRAGMENT_SHADER);
		createMesh(size);
	}

	private void createMesh(float size) {
		// 2-----3     x
		// |    /|     |     (right-handed coordinates)
		// |  /  |     +--z
		// |/    |    /
		// 0-----1   y (out)
	
		vertices = new Vector4f[] {
			new Vector4f(0,0,0,1),
			new Vector4f(0,0,size,1),
			new Vector4f(size,0,0,1),
			new Vector4f(size,0,size,1),				
		};
		
		vertexBuffer = GLBuffers.createBuffer(vertices);

		indices = new int[] {
			0,1,3,
			3,2,0,
		};
		
		indexBuffer =  GLBuffers.createIndexBuffer(indices);

	}
	
	@Override
	protected void drawSelf(Matrix4f mvpMatrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.enable();		
		shader.setUniform("u_mvpMatrix", mvpMatrix);
		shader.setAttribute("a_position", vertexBuffer);
		shader.setUniform("u_colour", colour);
		
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, indexBuffer);
		gl.glPolygonMode(GL.GL_FRONT_AND_BACK, GL4.GL_FILL);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);		
	}

	
}
