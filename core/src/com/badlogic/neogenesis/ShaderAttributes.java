package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

/**
 * The Class ShaderAttributes.
 */
public class ShaderAttributes {
	
	/** The Constant DEFAULT_LIGHT_Z. */
	public static final float DEFAULT_LIGHT_Z = 0.075f;
	
	/** The Constant AMBIENT_INTENSITY. */
	public static final float AMBIENT_INTENSITY = 0.2f;
	
	/** The Constant LIGHT_INTENSITY. */
	public static final float LIGHT_INTENSITY = 3f;
	
	/** The Constant LIGHT_POS. */
	public static final Vector3 LIGHT_POS = new Vector3(0f,0f,DEFAULT_LIGHT_Z);
	
	//Light RGB and intensity (alpha)
	/** The Constant LIGHT_COLOR. */
	public static final Vector3 LIGHT_COLOR = new Vector3(1f, 0.8f, 0.6f);
 
	//Ambient RGB and intensity (alpha)
	/** The Constant AMBIENT_COLOR. */
	public static final Vector3 AMBIENT_COLOR = new Vector3(0.6f, 0.6f, 1f);
 
	//Attenuation coefficients for light falloff
	/** The Constant FALLOFF. */
	public static final Vector3 FALLOFF = new Vector3(.4f, 3f, 20f);
	
	/** The vert. */
	final String VERT =  
			"attribute vec4 "+ShaderProgram.POSITION_ATTRIBUTE+";\n" +
			"attribute vec4 "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			"attribute vec2 "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			
			"uniform mat4 u_projTrans;\n" + 
			" \n" + 
			"varying vec4 vColor;\n" +
			"varying vec2 vTexCoord;\n" +
			
			"void main() {\n" +  
			"	vColor = "+ShaderProgram.COLOR_ATTRIBUTE+";\n" +
			"	vTexCoord = "+ShaderProgram.TEXCOORD_ATTRIBUTE+"0;\n" +
			"	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
			"}";

	/** The frag. */
	final String FRAG = 
			  "#ifdef GL_ES\n" //
			+ "#define LOWP lowp\n" //
			+ "precision mediump float;\n" //
			+ "#else\n" //
			+ "#define LOWP \n" //
			+ "#endif\n" + //
			"//attributes from vertex shader\n" + 
			"varying LOWP vec4 vColor;\n" + 
			"varying vec2 vTexCoord;\n" + 
			"\n" + 
			"//our texture samplers\n" + 
			"uniform sampler2D u_texture;   //diffuse map\n" + 
			"uniform sampler2D u_normals;   //normal map\n" + 
			"\n" + 
			"//values used for shading algorithm...\n" + 
			"uniform vec2 Resolution;         //resolution of screen\n" + 
			"uniform vec3 LightPos;           //light position, normalized\n" + 
			"uniform LOWP vec4 LightColor;    //light RGBA -- alpha is intensity\n" + 
			"uniform LOWP vec4 AmbientColor;  //ambient RGBA -- alpha is intensity \n" + 
			"uniform vec3 Falloff;            //attenuation coefficients\n" + 
			"\n" + 
			"void main() {\n" + 
			"	//RGBA of our diffuse color\n" + 
			"	vec4 DiffuseColor = texture2D(u_texture, vTexCoord);\n" + 
			"	\n" + 
			"	//RGB of our normal map\n" + 
			"	vec3 NormalMap = texture2D(u_normals, vTexCoord).rgb;\n" + 
			"	\n" + 
			"	//The delta position of light\n" + 
			"	vec3 LightDir = vec3(LightPos.xy - (gl_FragCoord.xy / Resolution.xy), LightPos.z);\n" + 
			"	\n" + 
			"	//Correct for aspect ratio\n" + 
			"	LightDir.x *= Resolution.x / Resolution.y;\n" + 
			"	\n" + 
			"	//Determine distance (used for attenuation) BEFORE we normalize our LightDir\n" + 
			"	float D = length(LightDir);\n" + 
			"	\n" + 
			"	//normalize our vectors\n" + 
			"	vec3 N = normalize(NormalMap * 2.0 - 1.0);\n" + 
			"	vec3 L = normalize(LightDir);\n" + 
			"	\n" + 
			"	//Pre-multiply light color with intensity\n" + 
			"	//Then perform \"N dot L\" to determine our diffuse term\n" + 
			"	vec3 Diffuse = (LightColor.rgb * LightColor.a) * max(dot(N, L), 0.0);\n" + 
			"\n" + 
			"	//pre-multiply ambient color with intensity\n" + 
			"	vec3 Ambient = AmbientColor.rgb * AmbientColor.a;\n" + 
			"	\n" + 
			"	//calculate attenuation\n" + 
			"	float Attenuation = 1.0 / ( Falloff.x + (Falloff.y*D) + (Falloff.z*D*D) );\n" + 
			"	\n" + 
			"	//the calculation which brings it all together\n" + 
			"	vec3 Intensity = Ambient + Diffuse * Attenuation;\n" + 
			"	vec3 FinalColor = DiffuseColor.rgb * Intensity;\n" + 
			"	gl_FragColor = vColor * vec4(FinalColor, DiffuseColor.a);\n" + 
			"}";
	
	/** The shader. */
	public ShaderProgram shader;
	
	/**
	 * Instantiates a new shader attributes.
	 */
	public ShaderAttributes(){
		ShaderProgram.pedantic = false;
		shader = new ShaderProgram(VERT, FRAG);
		//ensure it compiled
		if (!shader.isCompiled())
			throw new GdxRuntimeException("Could not compile shader: "+shader.getLog());
		//print any warnings
		if (shader.getLog().length()!=0)
			System.out.println(shader.getLog());
		
		//setup default uniforms
		shader.begin();
		 
		//our normal map
		shader.setUniformi("u_normals", 1); //GL_TEXTURE1
				
		//light/ambient colors
		shader.setUniformf("LightColor", LIGHT_COLOR.x, LIGHT_COLOR.y, LIGHT_COLOR.z, LIGHT_INTENSITY);
		shader.setUniformf("AmbientColor", AMBIENT_COLOR.x, AMBIENT_COLOR.y, AMBIENT_COLOR.z, AMBIENT_INTENSITY);
		shader.setUniformf("Falloff", FALLOFF);
				
		shader.end();
	}
}
