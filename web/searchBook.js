function searchBook(isWishlist) {

    var searchBar = document.getElementById("srch-term");

    var searchTerm = searchBar.value;
    
//    if (searchTerm == ''){
//        $.get("http://localhost:8080/MyServlet/GetAll/", function (data, status) {
//        var status = $.parseJSON(data).status;
//        if (status == 200) {
//            console.log(data);
//            var jsonArray = $.parseJSON(data).body;
//            displayBooks(jsonArray);
//            displayBookDetailModal(jsonArray);
//        } else {
//            alert("Status: "+status+", Message: "+$.parseJSON(data).message);
//        }
//    });
//    } else {
        $.get("http://localhost:8080/MyServlet/FilterUsers/"+searchTerm, function (data, status) {
        var status = $.parseJSON(data).status;
        if (status == 200) {
            console.log(data);
            var jsonArray = $.parseJSON(data).body;
            displayBooks(jsonArray);
            displayBookDetailModal(jsonArray);
        } else {
            alert("Status: "+status+", Message: "+$.parseJSON(data).message);
        }
    });
//    }

}