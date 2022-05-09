import React from "react";
import {ToggleButton, ToggleButtonGroup} from "@mui/material";

function WeatherControls() {
    const [alignment, setAlignment] = React.useState<string | null>('random');

    const handleAlignment = (
        event: React.MouseEvent<HTMLElement>,
        newAlignment: string | null,
    ) => {
        setAlignment(newAlignment);
    };

    return (
        <>
            <h3>Weather controls</h3>
            <ToggleButtonGroup exclusive value={alignment} onChange={handleAlignment} >
                <ToggleButton value={"random"} onClick={() => fetch("http://localhost:8080/randomWeather")}>Random</ToggleButton>
                <ToggleButton value={"sunny"} onClick={() => fetch("http://localhost:8080/setWeather/sunny")} >Sunny</ToggleButton>
                <ToggleButton value={"cloudy"} onClick={() => fetch("http://localhost:8080/setWeather/cloudy")} >Cloudy</ToggleButton>
                <ToggleButton value={"rain"} onClick={() => fetch("http://localhost:8080/setWeather/rain")} >Rain</ToggleButton>
            </ToggleButtonGroup>
        </>
    )
}

export default WeatherControls;