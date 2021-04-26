var main = {

    init : function() {

        $('#submit').on('click', function () {
            window.location.href='/search?keyword=' + $('#search').val();
        });

    }
}

main.init();