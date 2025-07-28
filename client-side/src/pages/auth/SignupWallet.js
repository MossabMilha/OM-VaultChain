import {connectWallet, signWalletMessage} from "../../utils/walletUtils.js";

document.addEventListener("DOMContentLoaded", ()=>{
    const form = document.getElementById("walletSignupForm");
    const signupBtn = document.getElementById("signupBtn");
    const status = document.getElementById("status");
    signupBtn.addEventListener("click",async ()=>{
        try{
            const firstName = document.getElementById("firstName").value.trim();
            const lastName = document.getElementById("lastName").value.trim();
            const email = document.getElementById("email").value.trim();

            if(!firstName || !lastName || !email){
                status.textContent = "❌ Please fill in all fields.";
                return;
            }
            status.textContent ="🔗 Connecting wallet..."
            const walletAddress = await connectWallet();
            status.textContent = "🖊️ Please sign the verification message in MetaMask..."
            const {message,signature} = await signWalletMessage(walletAddress);
            console.log(message);
            console.log(signature);
            status.textContent = "✅ Wallet signup successful!";

        }catch (error){
            console.error(error);
            status.textContent = "❌ Error: " + error.message;
        }
    })
})