const createButton = document.getElementById('login-btn');
if (createButton) {
    createButton.addEventListener('click', (event) => {
        event.preventDefault(); // 기본 버튼 클릭 동작 방지
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
                location.replace('/articles'); // 홈 페이지로 리디렉션 (백은 /경로로 보내는게 있음, 충돌 조심:토큰 값 담는 부분)

                // Authorization 헤더에서 토큰을 가져옵니다.
                const accessToken = response.headers.get("Authorization");
                if (accessToken) {
                    // 'Bearer ' 이후의 문자열을 추출합니다.
                    const token = accessToken.substr(7);
                    // 액세스 토큰을 로컬 스토리지에 저장
                    localStorage.setItem('access_token', token);

                    alert('로그인되었습니다.');


                    const headers = new Headers();
                    headers.append('Authorization', `Bearer ${token}`);


                    const body = JSON.stringify({}); // 빈 객체를 JSON 문자열로 변환합니다.


                    // HTTP 요청을 보냅니다.
                    httpRequest('POST', '/api/login', body,
                        () => {
                            // 요청 성공 시
                            console.log('Token sent to server successfully.');
                            location.replace(`/?token=` + token);
                        },
                        (errorResponse) => {
                            // 요청 실패 시 무시하고 이동 (어차피 http 요청이 한번만 날아가면 됨)
                            location.replace(`/?token=` + token); // 실패 후에도 리디렉션
                        }
                    );

                } else {
                    alert('Login failed: Access token not received.');
                }
            } else {
                alert('로그인에 실패하였습니다: ' + response.statusText);
                return Promise.reject('Login failed');
            }
        }).catch((error) => {
            console.error('Error during login:', error);
            alert('아이디가 없거나, 비밀번호가 틀렸습니다.');
        });
    });
}

// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    const headers = {
        Authorization: 'Bearer ' + localStorage.getItem('access_token'),
        'Content-Type': 'application/json'  // JSON 형식으로 전송
    };

    fetch(url, {
        method: method,
        headers: headers,
        body: body, // 요청 본문
    }).then(response => {
        if (response.ok) {
            return success();
        } else {
            return fail(response);
        }
    }).catch(error => fail(error));
}