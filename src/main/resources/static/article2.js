

const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {
            method: 'DELETE',
            credentials: 'include', // fetch 요청에서는 브라우저가 쿠키를 자동으로 전송하므로 별도의 헤더 설정 X
        })
            .then(() => {
                alert('Delete Complete.');
                location.replace(`/articles`);
            });
    });
}

const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/articles/${id}`, {
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            credentials: 'include', // 쿠키를 포함하여 요청
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
            }),
        })
            .then(() => {
                alert('Update Complete.');
                location.replace(`/articles/${id}`);
            });
    });
}

const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', (event) => {
        fetch(`/api/articles`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            credentials: 'include', // 쿠키를 포함하여 요청
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
            }),
        }).then((response) => {
            alert('Create Complete.');
            location.replace(`/articles`);
        });
    });
}

async function fetchWithTokenReissue(url, options) {
    const response = await fetch(url, options);

    // 엑세스 토큰이 만료되어 401이 반환되면 리프레시 토큰으로 재발급 요청
    if (response.status === 401) {
        const reissueResponse = await fetch('/api/v1/jwt/reissue', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${getRefreshTokenFromCookies()}`, // 리프레시 토큰을 헤더에 포함
            },
        });

        if (reissueResponse.ok) {
            const { accessToken } = await reissueResponse.json();
            setAccessTokenInCookies(accessToken); // 새 엑세스 토큰을 쿠키에 저장

            // 다시 원래 요청을 새 토큰으로 시도
            options.headers['Authorization'] = `Bearer ${accessToken}`;
            return fetch(url, options); // 새 엑세스 토큰으로 원래 요청 재시도
        }
    }

    return response; // 처음 요청의 응답 반환
}

// 쿠키에서 리프레시 토큰을 가져오는 함수
function getRefreshTokenFromCookies() {
    // 쿠키에서 리프레시 토큰을 추출하는 로직 필요
    // 쿠키 파싱 후 'refreshToken' 값 반환
}

// 새 엑세스 토큰을 쿠키에 저장하는 함수
function setAccessTokenInCookies(accessToken) {
    document.cookie = `accessToken=${accessToken}; path=/;`;
}
