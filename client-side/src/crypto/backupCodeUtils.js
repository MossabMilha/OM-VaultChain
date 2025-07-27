export function generateBackupCode(){
    const charset = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    const segment = Array.from({length: 16}, () =>
        Array.from({length: 4}, () =>
            charset[Math.floor(crypto.getRandomValues(new Uint32Array(1))[0] / 2 ** 32 * charset.length)]
        ).join("")
    );
    return segment.join("-");
}