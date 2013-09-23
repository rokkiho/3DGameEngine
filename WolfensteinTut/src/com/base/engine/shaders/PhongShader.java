package com.base.engine.shaders;

import com.base.engine.components.BaseLight;
import com.base.engine.components.DirectionalLight;
import com.base.engine.components.Material;
import com.base.engine.components.PointLight;
import com.base.engine.components.SpotLight;
import com.base.engine.components.Transform;
import com.base.engine.math.Matrix4f;
import com.base.engine.math.Vector3f;
import com.base.engine.util.RenderUtil;

public class PhongShader extends Shader
{
	private static final PhongShader instance = new PhongShader();

	private static final int MAX_POINT_LIGHTS = 4;
	private static final int MAX_SPOT_LIGHTS = 4;

	public static PhongShader getInstance()
	{
		return instance;
	}

	private static Vector3f ambientLight = new Vector3f(0.1f, 0.1f, 0.1f); // ambient
																			// light
																			// color
	private static DirectionalLight directionalLight = new DirectionalLight(
			new BaseLight(new Vector3f(1, 1, 1), 0), new Vector3f(0, 0, 0));

	private static PointLight[] pointLights = new PointLight[] {};
	private static SpotLight[] spotLights = new SpotLight[] {};

	private PhongShader()
	{
		super();

		addVertexShaderFromFile("phongVertex.vs");
		addFragmentShaderFromFile("phongFragment.vfs");
		compileShader();

		addUniform("transform");
		addUniform("transformProjected");
		addUniform("baseColor");
		addUniform("ambientLight");

		addUniform("directionalLight.Base.Color");
		addUniform("directionalLight.Base.Intensity");
		addUniform("directionalLight.Direction");
		addUniform("specularIntensity");
		addUniform("specularPower");
		addUniform("eyePos");

		for (int i = 0; i < MAX_POINT_LIGHTS; i++)
		{
			addUniform("pointLights[" + i + "].Base.Color");
			addUniform("pointLights[" + i + "].Base.Intensity");
			addUniform("pointLights[" + i + "].Atten.Constant");
			addUniform("pointLights[" + i + "].Atten.Linear");
			addUniform("pointLights[" + i + "].Atten.Exponent");
			addUniform("pointLights[" + i + "].Position");
			addUniform("pointLights[" + i + "].Range");
		}

		for (int i = 0; i < MAX_SPOT_LIGHTS; i++)
		{
			addUniform("spotLights[" + i + "].PointLight.Base.Color");
			addUniform("spotLights[" + i + "].PointLight.Base.Intensity");
			addUniform("spotLights[" + i + "].PointLight.Atten.Constant");
			addUniform("spotLights[" + i + "].PointLight.Atten.Linear");
			addUniform("spotLights[" + i + "].PointLight.Atten.Exponent");
			addUniform("spotLights[" + i + "].PointLight.Position");
			addUniform("spotLights[" + i + "].PointLight.Range");

			addUniform("spotLights[" + i + "].Direction");
			addUniform("spotLights[" + i + "].Cutoff");
		}
	}

	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix,
			Material material)
	{
		if (material.getTexture() != null)
			material.getTexture().bind();
		else
			RenderUtil.unbindTextures();

		setUniform("transformProjected", projectedMatrix);
		setUniform("transform", worldMatrix);
		setUniform("baseColor", material.getColor());
		setUniform("ambientLight", ambientLight);
		setUniform("directionalLight", directionalLight);

		setUniformf("specularIntensity", material.getSpecularIntensity());
		setUniformf("specularPower", material.getSpecularPower());

		setUniform("eyePos", Transform.getCamera().getPos());

		for (int i = 0; i < pointLights.length; i++)
		{
			setUniform("pointLights[" + i + "]", pointLights[i]);
		}

		for (int i = 0; i < spotLights.length; i++)
		{
			setUniform("spotLights[" + i + "]", spotLights[i]);
		}
	}

	public static Vector3f getAmbientLight()
	{
		return ambientLight;
	}

	public static void setAmbientLight(Vector3f ambientLight)
	{
		PhongShader.ambientLight = ambientLight;
	}

	public static void setDirectionalLight(DirectionalLight directionalLight)
	{
		PhongShader.directionalLight = directionalLight;
	}

	public static void setPointLight(PointLight[] pointLights)
	{
		if (pointLights.length > MAX_POINT_LIGHTS)
		{
			System.err.println("Error: Too many point lights.");
			new Exception().printStackTrace();
			System.exit(1);
		}

		PhongShader.pointLights = pointLights;
	}

	public static void setSpotLight(SpotLight[] spotLights)
	{
		if (spotLights.length > MAX_SPOT_LIGHTS)
		{
			System.err.println("Error: Too many spot lights.");
			new Exception().printStackTrace();
			System.exit(1);
		}

		PhongShader.spotLights = spotLights;
	}

	public void setUniform(String uniformName, BaseLight baseLight)
	{
		setUniform(uniformName + ".Color", baseLight.getColor());
		setUniformf(uniformName + ".Intensity", baseLight.getIntensity());
	}

	public void setUniform(String uniformName, DirectionalLight directionalLight)
	{
		setUniform(uniformName + ".Base", directionalLight.getBase());
		setUniform(uniformName + ".Direction", directionalLight.getDirection());
	}

	public void setUniform(String uniformName, PointLight pointLight)
	{
		setUniform(uniformName + ".Base", pointLight.getBaseLight());
		setUniformf(uniformName + ".Atten.Constant", pointLight
				.getAttenuation().getConstant());
		setUniformf(uniformName + ".Atten.Linear", pointLight.getAttenuation()
				.getLinear());
		setUniformf(uniformName + ".Atten.Exponent", pointLight
				.getAttenuation().getExponent());
		setUniform(uniformName + ".Position", pointLight.getPosition());
		setUniformf(uniformName + ".Range", pointLight.getRange());
	}

	public void setUniform(String uniformName, SpotLight spotLight)
	{
		setUniform(uniformName + ".PointLight", spotLight.getPointLight());
		setUniform(uniformName + ".Direction", spotLight.getDirection());
		setUniformf(uniformName + ".Cutoff", spotLight.getCutoff());
	}
}
