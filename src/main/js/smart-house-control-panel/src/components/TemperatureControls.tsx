import React from "react";
import {Input, ToggleButton, ToggleButtonGroup} from "@mui/material";

function TemperatureControls() {
    const [alignment, setAlignment] = React.useState<string | null>('random');

    const handleAlignment = (
        event: React.MouseEvent<HTMLElement>,
        newAlignment: string | null,
    ) => {
        setAlignment(newAlignment);
    };

    let [tempInput, setTempInput] = React.useState("20");

    let keyDownHandler = (event: React.KeyboardEvent) => {
        if (event.key === "Enter") {
            let temp = Number(tempInput);

            fetch("http://localhost:8080/setTemp/"+temp)
        }
    }

    return (
        <>
            <h3>Temperature</h3>
            <ToggleButtonGroup exclusive value={alignment} onChange={handleAlignment} >
                <ToggleButton value={"on"} onClick={() => fetch("http://localhost:8080/randomTemp")}>Random</ToggleButton>
                <ToggleButton value={"fixed"} ><Input value={tempInput} onInput={e => setTempInput((e.target as HTMLInputElement).value)} onKeyDown={e => keyDownHandler(e)} /></ToggleButton>
            </ToggleButtonGroup>
        </>
    )
}

export default TemperatureControls;