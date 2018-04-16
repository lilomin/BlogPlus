var app = {};
app.clickTimes = 0;
app.totalPage = 1;
app.currentPage = 1;

app.timelinePageSize = 4;
app.homeCardPageSize = 6;

// first time to init the home card
app.firstTimeInit = true;

app.initBlogCardList = function() {
    app.getBlogListData(app.currentPage, app.homeCardPageSize, app.loadHomeCard);
};

app.loadHomeCard = function (data) {
    app.totalPage = data.totalPage;
    var list = data.list;
    
    var cardDeck_1 = '<div class="card-deck wow fadeInUp">';
    var cardDeck_2 = '<div class="card-deck wow fadeInUp">';
    for (var index = 1; index <= list.length; index++) {
        var blog = data.list[index - 1];
        
        var card = '<div class="card"><img class="card-img-top" src="' + blog.image + '" alt="Card image cap">' +
                '<div class="card-body"><h5 class="card-title">' + blog.title + '</h5>' + 
                '<p class="card-text card-desc">' + blog.description + '</p>' + 
                '<p class="card-text">' + 
                '<small class="text-muted">' + blog.createDay + '</small><a href="post/' + blog.path + '" ' +
                'style="float:right;">详情<span>>></span></a></p></div></div>';

        if (index <= app.homeCardPageSize / 2) {
            cardDeck_1 += card;
        } else {
            cardDeck_2 += card;
        }
    }
    cardDeck_1 += '</div>';
    cardDeck_2 += '</div>';
    var homeCard = $('#home-card');
    $(homeCard).empty();
    $(homeCard).append(cardDeck_1);
    if (list.length > app.homeCardPageSize / 2) {
        $(homeCard).append(cardDeck_2);
    }

    $('.row .pagination').empty();
    app.generatePagination();

    if (app.firstTimeInit) {
        app.firstTimeInit = false;
        return;
    }
    var jumpTO = $('#home-card').offset().top;
    $("html,body").animate({scrollTop:jumpTO},500);
}

app.generatePagination = function () {
    var items = [];

    var perviousItem =  [
        app.currentPage === 1 ? '<li class="page-item disabled pervious">' : '<li class="page-item pervious">',
        '<a id="-1" class="page-link" href="javascript:;" aria-label="Previous">' +
        '<span aria-hidden="true">&laquo;</span>' +
        '<span class="sr-only">Previous</span></a>',
        '</li>'
    ].join('');

    var nextItem = [
        app.currentPage === app.totalPage ? '<li class="page-item disabled">' : '<li class="page-item">',
        '<a id="0" class="page-link" href="javascript:;" aria-label="Next">' +
        '<span aria-hidden="true">&raquo;</span>' +
        '<span class="sr-only">Next</span></a>',
        '</li>'
    ].join('');

    items.push(perviousItem);
    for (var index = 1; index <= app.totalPage; index++) {
        var pageItem = [
            index === app.currentPage ? '<li class="page-item active">' : '<li class="page-item">',
            '<a id="' + index + '" class="page-link" href="javascript:;">' + index + '</a>',
            '</li>'
        ].join('');
        items.push(pageItem);
    }
    items.push(nextItem);
    $(".row .pagination").append(items.join(''));
    app.bindPagination();
};

app.bindPagination = function () {
    var pageLinks = $('.pagination .page-item .page-link');
    if (!pageLinks || !pageLinks.length) 
        return;

    $(pageLinks).unbind('click').bind('click', function () {
        var page = Number($(this).attr("id"));
        if (page === 0) {
            // Next
            app.currentPage++;
        } else
        if (page === -1) {
            // Pervious
            app.currentPage--;
        } else {
            app.currentPage = page;
        }
        app.getBlogListData(app.currentPage, app.homeCardPageSize, app.loadHomeCard);
    });
}

app.getBlogListData = function (currentPage, pageSize, successFn) {
    $.ajax({
        type: "GET",
        url: "/api/v1/manager/list",
        data: {
            currentPage: currentPage,
            pageSize: pageSize
        },
        success: function (data, status) {
            if (data.success) {
                successFn(data.data);
            } else {
                var errorMsg = "<p style=\"color:red;\">" + data.msg + "</p>";
                alert(errorMsg);
            }
        }
    });
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
                content: $('#editormd').val(),
                userId: $('#userId').val(),
                tags: $('#blog-tags').val().split(","),
                blogId: $('#blogId').length > 0 ? $('#blogId').val() : null
            }),
            success: function (data, status) {
                if (data.success) {
                    window.location.href="/management/1";
                } else {
                    var errorMsg = "<p style=\"color:red;\">" + data.msg + "</p>";
                    $('#bError').append(errorMsg);
                }
            }
        });
    });
};

app.scrollBefore = 0;
app.initTimeLine = function() {
    var load = false;

    app.getBlogListData(app.currentPage, app.timelinePageSize, app.loadTimeline);
    $(window).bind('scroll', function () {
        var windowTop = $(window).scrollTop();
        var lastTop = $(".time-card-wrapper:last").children(".time-card:last-child").offset().top;
        if (windowTop > app.scrollBefore && lastTop >= windowTop && lastTop < (windowTop + $(window).height())) {
            load = true;
            app.scrollBefore = windowTop;
        }
        if (!load) {
            return;
        }
        if (app.totalPage > app.currentPage) {
            app.currentPage = app.currentPage + 1;
            console.log(app.currentPage);
            app.getBlogListData(app.currentPage, app.timelinePageSize, app.loadTimeline);
        }
    });
}

app.loadTimeline = function (data) {
    app.totalPage = data.totalPage;
    var list = data.list;
    var cardWrapper = '<div class="time-card-wrapper wow fadeInUp" data-wow-duration="1s" data-wow-delay="0.5s">';
    if (list.length <= 2) {
        cardWrapper = '<div class="time-card-wrapper wow fadeInUp" data-wow-duration="1s" data-wow-delay="0.5s" style="height:510px;">';
    }
    for (var index = 1; index <= list.length; index++) {
        var blog = data.list[index - 1];
        var num = app.timelinePageSize * (data.currentPage - 1) + index;
        var strNum = num < 10 ? '0' + num : num;
        var card = '<div class="time-card time-card-step' + index + '"><div class="head"><div class="number-box">' +
                '<span>' + strNum + '</span></div><h2><span class=' +
                '"small">' + blog.createDay + '</span><a href="post/' + blog.path + '">' + blog.title + '</a></h2></div><div class="body">' +
                '<p>' + blog.description + '</p>' + 
                // '<img src="' + blog.image + '" alt="Graphic">' +
                '</div></div>';
        cardWrapper = cardWrapper + card;
    }
    cardWrapper = cardWrapper + '</div>';
    $('.timeline').append(cardWrapper);

    if (data.currentPage >= app.totalPage) {
        if ($('.timeline .timeline-end').length <= 0) {
            $('.timeline').append('<h1 class="timeline-end">没有啦~</h1>');
        }
    }
}

app.blogSwitchClick = function () {
    $('[id=blogSwitch]').bind('click', function () {
        $.ajax({
            type: "GET",
            url: "/api/v1/manager/switch",
            data: {
                blogId: $(this).parent().parent().children("th").attr("id"),
            },
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

app.blogEditClick = function () {
    $('[id=blogEdit]').bind('click', function () {
        var blogId = $(this).parent().parent().children("th").attr("id");
        window.location.href='/edit_post?blogId=' + blogId;
    });
};

app.initEditormd = function() {
    $(function () {
        editormd("editormd-container", {
            width: "100%",
            height: 760,
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

app.loading = function () {
    var loadingHtml = '<div class="loader"><div class="loader-inner"><div class="loader-line-wrap"><div' +
            ' class="loader-line"></div></div><div class="loader-line-wrap"><div class="loade' +
            'r-line"></div></div><div class="loader-line-wrap"><div class="loader-line"></div' +
            '></div><div class="loader-line-wrap"><div class="loader-line"></div></div><div c' +
            'lass="loader-line-wrap"><div class="loader-line"></div></div></div>';

    $('html').append(loadingHtml).fadeIn();
}

app.removeLoading = function () {
    $('.loader').fadeOut();
}

app.init = function() {
    app.loading();

    app.blogEditClick();
    app.blogSwitchClick();

    if ($('#home-card').length > 0) {
        app.initBlogCardList();
    }

    if ($('.editormd').length > 0) {
        app.initEditormd();
        app.blogPreview();
        app.blogSubmitClick();
    }

    if ($(".timeline").length > 0) {
        app.initTimeLine();
    }

    app.loginBtnDisplay();
    app.loginClickBtn();
};

$(function () {
    app.init();

    setTimeout("app.removeLoading()", 500);

    new WOW().init();
});
