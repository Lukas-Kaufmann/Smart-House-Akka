import {useState} from "react";
import {Button, Input} from "@mui/material";


function OrderControl() {
    let [name, setName] = useState("");
    let [amount, setAmount] = useState<number>(2);
    let [weight, setWeight] = useState<number>(2.4);


    let sendConsume = () => {
        fetch("http://localhost:8080/fridge/order/"+name+"/"+amount+"/"+weight).then(r => console.log(r))
    }

    return (
        <div>
            <h2>Order:</h2>
            <div style={{border: "1px solidBlack"}}>
                <div>
                    <p>Name</p>
                    <Input value={name} onChange={e => setName((e.target as HTMLInputElement).value)} placeholder={"Cheese"}/>
                </div>

                <p>Amount</p>
                <Input value={amount} type={"number"} onChange={e => setAmount((e.target as HTMLInputElement).valueAsNumber)} placeholder={"3"}/>

                <p>Weight</p>

                <Input value={weight} type={"number"} inputProps={{step: 0.1}} onChange={e => setWeight((e.target as HTMLInputElement).valueAsNumber)} placeholder={"3"}/>

                <br/>
                <Button onClick={sendConsume}> <b>OK</b></Button>
            </div>
        </div>
    )

}

export default OrderControl;