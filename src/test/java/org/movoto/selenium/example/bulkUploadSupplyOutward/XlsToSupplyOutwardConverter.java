package org.movoto.selenium.example.bulkUploadSupplyOutward;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hpsf.Decimal;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.movoto.selenium.example.pojo.GstCalculationDTO;
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

    protected static final DataFormatter dataFormatter = new DataFormatter();
    protected static final SimpleDateFormat dateFormatter = new SimpleDateFormat(BulkUploadSupplyOutwardExcelDocument.DATE_FORMAT);

    public SupplyOutwardDTO convert(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row, SupplyOutwardDTO dto){
        BulkSupplyOutwardParsingResult result = null;
        if(null == dto){
            dto = new SupplyOutwardDTO();
            //read common fields
            result = readCommonFields(row, dto);

            if(!result.getErrors().isEmpty()){
                row.failed(result.getErrors());
                return null;
            }
            return result.getSupplyOutwardDTO();
        } else {
            // read GSTCalculate DTO fields
            GstCalculationDTO gstCalculationDto = new GstCalculationDTO();
            gstCalculationDto  = readGSTCalculateFields(row, gstCalculationDto);
            if(null != gstCalculationDto) {
                dto.getGstCalculationList().add(gstCalculationDto);
                return dto;
            } else {
                return null;
            }
        }
    }

    public BulkSupplyOutwardParsingResult readCommonFields(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row, SupplyOutwardDTO supplyOutwardDTO){
        List<BulkSupplyOutwardError> errors = Lists.newArrayList();
        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.ERROR_DESCRIPTION)),
                supplyOutwardDTO::setErrorDescription,
                e -> errors.addAll(e));

        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.GSTR_1_SECTION)),
                supplyOutwardDTO::setGstr1Section,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.GSTR_1_SECTION_IS_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.GSTR_3B_SECTION)),
                supplyOutwardDTO::setGstr3BSection,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.GSTR_3B_SECTION_IS_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.GSTN)),
                supplyOutwardDTO::setGstn,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.GSTN_IS_EMPTY : null,
                v -> exceedsLength(v, 255) ? BulkSupplyOutwardError.GSTN_TOO_LENGHTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.PLACE_OF_SUPPY)),
                supplyOutwardDTO::setPlaceOfSupply,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.PLACE_OF_SUPPY_IS_EMPTY : null);

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

        validateAndSetValueOrReturnError(getDoubleValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.INVOICE_VALUE)),
                supplyOutwardDTO::setInvoiceValue,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? BulkSupplyOutwardError.DOC_NO_IS_EMPTY : null);
        GSTCalculationParsingResult gstCalculationParsingResult = new GstCalculationConvertor().convert(row, new GstCalculationDTO());
        if(null == gstCalculationParsingResult.getGstCalculationDTO()){
            errors.addAll(gstCalculationParsingResult.getErrors());
        }

        if(errors.isEmpty()){
            supplyOutwardDTO.getGstCalculationList().add(gstCalculationParsingResult.getGstCalculationDTO());
            return BulkSupplyOutwardParsingResult.success(supplyOutwardDTO);
        } else {
            return BulkSupplyOutwardParsingResult.failure(errors);
        }

    }

    public static class GstCalculationConvertor implements Converter<BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow, GstCalculationDTO, GSTCalculationParsingResult>{

        @Override
        public GSTCalculationParsingResult convert(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row, GstCalculationDTO gstCalculationDto) {
            List<BulkSupplyOutwardError> errors = Lists.newArrayList();
            validateAndSetValueOrReturnError(getDoubleValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.TAXABLE_VALUE)),
                    gstCalculationDto::setTaxableValue,
                    e -> errors.addAll(e),
                    v -> isEmpty(v) ? BulkSupplyOutwardError.TAXABLE_VALUE_IS_EMPTY : null);

            validateAndSetValueOrReturnError(getPercentageValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.TAX_RATES)),
                    gstCalculationDto::setTaxRate,
                    e -> errors.addAll(e),
                    v -> isEmpty(v) ? BulkSupplyOutwardError.TAX_RATE_IS_EMPTY : null);

            validateAndSetValueOrReturnError(getDoubleValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.IGST)),
                    gstCalculationDto::setIgstVal,
                    e -> errors.addAll(e));

            validateAndSetValueOrReturnError(getDoubleValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.CGST)),
                    gstCalculationDto::setCgstVal,
                    e -> errors.addAll(e));

            validateAndSetValueOrReturnError(getDoubleValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.SGST)),
                    gstCalculationDto::setSgstVal,
                    e -> errors.addAll(e));

            validateAndSetValueOrReturnError(getDoubleValue(row.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.CESS)),
                    gstCalculationDto::setCessVal,
                    e -> errors.addAll(e));

            if(errors.isEmpty()){
                return GSTCalculationParsingResult.success(gstCalculationDto);
            } else {
                return GSTCalculationParsingResult.failure(errors);
            }
        }
    }

    public GstCalculationDTO readGSTCalculateFields(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row, GstCalculationDTO gstCalculationDto){

            GSTCalculationParsingResult gstCalculationParsingResult = new GstCalculationConvertor().convert(row, gstCalculationDto);
            if(gstCalculationParsingResult.getErrors().isEmpty()){
                row.failed(gstCalculationParsingResult.getErrors());
                return null;
            }

        return gstCalculationParsingResult.getGstCalculationDTO();
    }



    public static String getStringValue(Cell cell){
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

    public static Long getNumericValue(Cell cell){
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

    public static Double getDoubleValue(Cell cell){
        if(cell == null || CellType.BLANK == cell.getCellType() || CellType._NONE == cell.getCellType()){
            return null;
        } else if(CellType.STRING == cell.getCellType()){
            String value = cell.getStringCellValue();
            if(null != value){
                value = value.trim();
            }
            return Double.parseDouble(value);
        } else if(CellType.NUMERIC == cell.getCellType()){
            return cell.getNumericCellValue();
        } else {
            throw new RuntimeException(String.format("Unable to parse double value : %s, type: %s", cell, cell.getCellType()));
        }
    }

    public static Double getPercentageValue(Cell cell){
        if(cell == null || CellType.BLANK == cell.getCellType() || CellType._NONE == cell.getCellType()){
            return null;
        } else if(CellType.STRING == cell.getCellType()){
            String value = cell.getStringCellValue();
            if(null != value){
                value = value.trim().replaceAll("%","");
            }
            return null != value ? Double.parseDouble(value) : null;
        } else if(CellType.NUMERIC == cell.getCellType()){
            String value = dataFormatter.formatCellValue(cell);
            value = value.trim().replaceAll("%","");
            return Double.parseDouble(value);
        } else {
            throw new RuntimeException(String.format("Unable to parse percentage value : %s, type: %s", cell, cell.getCellType()));
        }
    }

    public static Date getDateValue(Cell cell){

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

    protected static <T> boolean isEmpty(T value){
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

    private static <T> void validateAndSetValueOrReturnError(T value, Consumer<T> setter, Consumer<List<BulkSupplyOutwardError>> errorAccumulator, Function<T, BulkSupplyOutwardError>... validators){
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
