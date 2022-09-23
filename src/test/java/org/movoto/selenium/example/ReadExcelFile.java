package org.movoto.selenium.example;

import org.apache.commons.compress.utils.Lists;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.movoto.selenium.example.bulkUploadSupplyOutward.BulkSupplyOutwardError;
import org.movoto.selenium.example.bulkUploadSupplyOutward.BulkSupplyOutwardParsingResult;
import org.movoto.selenium.example.bulkUploadSupplyOutward.BulkUploadSupplyOutwardExcelDocument;
import org.movoto.selenium.example.bulkUploadSupplyOutward.XlsToSupplyOutwardConverter;
import org.movoto.selenium.example.pojo.SupplyOutwardDTO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ReadExcelFile {

    private static final String filePath = "C:\\GST\\OutwardSupply\\SupplyOutwardRegister.xls";
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
                    /*if(bulkUploadXlsRow.getCell(BulkUploadSupplyOutwardExcelDocument.SupplyOutwardColumns.GSTN)){

                    }*/
                    supplyOutwardDTOList.add(uploadSingleSupplyOutwardInvoice(bulkUploadXlsRow));
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
        BulkSupplyOutwardParsingResult parsingResult =  converter.convert(row);

        if(!parsingResult.getErrors().isEmpty()){
            row.failed(parsingResult.getErrors());
            return null;
        }
        return  parsingResult.getSupplyOutwardDTO();

    }

}
