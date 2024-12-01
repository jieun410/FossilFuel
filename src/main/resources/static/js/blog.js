const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {  // 'deletButton'을 'deleteButton'으로 수정
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('blog-id').value;  // 'article-id'를 'blog-id'로 수정
        fetch(`/api/blogs/${id}`, {  // URL의 'articles'를 'blogs'로 수정
            method: 'DELETE'
        })
            .then(() => {
                alert('삭제 완료');
                location.replace(`/`);
            });
    });
}

const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        fetch(`/api/blogs/${id}`, {  // URL의 'articles'를 'blogs'로 수정
            method: 'PUT',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('수정 완료');
                location.replace(`/blogs/${id}`);  // URL에서 'articles'를 'blogs'로 수정
            });
    });
}

const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', (event) => {
        fetch(`/api/blogs`, {  // URL의 'articles'를 'blogs'로 수정
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value,
            }),
        }).then((response) => {
            alert('블로그 작성이 완료되었습니다!');  // 메시지 수정
            location.replace(`/`);
        });
    });
}