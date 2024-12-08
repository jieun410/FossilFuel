// const confirmDeleteButton=document.getElementById("confirmDeleteBtn")
//
// if(confirmDeleteButton){
//     confirmDeleteButton.addEventListener("click", function() {
//         // 사용자 삭제 요청 (API 호출)
//
//
//         httpRequest('DELETE', `/api/user`, body, success, fail);
//
//     });
// }
//
//
// // HTTP 요청을 보내는 함수
// function httpRequest(method, url, body, success, fail) {
//     const headers = {
//         Authorization: 'Bearer ' + localStorage.getItem('access_token'),
//         'Content-Type': 'application/json'  // JSON 형식으로 전송
//     };
//
//     fetch(url, {
//         method: method,
//         headers: headers,
//         body: body,
//     }).then(response => {
//         if (response.ok) {
//             return response.json().then(data => success(data));  // JSON 응답을 성공 콜백으로 전달
//         }
//         const refresh_token = getCookie('refresh_token');
//         if (response.status === 401 && refresh_token) {
//             fetch('/api/token', {
//                 method: 'POST',
//                 headers: {
//                     Authorization: 'Bearer ' + localStorage.getItem('access_token'),
//                     'Content-Type': 'application/json'
//                 },
//                 body: JSON.stringify({
//                     refreshToken: getCookie('refresh_token'),
//                 }),
//             })
//                 .then(res => res.ok ? res.json() : Promise.reject())
//                 .then(result => {
//                     localStorage.setItem('access_token', result.accessToken);
//                     httpRequest(method, url, body, success, fail); // 토큰 갱신 후 다시 요청
//                 })
//                 .catch(() => fail(response));
//         } else {
//             return fail(response);
//         }
//     }).catch(error => fail(error));
// }
const confirmDeleteButton = document.getElementById("confirmDeleteBtn");

if (confirmDeleteButton) {
    confirmDeleteButton.addEventListener("click", function() {
        // 사용자 삭제 요청 (API 호출)
        const body = null;  // DELETE 요청에는 보통 body가 필요하지 않음
        httpRequest('DELETE', '/api/user', body,
            function success() {
                alert('탈퇴가 완료되었습니다.');
                window.location.href = '/';  // 성공 시 홈페이지로 이동
            },
            function fail(error) {
                alert('탈퇴에 실패했습니다. 다시 시도해주세요.');
                console.error('Error:', error);
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
        body: body,  // DELETE 요청에는 보통 body가 필요하지 않음
    }).then(response => {
        if (response.ok) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token) {
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
            })
                .then(res => res.ok ? res.json() : Promise.reject())
                .then(result => {
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail); // 토큰 갱신 후 다시 요청
                })
                .catch(() => fail(response));
        } else {
            return fail(response);
        }
    }).catch(error => fail(error));
}
