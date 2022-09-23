package org.movoto.selenium.example.bulkUploadSupplyOutward;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.movoto.selenium.example.pojo.SupplyOutwardDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class XlsToSupplyOutwardConverter {

    protected final DataFormatter dataFormatter = new DataFormatter();
    protected final SimpleDateFormat dateFormatter = new SimpleDateFormat(BulkUploadSupplyOutwardExcelDocument.DATE_FORMAT);

    public BulkSupplyOutwardParsingResult convert(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row){

        SupplyOutwardDTO supplyOutwardDTO = new SupplyOutwardDTO();
        //read common fields
        BulkSupplyOutwardParsingResult result = readCommonFields(row, supplyOutwardDTO);

        // read specific DTO fields

        return result;


    }

    public BulkSupplyOutwardParsingResult readCommonFields(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row, SupplyOutwardDTO supplyOutwardDTO){
        List<BulkSupplyOutwardError> errors = Lists.newArrayList();
        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.GSTN)),
                supplyOutwardDTO::setGstn,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.GSTN_IS_EMPTY : null,
                v -> exceedsLength(v, 255) ? BulkSupplyOutwardError.GSTN_TOO_LENGHTY : null);
        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.PARTY)),
                supplyOutwardDTO::setParty,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.PARTY_IS_EMPTY : null);
        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.DOC_NO)),
                supplyOutwardDTO::setDocNo,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.DOC_NO_IS_EMPTY : null);

        supplyOutwardDTO.setDocDate(getDateValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.DOC_DATE)));
        /*validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.DOC_DATE)),
                supplyOutwardDTO::setDocDate,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.DOC_NO_IS_EMPTY : null);*/

        validateAndSetValueOrReturnError(getNumericValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.INVOICE_VALUE)),
                supplyOutwardDTO::setInvoiceValue,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.DOC_NO_IS_EMPTY : null);

        return BulkSupplyOutwardParsingResult.success(supplyOutwardDTO);
    }



    protected String getStringValue(Cell cell){
        if(cell == null || CellType.BLANK == cell.getCellType() || CellType._NONE == cell.getCellType()){
            return null;
        } else if(CellType.STRING == cell.getCellType()){
            String value = cell.getStringCellValue();
            if(null != value){
                value = value.trim();
            }
            return value;
        } else if(CellType.NUMERIC == cell.getCellType()){
            return dataFormatter.formatCellValue(cell);
        } else {
            throw new RuntimeException(String.format("Unable to parse string value : %s, type: %s", cell, cell.getCellType()));
        }
    }

    protected Long getNumericValue(Cell cell){
        if(cell == null || CellType.BLANK == cell.getCellType() || CellType._NONE == cell.getCellType()){
            return null;
        } else if(CellType.STRING == cell.getCellType()){
            String value = cell.getStringCellValue();
            if(null != value){
                value = value.trim();
            }
            return Long.parseLong(value);
        } else if(CellType.NUMERIC == cell.getCellType()){
            return (long) cell.getNumericCellValue();
        } else {
            throw new RuntimeException(String.format("Unable to parse numeric value : %s, type: %s", cell, cell.getCellType()));
        }
    }

    protected Date getDateValue(Cell cell){

        if(cell == null || CellType.BLANK == cell.getCellType() || CellType._NONE == cell.getCellType()){
            return null;
        } else if(CellType.STRING == cell.getCellType()){
            String value = cell.getStringCellValue();
            if(null != value){
                value = value.trim();
            }
            try{
                return dateFormatter.parse(value);
            } catch(ParseException e){
                throw new RuntimeException(String.format("Unable to parse date value: %s, type: %s", cell, cell.getCellType()));
            }
        } else if(CellType.NUMERIC == cell.getCellType()){
            return cell.getDateCellValue();
        } else {
            throw new RuntimeException(String.format("Unable to parse date value : %s, type: %s", cell, cell.getCellType()));
        }
    }

    protected <T> boolean isEmpty(T value){
        if(null == value){
            return true;
        } else if(value instanceof String && ((String) value).isEmpty()){
            return true;
        }
        return false;
    }
    protected boolean exceedsLength(String value, int maxLength){
        return null != value && value.length() > maxLength;
    }

    private CellValue evaluateFormula(Cell cell){
        return cell.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator().evaluate(cell);
    }

    private<T> void validateAndSetValueOrReturnError(T value, Consumer<T> setter, Consumer<List<BulkSupplyOutwardError>> errorAccumulator, Function<T, BulkSupplyOutwardError>... validators){
       List<BulkSupplyOutwardError> errors = Arrays.stream(validators).map(v -> v.apply(value)).filter(Predicate.not(Objects::isNull)).collect(Collectors.toList());
       if(errors.isEmpty()){
           setter.accept(value);
       } else {
           errorAccumulator.accept(errors);
       }
    }

    /*private <T> void validateAndSetValueOrReturnError(Long numericValue, Consumer<T> setter, Consumer<List<BulkSupplyOutwardError>> errorAccumulator, Function<T, BulkSupplyOutwardError>... validators) {
        List<BulkSupplyOutwardError> errors = Arrays.stream(validators).map(v -> v.apply((T) numericValue)).filter(Predicate.not(Objects::isNull)).collect(Collectors.toList());
        if(errors.isEmpty()){
            setter.accept((T)numericValue);
        } else {
            errorAccumulator.accept(errors);
        }
    }*/
}
