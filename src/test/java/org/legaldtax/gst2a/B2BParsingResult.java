package org.legaldtax.gst2a;

import com.google.common.collect.FluentIterable;
import org.apache.commons.compress.utils.Lists;
import org.legaldtax.gst2a.dto.xml.Envelope;

import java.util.List;

public class B2BParsingResult {
    private Envelope envelope;

    private Double calculatedAmount;
    private Double calculatedRoundOff;
    private List<B2BExcelError> errors = Lists.newArrayList();

    public B2BParsingResult(Envelope envelope) {
        this.envelope = envelope;
    }

    public B2BParsingResult(List<B2BExcelError> errors) {
        this.errors = errors;
    }

    public Envelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(Envelope envelope) {
        this.envelope = envelope;
    }

    public List<B2BExcelError> getErrors() {
        return errors;
    }



    public void setErrors(List<B2BExcelError> errors) {
        this.errors = errors;
    }

    public Double getCalculatedAmount() {
        return calculatedAmount;
    }

    public void setCalculatedAmount(Double calculatedAmount) {
        this.calculatedAmount = calculatedAmount;
    }

    public Double getCalculatedRoundOff() {
        return calculatedRoundOff;
    }

    public void setCalculatedRoundOff(Double calculatedRoundOff) {
        this.calculatedRoundOff = calculatedRoundOff;
    }

    public static B2BParsingResult success(Envelope envelope){
        return new B2BParsingResult(envelope);
    }
    public static B2BParsingResult successGSTCalculation(Envelope envelope){
        return new B2BParsingResult(envelope);
    }
    public static B2BParsingResult failure(List<B2BExcelError> errors){
        return new B2BParsingResult(errors);
    }

    public static B2BParsingResult failure(B2BExcelError error, B2BExcelError... errors){
        return new B2BParsingResult(FluentIterable.of(error, errors).toList());
    }
}
