    const defaultArticlePaginationSize = 6;

    const getScrollTop = function() {
        return (window.pageYOffset !== undefined) ? window.pageYOffset : (document.documentElement || document.body.parentNode || document.body).scrollTop;
    };

    const getDocumentHeight = function () {
        const body = document.body;
        const html = document.documentElement;

        return Math.max(
            body.scrollHeight, body.offsetHeight,
            html.clientHeight, html.scrollHeight, html.offsetHeight
        );
    };

    const onscroll = function () {
        if (getScrollTop() === getDocumentHeight() - window.innerHeight) {
            const petSitterCard = document.querySelectorAll('.petSitter-card');

            const lastArticleId = Array.from(petSitterCard).map(function (card) {
                const sitterNo = card.getAttribute('data-sitterno');
                return parseLong(sitterNo, 10);
            }).reduce(function (previous, current) {
                return previous > current ? current : previous;
            });

            // mainBoardService.getListInfinite(lastArticleId, defaultArticlePaginationSize);
            // Ajax 로직 실행
            // 위에서 주석 처리된 코드는 서버에서 데이터를 가져와서 추가하는 로직을 포함해야 합니다.
        }
    };

//Ajax를 이용하여 특정 페이지 데이터 요청
function scroll() {
    $.ajax({
        url: '/api/list',
        type: 'GET',
        data: {
            lastArticleId: ,
            size: size,
        },
        success: function (data) {
            data.forEach(function (json) {
                // 가져온 게시물 데이터를 DOM에 그리는 작업
            });
        },
        error: function (xhr, status, error) {
            // 에러 처리
            console.error(error);
        }
    });
}