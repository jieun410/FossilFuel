// HTTP 요청을 보내는 함수
function httpRequest(method, url, body, success, fail) {
    const headers = {
        Authorization: 'Bearer ' + localStorage.getItem('access_token'),
        'Content-Type': 'application/json'  // JSON 형식으로 전송
    };

    fetch(url, {
        method: method,
        headers: headers,
        body: body,
    }).then(response => {
        if (response.ok) {
            return response.json().then(data => success(data));  // JSON 응답을 성공 콜백으로 전달
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

// 댓글 작성 요청
function submitComment(articleId, content, parentCommentId = null) {
    const body = {
        commentContent: content,
        parentCommentId: parentCommentId  // 부모 댓글 ID가 있을 경우 대댓글로 처리
    };


    const url = `/api/comment/${articleId}`;

    httpRequest('POST', url, JSON.stringify(body),
        (savedComment) => {  // 성공 시 처리 (savedComment는 서버에서 반환된 댓글 객체)
            console.log("새로운 댓글:", savedComment);  // 서버에서 반환된 저장된 댓글 객체 출력
            loadComments(articleId);  // 댓글 목록 다시 로드
            if (!parentCommentId) {
                document.querySelector('textarea[name="content"]').value = '';  // 메인 댓글 초기화
            } else {
                document.querySelector(`#reply-form-${parentCommentId} textarea`).value = '';  // 대댓글 textarea 초기화
            }
        },
        (response) => {  // 실패 시 처리
            console.error('댓글 작성 실패, 상태 코드:', response.status || '응답 없음');
            alert('댓글 작성에 실패했습니다. 다시 시도해주세요.');
        });

}

// 댓글 수정 요청 함수
function enableEditComment(commentId, currentContent, commentAuthor) {
    const commentCard = document.getElementById(`comment-card-${commentId}`);

    // 기존 댓글 내용을 textarea로 변경
    commentCard.innerHTML = `
        <h6 class="mb-2 text-muted">${commentAuthor}</h6>
        <form id="comment-form">
            <input type="hidden" name="commentId" value="${commentId}"/>
            <div class="form-group">
                <textarea class="form-control" name="content" rows="3">${currentContent}</textarea>  <!-- 기존 내용 유지 -->
            </div>
            <div class="text-right">
                <button type="button" class="btn btn-primary btn-sm" id="save-comment-btn-${commentId}">댓글 등록</button>
            </div>
        </form>
    `;

    const saveCommentButton = document.getElementById(`save-comment-btn-${commentId}`);
    if (saveCommentButton) {
        saveCommentButton.addEventListener('click', () => {
            const updatedContent = document.querySelector(`#comment-card-${commentId} textarea[name="content"]`).value;
            submitEdit(commentId, updatedContent);  // 수정된 내용 저장
        });
    }
}

// 댓글 수정 저장 요청
function submitEdit(commentId, updatedContent) {
    const body = JSON.stringify({
        commentContent: updatedContent,
    });

    function success() {
        alert('수정 완료되었습니다.');
        loadComments(document.getElementById('article-id').value);  // 수정 후 댓글 목록 새로고침
    }

    function fail(response) {
        response.text().then(text => {
            alert('수정 실패했습니다.');
        });
    }

    httpRequest('PUT', `/api/comment/${commentId}`, body, success, fail);
}

// 댓글 삭제 요청 함수 (isDeleted를 true로 업데이트)
function deleteComment(commentId) {
    const body = JSON.stringify({
        commentIsDeleted: true,  // 삭제 플래그 설정
        commentContent: '삭제된 댓글입니다.'  // 삭제된 댓글 내용
    });

    function success() {
        alert('댓글이 삭제되었습니다.');
        loadComments(document.getElementById('article-id').value);  // 삭제 후 댓글 목록 새로고침
    }

    function fail(response) {
        response.text().then(text => {
            alert('댓글 삭제에 실패했습니다.');
        });
    }

    httpRequest('PUT', `/api/comment/${commentId}`, body, success, fail);
}

// 댓글 목록 로드

function loadComments(articleId) {
    const url = `/api/comment/${articleId}`;
    httpRequest('GET', url, null,
        (data, response) => {  // 응답 데이터를 받고 response 객체도 전달받음
            if (!response.ok) {
                console.error('응답에 문제가 있습니다. 상태 코드:', response.status);
                return;
            }
            renderComments(data); // 댓글 목록을 화면에 렌더링
        },
        (error) => {
            console.error('댓글 불러오기 실패:', error);
        });
}


// 댓글 목록 렌더링
function renderComments(comments) {
    const commentSection = document.getElementById('comments-section');
    commentSection.innerHTML = ''; // 기존 댓글 목록 초기화

    // 부모 댓글과 대댓글을 트리 구조로 렌더링
    const topLevelComments = comments.filter(comment => !comment.parentCommentId);  // 최상위 부모 댓글들만 필터링

    topLevelComments.forEach(parentComment => {
        renderCommentWithReplies(parentComment, comments, 0);  // 부모 댓글과 대댓글 렌더링
    });
}



// 특정 댓글과 그 대댓글을 렌더링하는 함수
function renderCommentWithReplies(comment, allComments, depth) {
    const commentSection = document.getElementById('comments-section');
    const commentCard = document.createElement('div');
    commentCard.style.marginLeft = `${depth * 20}px`;  // 대댓글 들여쓰기

    // commentId로 고유한 div 설정
    commentCard.id = `comment-card-${comment.commentId}`;

    const isReply = comment.parentCommentId !== null;
    if (isReply) {
        commentCard.classList.add('comment-reply');
    } else {
        commentCard.classList.add('comment-main');
    }
    console.log(`댓글 ${comment.commentId}의${comment.commentAuthor} ${comment.commentCreatedAt} ${comment.commentAuthor}: ${comment.commentIsDeleted}`);
// 댓글 작성자와 현재 사용자 정보를 로그로 출력
    console.log("commentAuthor: ", comment.commentAuthor);
    console.log("currentUserName: ", currentUserName);

    // 댓글 삭제 여부에 따라 표시
    if (comment.commentIsDeleted) {
        commentCard.innerHTML = `
        <div class="card-body">
            <p>삭제된 댓글입니다</p>
        </div>
    `;
        console.log(`댓글 ${comment.commentId}이(가) 삭제되었습니다.`);
    } else {
        commentCard.innerHTML = `
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center">
                <h6 class="card-subtitle mb-2 text-muted" id="comment-Author" style="margin-top: 8px">${comment.commentAuthor}</h6>
                <div class="comment-button">
                        ${comment.commentAuthor === currentUserName ? `
                        <button type="button" id="comment-modify-btn-${comment.commentId}" class="btn btn-primary btn-sm">수정</button>
                        <button type="button" id="comment-delete-btn-${comment.commentId}" class="btn btn-secondary btn-sm">삭제</button>
                        ` : ''}
                </div>
            </div>
            <p class="card-text" id="comment-${comment.commentId}-content">${comment.commentContent}</p>
            <p class="commentCreatedAt">${comment.commentCreatedAt}</p>
<!--            <button class="btn btn-link reply-btn" id="reply-button-${comment.commentId}" data-comment-id="${comment.commentId}">댓글 쓰기</button>-->
            ${
            comment.commentAuthor !== "탈퇴한 사용자입니다." ?
                `<button class="btn btn-link reply-btn" id="reply-button-${comment.commentId}" data-comment-id="${comment.commentId}">댓글 쓰기</button>` : ''
                }
            <div id="reply-form-${comment.commentId}" class="reply-form mt-2" style="display: none;">
                <textarea class="form-control" rows="3" placeholder="대댓글을 입력하세요"></textarea>
                <div class="text-right">
                    <button type="button" class="btn btn-primary btn-sm mt-2 submit-reply-btn" data-comment-id="${comment.commentId}">댓글 등록</button>
                </div>
            </div>
        </div>
    `;
    }

    commentSection.appendChild(commentCard);

    // 수정 버튼에 이벤트 리스너 추가
    const modifyButton = document.getElementById(`comment-modify-btn-${comment.commentId}`);
    if (modifyButton) {
        modifyButton.addEventListener('click', () => enableEditComment(comment.commentId, comment.commentContent, comment.commentAuthor));
    }

    // 삭제 버튼에 이벤트 리스너 추가
    const deleteButton = document.getElementById(`comment-delete-btn-${comment.commentId}`);
    if (deleteButton) {
        deleteButton.addEventListener('click', () => deleteComment(comment.commentId));
    }

    // 대댓글 작성 버튼에 이벤트 리스너 추가
    const replyButton = document.getElementById(`reply-button-${comment.commentId}`);
    if (replyButton) {
        replyButton.addEventListener('click', (event) => {
            const commentId = event.target.getAttribute('data-comment-id');
            const replyForm = document.getElementById(`reply-form-${commentId}`);
            if (replyForm) {
                replyForm.style.display = replyForm.style.display === 'none' ? 'block' : 'none';
            }
        });
    }

    // 대댓글 등록 버튼에 이벤트 리스너 추가
    const submitReplyButton = document.querySelector(`.submit-reply-btn[data-comment-id="${comment.commentId}"]`);
    if (submitReplyButton) {
        submitReplyButton.addEventListener('click', (event) => {
            const parentCommentId = event.target.getAttribute('data-comment-id');
            const content = document.querySelector(`#reply-form-${parentCommentId} textarea`).value;
            if (content) {
                const articleId = document.getElementById('article-id').value;
                submitComment(articleId, content, parentCommentId);  // 부모 댓글 ID와 함께 대댓글 등록
            } else {
                alert('대댓글 내용을 입력하세요.');
            }
        });
    }

    // 자식 댓글은 항상 렌더링 (부모 댓글과 상관없이)
    const childComments = allComments.filter(c => c.parentCommentId === comment.commentId);
    childComments.forEach(childComment => {
        renderCommentWithReplies(childComment, allComments, depth+1);  // 대댓글 렌더링 (들여쓰기 증가)
    });
}

// 댓글 작성 버튼 클릭 시
const commentButton = document.getElementById('comment-btn');
if (commentButton) {
    commentButton.addEventListener('click', (event) => {
        event.preventDefault();  // 기본 동작 막기
        const articleId = document.getElementById('article-id').value;
        const content = document.querySelector('textarea[name="content"]').value;

        if (content) {
            submitComment(articleId, content);  // 메인 댓글 등록
        } else {
            alert('댓글 내용을 입력해주세요.');
        }
    });
}

// 페이지 로드 시 댓글 목록 로드
window.onload = () => {
    const articleId = document.getElementById('article-id').value;
    loadComments(articleId);  // 댓글 목록 로드
};
