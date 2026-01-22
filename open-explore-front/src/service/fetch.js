export async function FetchPost(url, options) {
  let data = {}
  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      ...options
    });
    if (!response.ok) {
      data.error = new Error('网络响应失败');
    }
    const result = await response.json();
    data.result = result;
  } catch (error) {
    data.error = error;
  }
  return data;
}

export async function FetchGet(url, options) {
  let data = {}
  try {
    const response = await fetch(url, {
      method: 'GET',
      ...options
    });
    if (!response.ok) {
      data.error = new Error('网络响应失败');
    }
    const result = await response.json();
    data.result = result;
  } catch (error) {
    data.error = error;
  }
  return data;
}