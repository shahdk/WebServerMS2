function createBookDetailModal() {
    $.get("http://localhost:8080/MyServlet/GetAll/", function (data, status) {
        var status = $.parseJSON(data).status;
        if (status == 200) {
            var jsonArray = $.parseJSON(data).body;
            displayBookDetailModal(jsonArray);
        }
    });
}

function displayBookDetailModal(usersList) {
    for (var i = 0; i < usersList.length; i++) {
        var user = usersList[i];
        
        var username = user['username'];
        var name = user['name'];
        var hobbies = user['hobbies'];

        var myDiv = document.getElementById("displayDiv");

        //=================================================================================
        var bookDetailModalDiv = document.createElement("div");
        setBookDetailModalDivAttributes(bookDetailModalDiv, username);
        myDiv.appendChild(bookDetailModalDiv);
        //=================================================================================

        //=================================================================================
        var myModalDiv = document.createElement("div");
        setMyModalDivAttributesForDetailModal(myModalDiv);
        bookDetailModalDiv.appendChild(myModalDiv);
        //=================================================================================

        //=================================================================================
        var modalWrapperDiv = document.createElement("div");
        setModalWrapperDivAttributesForDetailModal(modalWrapperDiv);
        myModalDiv.appendChild(modalWrapperDiv);
        //=================================================================================

        //=================================================================================
        var linkBoxTopDiv = document.createElement("div");
        setLinkBoxTopDivAttributesForDetailModal(linkBoxTopDiv);
        modalWrapperDiv.appendChild(linkBoxTopDiv);
        //=================================================================================

        //=================================================================================
        var bookCoverDiv = document.createElement("div");
        setBookCoverDivAttributesForDetailModal(bookCoverDiv);
        linkBoxTopDiv.appendChild(bookCoverDiv);
        //=================================================================================

        //=================================================================================
        var bookCoverImage = document.createElement("img");
        setBookCoverImageAttributesForDetailModal(bookCoverImage, username+".png");
        bookCoverDiv.appendChild(bookCoverImage);
        //=================================================================================

        //=================================================================================
        var bookHeaderDiv = document.createElement("div");
        setBookHeaderDivAttributesForDetailModal(bookHeaderDiv);
        linkBoxTopDiv.appendChild(bookHeaderDiv);
        //=================================================================================

        //=================================================================================
        var headerTwo = document.createElement("h2");
        headerTwo.innerHTML = "User Info";
        bookHeaderDiv.appendChild(headerTwo);
        //=================================================================================

        //=================================================================================
        var descriptionBoxDiv = document.createElement("div");
        descriptionBoxDiv.className = "description-box";
        modalWrapperDiv.appendChild(descriptionBoxDiv);
        //=================================================================================

        //=================================================================================
        var bookInfoTable = document.createElement("table");
        descriptionBoxDiv.appendChild(bookInfoTable);
        //=================================================================================

        //=================================================================================
        var isbnTr = document.createElement("tr");
        bookInfoTable.appendChild(isbnTr);
        //=================================================================================

        //=================================================================================
        var isbnTrLabelTd = document.createElement("td");
        setLabelTdAttributesForDetailModal(isbnTrLabelTd, "Username: ");
        isbnTr.appendChild(isbnTrLabelTd);
        //=================================================================================

        //=================================================================================
        var isbnTrInputTd = document.createElement("td");
        setTdAttributesForDetailModal(isbnTrInputTd, "isbnInput_" + username, username);
        isbnTr.appendChild(isbnTrInputTd);
        //=================================================================================

        //=================================================================================
        var publishDateTr = document.createElement("tr");
        bookInfoTable.appendChild(publishDateTr);
        //=================================================================================

        //=================================================================================
        var publishDateTrLabelTd = document.createElement("td");
        setLabelTdAttributesForDetailModal(publishDateTrLabelTd, "Name: ");
        publishDateTr.appendChild(publishDateTrLabelTd);
        //=================================================================================

        //=================================================================================
        var publishDateTrInputTd = document.createElement("td");
        setTdAttributesForDetailModal(publishDateTrInputTd, "publishedYearInput_" + username, name);
        publishDateTr.appendChild(publishDateTrInputTd);
        //=================================================================================

        //=================================================================================
        var totalPagesTr = document.createElement("tr");
        bookInfoTable.appendChild(totalPagesTr);
        //=================================================================================

        //=================================================================================
        var totalPagesTrLabelTd = document.createElement("td");
        setLabelTdAttributesForDetailModal(totalPagesTrLabelTd, "Hobbies: ");
        totalPagesTr.appendChild(totalPagesTrLabelTd);
        //=================================================================================

        //=================================================================================
        var totalPagesTrInputTd = document.createElement("td");
        totalPagesTr.appendChild(totalPagesTrInputTd);
        //=================================================================================

        //=================================================================================
        var totalPagesInput = document.createElement("input");
        setTdInputAttributes(totalPagesInput, "totalPagesInput_" + username, hobbies);
        totalPagesTrInputTd.appendChild(totalPagesInput);
        //=================================================================================

        //=================================================================================
        var linkBoxDiv = document.createElement("div");
        linkBoxDiv.className = "link-box";
        modalWrapperDiv.appendChild(linkBoxDiv);
        //=================================================================================

        //=================================================================================
        var saveChangesButton = document.createElement("button");
        setSaveButtonAttributesForDetailModal(saveChangesButton, username);
        linkBoxDiv.appendChild(saveChangesButton);
        //=================================================================================

        //=================================================================================
        var deleteButton = document.createElement("button");
        setDeleteButtonAttributesForDetailModal(deleteButton, username);
        linkBoxDiv.appendChild(deleteButton);
        //=================================================================================
    }
}

function setBookDetailModalDivAttributes(bookDetailModalDiv, isbn) {
    bookDetailModalDiv.className = "modal fade";

    var addBookIDAttr = document.createAttribute("id");
    addBookIDAttr.value = "myModal_" + isbn;
    bookDetailModalDiv.setAttributeNode(addBookIDAttr);

    var tabIndexAttr = document.createAttribute("tabindex");
    tabIndexAttr.value = "-1";
    bookDetailModalDiv.setAttributeNode(tabIndexAttr);

    var roleAttr = document.createAttribute("role");
    roleAttr.value = "dialog";
    bookDetailModalDiv.setAttributeNode(roleAttr);

    var ariaLabelAttr = document.createAttribute("aria-labelledby");
    ariaLabelAttr.value = "myModalLabel";
    bookDetailModalDiv.setAttributeNode(ariaLabelAttr);

    var ariaHiddenAttr = document.createAttribute("aria-hidden");
    ariaLabelAttr.value = "true";
    bookDetailModalDiv.setAttributeNode(ariaLabelAttr);
}

function setMyModalDivAttributesForDetailModal(myModalDiv) {
    myModalDiv.className = "popup-modal modal-dialog modal-vertical-centered";
}

function setModalWrapperDivAttributesForDetailModal(modalWrapperDiv) {
    var modalWrapperIdAttr = document.createAttribute("id");
    modalWrapperIdAttr.value = "modal-wrapper-div";
    modalWrapperDiv.setAttributeNode(modalWrapperIdAttr);
}

function setLinkBoxTopDivAttributesForDetailModal(linkBoxTopDiv) {
    var linkBoxTopIdAttr = document.createAttribute("id");
    linkBoxTopIdAttr.value = "link-box-top";
    linkBoxTopDiv.setAttributeNode(linkBoxTopIdAttr);
}

function setBookCoverDivAttributesForDetailModal(bookCoverDiv) {
    var bookCoverIdAttr = document.createAttribute("id");
    bookCoverIdAttr.value = "book-cover";
    bookCoverDiv.setAttributeNode(bookCoverIdAttr);
}

function setBookCoverImageAttributesForDetailModal(bookCoverImage, url) {
    var bookCoverImgAttr = document.createAttribute("src");
    bookCoverImgAttr.value = url;
    bookCoverImage.setAttributeNode(bookCoverImgAttr);
}

function setBookHeaderDivAttributesForDetailModal(bookHeaderDiv) {
    var bookheaderIdAttr = document.createAttribute("id");
    bookheaderIdAttr.value = "book-header";
    bookHeaderDiv.setAttributeNode(bookheaderIdAttr);
}

function setLabelTdAttributesForDetailModal(labelTd, innerText) {
    labelTd.innerHTML = innerText;
}

function setSaveButtonAttributesForDetailModal(buttonElement, isbn) {
    buttonElement.className = "save-flat-btn";
    buttonElement.innerHTML = "Save Changes";


    var buttonDataDismissAttr = document.createAttribute("data-dismiss");
    buttonDataDismissAttr.value = "modal";
    buttonElement.setAttributeNode(buttonDataDismissAttr);

    var buttonTypeAttr = document.createAttribute("type");
    buttonTypeAttr.value = "button";
    buttonElement.setAttributeNode(buttonTypeAttr);

    var buttonNameAttr = document.createAttribute("id");
    buttonNameAttr.value = "save_" + isbn;
    buttonElement.setAttributeNode(buttonNameAttr);

    var buttonOnClickAttr = document.createAttribute("onclick");
    buttonOnClickAttr.value = "saveChanges('"+isbn+"');";
    buttonElement.setAttributeNode(buttonOnClickAttr);
}

function setDeleteButtonAttributesForDetailModal(buttonElement, isbn) {
    buttonElement.className = "delete-flat-btn";
    buttonElement.innerHTML = "Delete User";

    var buttonDataDismissAttr = document.createAttribute("data-dismiss");
    buttonDataDismissAttr.value = "modal";
    buttonElement.setAttributeNode(buttonDataDismissAttr);

    var buttonTypeAttr = document.createAttribute("type");
    buttonTypeAttr.value = "button";
    buttonElement.setAttributeNode(buttonTypeAttr);

    var buttonNameAttr = document.createAttribute("id");
    buttonNameAttr.value = "delete_" + isbn;
    buttonElement.setAttributeNode(buttonNameAttr);

    var buttonOnClickAttr = document.createAttribute("onclick");
    buttonOnClickAttr.value = "deleteBook('" + isbn + "');";
    buttonElement.setAttributeNode(buttonOnClickAttr);
}

function setTdAttributesForDetailModal(tdElement, id, innerText) {
    tdElement.innerHTML = innerText;

    var inputIdAttr = document.createAttribute("id");
    inputIdAttr.value = id;
    tdElement.setAttributeNode(inputIdAttr);
}

function setTdInputAttributes(inputElement, id, val) {
    inputElement.className = "editInput";

    var inputIdAttr = document.createAttribute("id");
    inputIdAttr.value = id;
    inputElement.setAttributeNode(inputIdAttr);

    var inputTypeAttr = document.createAttribute("type");
    inputTypeAttr.value = "text";
    inputElement.setAttributeNode(inputTypeAttr);

    var inputValTypeAttr = document.createAttribute("value");
    inputValTypeAttr.value = val;
    inputElement.setAttributeNode(inputValTypeAttr);
}

function saveChanges(username) {
    var hobbies = document.getElementById("totalPagesInput_"+username).value; 
    var data = JSON.stringify({"username":username,"hobbies":hobbies});
    $.ajax({
        url: "http://localhost:8080/MyServlet/PutHobbies/",
        type: 'PUT',
        data: data,
        dataType: 'json',
        success: function (result) {
            var status  = result.status;
            var message = result.message;
            alert("Status: "+status+", Message: "+message);
        }
    });
}

function deleteBook(user) {

    $.ajax({
        url: "http://localhost:8080/MyServlet/DeleteUser/"+user,
        type: 'DELETE',
        success: function (result) {
            var status  = $.parseJSON(result).status;
            var message = $.parseJSON(result).message;
            if (status == 200){
                alert(message);
                var jsonArray = $.parseJSON(result).body;
                console.log(jsonArray);
                displayBooks(jsonArray);
                displayBookDetailModal(jsonArray);
            } else {
                alert("Status: "+status+", Message: "+message);
            }
        }
    });
    
    

}