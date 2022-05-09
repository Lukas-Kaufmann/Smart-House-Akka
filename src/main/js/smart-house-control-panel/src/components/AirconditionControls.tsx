import React, {useState} from "react";
import {ToggleButton, ToggleButtonGroup} from "@mui/material";

function AirconditionControls() {

    const [alignment, setAlignment] = React.useState<string | null>('on');

    const handleAlignment = (
        event: React.MouseEvent<HTMLElement>,
        newAlignment: string | null,
    ) => {
        setAlignment(newAlignment);
    };

    return (
        <>
            <h3>AirCondition</h3>
            <ToggleButtonGroup exclusive value={alignment} onChange={handleAlignment} >
                <ToggleButton value={"on"} onClick={() => fetch("http://localhost:8080/aircondition/on")}>On</ToggleButton>
                <ToggleButton value={"off"} onClick={() => fetch("http://localhost:8080/aircondition/off")} >Off</ToggleButton>
            </ToggleButtonGroup>
        </>
    )

}

export default AirconditionControls;