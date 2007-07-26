function fireMethod(methodName, formId) {
    form = document.getElementById(formId);
    createElementSubmitForm(form, methodName);
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