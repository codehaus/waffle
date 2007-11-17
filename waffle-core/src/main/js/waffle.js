function fireMethod(methodName) {
    submitMethodForm(document.forms[0], methodName, '');
}

function fireMethodWithEncoding(methodName, encoding) {
    submitMethodForm(document.forms[0], methodName, encoding);
}

function submitMethodForm(form, methodName, encoding) {
    var method = document.createElement("input");
    method.setAttribute("type", "hidden");
    method.setAttribute("name", "method");
    method.value = methodName;

    form.appendChild(method);
    form.encoding = encoding;
    form.submit();
    return true;
}
