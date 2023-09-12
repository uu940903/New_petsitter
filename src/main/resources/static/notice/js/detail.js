$$(document).ready(function () {
    $("#deleteButton").click(function (e) {
        e.preventDefault();

        var noNo = $(this).data("no"); // 데이터 속성에서 값을 가져옵니다.

        if (confirm("삭제하시겠습니까?")) {
            $.ajax({
                type: "POST",
                url: "/notice/delete/" + noNo,
                data: {
                    _method: "delete"
                },
                success: function (response) {
                    // 성공적으로 삭제되었을 때 할 작업을 여기에 추가하세요.
                    alert("삭제되었습니다.");
                    // 페이지 리로드 또는 필요한 작업 수행
                    location.reload();
                },
                error: function (err) {
                    // 삭제 중 오류 발생 시 처리할 작업을 여기에 추가하세요.
                    alert("삭제 중 오류가 발생했습니다.");
                }
            });
        }
    });
});