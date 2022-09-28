package org.movoto.selenium.example.enums;

public enum GSTEnum {
    GST_0("0%",0.0),
    GST_0p1("0.1%",0.1),
    GST_0p25("0.25%",0.25),
    GST_1("1%",1.0),
    GST_1p5("1.5%",1.5),
    GST_3("3%",3.0),
    GST_5("5%",5.0),
    GST_6("6%",6.0),
    GST_7p5("7.5%",7.5),
    GST_12("12%",12.0),
    GST_18("18%",18.0),
    GST_28("28%",28.0);

    String id;
    Double percentage;



    GSTEnum(String id, Double percentage){
        this.id = id;
        this.percentage = percentage;
    }
    public static GSTEnum findById(String id){
        for(GSTEnum gst : values()){
            if(gst.getId().equals(id)){
                return gst;
            }
        }
        return null;
    }

    public static GSTEnum findByPercentage(Double percentage){
        for(GSTEnum gst : values()){
            if(gst.getPercentage().equals(percentage)){
                return gst;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public String id(){
        return id;
    }

    public Double percentage(){
        return percentage;
    }
}
