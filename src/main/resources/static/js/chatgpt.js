document.getElementById('chat-form').addEventListener('submit', async (event) => {
    event.preventDefault(); // 폼 제출 방지
    const message = document.getElementById('message').value;
    const chatBox = document.getElementById('chat-box');

    // 사용자가 입력한 메시지 표시
    const userMessage = document.createElement('div');
    userMessage.classList.add('chat-message', 'user-message');
    userMessage.innerHTML = `<p>${message}</p>`;
    chatBox.appendChild(userMessage);
    chatBox.scrollTop = chatBox.scrollHeight;

    // Loading 메시지 추가
    const botMessage = document.createElement('div');
    botMessage.classList.add('chat-message', 'bot-message');
    botMessage.innerHTML = `<p>Loading...</p>`;
    chatBox.appendChild(botMessage);
    chatBox.scrollTop = chatBox.scrollHeight;

    // 서버에 메시지 전송
    try {
        const response = await fetch('/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ message })
        });

        const result = await response.json();
        botMessage.innerHTML = `<p>${result.reply || result.error}</p>`;
    } catch (error) {
        botMessage.innerHTML = '<p>에러 발생</p>';
    } finally {
        chatBox.scrollTop = chatBox.scrollHeight;
        document.getElementById('message').value = '';
    }
});