const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {  // 'deletButton'을 'deleteButton'으로 수정
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;
        fetch(`/api/articles/${id}`, {  // URL의 '//'를 '/'로 수정
            method: 'DELETE'
        })
            .then(() => {
                alert('Delete Complete.');
                location.replace('/articles');
            });
    });
}