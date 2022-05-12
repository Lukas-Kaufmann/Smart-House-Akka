import {Button, Input} from "@mui/material";
import {useState} from "react";

function ConsumeControl() {
    let [name, setName] = useState("");
    let [amount, setAmount] = useState<number>(2);


    let sendConsume = () => {
        fetch("http://localhost:8080/fridge/consume/" + name + "/" + amount).then(r => console.log(r));
    }

    return (
        <div>
            <h2>Consume:</h2>
            <div style={{border: "1px solidBlack"}}>
                <div>
                    <p>Name</p>
                    <Input value={name} onChange={e => setName((e.target as HTMLInputElement).value)} placeholder={"Cheese"}/>
                </div>
                <br/>
                <p>Amount</p>
                <Input value={amount} type={"number"} onChange={e => setAmount((e.target as HTMLInputElement).valueAsNumber)} placeholder={"3"}/>

                <br/>
                <Button onClick={sendConsume}> <b>OK</b></Button>
            </div>
        </div>
    )
}

export default ConsumeControl;