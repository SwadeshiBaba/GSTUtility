package org.legaldtax.gst2a;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.legaldtax.gst2a.dto.xml.Data;
import org.legaldtax.gst2a.dto.xml.DataDTO;
import org.legaldtax.gst2a.dto.xml.Envelope;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GSTR2AExcelToTallyXml {

    public static final Properties properties = new Properties();
    private XlsToXmlConverter converter;

    GSTR2AExcelToTallyXml(XlsToXmlConverter xlsToXmlConverter) throws IOException {
        super();
        this.converter = xlsToXmlConverter;
        this.properties.load(getClass().getClassLoader().getResourceAsStream("b2bapplication.properties"));
    }

    public static void main(String[] args) throws JAXBException, IOException {
        XlsToXmlConverter xlsToXmlConverter = new XlsToXmlConverter();
        GSTR2AExcelToTallyXml gstr2AExcelToTallyXml = new GSTR2AExcelToTallyXml(xlsToXmlConverter);
        JAXBContext contextObj = JAXBContext.newInstance(DataDTO.class);
        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        DataDTO dataDTO = new DataDTO();
        Data data = gstr2AExcelToTallyXml.getInputFile();
        dataDTO.setData(data);
        marshallerObj.marshal(dataDTO.getData(), new FileOutputStream("data.xml"));
        System.out.println("Successfully completed");

    }

    public Data getInputFile(){
        try {
            //obtaining input bytes from a file
            FileInputStream fis = new FileInputStream(new File(properties.getProperty("b2bFilePath")));
            //creating workbook instance that refers to .xls file
            HSSFWorkbook wb = new HSSFWorkbook(fis);

            B2BExcelDocument xls = new B2BExcelDocument(wb);
            Data data = new Data();
            List<Envelope> envelopes = new ArrayList<>();

            xls.B2BRows().forEach( b2bXlsRow -> {
                try{
                    envelopes.add(uploadSingleB2BInvoice(b2bXlsRow));
                } catch(RuntimeException e){
                    b2bXlsRow.failed(B2BExcelError.UNEXPECTED);
                    System.out.println("Single B2B upload failed : "+ e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            if(null!=envelopes && envelopes.size()>0){
                data.setEnvelope(envelopes.stream().toArray(Envelope[]::new));
            }

            return data;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Envelope uploadSingleB2BInvoice(B2BExcelDocument.B2BXlsRow row) throws IOException {
        return converter.convert(row);
    }
}
