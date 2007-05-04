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

function makeDraggable(className) {
    var items = document.getElementsByClassName(className);
    for (var i = 0; i < items.length; i++) {
        new Draggable(items[i].id, {ghosting:true, revert:true})
    }
}

function handleDrop(element, dropon, event) {
    var pars = 'method=' + element.id;
    new Ajax.Updater({success: 'selection'}, "ajaxexample.waffle", {method: 'get', parameters: pars, evalScripts:true});
}