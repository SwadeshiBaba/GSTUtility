package org.legaldtax.gst2a;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Streams;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.movoto.selenium.example.utils.ReflectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class B2BExcelDocument implements AutoCloseable {

    private Workbook workbook;

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    private final String extension;

    public B2BExcelDocument(Workbook workbook) {
        this.workbook = workbook;
        this.extension = workbook instanceof XSSFWorkbook ? "xlsx" : "xls";
    }

    public static final String B2B_SHEET_NAME = "Pivot_data";

    public Stream<B2BXlsRow> B2BRows(){
        return new B2BExcelDocument.B2BSheet(workbook.getSheet(B2B_SHEET_NAME)).getB2BXlsRows();
    }


    @Override
    public void close() throws Exception {
        try{
            workbook.close();
        } catch(Exception e){
            //ignored
        }
    }

    public interface InternalColumns{
        String STATUS = "Status";
        String COMMENTS = "Comments";
    }

    public interface B2BColumns{
        String GSTN_NUMBER = "GSTIN of supplier";
        String SUPPLIER_NAME_PORTAL = "Trade/Legal name of the Supplier";
        String INVOICE_NUMBER = "Invoice number";
        String INOVICE_TYPE = "Invoice type";
        String INVOICE_DATE = "Invoice Date";
        String INVOICE_VALUE = "Invoice Value";
        String PLACE_OF_SUPPLY = "Place of supply";
        String IS_REVERSE_CHARGE = "Supply Attract Reverse Charge";

        String TAXABLE_VALUE_0 = "Sum of Taxable Value 0";
        String IGST_0= "Sum of Integrated Tax  0";
        String CGST_0 = "Sum of Central Tax 0";
        String SGST_0 = "Sum of State/UT tax 0";
        String CESS_0 = "Sum of Cess  0";

        String TAXABLE_VALUE_5 = "Sum of Taxable Value 5";
        String IGST_5= "Sum of Integrated Tax  5";
        String CGST_5 = "Sum of Central Tax 5";
        String SGST_5 = "Sum of State/UT tax 5";
        String CESS_5 = "Sum of Cess  5";

        String TAXABLE_VALUE_12 = "Sum of Taxable Value 12";
        String IGST_12= "Sum of Integrated Tax  12";
        String CGST_12 = "Sum of Central Tax 12";
        String SGST_12 = "Sum of State/UT tax 12";
        String CESS_12 = "Sum of Cess  12";

        String TAXABLE_VALUE_18 = "Sum of Taxable Value 18";
        String IGST_18= "Sum of Integrated Tax  18";
        String CGST_18 = "Sum of Central Tax 18";
        String SGST_18 = "Sum of State/UT tax 18";
        String CESS_18 = "Sum of Cess  18";

        String TAXABLE_VALUE_28 = "Sum of Taxable Value 28";
        String IGST_28= "Sum of Integrated Tax  28";
        String CGST_28 = "Sum of Central Tax 28";
        String SGST_28 = "Sum of State/UT tax 28";
        String CESS_28 = "Sum of Cess  28";
        String ROUND_OFF = "Round off";
  /*
        String IS_GSTR1_FILED= "GSTR-1/IFF/GSTR-5 Filing Status";
        String GSTR1_FILED_DATE = "GSTR-1/IFF/GSTR-5 Filing Date";
        String GSTR1_FILING_PERIOD = "GSTR-1/IFF/GSTR-5 Filing Period";
        String IS_GSTR3B_FILED = "GSTR-3B Filing Status";
*/
        List<String> ALL_KNOWN_COLUMNS = ReflectionUtils.staticField(B2BColumns.class)
                .stream().map(field -> {
                    try{
                        return (String) field.get(null);
                    } catch (IllegalAccessException e){
                        throw new RuntimeException(e);
                    }
                }).filter(name -> !"ALL_KNOWN_COLUMNS".equals(name)).collect(Collectors.toList());
    }

    private static class B2BSheet{

        private Sheet sheet;
        private final Map<String, Integer> headerNameMap;

        private static final Predicate<String> ignoredColumnStringFilter = (column) -> !Strings.isNullOrEmpty(column) && B2BColumns.ALL_KNOWN_COLUMNS.contains(column);
        private static final Predicate<Cell> ignoreColumnsFilter = (cell) -> ignoredColumnStringFilter.apply(cell.getStringCellValue());

        B2BSheet(Sheet sheet){
            this.sheet = sheet;
            Row headerRow = sheet.getRow(0);
            //todo: check duplicate column and throw error

            headerNameMap = Streams.stream(headerRow.cellIterator()).filter(ignoreColumnsFilter)
                    .collect(Collectors.toMap(cell -> cell.getStringCellValue(), cell -> cell.getColumnIndex() ));
            int cellNo = headerRow.getLastCellNum();
            headerNameMap.put(InternalColumns.STATUS, cellNo);
            cellNo = headerRow.getLastCellNum();
            headerNameMap.put(InternalColumns.COMMENTS, cellNo);
        }

        public Stream<B2BXlsRow> getB2BXlsRows(){
            return Streams.stream(sheet.rowIterator())
                    //skip headers row
                    .skip(1)
                    .filter(row -> !Streams.stream(row.cellIterator()).allMatch((cell) ->{
                        switch(cell.getCellType()){
                            case STRING:
                                return Strings.isNullOrEmpty(cell.getStringCellValue());
                            case BLANK:
                                return true;
                            case FORMULA:
                                return Strings.isNullOrEmpty(cell.getCellFormula());
                            default:
                                return false;
                        }
                    })).map(row -> new B2BXlsRow(row, headerNameMap));
        }
    }

    public static class B2BXlsRow {
        private Row row;
        private Map<String,Integer> headers;

        public B2BXlsRow(Row row, Map<String, Integer> headers) {
            this.row = row;
            this.headers = headers;
        }

        public Cell getCell(String header){
            Integer index = headers.get(header);
            if(null == index){
                return null;
            } else {
                return row.getCell(index);
            }
        }

        public void succeeded() {
            row.createCell(headers.get(InternalColumns.STATUS), CellType.STRING).setCellValue("Success");
        }

        public void failed(B2BExcelError... errors){
            row.createCell(headers.get(InternalColumns.STATUS), CellType.STRING).setCellValue("Error");
            String message = Joiner.on("\n").join(Arrays.stream(errors)
                    .map(B2BExcelError::getMessage).collect(Collectors.toList()));
            row.createCell(headers.get(InternalColumns.COMMENTS), CellType.STRING).setCellValue(message);
        }

        public void failed(Iterable<B2BExcelError> errors){
            this.failed(Iterables.toArray(errors, B2BExcelError.class));
        }
    }


}
