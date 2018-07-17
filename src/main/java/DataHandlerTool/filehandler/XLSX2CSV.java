/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package DataHandlerTool.filehandler;

import DataHandlerTool.exception.DataHandlerException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A rudimentary XLSX -> CSV processor modeled on the
 * POI sample program XLS2CSfrom the package
 * org.apache.poi.hssf.eventusermodel.examples.
 * As with the HSSF version, this tries to spot missing
 *  rows and cells, and output empty entries for them.
 * <p>
 * Data sheets are read using a SAX parser to keep the
 * memory footprint relatively small, so this should be
 * able to read enormous workbooks.  The styles table and
 * the shared-string table must be kept in memory.  The
 * standard POI styles table class is used, but a custom
 * (read-only) class is used for the shared string table
 * because the standard POI SharedStringsTable grows very
 * quickly with the number of unique strings.
 * <p>
 * For a more advanced implementation of SAX event parsing
 * of XLSX files, see {@link XSSFEventBasedExcelExtractor}
 * and {@link XSSFSheetXMLHandler}. Note that for many cases,
 * it may be possible to simply use those with a custom 
 * {@link SheetContentsHandler} and no SAX code needed of
 * your own!
 */
public class XLSX2CSV implements FileHandler{
    Logger logger = LogManager.getLogger(getClass());
    /**
     * Uses the XSSF Event SAX helpers to do most of the work
     *  of parsing the Sheet XML, and outputs the contents
     *  as a (basic) CSV.
     */
    private class SheetToCSV implements SheetContentsHandler {
    	private boolean isFirstRow = false;
    	
        private int currentRow = -1;
        private int currentCol = -1;
        

        @Override
        public void startRow(int rowNum) {
        	if(rowNum == 0) {
        		isFirstRow = true;
        	}
            currentRow = rowNum;
            currentCol = -1;
        }

        @Override
        public void endRow(int rowNum) {
        	if(rowNum == 0) {
        		isFirstRow = false;
        	}
            // Ensure the minimum number of columns
            for (int i=currentCol; i<(minColumns - 1); i++) {
            	rowList.add("");
            }
        	List<String> coList = new ArrayList<String>();
        	coList.addAll(rowList);
        	dataList.add(coList);
        	rowList.clear();
        }

        @Override
        public void cell(String cellReference, String formattedValue,
                XSSFComment comment) {
        	if(isFirstRow) {
        		minColumns++;
        	}
            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if(cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }

            // Did we miss any cells?
            int thisCol = (new CellReference(cellReference)).getCol();
            int missedCols = thisCol - currentCol - 1;
            for (int i=0; i<missedCols; i++) {
            	rowList.add("");
            }
            currentCol = thisCol;
            rowList.add(formattedValue);
        }

		@Override
		public void headerFooter(String arg0, boolean arg1, String arg2) {
		}
    }


    ///////////////////////////////////////

    private  OPCPackage xlsxPackage = null;

    /**
     * Number of columns to read starting with leftmost
     */
    private int minColumns = 1;
    /**
     * 行数据
     */
    private List<String> rowList = new ArrayList<String>();
    
    /**
     * 总数据
     */
    private List<List<String>> dataList = new ArrayList<List<String>>();


    //无用
    private String charsetName;

    @Override
    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    @Override
    public void setFilePath(String filePath) throws DataHandlerException {
        try {
            this.xlsxPackage = OPCPackage.open(filePath,PackageAccess.READ);
        } catch (InvalidFormatException e) {
            throw new DataHandlerException();
        }
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles The table of styles that may be referenced by cells in the sheet
     * @param strings The table of strings that may be referenced by cells in the sheet
     * @param sheetInputStream The stream to read the sheet-data from.

     * @exception IOException An IO exception from the parser,
     *            possibly from a byte stream or character stream
     *            supplied by the application.
     * @throws SAXException if parsing the XML data fails.
     */
    public void processSheet(
            StylesTable styles,
            ReadOnlySharedStringsTable strings,
            SheetContentsHandler sheetHandler, 
            InputStream sheetInputStream) throws IOException, SAXException, DataHandlerException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(
                  styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
         } catch(ParserConfigurationException e) {
            throw new DataHandlerException("SAX parser appears to be broken - " + e.getMessage());
         }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException If reading the data from the package fails.
     * @throws SAXException if parsing the XML data fails.
     */
    public void process() throws DataHandlerException {
        try {
            ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(this.xlsxPackage);
            XSSFReader xssfReader = new XSSFReader(this.xlsxPackage);
            StylesTable styles = xssfReader.getStylesTable();
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
            int index = 0;
            while (iter.hasNext()) {
                try (InputStream stream = iter.next()) {
                    String sheetName = iter.getSheetName();
                   logger.info("[index="+index+ "],[SheetName="+sheetName+"]");
                    processSheet(styles, strings, new SheetToCSV(), stream);
                }
                ++index;
            }
        } catch (Exception e) {
            throw new DataHandlerException();
        }finally {
            try {
                xlsxPackage.close();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }


    @Override
    public List<List<String>> getDataList() {
		return dataList;
	}

}
