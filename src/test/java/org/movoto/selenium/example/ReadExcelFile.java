package org.movoto.selenium.example;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.movoto.selenium.example.bulkUploadSupplyOutward.*;
import org.movoto.selenium.example.pojo.GstCalculationDTO;
import org.movoto.selenium.example.pojo.SupplyOutwardDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReadExcelFile {

    private static final String filePath = ChromeDriverTest.properties.getProperty("filePath");
    private XlsToSupplyOutwardConverter converter;

    ReadExcelFile(XlsToSupplyOutwardConverter supplyOutwardConverter){
        super();
        this.converter = supplyOutwardConverter;
    }


    public List<SupplyOutwardDTO> getInputFile(){
        try {
            //obtaining input bytes from a file
            FileInputStream fis = new FileInputStream(new File(filePath));
            //creating workbook instance that refers to .xls file
            HSSFWorkbook wb = new HSSFWorkbook(fis);

            BulkUploadSupplyOutwardExcelDocument xls = new BulkUploadSupplyOutwardExcelDocument(wb);
            List<SupplyOutwardDTO> supplyOutwardDTOList = Lists.newArrayList();

            xls.supplyOutwardRows().forEach( bulkUploadXlsRow -> {
                try{
                    String doc_no = XlsToSupplyOutwardConverter.getStringValue(bulkUploadXlsRow.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.DOC_NO));
                    if(null == doc_no){
                        SupplyOutwardDTO supplyOutwardDTO = uploadSingleSupplyOutwardGSTInvoice(bulkUploadXlsRow, supplyOutwardDTOList.get(supplyOutwardDTOList.size()-1));
                        supplyOutwardDTOList.set(supplyOutwardDTOList.size()-1, supplyOutwardDTO);
                    }else{
                        supplyOutwardDTOList.add(uploadSingleSupplyOutwardInvoice(bulkUploadXlsRow));
                    }

                } catch(RuntimeException e){
                    bulkUploadXlsRow.failed(BulkSupplyOutwardError.UNEXPECTED);
                    System.out.println("Single Supply Outward upload failed : "+ e);
                }
            });

            return supplyOutwardDTOList;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SupplyOutwardDTO uploadSingleSupplyOutwardInvoice(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row){
        return converter.convert(row, null);
    }

    private SupplyOutwardDTO uploadSingleSupplyOutwardGSTInvoice(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row, SupplyOutwardDTO dto){
        GstCalculationDTO gstCalculationDTO = new GstCalculationDTO();
        GSTCalculationParsingResult gstCalculationParsingResult = new XlsToSupplyOutwardConverter.GstCalculationConvertor().convert(row, gstCalculationDTO);
        if(gstCalculationParsingResult.getErrors().isEmpty()){
            dto.getGstCalculationList().add(gstCalculationDTO);
            return dto;
        } else {
            row.failed(gstCalculationParsingResult.getErrors());
            return null;
        }
    }

    /*private List<GstCalculationDTO> uploadSingleSupplyOutwardGSTCalculations(BulkUploadSupplyOutwardExcelDocument.BulkUploadXlsRow row, SupplyOutwardDTO dto){
        BulkSupplyOutwardParsingResult parsingResult =  converter.convert(row);

        if(!parsingResult.getErrors().isEmpty()){
            row.failed(parsingResult.getErrors());
            return null;
        }
        return  parsingResult.getSupplyOutwardDTO();

    }*/

}
