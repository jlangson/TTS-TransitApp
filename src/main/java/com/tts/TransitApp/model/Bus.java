package com.tts.TransitApp.model;

import org.json.simple.JSONObject;

public class Bus {
    public String ADHERENCE;
    public String BLOCKID;
    public String BLOCK_ABBR;
    public String DIRECTION;
    public String LATITUDE;
    public String LONGITUDE;
    public String MSGTIME;
    public String ROUTE;
    public String STOPID;
    public String TIMEPOINT;
    public String TRIPID;
    public String VEHICLE;
    public double distance;
//{"ADHERENCE":"0","BLOCKID":"255","BLOCK_ABBR":"2-5","DIRECTION":"Eastbound","LATITUDE":"33.8050534","LONGITUDE":"-84.4519618","MSGTIME":"12\/16\/2020 12:48:24 PM","ROUTE":"26","STOPID":"904519","TIMEPOINT":"Bankhead Station","TRIPID":"6999435","VEHICLE":"2594"}
    public Bus(JSONObject busJSON){
      this.ADHERENCE = (String) busJSON.get("ADHERENCE");
      this.BLOCKID = (String) busJSON.get("BLOCKID");
      this.BLOCK_ABBR = (String) busJSON.get("BLOCK_ABBR");
      this.DIRECTION = (String) busJSON.get("DIRECTION");
      this.LATITUDE = (String) busJSON.get("LATITUDE");
      this.LONGITUDE = (String) busJSON.get("LONGITUDE");
      this.MSGTIME = (String) busJSON.get("MSGTIME");
      this.ROUTE = (String) busJSON.get("ROUTE");
      this.STOPID = (String) busJSON.get("STOPID");
      this.TIMEPOINT = (String) busJSON.get("TIMEPOINT");
      this.TRIPID = (String) busJSON.get("TRIPID");
      this.VEHICLE = (String) busJSON.get("VEHICLE");
      this.distance = 0; //is this correct?
    }
}
