$(function () {
    $('a').each(function () {
        $(this).click(function (event) {
            var href = $(this).attr('href');
            if (href.indexOf('v.qq.com') === 0) {
                event.preventDefault();
                alert('click tencent video')
            }
            if (href.indexOf('juji123.com') === 0) {
                event.preventDefault();
                href.replace('juji123.com','');
                window.location.href = '/' + href;
            }
        });
    });
});
