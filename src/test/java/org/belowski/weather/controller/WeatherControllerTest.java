package org.belowski.weather.controller;

import org.belowski.weather.model.ConditionsConstants.ConditionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@WebMvcTest
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;
        
    @Test
    public void testGetWeather() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/data/2.5/weather").
                param("lat", "1.23").
                param("lon", "3.45").
                param("APPID", "aaaa")).andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
    }
    
    @Test
    public void testGetForecast() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/data/2.5/forecast").
                param("lat", "35").
                param("lon", "-120").
                param("APPID", "aaaa")).andReturn();
        String content = result.getResponse().getContentAsString();
        assertNotNull(content);
    }
    
    @Test
    public void testCreateWeatherConditions() throws Exception {
        String json = new BufferedReader(new InputStreamReader(WeatherControllerTest.class.getResourceAsStream("/createPayload.json"))).lines().collect(Collectors.joining("\n"));
        this.mockMvc.perform(post("/weather/create/conditions").
                contentType(MediaType.APPLICATION_JSON).
                content(json)).andExpect(status().isOk());
        
        // now check we get back the weather we're expecting
        MvcResult result = this.mockMvc.perform(get("/data/2.5/weather").
                param("lat", "1.23").
                param("lon", "3.45").
                param("APPID", "aaaa")).andReturn();
        String content = result.getResponse().getContentAsString();
        // TODO: xpath assertions
        /*assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><current><clouds name=\"broken clouds\" value=\"20\"/>"
                + "<humidity unit=\"%\" value=\"50\"/><precipitation mode=\"no\" unit=\"3h\" value=\"0.0\"/>"
                + "<pressure unit=\"hPa\" value=\"2000.0\"/><temperature max=\"273.0\" min=\"273.0\" unit=\"kelvin\" value=\"273.0\"/>"
                + "<visiblity value=\"10000\"/><wind><direction code=\"E\" name=\"East\" value=\"100\"/><speed name=\"gentle breeze\" value=\"10.0\"/></wind></current>", content);*/
        
        // and the forecast
        result = this.mockMvc.perform(get("/data/2.5/forecast").
                param("lat", "1.23").
                param("lon", "3.45").
                param("APPID", "aaaa")).andReturn();
        content = result.getResponse().getContentAsString();
        assertNotNull(content);
    }
    
    @Test
    public void testCreateWeatherSlots() throws Exception {
        this.mockMvc.perform(put("/weather/create/slots").
                param("slotLength", "20").
                param("slot", ConditionType.RAIN.name()).
                param("slot", ConditionType.RAIN.name()).
                param("slot", ConditionType.RAIN.name()).
                param("slot", ConditionType.RAIN.name()).
                param("slot", ConditionType.CLEAR.name()).
                param("slot", ConditionType.CLEAR.name()).
                param("slot", ConditionType.CLEAR.name()).
                param("slot", ConditionType.CLEAR.name()).
                param("slot", ConditionType.CLEAR.name()))
                .andExpect(status().isOk());
        
        // now check we get back the weather we're expecting
        MvcResult result = this.mockMvc.perform(get("/data/2.5/weather").
                param("lat", "1.23").
                param("lon", "3.45").
                param("APPID", "aaaa")).andReturn();
        String content = result.getResponse().getContentAsString();
        // TODO: xpath assertions
        /*assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><current><clouds name=\"broken clouds\" value=\"20\"/>"
                + "<humidity unit=\"%\" value=\"50\"/><precipitation mode=\"no\" unit=\"3h\" value=\"0.0\"/>"
                + "<pressure unit=\"hPa\" value=\"2000.0\"/><temperature max=\"273.0\" min=\"273.0\" unit=\"kelvin\" value=\"273.0\"/>"
                + "<visiblity value=\"10000\"/><wind><direction code=\"E\" name=\"East\" value=\"100\"/><speed name=\"gentle breeze\" value=\"10.0\"/></wind></current>", content);*/
        
        // and the forecast
        result = this.mockMvc.perform(get("/data/2.5/forecast").
                param("lat", "1.23").
                param("lon", "3.45").
                param("APPID", "aaaa")).andReturn();
        content = result.getResponse().getContentAsString();
        assertNotNull(content);
    }
}