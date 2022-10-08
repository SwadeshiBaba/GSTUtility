package org.movoto.selenium.example.bulkUploadSupplyOutward;

import com.google.common.collect.FluentIterable;
import org.apache.commons.compress.utils.Lists;
import org.movoto.selenium.example.pojo.GstCalculationDTO;

import java.util.List;

public class GSTCalculationParsingResult {
    private GstCalculationDTO gstCalculationDTO;
    private List<BulkSupplyOutwardError> errors = Lists.newArrayList();

    public GSTCalculationParsingResult(GstCalculationDTO gstCalculationDTO) {
        this.gstCalculationDTO = gstCalculationDTO;
    }

    public GSTCalculationParsingResult(List<BulkSupplyOutwardError> errors) {
        this.errors = errors;
    }

    public GstCalculationDTO getGstCalculationDTO(){
        return gstCalculationDTO;
    }

    public List<BulkSupplyOutwardError> getErrors() {
        return errors;
    }

    public static GSTCalculationParsingResult success(GstCalculationDTO gstCalculationDTO){
        return new GSTCalculationParsingResult(gstCalculationDTO);
    }
    public static GSTCalculationParsingResult successGSTCalculation(GstCalculationDTO gstCalculationDTO){
        return new GSTCalculationParsingResult(gstCalculationDTO);
    }
    public static GSTCalculationParsingResult failure(List<BulkSupplyOutwardError> errors){
        return new GSTCalculationParsingResult(errors);
    }

    public static GSTCalculationParsingResult failure(BulkSupplyOutwardError error, BulkSupplyOutwardError... errors){
        return new GSTCalculationParsingResult(FluentIterable.of(error, errors).toList());
    }
}
