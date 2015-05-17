function createAddBookModal() {
    var myDiv = document.getElementById("displayDiv");

    //=================================================================================
    var addBookModalDiv = document.createElement("div");
    setAddBookModalDivAttributes(addBookModalDiv);
    myDiv.appendChild(addBookModalDiv);
    //=================================================================================

    //=================================================================================
    var myModalDiv = document.createElement("div");
    setMyModalDivAttributes(myModalDiv);
    addBookModalDiv.appendChild(myModalDiv);
    //=================================================================================

    //=================================================================================
    var modalWrapperDiv = document.createElement("div");
    setModalWrapperDivAttributes(modalWrapperDiv);
    myModalDiv.appendChild(modalWrapperDiv);
    //=================================================================================

    //=================================================================================
    var linkBoxTopDiv = document.createElement("div");
    setLinkBoxTopDivAttributes(linkBoxTopDiv);
    modalWrapperDiv.appendChild(linkBoxTopDiv);
    //=================================================================================

    //=================================================================================
    var bookHeaderDiv = document.createElement("div");
    setBookHeaderDivAttributes(bookHeaderDiv);
    linkBoxTopDiv.appendChild(bookHeaderDiv);
    //=================================================================================

    //=================================================================================
    var headerOne = document.createElement("h1");
    headerOne.innerHTML = "Add User";
    bookHeaderDiv.appendChild(headerOne);
    //=================================================================================

    //=================================================================================
    var descriptionBoxDiv = document.createElement("div");
    descriptionBoxDiv.className = "description-box";
    modalWrapperDiv.appendChild(descriptionBoxDiv);
    //=================================================================================

    //=================================================================================
    var isbnTable = document.createElement("table");
    descriptionBoxDiv.appendChild(isbnTable);
    //=================================================================================

    //=================================================================================
    var isbnTr = document.createElement("tr");
    isbnTable.appendChild(isbnTr);
    //=================================================================================

    //=================================================================================
    var isbnTrLabelTd = document.createElement("td");
    setLabelTdAttributes(isbnTrLabelTd, "Username*: ");
    isbnTr.appendChild(isbnTrLabelTd);
    //=================================================================================

    //=================================================================================
    var isbnTrInputTd = document.createElement("td");
    isbnTr.appendChild(isbnTrInputTd);
    //=================================================================================

    //=================================================================================
    var isbnInput = document.createElement("input");
    isbnInput.className = "editInput";

    var isbnInputIdAttr = document.createAttribute("id");
    isbnInputIdAttr.value = "addIsbnInput";
    isbnInput.setAttributeNode(isbnInputIdAttr);

    var isbnInputTypeAttr = document.createAttribute("type");
    isbnInputTypeAttr.value = "text";
    isbnInput.setAttributeNode(isbnInputTypeAttr);

    isbnTrInputTd.appendChild(isbnInput);
    //=================================================================================

    //=================================================================================
    var bookTitleTr = document.createElement("tr");
    isbnTable.appendChild(bookTitleTr);
    //=================================================================================

    //=================================================================================
    var bookTitleTrLabelTd = document.createElement("td");
    setLabelTdAttributes(bookTitleTrLabelTd, "Name*: ");
    bookTitleTr.appendChild(bookTitleTrLabelTd);
    //=================================================================================

    //=================================================================================
    var bookTitleTrInputTd = document.createElement("td");
    bookTitleTr.appendChild(bookTitleTrInputTd);
    //=================================================================================

    //=================================================================================
    var bookTitleInput = document.createElement("input");
    setInputAttributes(bookTitleInput, "addBookTitleInput");
    bookTitleTrInputTd.appendChild(bookTitleInput);
    //=================================================================================

    //=================================================================================
    var authorNameTr = document.createElement("tr");
    isbnTable.appendChild(authorNameTr);
    //=================================================================================

    //=================================================================================
    var authorNameTrLabelTd = document.createElement("td");
    setLabelTdAttributes(authorNameTrLabelTd, "Hobbies*: ");
    authorNameTr.appendChild(authorNameTrLabelTd);
    //=================================================================================

    //=================================================================================
    var authorNameTrInputTd = document.createElement("td");
    authorNameTr.appendChild(authorNameTrInputTd);
    //=================================================================================

    //=================================================================================
    var authorNameInput = document.createElement("input");
    setInputAttributes(authorNameInput, "addAuthorNameInput");
    authorNameTrInputTd.appendChild(authorNameInput);
    //=================================================================================

    //=================================================================================
    var linkBoxDiv = document.createElement("div");
    linkBoxDiv.className = "link-box";
    modalWrapperDiv.appendChild(linkBoxDiv);
    //=================================================================================

    //=================================================================================
    var addLibraryButton = document.createElement("button");
    setAddButtonAttributes(addLibraryButton, "library", "addUser();", "Create User");
    linkBoxDiv.appendChild(addLibraryButton);
    //=================================================================================
}

function setAddBookModalDivAttributes(addBookModalDiv) {
    addBookModalDiv.className = "modal fade";

    var addBookIDAttr = document.createAttribute("id");
    addBookIDAttr.value = "addBookModal";
    addBookModalDiv.setAttributeNode(addBookIDAttr);

    var tabIndexAttr = document.createAttribute("tabindex");
    tabIndexAttr.value = "-1";
    addBookModalDiv.setAttributeNode(tabIndexAttr);

    var roleAttr = document.createAttribute("role");
    roleAttr.value = "dialog";
    addBookModalDiv.setAttributeNode(roleAttr);

    var ariaLabelAttr = document.createAttribute("aria-labelledby");
    ariaLabelAttr.value = "myModalLabel";
    addBookModalDiv.setAttributeNode(ariaLabelAttr);

    var ariaHiddenAttr = document.createAttribute("aria-hidden");
    ariaLabelAttr.value = "true";
    addBookModalDiv.setAttributeNode(ariaLabelAttr);
}

function setMyModalDivAttributes(myModalDiv) {
    myModalDiv.className = "popup-modal modal-dialog modal-vertical-centered";
}

function setModalWrapperDivAttributes(modalWrapperDiv) {
    var modalWrapperIdAttr = document.createAttribute("id");
    modalWrapperIdAttr.value = "modal-wrapper-div";
    modalWrapperDiv.setAttributeNode(modalWrapperIdAttr);
}

function setLinkBoxTopDivAttributes(linkBoxTopDiv) {
    var linkBoxTopIdAttr = document.createAttribute("id");
    linkBoxTopIdAttr.value = "link-box-top";
    linkBoxTopDiv.setAttributeNode(linkBoxTopIdAttr);
}

function setBookHeaderDivAttributes(bookHeaderDiv) {
    var bookheaderIdAttr = document.createAttribute("id");
    bookheaderIdAttr.value = "book-header";
    bookHeaderDiv.setAttributeNode(bookheaderIdAttr);
}

function setLabelTdAttributes(labelTd, innerText) {
    labelTd.innerHTML = innerText;
}

function setInputAttributes(inputElement, id) {
    inputElement.className = "editInput";

    var inputIdAttr = document.createAttribute("id");
    inputIdAttr.value = id;
    inputElement.setAttributeNode(inputIdAttr);

    var inputTypeAttr = document.createAttribute("type");
    inputTypeAttr.value = "text";
    inputElement.setAttributeNode(inputTypeAttr);
}

function setAddButtonAttributes(buttonElement, buttonName, onClickFunc, innerText) {
    buttonElement.className = "add-book-flat-btn";
    buttonElement.innerHTML = innerText;

    var buttonDataDismissAttr = document.createAttribute("data-dismiss");
    buttonDataDismissAttr.value = "modal";
    buttonElement.setAttributeNode(buttonDataDismissAttr);

    var buttonTypeAttr = document.createAttribute("type");
    buttonTypeAttr.value = "button";
    buttonElement.setAttributeNode(buttonTypeAttr);

    var buttonNameAttr = document.createAttribute("name");
    buttonNameAttr.value = buttonName;
    buttonElement.setAttributeNode(buttonNameAttr);

    var buttonOnClickAttr = document.createAttribute("onclick");
    buttonOnClickAttr.value = onClickFunc;
    buttonElement.setAttributeNode(buttonOnClickAttr);
}

function addUser() {

    var username = document.getElementById("addIsbnInput").value;
    var name = document.getElementById("addBookTitleInput").value;
    var hobbies = document.getElementById("addAuthorNameInput").value;

    $.post("http://localhost:8080/MyServlet/CreateUser/", JSON.stringify({
        "username": username,
        "name": name,
        "hobbies": hobbies
    }), function (data, status) {
        var status = $.parseJSON(data).status;
        if (status == 201) {
            alert($.parseJSON(data).message);
            var jsonArray = $.parseJSON(data).body;
            console.log(">> "+jsonArray);
            displayBooks(jsonArray);
            displayBookDetailModal(jsonArray);
        } else {
            alert("Status: " + status + ", Message: " + $.parseJSON(data).message);
        }
    });
}