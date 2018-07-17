package DataHandlerTool.filehandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import DataHandlerTool.exception.DataHandlerException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * 处理CSV文件
 */
public class CSV implements FileHandler{


	Logger logger = LogManager.getLogger(getClass());

	//默认字符集
	private String charsetName = "UTF-8";
	
	//文件路径
	private String filePath;

	@Override
    public void setCharsetName(String charsetName) {
		this.charsetName = charsetName;
	}

	@Override
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
     * 总数据
     */
    private List<List<String>> dataList = new ArrayList<List<String>>();

	/**
	 * 文件处理方法
	 * 调用Apache 的csv工具类
	 */
	public void process() throws DataHandlerException {
		Reader in = null;
		try {
			in = new InputStreamReader(new FileInputStream(filePath),charsetName);
			Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
			for(CSVRecord record : records) {
				int size = record.size();
				List<String> list = new ArrayList<String>(size);
				for(int i = 0; i < size; i++) {
					list.add(record.get(i));
				}
				dataList.add(list);
			}
		} catch (UnsupportedEncodingException e) {
			throw new DataHandlerException();
		} catch (FileNotFoundException e) {
			logger.error("未找到相关文件：%s",e);
		} catch (IOException e) {
			throw new DataHandlerException();
		}finally{
			try {
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

	@Override
	public List<List<String>> getDataList() {
		return dataList;
	}
    
    
	

}
