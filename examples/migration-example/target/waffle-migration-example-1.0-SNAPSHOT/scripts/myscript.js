function fireMethod(methodName) {
    createElementSubmitForm(document.forms[0], methodName);
}

function createElementSubmitForm(form, methodName) {
    var method = document.createElement("input");
    method.setAttribute("type", "hidden");
    method.setAttribute("name", "method");
    method.value = methodName;

    form.appendChild(method);
    form.submit();
    return true;
}

