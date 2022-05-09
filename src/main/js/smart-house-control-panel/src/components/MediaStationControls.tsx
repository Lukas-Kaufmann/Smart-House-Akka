import React from "react";
import {ToggleButton, ToggleButtonGroup} from "@mui/material";

function MediaStationControls() {

    const [alignment, setAlignment] = React.useState<string | null>('stopped');

    const handleAlignment = (
        event: React.MouseEvent<HTMLElement>,
        newAlignment: string | null,
    ) => {
        setAlignment(newAlignment);
    };

    return (
        <>
            <h3>MediaStation</h3>
            <ToggleButtonGroup exclusive value={alignment} onChange={handleAlignment} >
                <ToggleButton value={"playing"} onClick={() => fetch("http://localhost:8080/startMovie")}>Playing</ToggleButton>
                <ToggleButton value={"stopped"} onClick={() => fetch("http://localhost:8080/stopMovie")} >Stopped</ToggleButton>
            </ToggleButtonGroup>
        </>
    )
}

export default MediaStationControls;