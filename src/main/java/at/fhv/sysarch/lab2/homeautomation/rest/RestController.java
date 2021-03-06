package at.fhv.sysarch.lab2.homeautomation.rest;

import at.fhv.sysarch.lab2.homeautomation.devices.AirCondition;
import at.fhv.sysarch.lab2.homeautomation.devices.Fridge;
import at.fhv.sysarch.lab2.homeautomation.devices.MediaStation;
import at.fhv.sysarch.lab2.homeautomation.simulator.TemperatureSimulator;
import at.fhv.sysarch.lab2.homeautomation.simulator.WeatherSimulator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin
public class RestController {

    @GetMapping(path = "/randomTemp")
    public String setRandomTemp() {
        TemperatureSimulator.instanceRef.tell(new TemperatureSimulator.SetTemperatureRandomly());
        return "Ok";
    }

    @GetMapping(path = "/setTemp/{value}")
    public String setTemp(@PathVariable double value) {
        TemperatureSimulator.instanceRef.tell(new TemperatureSimulator.SetTemperatureTo(value));
        return "Ok";
    }

    @GetMapping(path = "/aircondition/on")
    public String airConditionOn() {
        AirCondition.instanceRef.tell(new AirCondition.PowerAirCondition(true));
        return "Ok";
    }
    @GetMapping(path = "/aircondition/off")
    public String airConditionOff() {
        AirCondition.instanceRef.tell(new AirCondition.PowerAirCondition(false));
        return "Ok";
    }

    @GetMapping(path= "/randomWeather")
    public String randomWeather() {
        WeatherSimulator.instanceRef.tell(new WeatherSimulator.RandomWeather());
        return "Ok";
    }

    @GetMapping(path="/setWeather/{weather}")
    public String setWeather(@PathVariable String weather) {
        WeatherSimulator.WEATHER w;

        switch (weather) {
            case "sunny":
                w = WeatherSimulator.WEATHER.SUNNY;
                break;
            case "cloudy":
                w = WeatherSimulator.WEATHER.CLOUDY;
                break;
            case "rain":
                w = WeatherSimulator.WEATHER.RAIN;
                break;
            default:
                w = null;
        }
        WeatherSimulator.instanceRef.tell(new WeatherSimulator.SetWeather(w));
        return "Ok";
    }

    @GetMapping("/startMovie")
    public String startMovie() {
        MediaStation.instanceRef.tell(new MediaStation.PlayMovie());
        return "Ok";
    }
    @GetMapping("/stopMovie")
    public String stopMovie() {
        MediaStation.instanceRef.tell(new MediaStation.StopMovie());
        return "OK";
    }

    @GetMapping("/fridge/getProducts")
    public String getProducts() {
        Fridge.instanceRef.tell(new Fridge.GetProducts());
        return "OK";
    }

    @GetMapping("/fridge/orders")
    public String getOrders() {
        Fridge.instanceRef.tell(new Fridge.GetOrders());
        return "OK";
    }

    @GetMapping("/fridge/order/{name}/{amount}/{weight}")
    public String order(@PathVariable String name, @PathVariable int amount, @PathVariable double weight) {
//        int amountI = Integer.parseInt(amount);
//        double weightD = Double.parseDouble(weight);
        Fridge.instanceRef.tell(new Fridge.OrderProduct(name, weight, amount));
        return "OK";
    }

    @GetMapping("/fridge/consume/{name}/{amount}")
    public String consume(@PathVariable String name, @PathVariable int amount) {
        Fridge.instanceRef.tell(new Fridge.ConsumeProduct(name, amount));
        return "OK";
    }
}
