package Data;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class ExcelReader {

    static FileInputStream fis = null ;

    public FileInputStream getFileinputStream() throws FileNotFoundException {
        String filePath = System.getProperty("user.dir") + "/src/test/java/Data/testdata.xlsx";
        File srcFile = new File(filePath);

        try {
            fis = new FileInputStream(srcFile);
        } catch (FileNotFoundException e) {
            System.out.println("Test data file not found. termenating process !, check file path of Test data : " +e.getMessage());
        }

        return fis;
    }

    public Object [][] getExcelData() throws IOException
    {

        fis = getFileinputStream() ;
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);
        int TotalNumberOfRows = 1 ;
        int TotalNumberOfColomns = 7 ;

        String[][] arrayExcelData = new String[TotalNumberOfRows][TotalNumberOfColomns];

        for ( int i=0; i<TotalNumberOfRows; i++)
        {

            for ( int x=0; x<TotalNumberOfColomns; x++)
            {
                XSSFRow row = sheet.getRow(i);
                arrayExcelData[i][x] = row.getCell(x).toString();
            }
        }

        wb.close();
        return arrayExcelData;

    }
}
