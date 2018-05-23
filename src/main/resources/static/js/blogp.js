var app = {};
app.clickTimes = 0;
app.totalPage = 1;
app.currentPage = 1;

app.timelinePageSize = 4;
app.homeCardPageSize = 6;

// first time to init the home card
app.firstTimeInit = true;

app.initBlogCardList = function() {
    app.getBlogListData(app.currentPage, app.homeCardPageSize, app.loadHomeCard, true);
};

app.loadHomeCard = function (data) {
    app.totalPage = data.totalPage;
    var list = data.list;
    
    var cardDeck_1 = '<div class="card-deck wow fadeInUp" data-wow-duration="1s" data-wow-delay="0.5s">';
    var cardDeck_2 = '<div class="card-deck wow fadeInUp" data-wow-duration="1s" data-wow-delay="0.5s">';
    for (var index = 1; index <= list.length; index++) {
        var blog = data.list[index - 1];
        var tags = app.generateTags(blog.tags);
        
        var card = '<div class="card"><img class="card-img-top" src="/api/v1/file/' + blog.image + '" alt="Card image cap">' +
                '<div class="card-body"><a href="/post/' + blog.path + '"><h5 class="card-title">' + blog.title + '</h5></a>' +
                '<div class="card-tag">' + tags + '</div>' +
                '<p class="card-text card-desc">' + blog.description + '</p>' + 
                '<p class="card-text">' + 
                '<small class="text-muted">' + blog.createDay + '</small><a href="/post/' + blog.path + '" ' +
                'style="float:right;">详情<span></span></a></p></div></div>';

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
    var jumpTO = $(homeCard).offset().top;
    $("html,body").animate({scrollTop:jumpTO},500);
};

app.generateTags = function (tags) {
    var tagPrefix = '<embed src="/images/svg/tag.svg" width="20" height="20" type="image/svg+xml"></embed>&nbsp;<small class="text-muted">';
    var tagSuffix = '</small>';
    if (tags.length <= 0) {
        return '';
    }
    var str = "";
    for(var t = 0; t < tags.length; t++) {
        str += tagPrefix + tags[t] + tagSuffix + " ";
    }
    return str
};

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
            // update browser url
            var originUrl = window.location.origin;
            window.history.pushState({}, 0, originUrl + '/page/' + page);
        }
        app.getBlogListData(app.currentPage, app.homeCardPageSize, app.loadHomeCard, true);
    });
};

app.getBlogListData = function (currentPage, pageSize, successFn, isAsync) {
    var search = window.location.search;
    var filter = null;
    if (search !== null && search.length > 0 && search.indexOf("=") !== -1) {
        filter = search.substr(search.indexOf("=") + 1, search.length);
    }
    $.ajax({
        type: "GET",
        url: "/api/v1/manager/list",
        async: isAsync,
        data: {
            currentPage: currentPage,
            pageSize: pageSize,
            filter: filter
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

app.uploadImage = function (blogId, file) {
    if (blogId === null || file === null || file === undefined) {
        return null;
    }
    var image = null;
    var formData = new FormData();
    formData.append('file', file);
    $.ajax({
        type: "PUT",
        url: "/api/v1/file/upload?blogId=" + blogId,
        data: formData,
        dataType:"json",
        async: false,
        cache: false,
        processData: false,
        contentType: false,
        success: function (data, status) {
            if (data.success) {
                image = data.data;
            }
        }
    });
    return image;
};

app.blogSubmitClick = function () {
    $('#blogSubmit').bind('click', function () {
        var blogId = $('#blogId').length > 0 ? $('#blogId').val() : null;
        var fileObj = $('#blog-image').prop('files');
        var image = null;
        if (fileObj.length > 0) {
            image = app.uploadImage(blogId, fileObj[0]);
            image = blogId + '/' + image;
        }
        $.ajax({
            type: "POST",
            url: "/api/v1/manager/save",
            async: false,
            datatype: "json",
            contentType: "application/json",
            data: JSON.stringify({
                title: $('#blog-title').val(),
                description: $('#blog-desc').val(),
                content: $('#editormd').val(),
                userId: $('#userId').val(),
                tags: $('#blog-tags').val().split(","),
                image: image,
                blogId: blogId
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

    app.getBlogListData(app.currentPage, app.timelinePageSize, app.loadTimeline, false);
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
            app.getBlogListData(app.currentPage, app.timelinePageSize, app.loadTimeline, false);
        }
    });
};

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
            $('.timeline').append('<h3 class="timeline-end">&lt;完&gt;</h3>');
        }
    }
};

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

app.removeLoading = function () {
    $('.loader').fadeOut();
};

app.init = function() {
    var pathname = window.location.pathname;
    if (pathname === null || pathname === undefined || pathname === '/') {
        $('#nav-home').addClass("active");
    } else
    if (pathname.indexOf('about') !== -1) {
        $('#nav-aboutme').addClass("active");
    } else if (pathname.indexOf('timeline') !== -1) {
        $('#nav-timeline').addClass("active");
    } else if (pathname.indexOf('management') !== -1) {
        $('#nav-manageDropdown').addClass("active");
    } else {
        var path = pathname.substr(pathname.lastIndexOf('/') + 1, pathname.length);
        if (!isNaN(path) && path !== null && path !== '') {
            app.currentPage = parseInt(path);
            $('#nav-home').addClass("active");
        }
    }

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

    setTimeout("app.removeLoading()", 1000);

    new WOW().init();
});
