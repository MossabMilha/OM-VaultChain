export function storePrivateKey(email,privateKeyBase64,publicKeyBase64) {
    const vaultData = JSON.parse(localStorage.getItem("vaultData") || "{}");
    vaultData[email] = {
        privateKey: privateKeyBase64,
        publicKey: publicKeyBase64,
        lastLogin: Date.now()
    };
     localStorage.setItem("VaultChain_Users", JSON.stringify(vaultData));
}

export function getUserPrivateKey(email){
    const vaultData = JSON.parse(localStorage.getItem("VaultChain_Users")|| "{}");
    return vaultData[email] || null;
}
export function deleteUserPrivateKey(email) {
    const vaultData = JSON.parse(localStorage.getItem("VaultChain_Users") || "{}");
    delete vaultData[email];
    localStorage.setItem("VaultChain_Users", JSON.stringify(vaultData));
}