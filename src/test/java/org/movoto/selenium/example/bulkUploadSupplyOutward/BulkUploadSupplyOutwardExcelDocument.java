package org.movoto.selenium.example.bulkUploadSupplyOutward;

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

public class BulkUploadSupplyOutwardExcelDocument implements AutoCloseable {

    private Workbook workbook;

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    private final String extension;

    public BulkUploadSupplyOutwardExcelDocument(Workbook workbook) {
        this.workbook = workbook;
        this.extension = workbook instanceof XSSFWorkbook ? "xlsx" : "xls";
    }

    public static final String SUPPLY_OUTWARD_REGISTER = "Supply Outward Register";

    /*public Stream<BulkUploadXlsRow> allSupplyOutwardRows(){
        return Stream.concat(allSupplyOutwardRows(), allSupplyOutwardRows());
    }*/

    public Stream<BulkUploadXlsRow> supplyOutwardRows(){
        return new BulkUploadSupplyOutwardXlsSheet(workbook.getSheet(SUPPLY_OUTWARD_REGISTER)).getSupplyInwardRows();
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

    public interface SupplyOutwardColumns{
        String GSTR_1_SECTION = "GSTR-1 Section";
        String GSTR_3B_SECTION = "GSTR-3B Section";
        String PARTY = "Party";
        String GSTN = "GSTIN / UIN";
        String PLACE_OF_SUPPY = "Place of Supply";
        String DOC_NO = "Doc. No.";
        String DOC_DATE = "Doc. Date";
        String INVOICE_VALUE = "Invoice Value";
        String TAXABLE_VALUE= "Taxable Value";
        String TAX_RATES = "Tax Rates";
        String IGST = "IGST";
        String CGST = "CGST";
        String SGST = "SGST";
        String CESS = "Cess";

        List<String> ALL_KNOWN_COLUMNS = ReflectionUtils.staticField(SupplyOutwardColumns.class)
                .stream().map(field -> {
                    try{
                        return (String) field.get(null);
                        } catch (IllegalAccessException e){
                        throw new RuntimeException(e);
                        }
                }).filter(name -> !"ALL_KNOWN_COLUMNS".equals(name)).collect(Collectors.toList());
    }

    private static class BulkUploadSupplyOutwardXlsSheet{

        private Sheet sheet;
        private final Map<String, Integer> headerNameMap;

        private static final Predicate<String> ignoredColumnStringFilter = (column) -> !Strings.isNullOrEmpty(column) && SupplyOutwardColumns.ALL_KNOWN_COLUMNS.contains(column);
        private static final Predicate<Cell> ignoreColumnsFilter = (cell) -> ignoredColumnStringFilter.apply(cell.getStringCellValue());

        BulkUploadSupplyOutwardXlsSheet(Sheet sheet){
            this.sheet = sheet;
            Row headerRow = sheet.getRow(3);
            //todo: check duplicate column and throw error

            headerNameMap = Streams.stream(headerRow.cellIterator()).filter(ignoreColumnsFilter)
                    .collect(Collectors.toMap(cell -> cell.getStringCellValue(), cell -> cell.getColumnIndex() ));
            int cellNo = headerRow.getLastCellNum();
            headerNameMap.put(InternalColumns.STATUS, cellNo);
            cellNo = headerRow.getLastCellNum();
            headerNameMap.put(InternalColumns.COMMENTS, cellNo);
        }

        public Stream<BulkUploadXlsRow> getSupplyInwardRows(){
            return Streams.stream(sheet.rowIterator())
                    //skip headers row
                    .skip(4)
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
                    })).map(row -> new BulkUploadXlsRow(row, headerNameMap));
        }
    }

    public static class BulkUploadXlsRow {
        private Row row;
        private Map<String,Integer> headers;

        public BulkUploadXlsRow(Row row, Map<String, Integer> headers) {
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

        public void failed(BulkSupplyOutwardError... errors){
            row.createCell(headers.get(InternalColumns.STATUS), CellType.STRING).setCellValue("Error");
            String message = Joiner.on("\n").join(Arrays.stream(errors)
                    .map(BulkSupplyOutwardError::getMessage).collect(Collectors.toList()));
            row.createCell(headers.get(InternalColumns.COMMENTS), CellType.STRING).setCellValue(message);
        }

        public void failed(Iterable<BulkSupplyOutwardError> errors){
            this.failed(Iterables.toArray(errors, BulkSupplyOutwardError.class));
        }
    }


}
