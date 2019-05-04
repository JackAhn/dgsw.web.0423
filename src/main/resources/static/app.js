$(function () {
    initialize();
});

let userId;

let initialize = async () =>{
    let response = $.get('http://localhost:8080/postfind/1');
    console.log(response);
}

async function setLogin(){
    $('#user-id').empty();
    $('#user-name').empty();
    $('#user-created').empty();
    try{
        let response = await $.ajax({
            type: 'GET',
            url: '/login/user1/1234',
            accept: 'application/json',
            success: function(data){
                return data;
            },
            error: function(err){
                alert('아이디나 비밀번호가 일치하지 않습니다.');
                return;
            }
        });
        userId = response.data.id;
        $('#user-id').html('사용자아이디<br>'+ response.data.account);
        $('#user-name').html('사용자 이름<br> ' + response.data.username);
        $('#user-created').html('가입일<br>' + response.data.created);
        setPostCount(response.data.id);
    }catch(err){
        alert(err);
    }
}

async function setPostCount(id){
    try{
        let response = await $.ajax({
            type: 'GET',
            url: '/postcount/'+id,
            accept: 'application/json',
            success: function(data){
                return data;
            },
            error: function(err){
                alert('아이디나 비밀번호가 일치하지 않습니다.');
                return;
            }
        });
        $('#user-count').html('게시물 수<br>'+ response.data);
    }catch(err){
        alert(err);
    }
}

function newPost(){
    $('#main-container').css("display", "none");
    $('#write-container').css("display", "block");
}

function showPost(){
    $('#main-container').css("display", "block");
    $('#write-container').css("display", "none");
    $('#headnewtitle').html('새 글쓰기');
    $('#postbtn').removeAttr('onclick');
    $('#postbtn').attr('onclick', 'createPost();');
    $('#postbtn').html('등록');
}

async function createPost(){
    let title = $('#newtitle').val();
    let text = $('#newtext').val().replace(/\n/g, "<br />");
    let data = {"userId" : userId, "title": title, "content": text};
    if(title === '' || text === ''){
        alert('공백이 존재합니다.');
        return;
    }
    let response = $.ajax({
        type: 'POST',
        url: '/postadd',
        contentType: 'application/json',
        data: JSON.stringify(data),
        success: function(data){
            alert('게시물 등록이 완료되었습니다.');
            $('#newtitle').val("");
            $('#newtext').val("");
            showPost();
            setPostCount(userId);
            fetchPost();
        },
        error: function(err){
            alert(err);
        }
    });
}

async function fetchPost(){
    try{
        $('#title-container').empty();
        let response = await $.ajax({
            type: 'GET',
            url: '/postlist',
            accept: 'application/json',
            success: function(data){
                return data;
            },
            error: function(err){
                console.log(err);
                return;
            }
        });
        let data = response.data;
        let count = 0;
        for(let i = data.length-1; i>=0; i--){
            if(count === 10)
                break;
            let comment = data[i];
            addComment(comment);
            count++;
        }
        setPost(data[data.length-1].id);
    }catch(err){
        console.log(err);
    }
}

function addComment(comment){
    $('#title-container').append(`<div id="line${comment.id}" style="color: blue; text-decoration: underline; cursor: pointer;" onclick="setPost(${comment.id});">${comment.title}</div>`);
}

let lineId;


async function setPost(id){
    try{
        $('#post-title').html('');
        $('#post-username').html('');
        $('#post-created').html('');
        $('#post-content').html('');
        lineId = id;
        let response = await $.get({
            type: 'GET',
            url: '/postfind/'+id,
            contentType: 'application/json',
            success: function(data){
                return data;
            },
            error : function(err){
                alert(err);
                return;
            }
        });
        let data = response.data;
        if(data.length === null || data.length === 0)
            return;
        $('#post-title').html(`${data.title}`);
        $('#post-username').html(`${data.username}`);
        $('#post-created').html(`${data.created}`);
        $('#post-content').html(`${data.content}`);
    }catch(err){
        console.log(err);
    }
}


function setmodifyPost(){
    newPost();
    $('#headnewtitle').html('글 수정하기');
    $('#newtitle').val($('#post-title').html());
    $('#newtext').val($('#post-content').html());
    $('#postbtn').removeAttr('onclick');
    $('#postbtn').attr('onclick', 'modifyPost();');
    $('#postbtn').html('수정');
}

async function modifyPost(){
    try{
        let title = $('#newtitle').val();
        let text = $('#newtext').val().replace(/\n/g, "<br />");
        let data = {"id" : lineId, "userId" : userId, "title": title, "content": text};
        if(title === '' || text === ''){
            alert('공백이 존재합니다.');
            return;
        }
        let response = $.ajax({
            type: 'PUT',
            url: '/postmodify',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(data){
                alert('게시물 수정이 완료되었습니다.');
                $('#newtitle').val("");
                $('#newtext').val("");
                showPost();
                setPostCount(userId);
                fetchPost();
            },
            error: function(err){
                alert(err);
            }
        });
    }catch(err){
        console.log(err);
    }
}

async function deletePost(){
    try{
        let response = $.ajax({
            type: 'DELETE',
            url: '/postdelete/'+lineId,
            contentType: 'application/json',
            success: function(data){
                alert('게시물 삭제가 완료되었습니다.');
                showPost();
                fetchPost();
                setPostCount(userId);
            },
            error: function(err){
                alert(err);
            }
        });
    }catch(err){
        console.log(err);
    }

}