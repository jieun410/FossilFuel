//목록으로 돌아가기
const returnButton = document.getElementById('article-return-btn');
if (returnButton) {
    returnButton.addEventListener('click', event => {
        // 사용자에게 경고창을 띄우고, '확인'을 누르면 목록으로 이동
        const userConfirmed = confirm('게시글 목록으로 돌아가시겠습니까? 작성하고 있는 글이 저장되지 않습니다.');
        if (userConfirmed) {
            // '확인'을 누르면 목록 페이지로 이동
            location.replace('/articles');
        } else {
            // '취소'를 누르면 아무 동작도 하지 않음
            event.preventDefault();
        }
    });
}

// 삭제 기능
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        const userConfirmed = confirm('삭제하시겠습니까?');
        let id = document.getElementById('article-id').value;

        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        }

        // function fail(response) {
        //     response.text().then(text => {
        //         alert('삭제에 실패했습니다');
        //         location.replace('/articles');
        //     });
        // }
        function fail(response) {
            console.log("Fail function called");  // fail 함수 호출 확인
            response.json().then(data => {  // JSON 응답 처리
                console.log("Response JSON:", data);
                if (response.status === 500) {
                    alert('서버에서 문제가 발생했습니다: ' + data.message);
                } else {
                    alert('삭제에 실패했습니다: ' + data.message);
                }
                location.replace('/articles');
            }).catch(error => {
                console.log("JSON 처리 실패:", error);
                alert('삭제 과정에서 문제가 발생했습니다.');
            });
        }

        if (userConfirmed) {
            httpRequest('DELETE', `/api/article/${id}`, null, success, fail);
        } else {
            event.preventDefault();
        }

    });
}

// 수정 기능
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        const body = new FormData();

        body.append('request', JSON.stringify({
            title: document.getElementById('title').value,
            content: editorInstance.getData()  // CKEditor의 내용을 가져옴
        }));

        const fileInput = document.getElementById('file-input'); // 파일 input 요소
        if (fileInput && fileInput.files.length > 0) {
            for (let i = 0; i < fileInput.files.length; i++) {
                body.append('files', fileInput.files[i]); // 파일 추가
            }
        }

        function success() {
            alert('수정 완료되었습니다.');
            location.replace(`/articles/${id}`);
        }

        function fail(response) {
            response.text().then(text => {
                alert('수정 실패했습니다.' );
                // console.log(text)
                location.replace(`/articles/${id}`);
            });
        }

        httpRequest('PUT', `/api/article/${id}`, body, success, fail);
    });
}


// 등록 기능
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {
        const body = new FormData();
        // CKEditor 5에서 데이터 가져오기 (index.js에서 초기화된 전역 변수 사용)

        body.append('request', JSON.stringify({
            title: document.getElementById('title').value,
            content: editorInstance.getData()   // CKEditor5에서 데이터 가져오기

        }));

        const fileInput = document.getElementById('file-input'); // 파일 input 요소
        if (fileInput && fileInput.files.length > 0) {
            for (let i = 0; i < fileInput.files.length; i++) {
                body.append('files', fileInput.files[i]); // 파일 추가
            }
        }

        function success() {
            alert('등록 완료되었습니다.');
            location.replace('/articles');
        }

        function fail(response) {
            response.text().then(text => {
                alert('등록 실패했습니다. 로그인 후 이용해주세요! ');
                location.replace('/articles');
            });
        }

        httpRequest('POST', '/api/article', body, success, fail);
    });
}


// 쿠키를 가져오는 함수
function getCookie(key) {
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');
        var dic = item.split('=');
        if (key === dic[0]) {
            result = dic[1];
            return true;
        }
    });
    return result;
}

// HTTP 요청을 보내는 함수
let retryCount = 0;  // 재시도 횟수 제한

function httpRequest(method, url, body, success, fail) {
    const headers = {
        Authorization: 'Bearer ' + localStorage.getItem('access_token'),
        // Content-Type 설정하지 않음, FormData 사용 시 브라우저가 자동으로 설정함
    };

    fetch(url, {
        method: method,
        headers: headers,  // Content-Type 설정하지 않음
        body: body, // FormData 객체 전달
        // credentials: 'include'
    }).then(response => {
        if (response.ok) {
            return success();
        }
        const refresh_token = getCookie('refresh_token');
        if (response.status === 401 && refresh_token && retryCount < 3) {
            retryCount++;
            fetch('/api/token', {
                method: 'POST',
                headers: {
                    Authorization: 'Bearer ' + localStorage.getItem('access_token'),
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    refreshToken: getCookie('refresh_token'),
                }),
                // credentials: 'include'
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
document.addEventListener('DOMContentLoaded', () => {
    const notificationCount = document.getElementById('notification-count');

    // 읽지 않은 알림 수를 가져오는 함수 정의
    function fetchUnreadNotificationCount() {
        fetch('/api/notifications/unread-count', {
            method: 'GET',
            credentials: 'include'
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Error fetching unread count: ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                console.log("Fetched unread count in article.js:", data);
                const unreadCount = data.unreadCount || 0;
                notificationCount.textContent = unreadCount;
                notificationCount.classList.toggle('hidden', unreadCount === 0);
            })
            .catch(error => {
                console.error('Error fetching unread notification count in article.js:', error);
                notificationCount.textContent = '0';
                notificationCount.classList.add('hidden');
            });
    }

    // 페이지 로드 시마다 알림 수 갱신
    fetchUnreadNotificationCount();
});



