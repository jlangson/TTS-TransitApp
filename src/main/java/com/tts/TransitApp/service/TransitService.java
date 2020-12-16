package com.tts.TransitApp.service;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.util.JSONPObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tts.TransitApp.model.Bus;
import com.tts.TransitApp.model.BusComparator;
import com.tts.TransitApp.model.BusRequest;
import com.tts.TransitApp.model.DistanceResponse;
import com.tts.TransitApp.model.GeocodingResponse;
import com.tts.TransitApp.model.Location;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class TransitService {

    @Value("${transit_url}")
    public String transitUrl;

    @Value("${geocoding_url}")
    public String geocodingUrl;

    @Value("${distance_url}")
    public String distanceUrl;

    @Value("${google_api_key}")
    public String googleApiKey;
    
     public JSONArray parseJSON(String file) throws IOException, ParseException{
        JSONParser parser = new JSONParser();
        
        try (FileReader reader = new FileReader(file))
        {
            //Read JSON file
            Object obj = parser.parse(reader);
 
            JSONArray busList = (JSONArray) obj;
            System.out.println(busList);
            return busList;
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
        
        return new JSONArray();
            
    }

    private List<Bus> getBuses(){
        RestTemplate restTemplate = new RestTemplate();
        Bus[] buses = restTemplate.getForObject(transitUrl, Bus[].class);
        return Arrays.asList(buses);
    }
    
    public boolean validate(GeocodingResponse response){
        
//        return response.results.get(0);
        return true;
    }

    public Location getCoordinates(String description) {
        description = description.replace(" ", "+");
        String url = geocodingUrl + description + "+GA&key=" + googleApiKey;
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(url);
        
        
        try {
            GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);//error here
            return response.results.get(0).geometry.location;
        }
        catch (HttpClientErrorException ex) {
            Location emptyLocation = new Location();
            emptyLocation.setLat("0");
            emptyLocation.setLng("0");
            return emptyLocation;
        }
        catch (IndexOutOfBoundsException ex){
            Location emptyLocation = new Location();
            emptyLocation.setLat("0");
            emptyLocation.setLng("0");
            return emptyLocation;
        }
    }
        
//        33.762395223604535, -84.3946210122613 - a park in atlanta
          

    private double getDistance(Location origin, Location destination) {
        String url = distanceUrl + "origins=" + origin.lat + "," + origin.lng + "&destinations=" 
                        + destination.lat + "," + destination.lng + "&key=" + googleApiKey;
        RestTemplate restTemplate = new RestTemplate();
        DistanceResponse response = restTemplate.getForObject(url, DistanceResponse.class);
        return response.rows.get(0).elements.get(0).distance.value * 0.000621371;
    }

    public List<Bus> getNearbyBuses(BusRequest request){
        List<Bus> allBuses = this.getBuses();
        Location personLocation = this.getCoordinates(request.address + " " + request.city); //error here
        List<Bus> nearbyBuses = new ArrayList<>();
        for(Bus bus : allBuses) {
                Location busLocation = new Location();
                busLocation.lat = bus.LATITUDE;
                busLocation.lng = bus.LONGITUDE;
                double latDistance = Double.parseDouble(busLocation.lat) - Double.parseDouble(personLocation.lat);
                double lngDistance = Double.parseDouble(busLocation.lng) - Double.parseDouble(personLocation.lng);
                if (Math.abs(latDistance) <= 0.02 && Math.abs(lngDistance) <= 0.02) {
                        double distance = getDistance(busLocation, personLocation);
                        if (distance <= 1) {
                                bus.distance = (double) Math.round(distance * 100) / 100;
                                nearbyBuses.add(bus);
                        }
                }
        }
        Collections.sort(nearbyBuses, new BusComparator());
        return nearbyBuses;
    }


}
