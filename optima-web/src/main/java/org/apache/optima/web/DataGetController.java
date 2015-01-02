package org.apache.optima.web;


import java.sql.SQLException;

import org.apache.optima.web.component.HiveAdapterService;
import org.apache.optima.web.util.JsonResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

@Controller
public class DataGetController {
	@Autowired
	HiveAdapterService hService;
	
    @RequestMapping("/data/sample")
    public @ResponseBody String upload(
            @RequestParam(value="table_name") String tableName) {
    	JsonObject result;
		try {
			result = hService.getDataFromTable(tableName);
		} catch (SQLException e) {
			e.printStackTrace();
			return JsonResultUtil.getDefaultServerErrorJson().toString();
		}
    	return result.toString();
    }

}
