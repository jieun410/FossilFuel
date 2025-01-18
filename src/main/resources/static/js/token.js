const token = searchParam('token')

if (token) {
    localStorage.setItem("access_token", token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}

// 쿠키 삭제하는 함수
function deleteCookie(name) {
    document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT; path=/;';
}


// 로그아웃 버튼이 존재하는지 확인하고, 클릭 이벤트 리스너 추가
const logoutButton = document.getElementById('logout-btn');
if (logoutButton) {
    logoutButton.addEventListener('click', function() {
        // 로그아웃 함수
        function logout() {
            alert('로그아웃되었습니다.');
            deleteCookie('oauth2_auth_request');
            deleteCookie('refresh_token'); // Refresh Token 쿠키 삭제
            // 로컬 스토리지에서 액세스 토큰 제거
            localStorage.removeItem("access_token");
            fetch('/logout', {
                method: 'GET',
                credentials: 'include',
            })
                .then(() => {
                    // 로그아웃 후 리다이렉트
                    window.location.href = '/login';
                })
                .catch(err => {
                    console.error('로그아웃 요청 중 오류 발생:', err);
                });
        }

        // 로그아웃 함수 호출
        logout();
    });
}