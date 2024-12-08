const createButton = document.getElementById('login-btn');
if (createButton) {
    createButton.addEventListener('click', (event) => {
        event.preventDefault(); // 기본 버튼 클릭 동작 방지

        // 로그인 요청
        fetch(`/api/login`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username: document.getElementById('username').value, // 유저네임 입력값
                password: document.getElementById('password').value, // 패스워드 입력값
            }),
        }).then((response) => {
            if (response.ok) {
                // 로그인 성공 시
                alert('로그인되었습니다.');
                location.replace('/articles'); // 홈 페이지로 리디렉션
            } else {
                // 로그인 실패 시
                alert('로그인에 실패하였습니다: ' + response.statusText);
                return Promise.reject('Login failed');
            }
        }).catch((error) => {
            console.error('Error during login:', error);
            alert('아이디가 없거나, 비밀번호가 틀렸습니다.');
        });
    });
}