package org.apache.optima.web.component;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.optima.web.util.JsonResultUtil;
import org.optima.server.model.Column;
import org.optima.server.services.HiveService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class HiveAdapterService {

	static HiveService hService;
	static final String FILE_PATH_PREFIX = "/tmp/";
	static FileOutputStream writer;
	static BufferedReader reader;
	static {
		try {
			hService = new HiveService("localhost", 10000, "", "root");
//			hService = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> loadTableByFile(MultipartFile file) {
		List<String> result = new ArrayList<String>();

		try {
			String tableName = file.getOriginalFilename();
			tableName = tableName.split("\\.")[0];
			String localPath = FILE_PATH_PREFIX + tableName;
			writer = new FileOutputStream(localPath);
			writer.write(file.getBytes());
			reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String columnString = reader.readLine();
			List<Column> columns = transferColumnString2Array(columnString);
			hService.createTable(tableName, true, columns);
			hService.loadLocalData(localPath, tableName);

			// return the head of the file
			for (int i = 0; i < 3; i++) {
				String returnString = "";
				if ((returnString = reader.readLine()) != null) {
					result.add(returnString);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public JsonArray transferFiltToJson(MultipartFile file) {
		JsonArray result = new JsonArray();
		try {
			reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
			String columnString = reader.readLine();
			List<Column> columns = transferColumnString2Array(columnString);
			int columnSize = columns.size();
			String valueString = "";
			while ((valueString = reader.readLine()) != null) {
				String[] lineArray = valueString.trim().split(",");
				if (lineArray.length != columnSize)
					continue;
				JsonObject jobject = new JsonObject();
				for (int i = 0; i < columnSize; i++) {
					Column c = columns.get(i);
					String cValue = lineArray[i];
					jobject.addProperty(c.getColumnName(), cValue);
				}
				result.add(jobject);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public JsonObject getDataFromTable(String tableName) throws SQLException{
		JsonObject result = JsonResultUtil.getDefaultSuccessJson();
		JsonArray dataArray = new JsonArray();
		result.add("list", dataArray);
		ResultSet columnSet = hService.describleTable(tableName);
		List<Column> columns = transferColumnResultSet2Array(columnSet);
		int columnSize = columns.size();
		ResultSet dataSet = hService.getDataFromTable(tableName);
		while(dataSet.next()){
			JsonObject jobject = new JsonObject();
			for (int i = 0; i < columnSize; i++) {
				Column c = columns.get(i);
				String cValue = dataSet.getString(i+1);
				jobject.addProperty(c.getColumnName(), cValue);
			}
			dataArray.add(jobject);
		}
		return result;
	}

	private List<Column> transferColumnString2Array(String columnString) {
		List<Column> result = new ArrayList<Column>();
		String[] columnArray = columnString.split(",");
		for (String s : columnArray) {
			Column c = new Column();
			c.setColumnName(s);
			c.setColumnType("string");
			result.add(c);
		}
		return result;
	}

	private List<Column> transferColumnResultSet2Array(ResultSet res) throws SQLException {
		List<Column> result = new ArrayList<Column>();
		while (res.next()) {
			Column c = new Column();
			c.setColumnName(res.getString(1));
			c.setColumnType(res.getString(2));
			result.add(c);
		}
		return result;
	}
}
