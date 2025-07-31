const charset = 'ABCDEFGHJKLMNPQRSTUVWXYZ23456789';
export function generateBackupCode(){
    let code= "";
    for(let i = 0;i<16;i++){
        let chunk = "";
        for(let j = 0;j<4;j++){
            chunk += charset.charAt(Math.floor(Math.random()*charset.length));
        }
        code += chunk + (i!== 15?"-":"");
    }
    return code
}