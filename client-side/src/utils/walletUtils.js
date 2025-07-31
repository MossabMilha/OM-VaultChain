export async function connectWallet(){
    if(!window.ethereum)throw new Error("MetaMask not found");
    const accounts = await window.ethereum.request({ method: 'eth_requestAccounts' });
    return { address: accounts[0] };
}

export async function signMessage(message, address) {
    return await window.ethereum.request({
        method: 'personal_sign',
        params: [message, address]
    });
}