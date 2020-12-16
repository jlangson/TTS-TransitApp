package com.tts.TransitApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tts.TransitApp.model.Bus;
import com.tts.TransitApp.model.BusRequest;
import com.tts.TransitApp.model.Location;
import com.tts.TransitApp.service.TransitService;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

@Controller
public class TransitController {
    @Autowired
    private TransitService apiService;

    @GetMapping(value = {"/", "/buses"})
    public String getBusesPage(Model model){
        model.addAttribute("request", new BusRequest());
        return "index";
    }

    @PostMapping("/buses")
    public String getNearbyBuses(BusRequest request, Model model) {
        //do error handling on request
        
        List<Bus> buses = apiService.getNearbyBuses(request);
        Location person = apiService.getCoordinates(request.getAddress()+ " " + request.getCity());
        model.addAttribute("person", person);
        model.addAttribute("buses", buses);
        model.addAttribute("request", request);
        return "index";
    }
    
    //marta api isn't working so I'm hard-coding the json from a file
    @PostMapping("marta-down")
    public String getFakeBuses(BusRequest request, Model model) throws IOException, ParseException{
  
        Location person = apiService.getCoordinates(request.getAddress()+ " " + request.getCity());
        JSONArray busJSONArray = apiService.parseJSON("marta-all.json");
        ArrayList<Bus> buses = new ArrayList<Bus>(busJSONArray.size());
        for(int i=0; i < busJSONArray.size(); i++){
            buses.add(new Bus((JSONObject) busJSONArray.get(i)));
        }
        model.addAttribute("person", person);
        model.addAttribute("buses", buses);
        model.addAttribute("request", request);
        return "index";
    }
}

