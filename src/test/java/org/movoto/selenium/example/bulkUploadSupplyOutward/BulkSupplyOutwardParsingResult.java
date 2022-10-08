package org.movoto.selenium.example.bulkUploadSupplyOutward;

import com.google.common.collect.FluentIterable;
import org.apache.commons.compress.utils.Lists;
import org.movoto.selenium.example.pojo.SupplyOutwardDTO;

import java.util.List;

public class BulkSupplyOutwardParsingResult {
    private SupplyOutwardDTO supplyOutwardDTO;
    private List<BulkSupplyOutwardError> errors = Lists.newArrayList();

    public BulkSupplyOutwardParsingResult(SupplyOutwardDTO supplyOutwardDTO) {
        this.supplyOutwardDTO = supplyOutwardDTO;
    }

    public BulkSupplyOutwardParsingResult(List<BulkSupplyOutwardError> errors) {
        this.errors = errors;
    }

    public SupplyOutwardDTO getSupplyOutwardDTO(){
        return supplyOutwardDTO;
    }

    public List<BulkSupplyOutwardError> getErrors() {
        return errors;
    }

    public static BulkSupplyOutwardParsingResult success(SupplyOutwardDTO supplyOutwardDTO){
        return new BulkSupplyOutwardParsingResult(supplyOutwardDTO);
    }
    public static BulkSupplyOutwardParsingResult successGSTCalculation(SupplyOutwardDTO supplyOutwardDTO){
        return new BulkSupplyOutwardParsingResult(supplyOutwardDTO);
    }
    public static BulkSupplyOutwardParsingResult failure(List<BulkSupplyOutwardError> errors){
        return new BulkSupplyOutwardParsingResult(errors);
    }

    public static BulkSupplyOutwardParsingResult failure(BulkSupplyOutwardError error, BulkSupplyOutwardError... errors){
        return new BulkSupplyOutwardParsingResult(FluentIterable.of(error, errors).toList());
    }
}
