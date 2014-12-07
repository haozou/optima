package org.apache.optima.web;


import org.apache.optima.web.component.HiveAdapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class CreateController {
	@Autowired
	HiveAdapterService hService;
	
    @RequestMapping("/file/upload")
    public @ResponseBody String upload(
            @RequestParam(value="file") MultipartFile file) {
    	JsonObject result = new JsonObject();
//    	hService.loadTableByFile(file);
    	JsonArray array = hService.transferFiltToJson(file);
    	result.addProperty("code", 0);
    	result.add("list", array);
    	return result.toString();
    }

}
