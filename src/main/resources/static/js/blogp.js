$(document).ready(function () {
    if ($('.editormd').length > 0) {
        initEditormd();
    }

    var clickTimes = 0;

    cleanClickTimes = function () {
        clickTimes = 0;
    };

    $('.navbar').click(function () {
        setTimeout(cleanClickTimes, 5000);
        clickTimes = parseInt(clickTimes) + 1;
        console.log(clickTimes);
        if (clickTimes == '5') {
            $('.login-btn').fadeIn('slow');
        }
    });

    $('#lBtn').click(function () {
        $('#lError').empty();
        $.ajax({
            type: "POST",
            url: "api/v1/user/login",
            datatype: "json",
            contentType: "application/json",
            data: JSON.stringify({
                username: $('#lUsername').val(),
                password: $('#lPassword').val()
            }),
            success: function (data, status) {
                if (data.code == "1") {
                    location.reload();
                } else {
                    var errorMsg = "<p style=\"color:red;\">" + data.msg + "</p>";
                    $('#lError').append(errorMsg);
                }
            }
        });
    });

    $('#blogPreview').click(function () {
        $(".fa-desktop[name='preview']").parent().trigger('click');
    });

    $('#blogSubmit').click(function () {
        $.ajax({
            type: "POST",
            url: "api/v1/manager/save",
            datatype: "json",
            contentType: "application/json",
            data: JSON.stringify({
                title: $('.blog-title').val(),
                description: $('.blog-desc').val(),
                content: $('#editorHtml').val(),
                userId: $('#userId').val()
            }),
            success: function (data, status) {
                if (data.code == "1") {
                    location.reload();
                } else {
                    var errorMsg = "<p style=\"color:red;\">" + data.msg + "</p>";
                    $('#bError').append(errorMsg);
                }
            }
        });
    });
});

function initEditormd() {
    var editormdContainer = $(function () {
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
}