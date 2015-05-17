//document.onload = getAllUsers();

function getAllUsers() {
    $.get("http://localhost:8080/MyServlet/GetAll/", function (data, status) {
        var status = $.parseJSON(data).status;
        if (status == 200) {
            console.log(data);
            var jsonArray = $.parseJSON(data).body;
            displayBooks(jsonArray);
        } else {
            alert("Status: "+status+", Message: "+$.parseJSON(data).message);
        }
    });
}

function displayBooks(user_array) {

    var bookDivElement = document.getElementById("book-div");
    if (bookDivElement) {
        while (bookDivElement.firstChild) {
            bookDivElement.removeChild(bookDivElement.firstChild);
        }
    }
    createBookDivs(user_array);
}

function createBookDivs(user_array) {

    var myDiv = document.getElementById("displayDiv");

    var containerDiv = document.getElementById("book-div");
    if (!containerDiv) {
        containerDiv = document.createElement("div");
        var containerDivAttr = document.createAttribute("id");
        containerDivAttr.value = "book-div";
        containerDiv.setAttributeNode(containerDivAttr);
        myDiv.appendChild(containerDiv);
    }

    for (var i = 0; i < user_array.length; i++) {

        var user = user_array[i];

        var bookDiv = document.createElement("div");
        bookDiv.className = "col-lg-3 col-md-4 col-xs-6 thumb tooltip_link";

        var dataToggleAttr = document.createAttribute("data-toggle");
        dataToggleAttr.value = "tooltip";
        bookDiv.setAttributeNode(dataToggleAttr);

        var dataOriginalTitleAttr = document.createAttribute("data-original-title");
        dataOriginalTitleAttr.value = user["name"];
        bookDiv.setAttributeNode(dataOriginalTitleAttr);

        //=================================================================================
        var thumbnailA = document.createElement("a");
        thumbnailA.className = "thumbnail";

        var dataToggleAttrA = document.createAttribute("data-toggle");
        dataToggleAttrA.value = "modal";
        thumbnailA.setAttributeNode(dataToggleAttrA);

        var dataTarget = document.createAttribute("data-target");
        dataTarget.value = "#myModal_" + user["username"];
        thumbnailA.setAttributeNode(dataTarget);

        bookDiv.appendChild(thumbnailA);
        //=================================================================================

        //=================================================================================
        var coverImg = document.createElement("img");
        coverImg.className = "img-responsive";

        var imgSrc = document.createAttribute("src");
        imgSrc.value = user["username"]+".png";
        coverImg.setAttributeNode(imgSrc);


        var idElement = document.createAttribute("id");
        idElement.value = "coverImage_" + user["username"];
        coverImg.setAttributeNode(idElement);

        thumbnailA.appendChild(coverImg);
        //=================================================================================

        containerDiv.appendChild(bookDiv);
    }

//    createBookDetailModal();
}