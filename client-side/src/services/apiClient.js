const API_BASE_URL = 'http://127.0.0.1:8000/api';

export async function post(url,data){
    const fullUrl = `${API_BASE_URL}${url}`;

    const response = await fetch(fullUrl,{
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    const responseData = await response.json();

    if (!response.ok) {
        console.error("API Error:", responseData);
        throw new Error(`HTTP ${response.status}: ${responseData.message || responseData.error || 'Unknown error'}`);
    }

    return responseData;
}