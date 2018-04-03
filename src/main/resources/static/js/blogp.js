var app = {};
app.clickTimes = 0;
// app.totalPage = [[${totalPage}]] || 1;

// app.intBlogList = function() {
//     var totalPage = 0;
//     var blogList = app.getBlogList(1, 6);
//
//     var firstGroup = '<div class="card-deck wow fadeInRight">';
//     var lastGroup = '<div class="card-deck wow fadeInLeft">';
//     if (blogList.length > 0) {
//         totalPage = blogList.total;
//         for (var index=1; index<=blog in blogList) {
//
//         }
//         this.generatePagination(blogList.currentPage, totalPage);
//     }
// };

app.generatePagination = function (currentPage, totalPage) {
    var items = [];

    var perviousItem =  [
        '<li class="page-item disabled pervious">',
        '<a class="page-link" href="#" tabindex="-1">Previous</a>',
        '</li>'
    ].join('');

    var nextItem = [
        '<li class="page-item">',
        '<a class="page-link" href="#">Next</a>',
        '</li>'
    ].join('');

    items.push(perviousItem);
    for(var index=1; index<=totalPage; index++) {
        var pageItem = [
            '<li class="page-item">',
            index === currentPage ? '<a class="page-link" href="/">'+ index +'</a>' : '<a class="page-link" href="/">'+ index +'</a>',
            '</li>'
        ].join('');
        items.push(pageItem);
    }
    items.push(nextItem);
    $(".row .pagination").append(items.join(''));
};

app.getBlogList = function (currentPage, pageSize) {
    var blogList = [];
    $.ajax({
        type: "GET",
        url: "/api/v1/manager/list",
        data: {
            currentPage: currentPage,
            pageSize: pageSize
        },
        success: function (data, status) {
            if (data.success) {
                blogList = data.data;
            } else {
                var errorMsg = "<p style=\"color:red;\">" + data.msg + "</p>";
                alert(errorMsg);
            }
        }
    });
    return blogList;
};

app.loginBtnDisplay = function () {
    $('.navbar').bind('click', function () {
        setTimeout(app.cleanClickTimes, 5000);
        app.clickTimes = parseInt(app.clickTimes) + 1;
        console.log(app.clickTimes);
        if (app.clickTimes === 5) {
            $('.login-btn').fadeIn('slow');
        }
    });
};

app.cleanClickTimes = function () {
    app.clickTimes = 0;
};

app.loginClickBtn = function () {
    $('#lBtn').bind('click', function () {
        $('#lError').empty();
        $.ajax({
            type: "POST",
            url: "/api/v1/user/login",
            datatype: "json",
            contentType: "application/json",
            data: JSON.stringify({
                username: $('#lUsername').val(),
                password: $('#lPassword').val()
            }),
            success: function (data, status) {
                if (data.success) {
                    location.reload();
                } else {
                    var errorMsg = "<p style=\"color:red;\">" + data.msg + "</p>";
                    $('#lError').append(errorMsg);
                }
            }
        });
    });
};

app.blogPreview = function () {
    $('#blogPreview').bind('click', function () {
        $(".fa-desktop[name='preview']").parent().trigger('click');
    });
};

app.blogSubmitClick = function () {
    $('#blogSubmit').bind('click', function () {
        $.ajax({
            type: "POST",
            url: "/api/v1/manager/save",
            datatype: "json",
            contentType: "application/json",
            data: JSON.stringify({
                title: $('#blog-title').val(),
                description: $('#blog-desc').val(),
                content: $('#editorHtml').val(),
                userId: $('#userId').val(),
                tags: $('#blog-tags').val().split(",")
            }),
            success: function (data, status) {
                if (data.success) {
                    location.reload();
                } else {
                    var errorMsg = "<p style=\"color:red;\">" + data.msg + "</p>";
                    $('#bError').append(errorMsg);
                }
            }
        });
    });
};

app.initEditormd = function() {
    $(function () {
        editormd("editormd-container", {
            width: "100%",
            height: 660,
            //markdown : md,
            codeFold: true,
            syncScrolling: "single",
            //你的lib目录的路径
            path: "/plugin/editormd/lib/",
            imageUpload: true, //关闭图片上传功能
            /*  theme: "dark",//工具栏主题
             previewTheme: "dark",//预览主题
             editorTheme: "pastel-on-dark",//编辑主题 */
            emoji: true,
            taskList: true,
            tocm: true, // Using [TOCM]
            tex: true, // 开启科学公式TeX语言支持，默认关闭
            flowChart: true, // 开启流程图支持，默认关闭
            sequenceDiagram: true, // 开启时序/序列图支持，默认关闭,
            //这个配置在simple.html中并没有，但是为了能够提交表单，使用这个配置可以让构造出来的HTML代码直接在第二个隐藏的textarea域中，方便post提交表单。
            saveHTMLToTextarea: true,
            toolbarIcons: function () {
                // return editormd.toolbarModes['simple']; // full, simple, mini Using "||" set
                // icons align right.
                return [
                    "undo",
                    "redo",
                    "|",
                    "bold",
                    "del",
                    "italic",
                    "quote",
                    "ucwords",
                    "uppercase",
                    "lowercase",
                    "|",
                    "h1",
                    "h2",
                    "h3",
                    "h4",
                    "h5",
                    "h6",
                    "|",
                    "list-ul",
                    "list-ol",
                    "hr",
                    "|",
                    "link",
                    "reference-link",
                    "image",
                    "code",
                    "preformatted-text",
                    "code-block",
                    "table",
                    "datetime",
                    "emoji",
                    "html-entities",
                    "pagebreak",
                    "|",
                    "watch",
                    "preview",
                    "clear",
                    "search",
                    "|",
                    "info"
                ];
            }
        });
    });
};

app.init = function() {
    if ($('.editormd').length > 0) {
        app.initEditormd();
        app.blogPreview();
        app.blogSubmitClick();
    }

    app.loginBtnDisplay();
    app.loginClickBtn();
};

$(function () {
    app.init();
});
