import React from 'react';
import AirconditionControls from "./AirconditionControls";
import TemperatureControls from "./TemperatureControls";
import WeatherControls from "./WeatherControls";
import {Card, CardContent} from "@mui/material";
import MediaStationControls from "./MediaStationControls";
import FridgeControls from "./FridgeControls";

function ControlPanel() {


    return (
        <div className="ControlPanel">
            <h1>Smart house Control Panel</h1>
            <hr/>

            <div style={{display: "flex", flexDirection: "row", justifyContent: "space-around"}}>
                <Card sx={{minWidth: 300}}>
                    <CardContent>
                        <h2>Environment</h2>
                        <TemperatureControls/>
                        <WeatherControls/>
                    </CardContent>
                </Card>

                <Card sx={{minWidth: 300}}>
                    <CardContent>
                        <h2>Devices</h2>
                        <AirconditionControls/>
                        <MediaStationControls/>
                    </CardContent>
                </Card>
            </div>
            <br/>
            <div style={{display: "flex", flexDirection: "row", justifyContent: "space-around"}}>
                <Card sx={{minWidth: 400}}>
                    <CardContent>
                        <FridgeControls/>
                    </CardContent>
                </Card>
            </div>

        </div>
    );
}

export default ControlPanel;
