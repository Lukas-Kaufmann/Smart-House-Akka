import React from 'react';
import AirconditionControls from "./AirconditionControls";
import TemperatureControls from "./TemperatureControls";
import WeatherControls from "./WeatherControls";

function ControlPanel() {


    return (
        <div className="ControlPanel">
            <h1>Smart house Control Panel</h1>
            <hr/>

            <AirconditionControls/>
            <TemperatureControls/>
            <WeatherControls/>
        </div>
    );
}

export default ControlPanel;
