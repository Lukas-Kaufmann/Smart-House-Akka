import {Button} from "@mui/material";
import ConsumeControl from "./ConsumeControl";
import OrderControl from "./OrderControl";


function FridgeControls() {

    return (
        <>
            <h3>Fridge</h3>

            <div style={{display: "flex", justifyContent: "space-between"}}>
                <div style={{margin: 50}}>
                    <Button onClick={() => fetch("http://localhost:8080/fridge/getProducts")}>List Products</Button>
                    <br/>
                    <Button onClick={() => fetch("http://localhost:8080/fridge/orders")}>List Orders</Button>
                </div>
                <div style={{margin: 50}}>
                    <ConsumeControl/>
                </div>
                <div style={{margin: 50}}>
                    <OrderControl/>
                </div>
            </div>
        </>
    )
}

export default FridgeControls;