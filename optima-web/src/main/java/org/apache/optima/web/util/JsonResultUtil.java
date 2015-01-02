package org.apache.optima.web.util;

import com.google.gson.JsonObject;


public class JsonResultUtil {
	
	
	public static JsonObject getDefaultSuccessJson(){
		JsonObject result = new JsonObject();
		result.addProperty("code", ReturnCodeConstant.SUCCESS_CODE);
		return result;
	}
	
	public static JsonObject getDefaultServerErrorJson(){
		JsonObject result = new JsonObject();
		result.addProperty("code", ReturnCodeConstant.SERVER_ERROR_CODE);
		return result;
	}
}
