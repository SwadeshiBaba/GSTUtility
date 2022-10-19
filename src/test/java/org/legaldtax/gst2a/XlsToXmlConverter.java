package org.legaldtax.gst2a;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.legaldtax.gst2a.dto.pojo.AllLedgerentries;
import org.legaldtax.gst2a.dto.xml.*;
import org.legaldtax.gst2a.dto.xmlenum.BooleanEnum;
import org.movoto.selenium.example.bulkUploadSupplyOutward.*;
import org.movoto.selenium.example.pojo.GstCalculationDTO;
import org.movoto.selenium.example.pojo.SupplyOutwardDTO;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class XlsToXmlConverter {

    protected static final DataFormatter dataFormatter = new DataFormatter();
    protected static final SimpleDateFormat dateFormatter = new SimpleDateFormat(B2BExcelDocument.DATE_FORMAT);

    public Envelope convert(B2BExcelDocument.B2BXlsRow row) throws IOException {
        B2BParsingResult result = null;
        List<Envelope> envelopes = new ArrayList<>();

        //read common fields
        result = readCommonFields(row);
        result = readVoucher(row,result);
        result = readGstDetails(row,result);
        //calculateRoundoff();
        if(!result.getErrors().isEmpty()){
            row.failed(result.getErrors());
            return null;
        }
        return result.getEnvelope();
    }

    public B2BParsingResult readCommonFields(B2BExcelDocument.B2BXlsRow row) throws IOException {
        List<B2BExcelError> errors = Lists.newArrayList();
        Envelope envelope = new Envelope();
        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.GSTN_NUMBER)),
                envelope::setPartyGSTNumber,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.GST_NUMBER_EMPTY : null,
                v -> exceedsLength(v, 15) ? B2BExcelError.GSTN_TOO_LENGHTY : null);

        if(errors.isEmpty()){
            return B2BParsingResult.success(envelope);
        } else {
            return B2BParsingResult.failure(errors);
        }

    }

    public B2BParsingResult readVoucher(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {
        List<B2BExcelError> errors = b2BParsingResult.getErrors();
        Envelope envelope = b2BParsingResult.getEnvelope();
        Voucher voucher = new Voucher();

        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.SUPPLIER_NAME_PORTAL)),
                voucher::setPartyledgername,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.GST_PARTY_LEDGER_NAME_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.INVOICE_NUMBER)),
                voucher::setReference,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.INVOICE_NO_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.INVOICE_DATE)),
                voucher::setDate,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.INVOICE_DATE_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.INVOICE_DATE)),
                voucher::setReferencedate,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.INVOICE_REFERENCE_DATE_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.INVOICE_DATE)),
                voucher::setReferencedate,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.INVOICE_EFFECTIVE_DATE_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.INVOICE_VALUE)),
                voucher::setReference,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.INVOICE_VALUE_EMPTY : null);

        validateAndSetValueOrReturnError(getStringValue(row.getCell(B2BExcelDocument.B2BColumns.PLACE_OF_SUPPLY)),
                voucher::setPlaceOfService,
                e -> errors.addAll(e),
                v -> isEmpty(v) ? B2BExcelError.PLACE_OF_SUPPY_IS_EMPTY : null);

        if(errors.isEmpty()){
            envelope.getBody().getImportdata().getRequestdata().getTallymessage().setVoucher(voucher);
            return B2BParsingResult.success(envelope);
        } else {
            return B2BParsingResult.failure(errors);
        }

    }

    public B2BParsingResult readGstDetails(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {
        List<B2BExcelError> errors = b2BParsingResult.getErrors();
        Envelope envelope = b2BParsingResult.getEnvelope();
        List<AllledgerentriesList> allledgerentry;
        List<AllledgerentriesList> allledgerentriesList = new ArrayList<>();

        allledgerentry = readTotal(row, b2BParsingResult);
        if(null != allledgerentry){
            allledgerentriesList.addAll(allledgerentry);
        }
        allledgerentry = read0Percent(row, b2BParsingResult);
        if(null != allledgerentry){
            allledgerentriesList.addAll(allledgerentry);
        }

        allledgerentry = read5Percent(row, b2BParsingResult);
        if(null != allledgerentry){
            allledgerentriesList.addAll(allledgerentry);
        }

        allledgerentry = read12Percent(row, b2BParsingResult);
        if(null != allledgerentry){
            allledgerentriesList.addAll(allledgerentry);
        }

        allledgerentry = read18Percent(row, b2BParsingResult);
        if(null != allledgerentry){
            allledgerentriesList.addAll(allledgerentry);
        }

        allledgerentry = read28Percent(row, b2BParsingResult);
        if(null != allledgerentry){
            allledgerentriesList.addAll(allledgerentry);
        }

        if(errors.isEmpty()){
            if(null != allledgerentriesList && allledgerentriesList.size() > 0){
                envelope.getBody().getImportdata().getRequestdata().getTallymessage().getVoucher().setAllledgerentriesList(allledgerentriesList.stream().toArray(AllledgerentriesList[]::new));
            }
            return B2BParsingResult.success(envelope);
        } else {
            return B2BParsingResult.failure(errors);
        }
    }

    public List<AllledgerentriesList> readTotal(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {

        List<AllledgerentriesList> allledgerentriesLists = new ArrayList<>();
        Double amount;
        amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.INVOICE_VALUE));
        if(null != amount && amount != 0){
            allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.INVOICE_VALUE, BooleanEnum.NO, amount));
            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CGST_28));
        }

        return allledgerentriesLists.size() > 0 ? allledgerentriesLists : null;
    }
    public List<AllledgerentriesList> read28Percent(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {

        List<AllledgerentriesList> allledgerentriesLists = new ArrayList<>();
        Double amount;
        amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_28));
        if(null != amount && amount != 0){
            allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_28, BooleanEnum.YES, -amount));
            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CGST_28));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CGST_28, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.SGST_28));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.SGST_28, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.IGST_28));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.IGST_28, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CESS_28));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CESS_28, BooleanEnum.YES, -amount));
            }
        }

        return allledgerentriesLists.size() > 0 ? allledgerentriesLists : null;
    }
    public List<AllledgerentriesList> read18Percent(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {

        List<AllledgerentriesList> allledgerentriesLists = new ArrayList<>();
        Double amount;
        amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_18));
        if(null != amount && amount != 0){
            allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_18, BooleanEnum.YES, -amount));
            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CGST_18));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CGST_18, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.SGST_18));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.SGST_18, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.IGST_18));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.IGST_18, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CESS_18));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CESS_18, BooleanEnum.YES, -amount));
            }
        }

        return allledgerentriesLists.size() > 0 ? allledgerentriesLists : null;
    }

    public List<AllledgerentriesList> read12Percent(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {

        List<AllledgerentriesList> allledgerentriesLists = new ArrayList<>();
        Double amount;
        amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_12));
        if(null != amount && amount != 0){
            allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_12, BooleanEnum.YES, -amount));
            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CGST_12));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CGST_12, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.SGST_12));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.SGST_12, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.IGST_12));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.IGST_12, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CESS_12));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CESS_12, BooleanEnum.YES, -amount));
            }
        }

        return allledgerentriesLists.size() > 0 ? allledgerentriesLists : null;
    }

    public List<AllledgerentriesList> read5Percent(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {

        List<AllledgerentriesList> allledgerentriesLists = new ArrayList<>();
        Double amount;
        amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_5));
        if(null != amount && amount != 0){
            allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_5, BooleanEnum.YES, -amount));
            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CGST_5));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CGST_5, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.SGST_5));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.SGST_5, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.IGST_5));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.IGST_5, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CESS_5));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CESS_5, BooleanEnum.YES, -amount));
            }
        }

        return allledgerentriesLists.size() > 0 ? allledgerentriesLists : null;
    }

    public List<AllledgerentriesList> read0Percent(B2BExcelDocument.B2BXlsRow row, B2BParsingResult b2BParsingResult) throws IOException {

        List<AllledgerentriesList> allledgerentriesLists = new ArrayList<>();
        Double amount;
        amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_0));
        if(null != amount && amount != 0){
            allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.TAXABLE_VALUE_0, BooleanEnum.YES, -amount));
            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CGST_0));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CGST_0, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.SGST_0));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.SGST_0, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.IGST_0));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.IGST_0, BooleanEnum.YES, -amount));
            }

            amount = getDoubleValue(row.getCell(B2BExcelDocument.B2BColumns.CESS_0));
            if(null != amount && amount != 0){
                allledgerentriesLists.add(new AllledgerentriesList(B2BExcelDocument.B2BColumns.CESS_0, BooleanEnum.YES, -amount));
            }
        }


        return allledgerentriesLists.size() > 0 ? allledgerentriesLists : null;
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

    private static <T> void validateAndSetValueOrReturnError(T value, Consumer<T> setter, Consumer<List<B2BExcelError>> errorAccumulator, Function<T, B2BExcelError>... validators){
       List<B2BExcelError> errors = Arrays.stream(validators).map(v -> v.apply(value)).filter(Predicate.not(Objects::isNull)).collect(Collectors.toList());
       if(errors.isEmpty()){
           setter.accept(value);
       } else {
           errorAccumulator.accept(errors);
       }
    }

}
