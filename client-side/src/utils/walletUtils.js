// Connect to MetaMask
export async function connectWallet(){
    if(!window.ethereum) throw new Error("MetaMask is not installed. Please install it to connect your wallet.");
    const accounts =  await window.ethereum.request({method: "eth_requestAccounts"});
    return accounts[0];
}
// Sign a unique message (ownership proof)
export async function signWalletMessage(walletAddress){
    const message = `Welcome to OM VaultChain!\nNonce: ${Date.now()}-${Math.random()}`;
    const signature = await window.ethereum.request({
        method: "personal_sign",
        params: [message, walletAddress]
    })
    return {message, signature}
}