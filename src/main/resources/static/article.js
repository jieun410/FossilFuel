const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {  // 'deletButton'을 'deleteButton'으로 수정
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {  // URL의 '//'를 '/'로 수정
            method: 'DELETE'
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
            body: JSON.stringify({
                title: document.getElementById('title').value,
                content: document.getElementById('content').value
            })
        })
            .then(() => {
                alert('Update Complete.');
                // location.replace('/articles/${id}'); // 잘못된 코드
                location.replace(`/articles/${id}`); // 올바른 코드

                // 와 아니 이거 백틱 아니라고 ㅋㅋㅋㅋ 와 아니 2시간을 와 아니 이게 와
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
